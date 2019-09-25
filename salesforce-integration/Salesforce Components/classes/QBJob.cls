/**************************************************************************************************************************
* Copyright 2014-2017 Revenova, LLC. All rights reserved.             
* 
* Selects and contructs QB records  
*      Accounts (Customers)
*           Invoices (Receivables) & Credit Memos(Invoice Adjustments)
*      Vendors  (Carriers)
*           Bills(Payables) and Bill Refunds
*   
* IMPORTANT:  In Quickbooks an Account(Customer) can NOT also be a Vendor(Carrier) 
*
* Requires the following custom fields
*       TP: rtms__Carrier_Remit_To__c
*       Customer Invoice:  Accounting_Transaction_Type__c 
*       Carrier  Invoice:  Accounting_Transaction_Type__c 
*       Customer Payment:  Payment_Transaction_Type__c, Accounting_Processed_Date__c
*       Carrier  Payment:  Payment_Transaction_Type__c, Accounting_Processed_Date__c         
*******************************************************************************************************************************/ 
global with sharing class QBJob implements Database.Batchable<SObject>, Schedulable, Database.Stateful {
    public QuickBooksConfiguration__c m_config;
    public QBBuilderBase m_qbb;

    public QBJob() { 
        m_config = QuickBooksConfiguration__c.getOrgDefaults(); 
        if ( Test.isRunningTest() ) {
            m_config = new QuickBooksConfiguration__c();
            m_config.Company_Prefix__c   = 'QB';
            m_config.Email_QB_File__c    = false;
            m_config.Email_QB_File_To__c = 'support@revenova.com';  
            m_config.Save_To_Folder__c   = 'QuickBooks';
            m_config.Process_Customer_Invoice_Load_Status__c = 'Completed';
            m_config.Process_Carrier_Invoice_Load_Status__c  = 'Completed';
            m_config.Process_Customer_Invoice_Status__c      = 'Invoiced';
            m_config.Process_Vendor_Invoice_Status__c        = 'Approved';
            m_config.Create_Vendor_List__c          = true;
            m_config.Create_Customer_List__c        = true;
            m_config.Create_Invoice_Transactions__c = true;
            m_config.Create_Bill_Transactions__c    = true;
            m_config.batchSize__c = 1;
            m_config.File_Type__c = 'ZED';
            m_config.Record_Limit__c = 100;             
        }
        if ( m_config.File_Type__c == 'ZED') {
            m_qbb = new QBBuilder2();             
        }
        else {
            m_qbb = new QBBuilder1(); 
        }
    }
  
//  ------------------------------------------------------------------------------------------------------
//  Batchable interface methods
//  -----------------------------------------------------------------------------------------------------  
    global Iterable<SObject> start(Database.BatchableContext context) {
        List<SObject> soList = new List<SObject>();
        
        if ( m_config.Create_Invoice_Transactions__c ) {
            List<SObject> soList1 = getCustomerInvoices();            
            soList.addAll(soList1);
        }    
 
        if ( m_config.Create_Bill_Transactions__c ) {       
            List<SObject> soList2 = getCarrierInvoices();
            soList.addAll(soList2);
        }

        System.debug('ALL ************************************************** soList = ' + soList.size() ); 
        return soList;      
    }    

    global void execute(Database.BatchableContext context, List<SObject> soList) {
        for (SObject soObject : soList) {
            if(soObject instanceof rtms__CustomerInvoice__c) {
                processCustomerInvoice((rtms__CustomerInvoice__c)soObject);
            }
            if(soObject instanceof rtms__CarrierInvoice__c) {
                processCarrierInvoice((rtms__CarrierInvoice__c)soObject);
            }
        }     
    }
 
    global void finish(Database.BatchableContext context) {
        update m_invoiceProcessedMap.values();
        update m_paymentProcessedMap.values();

        m_config.Last_Run_Date__c = Date.today();
        if( !Test.isRunningTest() ) {
            update m_config;
        }
        String filePrefix = m_config.Company_Prefix__c+ '-';
        String fileSuffix = '-'+ (Datetime.now().formatGmt('YYYY-MM-dd-HHmmss'))  + '.' + 'txt';
        
        if ( m_qbb.WRITE_SINGLE_FILE ) {
            writeSingleFile(filePrefix + fileSuffix );
        } 
        else {
            writeCustomers(filePrefix + 'CUSTOMER' + fileSuffix);
            writeVendors(filePrefix   + 'VENDOR'   + fileSuffix);
            writeInvoices(filePrefix  + 'INVOICE'  + fileSuffix);
            writeBills(filePrefix     + 'BILL'     + fileSuffix);
        }          
 
        // 14-Mar-2017 May need to run again since limits have been added to control file sizes
        // Because some customer invoices with no bill to on load are bypassed, the indicator of "done" 
        // is when no more records are written. 
        if ( m_customerRecords.size() == 0 &&      
             m_vendorRecords.size()   == 0 &&       
             m_billRecords.size()     == 0 &&           
             m_invoiceRecords.size()  == 0  ) {
            System.debug('Done');                
        }
        else {
            if( !Test.isRunningTest() ) {
                QBJob j = new QBJob();
                System.scheduleBatch(j, 'QBJob', 1, Integer.valueOf(m_config.batchSize__c));  
            }            
        }  
    } 

    global void execute(SchedulableContext context) {
        QBJob j = new QBJob();
        Database.executeBatch(j, Integer.valueOf(m_config.batchSize__c));
    }

//  --------------------------------------------------------------------------------------------------------------
//  SOQL Retrieves
//  --------------------------------------------------------------------------------------------------------------
    public List<rtms__CarrierInvoice__c> getCarrierInvoices() {    
        String invoiceColumns = String.join(getColumnNamesForTable(rtms__CarrierInvoice__c.sObjectType), ',');
        String loadColumns = 'rtms__Load__r.' + String.join(getColumnNamesForTable(rtms__Load__c.sObjectType), ',rtms__Load__r.');
        String carrierAcctColumns = 'rtms__Load__r.rtms__Carrier__r.' + String.join(getColumnNamesForTable(Account.sObjectType), ',rtms__Load__r.rtms__Carrier__r.');
        String carrierTPColumns = 'rtms__Load__r.rtms__Carrier__r.rtms__Transportation_Profile__r.' + String.join(getColumnNamesForTable(rtms__TransportationProfile__c.sObjectType), ',rtms__Load__r.rtms__Carrier__r.rtms__Transportation_Profile__r.');
        String vendorAcctColumns = 'rtms__Vendor__r.' + String.join(getColumnNamesForTable(Account.sObjectType), ',rtms__Vendor__r.');
        String vendorTPColumns = 'rtms__Vendor__r.rtms__Transportation_Profile__r.' + String.join(getColumnNamesForTable(rtms__TransportationProfile__c.sObjectType), ',rtms__Vendor__r.rtms__Transportation_Profile__r.');
        String firstStopColumns = 'rtms__Load__r.rtms__First_Stop__r.' + String.join(getColumnNamesForTable(rtms__Stop__c.sObjectType), ',rtms__Load__r.rtms__First_Stop__r.');
        String lastStopColumns = 'rtms__Load__r.rtms__Last_Stop__r.' + String.join(getColumnNamesForTable(rtms__Stop__c.sObjectType), ',rtms__Load__r.rtms__Last_Stop__r.');
        String accessorialColumns = String.join(getColumnNamesForTable(rtms__CarrierInvoiceAccessorial__c.sObjectType), ',');
        String paymentColumns = String.join(getColumnNamesForTable(rtms__CarrierPayment__c.sObjectType), ',');
        String lookUPInvoiceColumns = 'rtms__Carrier_Invoice__r.' + String.join(getColumnNamesForTable(rtms__CarrierInvoice__c.sObjectType), ',rtms__Carrier_Invoice__r.');
        
        String Query = 'SELECT ' +  invoiceColumns;
            query += ' ,' +  loadColumns ; 
            query += ' ,' +  carrierAcctColumns ; 
            query += ' ,' +  carrierTPColumns ; 
            query += ' ,' +  vendorAcctColumns ; 
            query += ' ,' +  vendorTPColumns ; 
            query += ' ,' +  firstStopColumns ;
            query += ' ,' +  lastStopColumns ;
            query += ' ,( SELECT ' + accessorialColumns + ',' + lookUPInvoiceColumns + ' FROM rtms__Carrier_Invoice_Accessorials__r )';
            query += ' ,( SELECT ' + paymentColumns     + ',' + lookUPInvoiceColumns + ' FROM rtms__Carrier_Payments__r )';
            query += ' FROM rtms__CarrierInvoice__c ';
            query += ' WHERE rtms__Accounting_Processed_Date__c = null';
//            query += ' AND rtms__Load__r.rtms__Load_Status__c = ' + quote(m_config.Process_Carrier_Invoice_Load_Status__c);
            query += ' AND (rtms__Invoice_Status__c = ' + quote('Approved');
            query += '  OR  rtms__Invoice_Status__c = ' + quote('Paid');
            query += '  OR  rtms__Invoice_Status__c = ' + quote('Short Paid') + ')';
            query += ' LIMIT ' + Integer.valueOf(m_config.Record_Limit__c);

        System.debug('********************** ' + query);
        List<rtms__CarrierInvoice__c>  resultSet =  Database.query(query);
        System.debug('CARRIER Invoices Selected:  '  +  resultSet.size() );
        return resultSet;
    }
 
    public List<rtms__CustomerInvoice__c> getCustomerInvoices() { 
        String invoiceColumns = String.join(getColumnNamesForTable(rtms__CustomerInvoice__c.sObjectType), ',');
        String loadColumns = 'rtms__Load__r.' + String.join(getColumnNamesForTable(rtms__Load__c.sObjectType), ',rtms__Load__r.');
        String billingAcctColumns = 'rtms__Load__r.rtms__Bill_To__r.' + String.join(getColumnNamesForTable(Account.sObjectType), ',rtms__Load__r.rtms__Bill_To__r.');
        String billingContactColumns = 'rtms__Billing_Contact__r.' + String.join(getColumnNamesForTable(Contact.sObjectType), ',rtms__Billing_Contact__r.');
        String billingTPColumns = 'rtms__Load__r.rtms__Bill_To__r.rtms__Transportation_Profile__r.' + String.join(getColumnNamesForTable(rtms__TransportationProfile__c.sObjectType), ',rtms__Load__r.rtms__Bill_To__r.rtms__Transportation_Profile__r.');
        String firstStopColumns = 'rtms__Load__r.rtms__First_Stop__r.' + String.join(getColumnNamesForTable(rtms__Stop__c.sObjectType), ',rtms__Load__r.rtms__First_Stop__r.');
        String lastStopColumns = 'rtms__Load__r.rtms__Last_Stop__r.' + String.join(getColumnNamesForTable(rtms__Stop__c.sObjectType), ',rtms__Load__r.rtms__Last_Stop__r.');
        String accessorialColumns = String.join(getColumnNamesForTable(rtms__CustomerInvoiceAccessorial__c.sObjectType), ',');
        String paymentColumns = String.join(getColumnNamesForTable(rtms__CustomerPayment__c.sObjectType), ',');
        String taxColumns = String.join(getColumnNamesForTable(rtms__CustomerInvoiceTax__c.sObjectType), ',');
        String lookUPInvoiceColumns = 'rtms__Customer_Invoice__r.' + String.join(getColumnNamesForTable(rtms__CustomerInvoice__c.sObjectType), ',rtms__Customer_Invoice__r.');
        String lookUPTaxColumns = 'rtms__Sales_Tax__r.' + String.join(getColumnNamesForTable(rtms__SalesTax__c.sObjectType), ',rtms__Sales_Tax__r.');
        
        String Query = 'SELECT ' +  invoiceColumns;
            query += ' ,' +  loadColumns ; 
            query += ' ,' +  billingAcctColumns ; 
            query += ' ,' +  billingContactColumns ; 
            query += ' ,' +  billingTPColumns ;
            query += ' ,' +  firstStopColumns ;
            query += ' ,' +  lastStopColumns ;
            query += ' ,( SELECT ' + accessorialColumns + ',' + lookUPInvoiceColumns + ' FROM rtms__Customer_Invoice_Accessorials__r )';
            query += ' ,( SELECT ' + taxColumns         + ',' + lookUPInvoiceColumns +  ',' + lookUPTaxColumns + ' FROM rtms__Customer_Invoice_Taxes__r )';
            query += ' ,( SELECT ' + paymentColumns     + ',' + lookUPInvoiceColumns + ' FROM rtms__Customer_Payments__r )';
            query += ' FROM rtms__CustomerInvoice__c ';
            query += ' WHERE rtms__Accounting_Processed_Date__c = null';
//            query += ' AND rtms__Load__r.rtms__Load_Status__c = ' + quote(m_config.Process_Customer_Invoice_Load_Status__c);
            query += ' AND rtms__Invoice_Status__c != ' + quote('Pending');
            query += ' LIMIT ' + Integer.valueOf(m_config.Record_Limit__c);

        List<rtms__CustomerInvoice__c>  resultSet =  Database.query(query);
        System.debug('CUSTOMER Invoices Selected:  '  +  resultSet.size() );
        return resultSet;
    }

    public static Account getAccountAndContact(Id acctId, Id contactID ) { 
        //a.rtms__Transportation_Profile__r.rtms__Carrier_Remit_To__r.name
        String acctColumns = String.join(getColumnNamesForTable(Account.sObjectType), ',');
        String tpColumns = 'rtms__Transportation_Profile__r.' + String.join(getColumnNamesForTable(rtms__TransportationProfile__c.sObjectType), ',rtms__Transportation_Profile__r.');
        String remitToColumns = 'rtms__Transportation_Profile__r.rtms__Carrier_Remit_To__r.' + String.join(getColumnNamesForTable(Account.sObjectType), ',rtms__Transportation_Profile__r.rtms__Carrier_Remit_To__r.');
        String contactColumns = String.join(getColumnNamesForTable(Contact.sObjectType), ',');
        
        String Query = 'SELECT ' +  acctColumns;
            query += ' ,' +  tpColumns ; 
            query += ' ,' +  remitToColumns ; 
            query += ' ,( SELECT ' + contactColumns + ' FROM Contacts WHERE id = ' + + quote(contactId) + ' )';
            query += ' FROM Account ';
            query += ' WHERE id = '  + quote(acctId);

        Account acct  =  (Account)selectOne(query);
        return acct;
    }

    public static Account getAccount(Id acctId ) { 
        //a.rtms__Transportation_Profile__r.rtms__Carrier_Remit_To__r.name
        String acctColumns = String.join(getColumnNamesForTable(Account.sObjectType), ',');
        String tpColumns = 'rtms__Transportation_Profile__r.' + String.join(getColumnNamesForTable(rtms__TransportationProfile__c.sObjectType), ',rtms__Transportation_Profile__r.');
        String remitToColumns = 'rtms__Transportation_Profile__r.rtms__Carrier_Remit_To__r.' + String.join(getColumnNamesForTable(Account.sObjectType), ',rtms__Transportation_Profile__r.rtms__Carrier_Remit_To__r.');
        String contactColumns = String.join(getColumnNamesForTable(Contact.sObjectType), ',');
        
        String Query = 'SELECT ' +  acctColumns;
            query += ' ,' +  tpColumns ; 
            query += ' ,' +  remitToColumns ; 
            query += ' ,( SELECT ' + contactColumns + ' FROM Contacts )';
            query += ' FROM Account ';
            query += ' WHERE id = '  + quote(acctId);

        Account acct  =  (Account)selectOne(query);
        return acct;
    }


//  -----------------------------------------------------------------------------------------------------------------------
//  Processing: Creating records for invoices, customers and vendors
//  ------------------------------------------------------------------------------------------------------------------------
    public void processCustomerInvoice(rtms__CustomerInvoice__c cInvoice) {     
        System.debug('************************* Processing CUSTOMER Invoice: ' + cInvoice.Name + ' Id: ' + cInvoice.Id);
        if (cInvoice.rtms__Load__r.rtms__Bill_To__c == null) { 
            System.debug('************************* skip CUSTOMER Invoice missing bill to');
            return;         
        }
        m_invoiceRecords.add(m_qbb.constructCustomerInvoice(cInvoice));
        processCustomer(cInvoice);
        // mark the invoice as processed                
        cInvoice.rtms__Accounting_Processed_Date__c = System.now();
        m_invoiceProcessedMap.put(cInvoice.Id,cInvoice);
        // mark the payments  
        for ( rtms__CustomerPayment__c payment : cInvoice.rtms__Customer_Payments__r ) { 
            payment.rtms__Accounting_Processed_Date__c = System.now();
            m_paymentProcessedMap.put(payment.Id,payment);
        } 
    }
 
     public void processCustomer(rtms__CustomerInvoice__c cInvoice) { 
        if ( !m_config.Create_Customer_List__c ) {  
            return;
        }                   
         // Add Bill-to the customer list  
        if ( !m_customerProcessedMap.containsKey(cInvoice.rtms__Load__r.rtms__Bill_To__c) ) {
            Account customerAcct;
            if ( cInvoice.rtms__Billing_Contact__r != null ) {
                customerAcct = getAccountAndContact(cInvoice.rtms__Load__r.rtms__Bill_To__c, cInvoice.rtms__Billing_Contact__c);                
            }
            else {
                 customerAcct = getAccount(cInvoice.rtms__Load__r.rtms__Bill_To__c);
            }      
            m_customerRecords.add(m_qbb.constructCustomerList(customerAcct));
            m_customerProcessedMap.put(customerAcct.Id,customerAcct);
        }
     }    
 
    public void processCarrierInvoice(rtms__CarrierInvoice__c cInvoice ) {
        System.debug('************************* Processing CARRIER Invoice: ' + cInvoice.Name + ' Id: ' + cInvoice.Id);
        m_billRecords.add(m_qbb.constructCarrierBill(cInvoice));
        processVendor(cInvoice);
        cInvoice.rtms__Accounting_Processed_Date__c = System.now();
        m_invoiceProcessedMap.put(cInvoice.Id, cInvoice);
    }
    
     public void processVendor(rtms__CarrierInvoice__c cInvoice ) {
        if ( !m_config.Create_Vendor_List__c ) { 
            return;
        } 
        // Add vendor to the vendor list  
        if ( !m_vendorProcessedMap.containsKey(cInvoice.rtms__Vendor__c) ) {
            Account vendorAcct = getAccount(cInvoice.rtms__Vendor__c);
            m_vendorRecords.add(m_qbb.constructVendorList(vendorAcct));
            m_vendorProcessedMap.put(vendorAcct.Id,vendorAcct);
        }
     }   

 // -----------------------------------------------------------------------------------------------------------------------
 // Writing records
 // -----------------------------------------------------------------------------------------------------------------------
    public void writeCustomers(String fileName ) {
        String fileString = '';
        if ( m_customerRecords.size() > 0 ) {
            fileString += m_qbb.constructCustomerHeader();
            fileString += nullFriendlyConcatenation(m_customerRecords,'');
            saveAsDocument(fileName,fileString);
            emailFile(fileName,fileString);
        }
    }        

    public void writeVendors(String fileName ) {
        String fileString = '';
        if ( m_vendorRecords.size() > 0 ) {
            fileString += m_qbb.constructVendorHeader();
            fileString += nullFriendlyConcatenation(m_vendorRecords,'');
            saveAsDocument(fileName,fileString);
            emailFile(fileName,fileString);
        } 
    }  

    public void writeBills(String fileName ) {
        String fileString = '';
        if ( m_billRecords.size() > 0 ) {
            fileString += m_qbb.constructBillHeader();
            fileString += nullFriendlyConcatenation(m_billRecords,'');
            saveAsDocument(fileName,fileString);
            emailFile(fileName,fileString);
        }
    }

    public void writeInvoices(String fileName ) {
        String fileString = '';
        if ( m_invoiceRecords.size() > 0 ) {
            fileString += m_qbb.constructInvoiceHeader();
            fileString += nullFriendlyConcatenation(m_invoiceRecords,'');
            saveAsDocument(fileName,fileString);
            emailFile(fileName,fileString);
        }
    }
    
    public void writeSingleFile(String fileName) {
        String fileString = '';
        
        // Vendors 
        if ( m_vendorRecords.size() > 0 ) {
           fileString += m_qbb.constructVendorHeader();
           fileString += nullFriendlyConcatenation(m_vendorRecords,'');
        }       

        // Customers
        if ( m_customerRecords.size() > 0 ) {
           fileString += m_qbb.constructCustomerHeader();
           fileString += nullFriendlyConcatenation(m_customerRecords,'');
        }   

        // Bills
        if (m_billRecords.size() > 0 ) {
           fileString += m_qbb.constructBillHeader();
           fileString += nullFriendlyConcatenation(m_billRecords,'');
        }  
        
        // Invoices
        if (m_invoiceRecords.size() > 0 ) {
           fileString += m_qbb.constructInvoiceHeader();
           fileString += nullFriendlyConcatenation(m_invoiceRecords,'');
        }   
                              
        saveAsDocument(fileName,fileString);
        emailFile(fileName,fileString);
    } 
 
    public void emailFile(String fileName, String fileString) {
        if (!m_config.Email_QB_File__c) {  
            return;
        } 
        if ( fileString == null || fileString.trim() == '' ) {
            fileString = '     ';
        }
        Messaging.EmailFileAttachment txtQB = new Messaging.EmailFileAttachment();
        txtQB.setFileName(fileName);
        txtQB.setContentType('text/csv');
        txtQB.setBody(Blob.valueOf(fileString));

        Messaging.SingleEmailMessage email =new Messaging.SingleEmailMessage();
        String[] toAddresses = new list<string> {m_config.Email_QB_File_To__c};
        email.setToAddresses( toAddresses );
        email.setSubject(fileName);
        email.setHtmlBody(fileName);
        email.setPlainTextBody(fileName);
        email.setFileAttachments(new Messaging.EmailFileAttachment[]{txtQB});
        Messaging.sendEmail(new Messaging.SingleEmailMessage[]{email});
    }
    
    public void saveAsDocument(String fileName, String fileString) {
        Folder fldr = getDocumentFolderByName(m_config.Save_To_Folder__c);
        Document doc = new Document();
        doc.Name = fileName;
        doc.ContentType = 'text/csv';
        doc.Body = Blob.valueOf(fileString);
        doc.FolderId = (fldr != null ? fldr.ID : UserInfo.getUserId()); 
        insert doc;         
    }
   
    public void setBuilder(QBBuilderBase qbb) {
        m_qbb = qbb;
    }

//  -----------------------------------------------------------------------------------------------------------------------
//  Utility functions
//  ------------------------------------------------------------------------------------------------------------------------
    public static List<String> getColumnNamesForTable(Schema.SObjectType sot) {    
        List<String> cols = new List<String>();
        for(Schema.SObjectField sof : sot.getDescribe().fields.getMap().values()) {
            string fieldName = sof.getDescribe().getName();
            if ( fieldName.contains('Reported_Geolocation') ) {
                continue;
            }
            cols.add(fieldName);
        }
        return cols;
    }
    
    public static Folder getDocumentFolderByName(String name) {
        List<Folder> folders = [SELECT Id, Name, DeveloperName, Type FROM Folder WHERE Name = :name AND Type = 'Document'];
        if ( folders.size() > 0 ) {
            return folders[0];
        }
        else {
            return null;
        }           
    }

    public static String nullFriendlyConcatenation(List<String> concatList, String separator) {
        // differs from String.join.  Does not add the separator if the first item is null
        // list:  null IL  >   IL   Join would give you ,IL                
        String concat = '';
        for(String s : concatList) {
            if(s != null && s != '') {
                concat += (concat.length() > 0 ? separator : '') + s;
            }
        }
        return concat;
    }
    
    public static String quote(String str) {
        return str == null ? str : '\'' + str + '\'';
    }
    
    public static SObject selectOne(String query) {
        List<sObject> resultList =  Database.query(query);
        if ( resultList.size() > 0 ) {
            return resultList[0];
        }
        else {
            return null;
        }       
    } 

    //-----------------------------
    // private
    //-----------------------------
    // formated records
    @TestVisible private List<String> m_customerRecords = new List<String>();     
    @TestVisible private List<String> m_vendorRecords   = new List<String>();       
    @TestVisible private List<String> m_billRecords     = new List<String>();           
    @TestVisible private List<String> m_invoiceRecords  = new List<String>();           
    @TestVisible private List<String> m_customerPaymentRecords = new List<String>();   
 
    // Processed - may require updates to SF 
    private Map<Id, Account>  m_customerProcessedMap = new Map<Id, Account>();     
    private Map<Id, Account>  m_vendorProcessedMap   = new Map<Id, Account>();  
    private Map<Id, sObject>  m_invoiceProcessedMap  = new Map<Id, sObject>();
    private Map<Id, sObject>  m_paymentProcessedMap  = new Map<Id, sObject>();
    
}  // EOF
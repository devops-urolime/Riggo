/*
* Copyright 2014-2017 Revenova, LLC. All rights reserved.   
*/  
public virtual class QBBuilderBase {       
    
    // Accounting Transactions
    public static final String TRANSACTION_TYPE_BILL    = 'BILL';
    public static final String TRANSACTION_TYPE_REFUND  = 'BILL REFUND';
    public static final String TRANSACTION_TYPE_INVOICE = 'INVOICE';
    public static final String TRANSACTION_TYPE_CREDIT  = 'CREDIT MEMO';  
    
    // Delimiters
    public static final String FIELD_DELIMITER  = '\t';
    public static final String RECORD_DELIMITER = '\n';
    
    // Field Sets
    public final String FS_VENDOR_LIST;
    public final String FS_CUSTOMER_LIST;
    public final String FS_INVOICE_HEADER;   
    public final String FS_INVOICE_LINE;
    public final String FS_BILL_HEADER;  
    public final String FS_BILL_LINE;
    public final boolean WRITE_SINGLE_FILE;

    // Record Types ( Not salesforce record types.  Record type defn's )
    public enum RecType {VENDOR_LIST               
                        ,CUSTOMER_LIST             
                        ,CUSTOMER_INVOICE         
                        ,CUSTOMER_TRANSPORTATION_TOTAL     
                        ,CUSTOMER_LINE_HAUL       
                        ,CUSTOMER_FUEL_SURCHARGE    
                        ,CUSTOMER_DISCOUNT         
                        ,CUSTOMER_ACCESSORIAL       
                        ,CUSTOMER_ACCESSORIAL_SHORTCUT
                        ,CUSTOMER_TAX              
                        ,CUSTOMER_INVOICE_PAYMENT   
                        ,CUSTOMER_INVOICE_PAYMENT_SHORTCUT
                        ,CARRIER_BILL              
                        ,CARRIER_LINE_HAUL          
                        ,CARRIER_FUEL_SURCHARGE    
                        ,CARRIER_DISCOUNT          
                        ,CARRIER_ACCESSORIAL       
                        ,CARRIER_ACCESSORIAL_SHORTCUT
                        ,CARRIER_BILL_PAYMENT       
                        ,CARRIER_BILL_PAYMENT_SHORTCUT
                        ,CARRIER_PAYMENT_FEE}
 
    public static Map<String,QuickBooks_Record__c> m_qbrMap;

    public class Record {
        Map<String,String> fieldMap = new Map<String,String>();
        List<String> fieldNames     = new List<String>();            

        public Record(String fieldSetName ){
            Schema.DescribeSObjectResult d =QuickBooks_Record__c.sObjectType.getDescribe();
            Map<String, Schema.FieldSet> fsMap = d.fieldSets.getMap();
            Schema.FieldSet fs = fsMap.get(fieldSetName);

            for (Schema.FieldSetMember f :    fs.getFields()  ) {
                fieldNames.add(f.getLabel());
                fieldMap.put(f.getLabel(),'');
            }
        }
        
        public void setField(String fieldName, String fieldValue ){
            if ( fieldValue == null ) {
                fieldValue = '';
            }
            fieldValue = fieldValue.trim();
            if ( fieldValue == null ) {
                fieldValue = '';
            }           
            fieldValue = fieldValue.replaceAll('[\n\r\t]',' ');    
            fieldMap.put(fieldName,fieldValue);
        }

        public void setField(String fieldName, Decimal decimalValue ){
            String fieldValue = '';
            if ( decimalValue != null ) {
                fieldValue = decimalValue.toPlainString() ;    
            }
            setField(fieldName,fieldValue);
        }
        
        public void setField(String fieldName, Date dateValue ){
            String fieldValue = '';
            if ( dateValue != null ) {
                fieldValue = datetime.newInstance(dateValue.year(),dateValue.month(),dateValue.day()).formatGMT('MM/dd/YYYY'); 
                //fieldValue = dateValue.format() ;    // <<<  change date format here if necessary
            }
            setField(fieldName,fieldValue);
        }

        public void setField(String fieldName, Object objectValue ){
            if ( objectValue == null ) {
                objectValue = '';
            }
            
            if(objectValue instanceof String) {
                setField(fieldName, (String)objectValue );
            }
            else if(objectValue instanceof Decimal) {
                setField(fieldName, (Decimal)objectValue );
            }
            else if(objectValue instanceof Date) {
                setField(fieldName, (Date)objectValue );
            }
            else {
                // exception.
            }
        }

        public string getField(String fieldName){
            return fieldMap.get(fieldName);                                 
        } 

        public Decimal getDecimalField(String fieldName) {
            Decimal d = 0;
            String  s = getField(fieldName);
            if ( isDecimal(s) ) {
                d = Decimal.valueOf(s.trim());
            }
            return d;
        } 

        public void invertField(String fieldName ) {
            Decimal d = getDecimalField(fieldName);
            setField(fieldName, ( d * -1)  );
        }

        public Decimal getAMOUNT() {
           return getDecimalField('AMOUNT');
        }

        public void setAMOUNT(Decimal decimalValue ) {
           setField('AMOUNT', decimalValue );
        }

        public void invertAMOUNT() {
           invertField('AMOUNT');
        }

        public void invertQNTY() {
           invertField('QNTY');
        } 
    
        public string getRecordString(){
            String record = '';
            for ( String fieldName : fieldNames ) {
               record += fieldMap.get(fieldName) + FIELD_DELIMITER;
            }
            return record;                                 
        } 

        public string getRecordHeader(){
            String record = '';
            for ( String fieldName : fieldNames ) {
                record += fieldName + FIELD_DELIMITER;
            }
            return record;                                 
        } 

        
    }

//  ----------------------------------------------------------------------------------------------------------------------------
//  sObject methods
//  -------------------------------------------------------------------------------------------------------
    public Static boolean isCalculatedField(String fieldName ) {
        Schema.SObjectType sot = QuickBooks_Record__c.sObjectType;
        Map<String, Schema.SObjectField> fieldMap = sot.getDescribe().fields.getMap() ;
        Schema.SObjectField sof = fieldMap.get(fieldName + '__c');
        Schema.DescribeFieldResult f = sof.getDescribe();
        return f.isCalculated();
    }

    public static object getValuesFromSObject(sObject so, String fieldName ) {
    // allows concatination of fields i.e. city+state+zip - WARNING all are converted to string
        if ( fieldName == null ) {
            return ' ';
        }
        object returnValue = '';
        List<String> nameList =  fieldName.split('\\+');
        for ( String namePart : nameList ) {
            object partValue = getValueFromSObject(so,namePart);
            if ( partValue != null ) { 
                if ( partValue instanceof Date ) {
                    partValue = formatDate((Date)partValue);
                }
                returnValue +=  '' + partValue;
            }
        }
        if ( returnValue == null ) {
            returnValue = ' ';
        }
        
        return returnValue;
    }
    
    public static object getValueFromSObject(sObject so, String fieldName ) {
        if ( fieldName == null ) {
            return '';
        }
        if ( so == null ) {
            return ' ';
        }
        
        // If this field is a related list ( ie list of conacts on an account ) first get the desired list item
        // example:  Contacts[0].LastName
        If ( fieldName.Contains('[' )  )  {
            so = getSOListItem(so,fieldName);
            fieldName = fieldName.substringAfter('.');
        }
        
        // If this is a literal ( in single quotes ) just return the literal
        if (  fieldName.startsWith('\'')  &&  FieldName.endsWith('\'')  ) {
            String literal =  fieldName.substringBetween('\'');
            return literal;
        }

        // Check again to be safe
        if ( fieldName == null ) {
            return '';
        }
        if ( so == null ) {
            return ' ';
        }
        
        // works down the dot notation to get to the inner object
        List<String> nameParts = fieldName.split('\\.');
        sObject innerSObject = so;
        object fieldValue = ' ';
        for ( integer i = 0 ; i < nameParts.size() ; i++ ) {
            if ( i == ( nameParts.size() - 1 ) ) {
                fieldValue = innerSObject.get(nameParts[i]);
            }
            else {
                innerSObject = innerSObject.getSObject(nameParts[i]);
                if ( innerSObject == null ) {
                    return ' ';
                }
            }
        }
        return fieldValue; 
    }

    public static sObject getSOListItem(sObject so, String fieldName ) {
        // i.e. so = account   fileName = 'Contacts[0].FirstName'
        // pulls the first contact out of the contact list on the account
        sObject soListItem = null;

        String listName = fieldName.substringBefore('[');
        String iString = (fieldName.substringAfter('[')).subStringBefore(']');
        Integer i = 0;
        If ( iString.isNumeric()  ) {
            i =  Integer.valueof(iString);
        }

        Map<String, Object> fieldsToValue = so.getPopulatedFieldsAsMap();
        List<sObject> soList = (List<sObject>) fieldsToValue.get(listName);

        if ( soList != null &&  soList.size() >= i   )  {
            soListItem = soList[i];
        }
        
        return soListItem;
    }


//  -----------------------------------------------------------------------------------------------------------------------
//  Utility Functions
//  --------------------------------------------------------------------------------------------------------------------------
    public static Boolean isDecimal(String s){
        Boolean returnValue = false;
        if ( s != null ) {
            s = s.trim();
            s = s.remove('.');
            s = s.remove('-');
            s = s.remove('+');
            returnValue = s.isNumeric();
        }
        return returnValue;             
    }

    public static String formatDate(Date d ) {
        String s = '';
        if ( d != null ) {
            String dateFormat = QuickBooksConfiguration__c.getOrgDefaults().Date_Format__c;
            if ( dateFormat == null ) {
                dateFormat = 'dd/MM/YYYY';
            }
            s = datetime.newInstance(d.year(),d.month(),d.day()).formatGMT(dateFormat);
        }
        return s;
    }

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

    public static QuickBooks_Record__c getQBRecord(String recType, String currencyCode ) {
        recType = recType.toUpperCase();
        // populate map
        if ( m_qbrMap == null ) {
            m_qbrMap = new Map<String,QuickBooks_Record__c>();
            string query = 'SELECT ' + String.join(getColumnNamesForTable(QuickBooks_Record__c.sObjectType), ',' )
                    + ' FROM QuickBooks_Record__c';
            List<QuickBooks_Record__c>  qbrList = Database.query(query);        
            for ( QuickBooks_Record__c qbr : qbrList ) {
                // MultiCurrency
                if ( UserInfo.isMultiCurrencyOrganization() ) {
                    String qbrCurrencyCode = (String)qbr.get('CurrencyIsoCode');
                    m_qbrMap.put(qbrCurrencyCode + qbr.Type_Name__c.toUpperCase(),qbr );
                }
                else {
                    m_qbrMap.put(qbr.Type_Name__c.toUpperCase(),qbr );
                }       
            }
            
        }
        
        // extract from map
        QuickBooks_Record__c qbrReturn;
        if ( UserInfo.isMultiCurrencyOrganization() ) {    
            qbrReturn = m_qbrMap.get(currencyCode + recType);
        }
        else {        
            qbrReturn = m_qbrMap.get(recType);
        } 

        If ( qbrReturn == null ) {   // Blow up here if type is missing...
            system.debug('*******************  Select QBR:  ' + recType  + '            currency code:  ' + currencyCode); 
            system.debug('*******************   Using QBR:  ' + qbrReturn.name + '      currency code:  ' + currencyCode);              
        }

        return qbrReturn;
    }
 
    public Record constructRecord(RecType recType, String fieldSet, sObject so) { 
        return constructRecord(recType.Name(), fieldSet, so );  
    }

    public Record constructRecord(String recType, String fieldSet, sObject so) {   
        String currencyCode = '';
        if ( UserInfo.isMultiCurrencyOrganization() ) {
            currencyCode = (String)so.get('CurrencyIsoCode');
        }
        QuickBooks_Record__c qbr = getQBRecord(recType, currencyCode );
 
        // create the "output" record USING the record definition
        //Record rec = new Record((String)qbr.get(FS_COL));   
        Record rec = new Record(fieldSet);
        
        // fill the values
        for ( String fieldName : rec.fieldNames ) {
            
            // Formula and Account take value as is                   
            if ( isCalculatedField(fieldName) || fieldName == 'ACCNT'  ) {
                rec.setField(fieldName,  qbr.get(fieldName + '__c') );   // get the value from the QB record definition
            }
            
            // sObject Fields 
            else {
                String soField = (String)qbr.get(fieldName + '__c');         // get the field to use from the record defn
                object soValue = getValuesFromSObject(so,soField);           // get the value of the field      
                rec.setField(fieldName,soValue);    
                system.debug(fieldName + ':  ' + soValue + '      soField:  ' + soField );
            }
            
        }  
 
        if ( qbr.Invert_Amount__c ) {
            doBalancing(rec);
        }
 
        // invert credit memos and bill refunds;
        String transactionType = rec.getField('TRNSTYPE');
        if ( transactionType == TRANSACTION_TYPE_CREDIT  ||  transactionType == TRANSACTION_TYPE_REFUND ) {
            rec.invertAMOUNT();
            rec.invertQNTY();
            System.debug('Amount Inverted: ' + transactionType);
        } 

        return rec;     
    } 
    
    public virtual Record doBalancing(Record lineItem) {
        return lineItem;  // the iif descendant does inversion to create a balanced transaction
    }

    public String constructRecordString(RecType recType, String fieldSet, sObject so ) {   // just a shortcut 
        return  constructRecord(recType, fieldSet, so).getRecordString();    
    }

    public List<Record> constructFromListOrShortcut(RecType rt, String fieldSet,  List<sObject> soList, sObject so  ) {
        // Payments and Accessorials can be entered in TWO ways.  Detail rows related to an invoice
        // OR as a shortcut, just one number on the invoice.  If detail rows exsist a split will
        // be created for each. If none then use the invoice level shortcut.  In either case records
        // created with an amount of ZERO are bypassed. 
        
        List<Record> recList = new List<Record>();
         
        // First try and use detail rows               
        if  ( soList.size() > 0 ) {
            for(sObject soItem : soList ) {
                Record rec = constructRecord(rt,fieldSet,soItem);
                if ( rec.getAMOUNT() != 0  ) {
                    recList.add(rec);
                }       
            }
        }
        // else use the amount on the invoice ( the shortcut )    
        else {
            Record rec = constructRecord(rt + '_Shortcut',fieldSet,so); 
            if ( rec.getAMOUNT() != 0 ) {
                recList.add(rec);
            }             
        }
        
        return recList;
        
    }

    public String constructCustomerInvoice(rtms__CustomerInvoice__c cInvoice ) {
        List<Record> lineItems = new List<Record>();
        
        Record lineHaul = constructRecord(RecType.CUSTOMER_LINE_HAUL, FS_INVOICE_LINE, cInvoice);
        // Each invoice MUST have line haul.  If zero use total
        // NO Line Haul can be zero in ZED so only make line haul equal to the total if creating an IIF file
        if ( lineHaul.getAMOUNT() == 0 && WRITE_SINGLE_FILE) {
            Decimal invoiceTotal = cInvoice.rtms__Invoice_Total__c;
            lineHaul.setField('PRICE', invoiceTotal );     
            lineHaul.setAmount(invoiceTotal);
            lineHaul = doBalancing(lineHaul);
            lineItems.add(lineHaul); 
        }
        else if (lineHaul.getAMOUNT() != 0){
            lineItems.add(lineHaul); 
        }      
        
        Record fuel =  constructRecord(RecType.CUSTOMER_FUEL_SURCHARGE, FS_INVOICE_LINE, cInvoice);
        if ( fuel.getAMOUNT() != 0 ) {
            lineItems.add(fuel);
        }     

        Record discountRec = constructRecord(RecType.CUSTOMER_DISCOUNT, FS_INVOICE_LINE, cInvoice);
        if ( discountRec.getAMOUNT() != 0 ) {
            lineItems.add(discountRec);
        } 

        List<Record> accessorialRecs = constructFromListOrShortcut(RecType.CUSTOMER_ACCESSORIAL, FS_INVOICE_LINE,
                cInvoice.rtms__Customer_Invoice_Accessorials__r, cInvoice );
        lineItems.addAll(accessorialRecs);
 
        List<Record> paymentRecs = constructFromListOrShortcut(RecType.CUSTOMER_INVOICE_PAYMENT, FS_INVOICE_LINE,
                cInvoice.rtms__Customer_Payments__r, cInvoice);
        lineItems.addAll(paymentRecs);
        
    //  NO lines rows - use TRANSPORTATION TOTAL !     
        if ( lineItems.size() == 0 ) {
            Record transportationTotal =  constructRecord(RecType.CUSTOMER_TRANSPORTATION_TOTAL, FS_INVOICE_LINE, cInvoice);
            if ( transportationTotal.getAMOUNT() != 0 ) {
                lineItems.add(transportationTotal);
            } 
        }
  
     // taxes
        for(sObject soItem : cInvoice.rtms__Customer_Invoice_Taxes__r ) {
            Record taxRec = constructRecord(RecType.CUSTOMER_TAX,FS_INVOICE_LINE,soItem);
            if ( taxRec.getAMOUNT() != 0  ) {
                lineItems.add(taxRec);
            }       
        }                      
        
        Record header = constructRecord(RecType.CUSTOMER_INVOICE, FS_INVOICE_HEADER, cInvoice);
        return bundleTransaction(header,lineItems);          
        
    }  


    //-------------------------------------------------------------------------------------------------------------------
    // Carrier Invoices generate BILLS and BILL REFUNDS  
    // ALMOST exactly like invoice
    // carriers can have fees on payments
    //-------------------------------------------------------------------------------------------------------------------
    public String constructCarrierBill(rtms__CarrierInvoice__c cInvoice ) {
        List<Record> lineItems = new List<Record>();
        
        Record lineHaul = constructRecord(RecType.CARRIER_LINE_HAUL, FS_BILL_LINE, cInvoice);
        if ( lineHaul.getAMOUNT() != 0 ) {
            lineItems.add(lineHaul);
        }      
        
        Record fuel =  constructRecord(RecType.CARRIER_FUEL_SURCHARGE, FS_BILL_LINE, cInvoice);
        if ( fuel.getAMOUNT() != 0 ) {
            lineItems.add(fuel);
        }     

        Record discountRec = constructRecord(RecType.CARRIER_DISCOUNT, FS_BILL_LINE, cInvoice);
        if ( discountRec.getAMOUNT() != 0 ) {
            lineItems.add(discountRec);
        }        

        List<Record> accessorialRecs = constructFromListOrShortcut(RecType.CARRIER_ACCESSORIAL, FS_BILL_LINE,
                    cInvoice.rtms__Carrier_Invoice_Accessorials__r, cInvoice );
        lineItems.addAll(accessorialRecs);
 
        List<Record> paymentRecs = constructFromListOrShortcut(RecType.CARRIER_BILL_PAYMENT, FS_BILL_LINE, 
                    cInvoice.rtms__Carrier_Payments__r, cInvoice);
        lineItems.addAll(paymentRecs);
        
        // fees
        for ( sObject payment : cInvoice.rtms__Carrier_Payments__r ) {
            Record paymentFee = constructRecord(RecType.CARRIER_PAYMENT_FEE, FS_BILL_LINE, payment);
            if ( paymentFee.getAmount() !=0 ) {
                lineItems.add(paymentFee);
            }
        }

        Record header = constructRecord(RecType.CARRIER_BILL, FS_BILL_HEADER, cInvoice);
        return bundleTransaction(header, lineItems);           
        
    } 

    // Lists --------------------------------------------------------------------------------------------------------------
    public virtual string bundleTransaction ( Record header, List<Record> lines) {
        Return 'define in decendant';
    }

    public string constructVendorList(Account acct) {
        return constructRecordString(RecType.VENDOR_LIST, FS_VENDOR_LIST, acct) + RECORD_DELIMITER;
    }

    public string constructCustomerList(Account acct) {
        return constructRecordString(RecType.CUSTOMER_LIST, FS_CUSTOMER_LIST, acct) + RECORD_DELIMITER;
    }
    
    // Column Headers -------------------------------------------------------------------------------------------------------
    public String constructHeader(String fieldSetName ) {
        Record rec = new Record(fieldSetName); 
        return rec.getRecordHeader();        
    }
     
    public virtual String constructVendorHeader() {
        return constructHeader(FS_VENDOR_LIST) ;
    }

    public virtual String constructCustomerHeader() {
        return constructHeader(FS_CUSTOMER_LIST) ;
    }

    public virtual String constructInvoiceHeader() {
        return constructHeader(FS_INVOICE_HEADER);
    }   

     public virtual String constructBillHeader() {
        return constructHeader(FS_BILL_HEADER);
    }    
       
}

// EOF
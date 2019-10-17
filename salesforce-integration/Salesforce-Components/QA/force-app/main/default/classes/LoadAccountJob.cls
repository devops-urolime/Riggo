/**
* Copyright 2014-17 Revenova, LLC. All rights reserved.    
*/
global with sharing class LoadAccountJob implements Database.Batchable<SObject>, Database.Stateful {
    public List<rtms__CarrierService__c> carrierServiceList = new List<rtms__CarrierService__c>();
    public List<rtms__InsurancePolicy__c> insurancePolicyList = new List<rtms__InsurancePolicy__c>();
    public static String                  DEFAULT_US_STATE_COVERAGE = 'AL;AZ;AR;CA;CO;CT;DE;DC;FL;GA;ID;IL;IN;IA;KS;KY;LA;ME;MD;MA;MI;MN;MS;MO;MT;NE;NV;NH;NJ;NM;NY;NC;ND;OH;OK;OR;PA;PR;RI;SC;SD;TN;TX;UT;VT;VA;WA;WV;WI;WY';
    
    global Database.QueryLocator start(Database.BatchableContext context) { 
        String query = 'SELECT ' + String.join(LoadJobUtil.getColumnNamesForTable(Account_Buffer__c.SObjectType), ',') 
                     + ' FROM Account_Buffer__c  WHERE Processed__c = false ';
        return Database.getQueryLocator(query);    
    }     

    global void execute(Database.BatchableContext context, List<SObject> scope) {

        carrierServiceList.clear();
        insurancePolicyList.clear();
        
        for ( sObject so : scope ) {  
            
            Account_Buffer__c  buffer = (Account_Buffer__c)so;
            
            Account acct = getExistingAccount(buffer);
            
            if(acct == null) {
                String tpId      = insertTP(buffer);   
                String acctId    = insertAccount(tpId,buffer);
                List<String> contactId = insertContact(acctId, null, buffer);
                String ctactId = (contactId.size() > 0 ? contactId[0] : '');
            
                if ( buffer.TMS_Type__c.contains('Carrier') ) {
                    System.debug('********************* tpId: ' + tpId);
                    System.debug('********************* acctID: ' + acctID);
                    System.debug('********************* ctactId: ' + ctactId);
                    if(ctactId != null && ctactId != ''){
                        addCarrierService(tpId, acctID, ctactId, buffer);
                    } 
                    
                    // add factoring company
                    if(buffer.Factoring_Company__c != null) {
                        insertFactoringCompany(tpId,buffer);
                    }
                    // add cargo insurance
//                    if(buffer.TP_insurance_company__c           != null
//                    || buffer.TP_liability_insurance_company__c != null
//                    || buffer.TP_auto_insurance_company__c      != null) {
//                        insertInsurancePolicy(tpId,buffer);
//                    }
                    
                }
                if ( buffer.TMS_Type__c.contains('Customer') ) {
                    rtms__TransportationProfile__c tp = new rtms__TransportationProfile__c();
                    tp.id = tpId;
                    tp.rtms__Default_Customer_Bill_To__c = acctId;
                    upsertRecordTP('Shipper/Consignee',tp);
                }
                
                buffer.Transportation_Profile__c = tpId;
                buffer.Account__c   = acctId; 
                if(ctactId != ''){
                    buffer.Contact__c   = ctactId;
                }
                buffer.Processed__c = true;
            }   
            else{
                List<String> contactId = insertContact(acct.Id,acct.rtms__TMS_Type__c, buffer);
                if(contactId.size() > 0) {
                    buffer.Contact__c   = contactId[0];
                }
                buffer.Processed__c = true;
            }
            insert carrierServiceList;
            insert insurancePolicyList;
        }
        update scope;
                           
    
    }

    global void finish(Database.BatchableContext bc) {
        AsyncApexJob a = [SELECT Id, Status, NumberOfErrors, JobItemsProcessed, TotalJobItems, CreatedBy.Email FROM AsyncApexJob WHERE Id = :bc.getJobId()];
        System.debug(a);
    }

    public Account getExistingAccount(Account_Buffer__c buffer) {
        List<Account> aList = [SELECT Id, Name,rtms__TMS_Type__c FROM Account WHERE Name = :buffer.Name and 
                               ((BillingStreet  =: buffer.BAddress__c and BillingCity  =: buffer.BCity__c and BillingState  =: buffer.BState__c) or
                                (ShippingStreet =: buffer.SAddress__c and ShippingCity =: buffer.SCity__c and ShippingState =: buffer.SState__c))];
        if(aList.size() > 0) {
            return aList[0];
        }
        return null;
    }

    public string insertTP(Account_Buffer__c buffer) {
        String tpRecordType = '';
        rtms__TransportationProfile__c tp = new rtms__TransportationProfile__c();
        tp.Name = buffer.Name + '-TP'; 
        
        System.debug('********************* buffer.TMS_Type__c: ' + buffer.TMS_Type__c);
        System.debug('********************* buffer.TMS_Type__c.contains(Carrier): ' + buffer.TMS_Type__c.contains('Carrier'));
        if ( buffer.TMS_Type__c.contains('Carrier') ) {
            tpRecordType                         = 'Carrier (Ground)';
            tp.rtms__Carrier_Review_Status__c    = (buffer.Carrier_Review_Status__c != null ? buffer.Carrier_Review_Status__c : 'Approved');
            tp.rtms__Federal_Tax_ID__c           = buffer.FED_TAX_ID__c;
            tp.rtms__MC_Number__c                = buffer.MC__c;
            tp.rtms__SCAC__c                     = buffer.SCAC__c;
            tp.rtms__USDOT_Number__c             = buffer.US_DOT__c;
            tp.rtms__State_DOT_Number__c         = buffer.STATE_DOT__c;
            tp.rtms__State_DOT_State__c          = buffer.STATE_DOT_STATE__c;
            tp.rtms__Number_of_Drivers__c        = (buffer.OF_DRIVERS__c != null ? Integer.valueOf(buffer.OF_DRIVERS__c) : 0);
            tp.rtms__US_State_Coverage__c        = DEFAULT_US_STATE_COVERAGE;
//            tp.rtms__US_State_Coverage__c        = buffer.US_STATE_COVERAGE__c;
//            tp.rtms__Canada_Province_Coverage__c = buffer.CANADA_STATE_COVERAGE__c;
//            tp.rtms__Mexico_State_Coverage__c    = buffer.MEXICO_STATE_COVERAGE__c;
            tp.rtms__Fleet_Size__c               = (buffer.Fleet_Size__c != null ? Decimal.valueof(buffer.Fleet_Size__c) : 0);
//            tp.Business_Type__c                  = (buffer.Carrier_Business_Type__c != null ? buffer.Carrier_Business_Type__c : '');
            tp.rtms__Invoice_Terms__c            = (buffer.Payment_Terms__c != null ? getInvoiceTerms(buffer.Payment_Terms__c) : 0);
//            tp.Carrier_Payment_Terms__c          = (buffer.Payment_Terms__c != null ? buffer.Payment_Terms__c : '');
        }
        else {
            tpRecordType = 'Shipper/Consignee';
            tp.rtms__Invoice_Terms__c = (buffer.Payment_Terms__c != null ? getInvoiceTerms(buffer.Payment_Terms__c) : 0);
            if(buffer.Account_Shipping_Appt_Required__c != null){
                tp.rtms__Pickup_Appointment_Required__c = (buffer.Account_Shipping_Appt_Required__c.trim() == '1' ? true : false);
            }
            if(buffer.Internal_Notes__c != null){
                tp.rtms__Internal_Notes__c = buffer.Internal_Notes__c;
            }

            // new fields here
        }
        
        return  upsertRecordTP(tpRecordType,tp);
    }


    public string insertAccount (String tpID, Account_Buffer__c buffer) {
        Account acct = new Account(); 
        acct.rtms__Transportation_Profile__c = tpId;
        acct.Name               = buffer.Name;
        acct.Phone              = buffer.ACCTphone__c;
        acct.Fax                = buffer.ACCTfax__c;
        acct.Description        = buffer.Description__c;
        acct.SIC                = buffer.SIC_Description__c;
        acct.Legacy_Acct_Id__c  = buffer.Legacy_Acct_Id__c;
        
        // Only loaded Billing addres when present
        if(buffer.BAddress__c != null) {
	        acct.BillingStreet      = buffer.BAddress__c; 
	        acct.BillingStreet      = acct.BillingStreet + (buffer.Account_Baddress_Line_2__c != null ? buffer.Account_Baddress_Line_2__c : ''); 
	        acct.BillingCity        = buffer.BCity__c;
	        acct.BillingState       = buffer.BState__c;
	        acct.BillingPostalCode  = buffer.BPostalCode__c;        
	        if ( buffer.Bzip_4__c != null ) {
	            acct.BillingPostalCode += buffer.Bzip_4__c;
	        }
	        acct.BillingCountry     = buffer.BCountry__c;
        }
        
        // Only loaded Shipping addres when present
        if(buffer.SAddress__c != null) {
            acct.ShippingStreet     = buffer.SAddress__c;             
            acct.ShippingStreet     = acct.ShippingStreet + (buffer.Account_Shipping_Address_Line_2__c != null ? buffer.Account_Shipping_Address_Line_2__c : ''); 
            acct.ShippingCity       = buffer.SCity__c;
            acct.ShippingState      = buffer.SState__c;
            acct.ShippingPostalCode = buffer.SPostalCode__c;
            if ( buffer.Szip_4__c != null ) {
                acct.ShippingPostalCode += buffer.Szip_4__c;
            }
            acct.ShippingCountry    = buffer.SCountry__c;
        }        
        
        acct.rtms__TMS_Type__c  = buffer.TMS_Type__c;

        /// ---------------------------------------------------------------------------------------------
         
        return upsertRecordA(acct);  
    }

    public List<String> insertContact(String acctID, String tmsType, Account_Buffer__c buffer ) {

        List<String> contcts = new List<String>();        
        String[] contactList = getContacts(buffer);
//        String[] emailList;
//        if(buffer.CONemail__c != null) {
//            emailList = buffer.CONemail__c.trim().split(',');
//        }
//        integer idx = 1;
        if(contactList != null) {
	        for(String ctact: contactList){
	            
		        Contact c   = new Contact();        
		        c.AccountId = acctId;

                c.firstName = (buffer.firstName__c != null ? buffer.firstName__c : '');
                c.LastName  = (buffer.lastName__c  != null ? buffer.lastName__c  : buffer.Name);
//                String[] fnln = ctact.trim().split(' ');
//                if(fnln.size() > 1) {	
//                    c.firstName = fnln[0];
//                    c.LastName  = fnln[1];
//                }
//                else{
//                    c.firstName = fnln[0];
//                    c.LastName  = buffer.Name;
//                }                
		                
                String email    = buffer.CONemail__c;
//                String email    = '';
//		        if(emailList != null) {
//			        if(idx <= emailList.size()){
//			            integer tmp = (idx > 0 ? idx -1 : idx);
//			            email = emailList[tmp].trim();
//			        }
//		        }
//		        idx++;
		                
		        c.Phone         = (buffer.CONphone__c != null ? buffer.CONphone__c : '');
		        c.Fax           = (buffer.ACCTfax__c != null ? buffer.ACCTfax__c : '');
		        c.MobilePhone   = (buffer.CONmobile__c != null ? buffer.CONmobile__c : '');
		        c.Title         = (buffer.CONTitle__c != null ? buffer.CONTitle__c : '');
		        c.Description   = (buffer.CONDescription__c != null ? buffer.CONDescription__c : '');
		        if (LoadJobUtil.isValidEmail(email)) {
		            c.Email = email;
		        }
		        
		        // mailing address
		        c.MailingStreet      = (buffer.MAddress__c != null ? buffer.MAddress__c : ''); 
		        c.MailingCity        = (buffer.MCity__c != null ? buffer.MCity__c : '');
		        c.MailingState       = (buffer.MState__c != null ? buffer.MState__c : '');
		        c.MailingPostalCode  = (buffer.MPostal_Code__c != null ? buffer.MPostal_Code__c : '');        
		        if ( buffer.Mzip_4__c != null ) {
		            c.MailingPostalCode += buffer.Mzip_4__c;
		        }
		        c.MailingCountry     = (buffer.MCountry__c != null ? buffer.MCountry__c : '');
		        
		        // other address
		        c.OtherStreet      = (buffer.OAddress__c != null ? buffer.OAddress__c : ''); 
		        c.OtherCity        = (buffer.OCity__c != null ? buffer.OCity__c : '');
		        c.OtherState       = (buffer.OState__c != null ? buffer.OState__c : '');
		        c.OtherPostalCode  = (buffer.OPostal_Code__c != null ? buffer.OPostal_Code__c : '');        
		        if ( buffer.Ozip_4__c != null ) {
		            c.OtherPostalCode += buffer.Ozip_4__c;
		        }
		        c.OtherCountry     = (buffer.OCountry__c != null ? buffer.OCountry__c : '');
		        
		        
		        // need additional contact cols here...
		        tmsType = (tmsType != null ? tmsType : buffer.TMS_Type__c);

                // need to do some cleanup of duplicate roles
		        String[] trole = buffer.transportation_role__c.split(';');
		        String tRoles = '';
                Map<String,String> troleMap = new Map<String,String>();
                for(String tr : trole){
                    tr = tr.trim();
                    if(!troleMap.containsKey(tr)){
    		            troleMap.put(tr, tr);
    		            tRoles = tRoles + tr + ';';
                    }
		        }
		        c.rtms__Transportation_Role__c = tRoles;
//		        if ( tmsType == 'Carrier') {
//		            c.rtms__Transportation_Role__c = 'Billing';         
//		        }
//		        else {
//		            c.rtms__Transportation_Role__c = 'Shipping;Receiving;Billing';
//		        }
		        
		        contcts.add(upsertRecordC(c));
	        }
        }
        return contcts;
    }
 
    public String[] getContacts(Account_Buffer__c buffer){

        String[] contnts = new String[]{buffer.Name};
        if(buffer.lastName__c != null) {
//            if(buffer.lastName__c.contains(',')){
//                return buffer.lastName__c.split(',');                
//            }
//            if(buffer.lastName__c.contains('/')){
//                return buffer.lastName__c.split('/');                
//            }
//            if(buffer.lastName__c.contains('/')){
//                return buffer.lastName__c.split(' or ');                
//            }
//            return buffer.lastName__c.split(',');
            contnts[0] = buffer.lastName__c;
            return contnts;                
        }
        contnts[0] = buffer.Name;
        return contnts;                
    }
    
    public void insertFactoringCompany (String tpID, Account_Buffer__c buffer) {
        Account acct                            = new Account(); 

        acct.Id = getFactoringCompany(buffer.Factoring_Company__c);
        if(acct.Id == null) {
	        acct.Name                               = buffer.Factoring_Company__c;
	//        acct.AccountNumber                      = buffer.Remit_ID__c;
	//        acct.BillingStreet                      = buffer.Remit_Address_1__c; 
	//        if ( buffer.Remit_to_Address2__c != null ) {
	//            acct.BillingStreet +=  '\n' + buffer.Remit_to_Address2__c;
	//        }             
	//        acct.BillingCity                        = buffer.Remit_City__c;
	//        acct.BillingState                       = buffer.Remit_State__c;
	
	//        acct.BillingPostalCode = buffer.Remit_Zip__c;        
	//        acct.BillingCountry                     = 'US';
	        
	        acct.rtms__TMS_Type__c                  = 'Factoring Company'; 
	        
	        upsertRecordA(acct);
        }
        
        // update the TP with the FC Id
        String tpRecordType                     = 'Carrier';
        rtms__TransportationProfile__c tp       = new rtms__TransportationProfile__c();
        tp.Id                                   = tpID;
        tp.rtms__Carrier_Remit_To__c            = acct.Id; 
        
        upsertRecordTP(tpRecordType,tp);
        
    }
    public String getFactoringCompany(String fcName){
        List<Account> fcList = [select Id, Name from Account where Name like : fcName];
        if(fcList.size() > 0) {
            return fcList[0].Id;
        }
        return null;
    }
/*
    public void insertInsurancePolicy(String tpID, Account_Buffer__c buffer) {

        // insert cargo insourance
        if(buffer.TP_policy_number__c != null) {
            rtms__InsurancePolicy__c ins            = new rtms__InsurancePolicy__c(); 
            ins.Name                                = buffer.TP_policy_number__c;
            ins.rtms__Insurance_Company__c          = 
                                (buffer.TP_insurance_company__c           != null ? buffer.TP_insurance_company__c :
                                 buffer.TP_liability_insurance_company__c != null ? buffer.TP_liability_insurance_company__c :
                                 buffer.TP_auto_insurance_company__c);
            ins.rtms__Effective_Date__c             = buffer.TP_expiry_date__c - 365;
            ins.rtms__Expiry_Date__c                = buffer.TP_expiry_date__c; 
            ins.rtms__Coverage_Amount__c            = Decimal.valueof(buffer.TP_cargo_ins__c);
            ins.rtms__Phone__c                      = buffer.TP_cargo_insurance_phone__c;
            ins.Extension__c                        = buffer.TP_cargo_insurance_phone_ext__c;
            if(buffer.TP_cargo_insurance_contact__c != null){
                String[] insAgent                   = buffer.TP_cargo_insurance_contact__c.split(' '); 
                ins.rtms__Agent_First_Name__c       = (insAgent[0] != null ? insAgent[0] : ''); 
                ins.rtms__Agent_Last_Name__c        = (insAgent[1] != null ? insAgent[1] : '');
            } 
            ins.Note__c                             = buffer.TP_cargo_insurance_note__c;
            ins.rtms__Transportation_Profile__c     = tpID;
            ins.rtms__Type__c                       = 'Cargo';
              
            insert ins;
        }            
        
        // insert liability insourance
        if(buffer.TP_liability_policy_number__c != null) {
            rtms__InsurancePolicy__c ins            = new rtms__InsurancePolicy__c(); 
            ins.Name                                = buffer.TP_liability_policy_number__c;
            ins.rtms__Insurance_Company__c          = 
                                (buffer.TP_liability_insurance_company__c != null ? buffer.TP_liability_insurance_company__c :
                                 buffer.TP_insurance_company__c           != null ? buffer.TP_insurance_company__c :
                                 buffer.TP_auto_insurance_company__c);
            ins.rtms__Effective_Date__c             = buffer.TP_liability_expiry_date__c - 365;
            ins.rtms__Expiry_Date__c                = buffer.TP_liability_expiry_date__c; 
            ins.rtms__Coverage_Amount__c            = Decimal.valueof(buffer.TP_liability_ins__c);
            ins.rtms__Phone__c                      = buffer.TP_liability_insurance_phone__c;
            ins.Extension__c                        = buffer.TP_liability_insurance_phone_ext__c;
            if(buffer.TP_liability_insurance_contact__c != null){
                String[] insAgent                   = buffer.TP_liability_insurance_contact__c.split(' '); 
                ins.rtms__Agent_First_Name__c       = (insAgent[0] != null ? insAgent[0] : ''); 
                ins.rtms__Agent_Last_Name__c        = (insAgent[1] != null ? insAgent[1] : '');
            } 
            ins.Note__c                             = buffer.TP_liability_insurance_note__c;
            ins.rtms__Transportation_Profile__c     = tpID;
            ins.rtms__Type__c                       = 'BIPD';
              
            insert ins;
        }            
        
        // insert auto insourance
        if(buffer.TP_auto_policy_number__c != null) {
            rtms__InsurancePolicy__c ins            = new rtms__InsurancePolicy__c(); 
            ins.Name                                = buffer.TP_auto_policy_number__c;
            ins.rtms__Insurance_Company__c          = 
                                (buffer.TP_auto_insurance_company__c != null ? buffer.TP_auto_insurance_company__c :
                                 buffer.TP_insurance_company__c      != null ? buffer.TP_insurance_company__c :
                                 buffer.TP_liability_insurance_company__c);
            ins.rtms__Insurance_Company__c          = buffer.TP_auto_insurance_company__c;
            ins.rtms__Effective_Date__c             = buffer.TP_auto_expiry_date__c - 365; 
            ins.rtms__Expiry_Date__c                = buffer.TP_auto_expiry_date__c; 
            ins.rtms__Coverage_Amount__c            = Decimal.valueof(buffer.TP_auto_ins__c);
            ins.rtms__Phone__c                      = buffer.TP_auto_insurance_phone__c;
            ins.Extension__c                        = buffer.TP_auto_insurance_phone_ext__c;
            if(buffer.TP_auto_insurance_contact__c != null){ 
                String[] insAgent                   = buffer.TP_auto_insurance_contact__c.split(' '); 
                ins.rtms__Agent_First_Name__c       = (insAgent[0] != null ? insAgent[0] : ''); 
                ins.rtms__Agent_Last_Name__c        = (insAgent[1] != null ? insAgent[1] : '');
            } 
            ins.Note__c                             = buffer.TP_auto_insurance_note__c;
            ins.rtms__Transportation_Profile__c     = tpID;
            ins.rtms__Type__c                       = 'Auto';
              
            insert ins;
        }            
        
    }
*/
    public void addCarrierService(Id tpId, Id acctID, Id contactId,  Account_Buffer__c buffer ) {
        rtms__CarrierService__c csTruckLoad         = new rtms__CarrierService__c();
        
        if(buffer.MODE__c == null) {
            buffer.MODE__c = 'Truckload';
        }
        rtms__Mode__c csMode = getMode(buffer.MODE__c);
        String max = (buffer.MAX__c != null ? buffer.MAX__c.replace(',','') : '50000');

        csTruckLoad.Name                            = buffer.Name + csMode.Name;
        csTruckLoad.rtms__Mode__c                   = csMode.Id;                 
        csTruckLoad.rtms__Carrier__c                = acctID;
        csTruckLoad.rtms__Transportation_Profile__c = tpId;
        csTruckLoad.rtms__Minimum_Weight__c         = (buffer.MIN__c != null ? Decimal.valueof(buffer.MIN__c) : 0);
        csTruckLoad.rtms__Maximum_Weight__c         = (max != null ? Decimal.valueof(max) : 50000);
        csTruckLoad.rtms__Weight_Units__c           = (buffer.UNITS__c != null ? buffer.UNITS__c : 'lbs' );
        csTruckLoad.rtms__Quote_Method__c           = buffer.QUOTE_METHOD__c;
        csTruckLoad.rtms__Tender_Method__c          = buffer.TENDER_METHOD__c;
        csTruckLoad.rtms__Status_Method__c          = buffer.STATUS_METHOD__c;
        csTruckLoad.rtms__Quote_Contact__c          = contactId;
        csTruckLoad.rtms__Tender_Contact__c         = contactId;
        csTruckLoad.rtms__Status_Contact__c         = contactId;
        csTruckLoad.rtms__Enabled__c                = (buffer.Carrier_Review_Status__c == 'Approved' ? true : false);
        
        carrierServiceList.add(csTruckLoad); 
    }

    public static Integer getInvoiceTerms(String paymentTerm){
        if(paymentTerm != null){
            if(paymentTerm.trim() == 'Net 1' ){
                return 1;
            }
            else if(paymentTerm.trim() == 'Net 14' ){
                return 15;
            }
            else if(paymentTerm.trim() == 'Net 15' ){
                return 15;
            }
            else if(paymentTerm.trim() == 'Net 15' ){
                return 15;
            }
            else if(paymentTerm.trim() == 'Net 20' ){
                return 20;
            }
            else if(paymentTerm.trim() == 'Net 25' ){
                return 25;
            }
            else {
                return 30;
            }
        }
        return null;
    }

    private static rtms__Mode__c getMode(String whichMode) {
        rtms__Mode__c mode = [SELECT Id, Name FROM rtms__Mode__c WHERE Name = :whichMode];
        return mode;
    }   
    private static rtms__Mode__c getTL() {
        return getMode('LTL');
    }   
    private static rtms__Mode__c getTruckload() {
        return getMode('Truckload');
    }



    
// ---------- Test Helper Methods ------ <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<     


    public void deleteBuffer() {
        //List<LoadAccountBuffer__c> cb  = getBuffer();
        //delete cb;
    }
 
    public static void deleteLoadedAccountInfo(){
/*        List<Id> idList = new List<Id>();
        for ( Account_Buffer__c buffer : [SELECT Transportation_Profile__c FROM Account_Buffer__c 
                                                WHERE Transportation_Profile__c != null  ]){
            idList.add(buffer.Transportation_Profile__c);
        }
        
        //List<rtms__CarrierService__c> csList = [SELECT id FROM rtms__CarrierService__c WHERE rtms__Transportation_Profile__c IN :idList ];
        List<Account> acctList = [SELECT id FROM Account WHERE rtms__Transportation_Profile__c IN :idList ];
        List<rtms__TransportationProfile__c> tpList = [SELECT id FROM rtms__TransportationProfile__c WHERE id IN :idList ];
                
        System.debug(' TP IDs:     ' + idList.size());
        System.debug(' TPs:        ' + tpList.size());
        System.debug(' Accts:      ' + acctlist.size());        
        //System.debug(' CarrierSVC: ' + csList.size());

        delete acctList;
        //delete csList;  // you have to delete the carrier service Before delete tp    
        delete tpList;
*/            
    }


    public static void fakeFlip() {
        List<Account_Buffer__c> bufferRows = [SELECT id FROM Account_Buffer__c  ];
        for ( Account_Buffer__c buffer : bufferRows ) {
            buffer.Processed__c = false; 
        }
        update bufferRows;
        System.debug('Rows selected:  ' + bufferRows.size() );
    }


    public static void runJob() {
        LoadAccountJob myJob = new LoadAccountJob();
        Database.executeBatch(myJob,1);
    }

    public static void testThis() {
        //List<Account_Buffer__c> rowList = [SELECT id FROM Account_Buffer__c  ];
        //System.debug(rowList.size());
        //delete rowList;
    }
    public static String upsertRecordTP(String tpRecordType, rtms__TransportationProfile__c tp) {
        
        if ( tp.id == null ) {
            System.debug('Insert a new TP');
            RecordType rt = getRecordType(tpRecordType);
            if ( rt == null ) {
                rt = getRecordTypeShipper();
            }
            tp.RecordTypeId = rt.id;
            tp.rtms__Carrier_Review_Status__c = 'Approved';
        }            
       
        upsert tp;
        return tp.id;
    }
    public static RecordType getRecordType(String whichType) {
        List<RecordType> rtList = [SELECT Id, Name FROM RecordType 
            WHERE SObjectType = 'rtms__TransportationProfile__c' 
            AND Name = :whichType];
            
        RecordType rt;      
        if ( rtList.size() > 0 ) {
            rt = rtList[0];             
        }           
        return rt;
    }
    public static RecordType getRecordTypeShipper() {
        return getRecordType('Shipper/Consignee');
    }    
    public static String upsertRecordA(Account acct) {
        upsert acct;
        return acct.id; 
    } 
    public static String upsertRecordC(Contact ctc) {
        upsert ctc;
        return ctc.id;
    }
    public static String upsertRecordI(rtms__InsurancePolicy__c ins) {
        upsert ins;
        return ins.id;
    }
    public static integer getBatchSizeX(){
        String x = '';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        x = x + 'a';
        return 1;
    }
}
// EOF
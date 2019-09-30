/*
* Copyright 2014-2017 Revenova, LLC. All rights reserved.      
*/    
@isTest 
private class QBTests { 
    
    //  --------------------------------------------------------------------------------------------------
    //  Note this method could cause problems if production quickbooks records are not setup properly
    //  Comment out if causing problems.  Coverage still good
    /*
    @isTest(SeeAllData=true)       
    static void testQBRecordSetup() {  
        insertInvoices();
        test.startTest();
        QBJob qbj = new QBJob();
        qbj.setBuilder(new QBBuilder2());
        Database.executeBatch(qbj,20);
        test.stopTest();       
    }
    */
    //  See all data end   --------------------------------------------------------------



    static testMethod void testCustomerListQBbuilder() { 
        insertInvoices();
        insertQBRows(); 
        QBJob qbj  = new QBJob(); 
        List<rtms__CustomerInvoice__c> cInvoices = qbj.getCustomerInvoices();
        
        System.debug('Builder 1');
        qbj.setBuilder(new QBBuilder1());
        for ( rtms__CustomerInvoice__c ci :  cInvoices  ) {
            System.debug(ci);
            qbj.processCustomer(ci);
            System.debug(qbj.m_customerRecords);
        }

        System.debug('Builder 2');
        qbj.setBuilder(new QBBuilder2());
        for ( rtms__CustomerInvoice__c ci :  cInvoices  ) {
            qbj.processCustomer(ci);
            System.debug(qbj.m_customerRecords);
        }

    }


    static testMethod void testCarrierInvoiceQBbuilder() { 
        insertInvoices();
        insertQBRows(); 
        QBJob qbj  = new QBJob(); 
        List<rtms__CarrierInvoice__c> cInvoices = qbj.getCarrierInvoices();
        
        System.debug('Builder 1');
        QBBuilderBase qbb  = new QBBuilder1();
        for ( rtms__CarrierInvoice__c ci :  cInvoices  ) {
            System.debug(ci);
            String rec = qbb.constructCarrierBill(ci);
            System.debug(rec);
        }
        System.debug(qbb.constructBillHeader());

        System.debug('Builder 2');
        qbb  = new QBBuilder2();
        for ( rtms__CarrierInvoice__c ci :  cInvoices  ) {
            String rec = qbb.constructCarrierBill(ci);
            System.debug(rec);
        }
        System.debug(qbb.constructBillHeader());
    }

    static testMethod void testCustomerInvoiceQBbuilder() { 
        insertInvoices();
        insertQBRows(); 
        QBJob qbj = new QBJob(); 
        List<rtms__CustomerInvoice__c> cInvoices = qbj.getCustomerInvoices();

        System.debug('Builder 1');        
        QBBuilderBase qbb = new QBBuilder1();
        for ( rtms__CustomerInvoice__c ci :  cInvoices  ) {
            System.debug(ci);
            String rec = qbb.constructCustomerInvoice(ci);
            System.debug(rec);
        }
        System.debug(qbb.constructInvoiceHeader());

        System.debug('Builder 2');
        qbb  = new QBBuilder2();
        for ( rtms__CustomerInvoice__c ci :  cInvoices  ) {
            String rec = qbb.constructCustomerInvoice(ci);
            System.debug(rec);
        }
        System.debug(qbb.constructInvoiceHeader());
        
    }

    static testMethod void testGetValuesFromSObject() {
        rtms__CustomerInvoice__c customerInvoice = new rtms__CustomerInvoice__c();
        customerInvoice.name = 'x';
        customerInvoice.rtms__Invoice_Date__c = Date.today();

        Object d = QBBuilderBase.getValuesFromSObject(customerInvoice, 'rtms__Invoice_Date__c' ) ; 
        System.assertEquals(   QBBuilderBase.formatDate(Date.today() ) , d, 'get date from sObject'  );

        Object s = QBBuilderBase.getValuesFromSObject(customerInvoice, 'name+\'yz\'' ) ; 
        System.assertEquals(  s , 'xyz', 'concatination'  );
        
        Account a = new Account ( name = 'a');
        //a.Sols_Planner__c = 'x';
        insert a;
        Contact c = new Contact ( LastName = 'mcelroy', FirstName = 'Chyrisse', AccountId = a.id );
        insert c;
        
        a = [SELECT name, ( SELECT LastName, FirstName FROM Contacts WHERE id = :c.id ) FROM Account WHERE id = :a.id ] ;
        
        List<Contact> cList = [ SELECT Id, LastName, FirstName FROM Contact  ];
        System.debug(cList);
        
        System.debug(a);
        
        Object cName = QBBuilderBase.getValuesFromSObject(a, 'Contacts[0].FirstName' ) ;
        System.assertEquals(c.FirstName,  cName , 'Child field'  ); 
        
    }

    static testMethod void testQBJob() {
        insertInvoices();
        insertQBRows(); 
        test.startTest();
        QBJob qbj = new QBJob();
        qbj.setBuilder(new QBBuilder1());
        Database.executeBatch(qbj,20);
        test.stopTest();
        
        // customer invoice
        List<rtms__CustomerInvoice__c> customerInvoicesAfter = [SELECT rtms__Accounting_Processed_Date__c, rtms__Invoice_status__c FROM rtms__CustomerInvoice__c ];
        System.assertEquals(Date.today(),customerInvoicesAfter[0].rtms__Accounting_Processed_Date__c.Date(),'Customer Invoice - Accounting_Processed_Date');

        // carrier invoice
        rtms__CarrierInvoice__c carrierInvoiceAfter = [SELECT rtms__Accounting_Processed_Date__c FROM rtms__CarrierInvoice__c ];
        System.assertEquals(Date.today(),carrierInvoiceAfter.rtms__Accounting_Processed_Date__c.Date(),'Carrier Invoice - Accounting_Processed_Date');

    }

    static testMethod void testQBRunJobController() {
        Test.startTest();
        PageReference p1 = Page.QBRunJob;
        Test.setCurrentPage(p1);    
 
        QBRunJobController c1 = new QBRunJobController();
        
        System.assert(!c1.getJobIsRunning());
        System.assert(!c1.getJobIsDone());       
        
        PageReference p2 = c1.doRun();
        System.assertNotEquals(null, p2, 'testQBRunJobController failed');
        
        c1.LifeIsGood();   
        
        Test.stopTest();
    }


    //--------------------------------------------------------------------------------------------------------------
     static void insertQBRows() {
        for ( QBBuilderBase.RecType rt : QBBuilderBase.RecType.values() ) {
            QuickBooks_Record__c qbr = new QuickBooks_Record__c();           
            qbr.Name         = 'TEST';
            qbr.Type_Name__c = rt.Name();

            if ( rt == QBBuilderBase.RecType.CUSTOMER_LIST ||  rt == QBBuilderBase.RecType.VENDOR_LIST ) { 
                qbr.NAME__c   = 'Name';
                qbr.BADDR1__c = 'Name';
                qbr.BADDR2__c = 'BillingStreet';
                qbr.BADDR3__c = 'BillingCity+'   + '\' \'' + '+BillingState+' + '\', \''   +  '+BillingPostalCode';
                qbr.BADDR4__c = 'BillingCountry';
            }
            if ( rt == QBBuilderBase.RecType.CUSTOMER_INVOICE ) { 
                qbr.DOCNUM__c = 'Name';
                qbr.ACCNT__c  = 'A/R';
                qbr.NAME__c   = 'rtms__Load__r.rtms__Bill_To__r.Name';
                qbr.AMOUNT__c = 'rtms__Balance_Due__c';
                qbr.DATE__c   = 'rtms__Invoice_Date__c';
                qbr.Invert_Amount__c = False;
            }
            if ( rt == QBBuilderBase.RecType.CUSTOMER_LINE_HAUL ) {  
                qbr.DOCNUM__c  = 'Name';
                qbr.ACCNT__c   = 'Income';
                qbr.INVITEM__c = '\'' + 'Line Haul' + '\'';
                qbr.AMOUNT__c  = 'rtms__Net_Line_Haul__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CUSTOMER_FUEL_SURCHARGE ) {  
                qbr.DOCNUM__c  = 'Name';
                qbr.ACCNT__c   = 'Income';
                qbr.INVITEM__c = '\'' + 'Fuel' + '\'';
                qbr.AMOUNT__c  = 'rtms__Fuel_Surcharge__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CUSTOMER_ACCESSORIAL ) {  
                qbr.DOCNUM__c  = 'rtms__Customer_Invoice__r.Name';
                qbr.ACCNT__c   = 'Income';
                qbr.INVITEM__c = '\'' + 'ACC' + '\'';
                qbr.AMOUNT__c  = 'rtms__Charge__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CUSTOMER_TAX ) {  
                qbr.DOCNUM__c  = 'rtms__Customer_Invoice__r.Name';
                qbr.ACCNT__c   = 'OCL';     // other current liability
                qbr.INVITEM__c = '\'' + 'Tax' + '\'';
                qbr.AMOUNT__c  = 'rtms__Tax_Amount__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CUSTOMER_INVOICE_PAYMENT_SHORTCUT ) {  
                qbr.DOCNUM__c  = 'Name';
                qbr.ACCNT__c   = 'PMT';      
                qbr.INVITEM__c = '\'' + 'Pmt' + '\'';   
                qbr.AMOUNT__c  = 'rtms__Paid_Amount__c';
                qbr.Invert_Amount__c = False;
            }



            // --------------------------------------------------------------------------------------

            if ( rt == QBBuilderBase.RecType.CARRIER_BILL ) { 
                qbr.DOCNUM__c = 'Name';
                qbr.ACCNT__c  = 'PAY';
                qbr.NAME__c   = 'rtms__Vendor__r.Name';
                qbr.AMOUNT__c = 'rtms__Balance_Due__c';
                qbr.Invert_Amount__c = False;
            }

            if ( rt == QBBuilderBase.RecType.CARRIER_LINE_HAUL ) {  
                qbr.DOCNUM__c  = 'Name';
                qbr.ACCNT__c   = 'COGS';
                qbr.INVITEM__c = '\'' + 'Line Haul' + '\'';
                qbr.AMOUNT__c  = 'rtms__Net_Line_Haul__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CARRIER_FUEL_SURCHARGE ) {  
                qbr.DOCNUM__c  = 'Name';
                qbr.ACCNT__c   = 'COGS';
                qbr.INVITEM__c = '\'' + 'Fuel' + '\'';
                qbr.AMOUNT__c  = 'rtms__Fuel_Surcharge__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CARRIER_ACCESSORIAL ) {  
                qbr.DOCNUM__c  = 'rtms__Carrier_Invoice__r.Name';
                qbr.ACCNT__c   = 'COGS';
                qbr.INVITEM__c = '\'' + 'ACC' + '\'';
                qbr.AMOUNT__c  = 'rtms__Charge__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CARRIER_ACCESSORIAL_SHORTCUT ) {  
                qbr.DOCNUM__c  = 'Name';
                qbr.ACCNT__c   = 'COGS';
                qbr.INVITEM__c = '\'' + 'ACC' + '\'';
                qbr.AMOUNT__c  = 'rtms__Accessorial_Charges__c';
                qbr.Invert_Amount__c = True;
            }
            if ( rt == QBBuilderBase.RecType.CARRIER_BILL_PAYMENT_SHORTCUT ) {  
                qbr.DOCNUM__c  = 'Name';
                qbr.ACCNT__c   = 'PMT';      
                qbr.INVITEM__c = '\'' + 'Pmt' + '\'';   
                qbr.AMOUNT__c  = 'rtms__Paid_Amount__c';
                qbr.Invert_Amount__c = False;
            }


            insert qbr; 
        }
    }

    static void insertInvoices() {
        
        // customer
        rtms__TransportationProfile__c customerTP = new rtms__TransportationProfile__c(name = 'Customer TP');
        insert customerTP;
        Account customerAcct = new Account(name = 'ATHANOR STEEL, LLC',rtms__TMS_Type__c = 'Customer');
        customerAcct.rtms__Transportation_Profile__c = customerTP.id;
        customerAcct.BillingStreet = '123 West Main Street';
        customerAcct.BillingCity   = 'Hinsdale';
        customerAcct.BillingStateCode  = 'IL';
        customerAcct.BillingState  = 'Illinois';
        customerAcct.BillingPostalCode = '60521';
        customerAcct.BillingCountryCode  = 'US';
        customerAcct.BillingCountry  = 'United States';
        insert customerAcct;
        Contact customerContact = new Contact(AccountId = customerAcct.id, LastName = 'McDonald');
        insert customerContact;

        // factoring company
        Account factoringAcct = new Account();
        factoringAcct.Name = 'TRIUMPH BUSINESS CAPITAL';
        factoringAcct.BillingStreet = 'PO BOX 610028';
        factoringAcct.BillingCity   = 'DALLAS';
        factoringAcct.BillingStateCode  = 'TX';
        factoringAcct.BillingState  = 'Texas';
        factoringAcct.BillingPostalCode = '75261';
        factoringAcct.BillingCountryCode  = 'US';
        factoringAcct.BillingCountry  = 'United States';
        insert factoringAcct;
        
        // carrier
        rtms__TransportationProfile__c carrierTP = new rtms__TransportationProfile__c();
        carrierTP.Name = 'OMEGA SHIPPING EXPRESS - TP';
        carrierTP.rtms__Carrier_Remit_To__c = factoringAcct.id;
        carrierTP.rtms__Invoice_Terms__c = 25;
        insert carrierTP;
        
        Account carrierAcct = new Account();
        carrierAcct.rtms__Transportation_Profile__c = carrierTP.id;
        carrierAcct.rtms__TMS_Type__c = 'Carrier';
        carrierAcct.Name = 'OMEGA SHIPPING EXPRESS INC';
        carrierAcct.Phone = '(214) 730-0956';
        insert carrierAcct;
        
        Contact carrierContact = new Contact();
        carrierContact.Account = carrierAcct;
        carrierContact.Salutation = 'MR';
        carrierContact.LastName = 'OMEGA DISPATCHER';
        carrierContact.Email  = 'omegashipping7@gmail.com';
        insert carrierContact;
        //List<Contact> carrierContacts = new List<Contact>();
        //carrierContacts.add(carrierContact);
        
        // mode
        rtms__Mode__c mode = new rtms__Mode__c(rtms__Order__c = 1, rtms__Enabled__c = true );
        insert mode;
        
        // cs 
        rtms__CarrierService__c cs = new rtms__CarrierService__c();
        cs.rtms__Carrier__c = carrierAcct.Id;
        cs.rtms__Transportation_Profile__c = carrierTP.Id;
        cs.rtms__Mode__c = mode.Id;
        cs.rtms__Enabled__c = true;
        cs.rtms__Maximum_Weight__c = 20000;
        cs.rtms__Minimum_Weight__c = 200;
        cs.rtms__Credentials__c = null;  
        insert cs;

        // load
        rtms__Load__c load = new rtms__Load__c();
        load.Name = 'LOAD-01';
        load.rtms__Bill_To__c = customerAcct.Id;
        load.rtms__Carrier__c = carrierAcct.Id;     
        load.rtms__Load_Status__c  = 'Completed';
        load.rtms__Order_Date__c   = Date.today();
        load.rtms__Order_Number__c = 'ORDER 01';
        load.rtms__PO_Number__c    = 'PO-01';
        load.rtms__Total_Weight__c = 1000;
        insert load;
        
        rtms__Stop__c pickupStop = new rtms__Stop__c();
        pickupStop.rtms__load__c = load.id;
        pickupStop.rtms__location2__c = customerAcct.Id;
        pickupStop.rtms__Number__c = 1;
        insert pickupStop;

        rtms__Stop__c deliveryStop = new rtms__Stop__c();
        deliveryStop.rtms__load__c = load.id;
        deliveryStop.rtms__location2__c = customerAcct.Id;
        deliveryStop.rtms__Number__c = 2;
        insert deliveryStop;
        
        rtms__LineItem__c li = new rtms__LineItem__c();
        li.rtms__Load__c = load.id;
        li.rtms__Pickup_Stop__c   = pickupStop.id;
        li.rtms__Delivery_Stop__c = deliveryStop.id;
        li.rtms__Item_Description__c = 'Oranges';
        li.rtms__Weight__c = 1000;
        insert li;
        
        // carrier invoice -------------------------------------------------------------------------------------------
        rtms__CarrierInvoice__c carrierInvoice = new rtms__CarrierInvoice__c();
        carrierInvoice.Name = 'BILL-01';
        carrierInvoice.rtms__Load__c   = load.Id;
        carrierInvoice.rtms__Vendor__c = carrierAcct.id;
        carrierInvoice.rtms__Accounting_Processed_Date__c = null;
        carrierInvoice.rtms__Invoice_Date__c     = Date.today();
        carrierInvoice.rtms__Invoice_Due_Date__c = Date.today(); 
        carrierInvoice.rtms__Invoice_Status__c = 'Approved';    
        carrierInvoice.rtms__Line_Haul__c     = 5000;    
        carrierInvoice.rtms__Fuel_Surcharge__c = 25;
        carrierInvoice.rtms__Accessorial_Charges__c = 75;
        insert carrierInvoice;

        // carrier payment 
        rtms__CarrierPayment__c carrierPayment = new rtms__CarrierPayment__c();
        carrierPayment.rtms__Load__c    = load.Id;
        carrierPayment.rtms__Carrier__c = carrierAcct.Id;
        carrierPayment.rtms__Accounting_Processed_Date__c = null;
        carrierPayment.rtms__Payment_Amount__c = 500;
        insert carrierPayment;

        // customer invoice  --------------------------------------------------------------------------------------------    
        Date cidd = Date.today() + 10;
        rtms__CustomerInvoice__c custInvoice = new rtms__CustomerInvoice__c(
            Name = 'INV-01',
            rtms__Load__c = load.Id,
            rtms__Billing_Contact__c  = customerContact.id,
            Accounting_Transaction_Type__c     = 'Invoice',
            rtms__Invoice_Status__c            = 'Paid',
            rtms__Accounting_Processed_Date__c = null,
            rtms__Invoice_Date__c         = Date.today(),
            rtms__Invoice_Due_Date__c     = Date.today(),
            rtms__Invoice_Total__c        = 0,        
            rtms__Transportation_Total__c = 0,
            rtms__Payment_Discount__c     = 0,
            rtms__Net_Line_Haul__c        = 5000,
            rtms__Fuel_Surcharge__c       = 25,
            rtms__Tax_Amount__c           = 10
            );
        insert custInvoice;

        // tax
        rtms__SalesTax__c tax = new rtms__SalesTax__c();
        tax.name = 'Alberta GST';
        tax.rtms__Tax_Rate__c = .5;
        insert tax;

        // tax line - MUST MATCH AMOUNT ON INVOICE HEADER - no auto rollup
        rtms__CustomerInvoiceTax__c taxLine = new rtms__CustomerInvoiceTax__c();
        taxLine.rtms__Customer_Invoice__c = custInvoice.id;
        taxLine.rtms__Sales_Tax__c  = tax.id;
        taxLine.rtms__Tax_Amount__c = 10;
        insert taxLine;        

        // accessorial
        rtms__Accessorial__c accessorial = new rtms__Accessorial__c();
        accessorial.Name             = 'Expedited';
        accessorial.rtms__Type__c    = 'Delivery';
        accessorial.rtms__Enabled__c = true;
        accessorial.rtms__Mode__c    = 'Truckload';
        insert accessorial;

        // accessorial line  
        rtms__CustomerInvoiceAccessorial__c cia = new rtms__CustomerInvoiceAccessorial__c();
        cia.rtms__Accessorial__c       = accessorial.id;
        cia.rtms__Customer_Invoice__c  = custInvoice.id;
        cia.rtms__Charge__c            = 75;
        insert cia;

        
        // customer payment  ( inserted to show change in balance due )
        //rtms__CustomerPayment__c customerPayment = new rtms__CustomerPayment__c();
        //customerPayment.rtms__Customer_Invoice__c = customerInvoice.Id;
        //customerPayment.rtms__Load__c           = load.Id;
        //customerPayment.rtms__Payment_Type__c   = 'Check';  
        //customerPayment.rtms__Payment_Amount__c = 1000;
        //customerPayment.rtms__Discount__c       = 100;
        //insert customerPayment;


    } 
 


}
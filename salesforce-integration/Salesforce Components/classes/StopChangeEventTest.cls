@IsTest
private class StopChangeEventTest {
     
    static testMethod void testLoadsTriggerforAPI() {
		Test.enableChangeDataCapture(); 
        test.startTest();
        
        Riggo_API_Configuration__c apiconfi = new Riggo_API_Configuration__c();
        apiconfi.Name = 'Riggo post';
        apiconfi.Time_Out__c = 200;
        apiconfi.Content_Type__c = 'Content-Type';
        apiconfi.End_Point_URL__c = 'http://example.com/example/test';
        apiconfi.Authorization_Header__c = 'Authorization';
        insert apiconfi;
        
        Apex_Trigger_On_Off__c atof = new Apex_Trigger_On_Off__c();
        atof.Name = 'LoadChangeEvent';
        atof.isEnable__c = true;
        insert atof; 
        
        Apex_Trigger_On_Off__c atof1 = new Apex_Trigger_On_Off__c();
        atof1.Name = 'LoadsTriggerforAPI';
        atof1.isEnable__c = true;
        insert atof1;
        
        Apex_Trigger_On_Off__c atof2 = new Apex_Trigger_On_Off__c();
        atof2.Name = 'LineItemChangeEvent';
        atof2.isEnable__c = true;
        insert atof2; 
        
        Apex_Trigger_On_Off__c atof3 = new Apex_Trigger_On_Off__c();
        atof3.Name = 'LineItemTriggerForAPI';
        atof3.isEnable__c = true;
        insert atof3;
        
        Apex_Trigger_On_Off__c atof4 = new Apex_Trigger_On_Off__c();
        atof4.Name = 'StopChangeEvent';
        atof4.isEnable__c = true;
        insert atof4; 
        
        Apex_Trigger_On_Off__c atof5 = new Apex_Trigger_On_Off__c();
        atof5.Name = 'StopTriggerForAPI';
        atof5.isEnable__c = true;
        insert atof5;
        
        
        // Auth0 API token test record
        Auth0_API_Token__c api = TestDataFactory.createTestRecordsforAPIToken();
        insert api;
        
        // Load Test Record
        rtms__Load__c loadobj =   TestDataFactory.createTestRecordsforLoad();
        insert loadobj;
        
        Marketplace_Exposed_Data__c mak =   TestDataFactory.createTestRecordsforMarketPlace(loadobj.Id);
        insert mak;
        
        rtms__Stop__c stopObj = TestDataFactory.createTestRecordsforStop(loadobj.Id);
        
        insert stopObj;
        
        MockHttpResponseGenerator fakeResponse = new MockHttpResponseGenerator(200,
                                                 'Complete',
                                                 '{{"status":200,"message":"success","data":[{"id":416,"extSysId":"a0j4B000001UgmpQAC","extSysTenantId":null,"expectedShipDate":"2019-09-04","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-08-30","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"34760","invoiceTotal":0,"carrierQuoteTotal":null,"carrierInvoiceTotal":null,"customerQuoteTotal":null,"customerTransportTotal":null,"hazMat":false,"loadStatusReq":null,"modeName":null,"orderDate":"2019-09-04T00:00:00","salesStatus":null,"loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Ugmp","shipperId":45,"distanceMiles":null}],"hashId":"d3a295184e03a91b7ea980757316f04f34517730e57311c81e1b8ef78de51696"}}',
                                                 null);
		Test.setMock(HttpCalloutMock.class, fakeResponse);
        
        
        //Test.setMock(HttpCalloutMock.class, new MockHttpResponseGenerator());
        Test.getEventBus().deliver();
        rtms__Stop__c stopObj1 = [select id,Name from rtms__Stop__c where id =: stopObj.id limit 1];
        ServiceProxy sr = new ServiceProxy();
        HttpResponse response = sr.callRestService('http://example.com/example/test','POST', '{"hashId":"test"}','test','test');
        
        update stopObj1; 
        
        MockHttpResponseGenerator fakeResponse1 = new MockHttpResponseGenerator(401,
                                                 'Complete',
                                                 '{{"status":200,"message":"success","data":[{"id":416,"extSysId":"a0j4B000001UgmpQAC","extSysTenantId":null,"expectedShipDate":"2019-09-04","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-08-30","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"34760","invoiceTotal":0,"carrierQuoteTotal":null,"carrierInvoiceTotal":null,"customerQuoteTotal":null,"customerTransportTotal":null,"hazMat":false,"loadStatusReq":null,"modeName":null,"orderDate":"2019-09-04T00:00:00","salesStatus":null,"loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Ugmp","shipperId":45,"distanceMiles":null}],"hashId":"d3a295184e03a91b7ea980757316f04f34517730e57311c81e1b8ef78de51696"}}',
                                                 null);
		Test.setMock(HttpCalloutMock.class, fakeResponse1);
        Test.getEventBus().deliver();
        
       test.stopTest();
    }
    static testMethod void testLoadsTriggerforAPI1() {
		Test.enableChangeDataCapture(); 
        test.startTest();
        
        Riggo_API_Configuration__c apiconfi = new Riggo_API_Configuration__c();
        apiconfi.Name = 'Riggo post';
        apiconfi.Time_Out__c = 200;
        apiconfi.Content_Type__c = 'Content-Type';
        apiconfi.End_Point_URL__c = 'http://example.com/example/test';
        apiconfi.Authorization_Header__c = 'Authorization';
        insert apiconfi;
        
        Apex_Trigger_On_Off__c atof = new Apex_Trigger_On_Off__c();
        atof.Name = 'LoadChangeEvent';
        atof.isEnable__c = true;
        insert atof; 
        
        Apex_Trigger_On_Off__c atof1 = new Apex_Trigger_On_Off__c();
        atof1.Name = 'LoadsTriggerforAPI';
        atof1.isEnable__c = true;
        insert atof1;
        
        Apex_Trigger_On_Off__c atof2 = new Apex_Trigger_On_Off__c();
        atof2.Name = 'LineItemChangeEvent';
        atof2.isEnable__c = true;
        insert atof2; 
        
        Apex_Trigger_On_Off__c atof3 = new Apex_Trigger_On_Off__c();
        atof3.Name = 'LineItemTriggerForAPI';
        atof3.isEnable__c = true;
        insert atof3;
        
        Apex_Trigger_On_Off__c atof4 = new Apex_Trigger_On_Off__c();
        atof4.Name = 'StopChangeEvent';
        atof4.isEnable__c = true;
        insert atof4; 
        
        Apex_Trigger_On_Off__c atof5 = new Apex_Trigger_On_Off__c();
        atof5.Name = 'StopTriggerForAPI';
        atof5.isEnable__c = true;
        insert atof5;
        
        
        // Load Test Record
        rtms__Load__c loadobj =   TestDataFactory.createTestRecordsforLoad();
        insert loadobj;
        
        Marketplace_Exposed_Data__c mak =   TestDataFactory.createTestRecordsforMarketPlace(loadobj.Id);
        insert mak;
        
        rtms__Stop__c stopObj = TestDataFactory.createTestRecordsforStop(loadobj.Id);
        
        insert stopObj;
        
        MockHttpResponseGenerator fakeResponse = new MockHttpResponseGenerator(200,
                                                 'Complete',
                                                 '{{"status":200,"message":"success","data":[{"id":416,"extSysId":"a0j4B000001UgmpQAC","extSysTenantId":null,"expectedShipDate":"2019-09-04","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-08-30","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"34760","invoiceTotal":0,"carrierQuoteTotal":null,"carrierInvoiceTotal":null,"customerQuoteTotal":null,"customerTransportTotal":null,"hazMat":false,"loadStatusReq":null,"modeName":null,"orderDate":"2019-09-04T00:00:00","salesStatus":null,"loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Ugmp","shipperId":45,"distanceMiles":null}],"hashId":"d3a295184e03a91b7ea980757316f04f34517730e57311c81e1b8ef78de51696"}}',
                                                 null);
		Test.setMock(HttpCalloutMock.class, fakeResponse);
        
        
        //Test.setMock(HttpCalloutMock.class, new MockHttpResponseGenerator());
        Test.getEventBus().deliver();
        rtms__Stop__c stopObj1 = [select id,Name from rtms__Stop__c where id =: stopObj.id limit 1];
         update stopObj1; 
        ServiceProxy sr = new ServiceProxy();
        HttpResponse response = sr.callRestService('http://example.com/example/test','POST', '{"hashId":"test"}','test','test');
        
       
        MockHttpResponseGenerator fakeResponse1 = new MockHttpResponseGenerator(401,
                                                 'Complete',
                                                 '{{"status":200,"message":"success","data":[{"id":416,"extSysId":"a0j4B000001UgmpQAC","extSysTenantId":null,"expectedShipDate":"2019-09-04","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-08-30","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"34760","invoiceTotal":0,"carrierQuoteTotal":null,"carrierInvoiceTotal":null,"customerQuoteTotal":null,"customerTransportTotal":null,"hazMat":false,"loadStatusReq":null,"modeName":null,"orderDate":"2019-09-04T00:00:00","salesStatus":null,"loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Ugmp","shipperId":45,"distanceMiles":null}],"hashId":"d3a295184e03a91b7ea980757316f04f34517730e57311c81e1b8ef78de51696"}}',
                                                 null);
		Test.setMock(HttpCalloutMock.class, fakeResponse1);
        Test.getEventBus().deliver();
        
       test.stopTest();
    }
    static testMethod void testLoadsTriggerforAPI2() {
		Test.enableChangeDataCapture(); 
        test.startTest();
        
        Riggo_API_Configuration__c apiconfi = new Riggo_API_Configuration__c();
        apiconfi.Name = 'Riggo post';
        apiconfi.Time_Out__c = 200;
        apiconfi.Content_Type__c = 'Content-Type';
        apiconfi.End_Point_URL__c = 'http://example.com/example/test';
        apiconfi.Authorization_Header__c = 'Authorization';
        insert apiconfi;
        
        Apex_Trigger_On_Off__c atof = new Apex_Trigger_On_Off__c();
        atof.Name = 'LoadChangeEvent';
        atof.isEnable__c = true;
        insert atof; 
        
        Apex_Trigger_On_Off__c atof1 = new Apex_Trigger_On_Off__c();
        atof1.Name = 'LoadsTriggerforAPI';
        atof1.isEnable__c = true;
        insert atof1;
        
        Apex_Trigger_On_Off__c atof2 = new Apex_Trigger_On_Off__c();
        atof2.Name = 'LineItemChangeEvent';
        atof2.isEnable__c = true;
        insert atof2; 
        
        Apex_Trigger_On_Off__c atof3 = new Apex_Trigger_On_Off__c();
        atof3.Name = 'LineItemTriggerForAPI';
        atof3.isEnable__c = true;
        insert atof3;
        
        Apex_Trigger_On_Off__c atof4 = new Apex_Trigger_On_Off__c();
        atof4.Name = 'StopChangeEvent';
        atof4.isEnable__c = true;
        insert atof4; 
        
        Apex_Trigger_On_Off__c atof5 = new Apex_Trigger_On_Off__c();
        atof5.Name = 'StopTriggerForAPI';
        atof5.isEnable__c = true;
        insert atof5;
        
        // Load Test Record
        rtms__Load__c loadobj =   TestDataFactory.createTestRecordsforLoad();
        insert loadobj;
        
        Marketplace_Exposed_Data__c mak =   TestDataFactory.createTestRecordsforMarketPlace(loadobj.Id);
        insert mak;
        
        rtms__Stop__c stopObj = TestDataFactory.createTestRecordsforStop(loadobj.Id);
        insert stopObj;
        
        //Test.setMock(HttpCalloutMock.class, new MockHttpResponseGenerator());
         
        rtms__Stop__c stopObj1 = [select id,Name from rtms__Stop__c where id =: stopObj.id limit 1];
         delete stopObj1; 
        ServiceProxy sr = new ServiceProxy();
        HttpResponse response = sr.callRestService('http://example.com/example/test','POST', '{"hashId":"test"}','test','test');
        
       
        MockHttpResponseGenerator fakeResponse1 = new MockHttpResponseGenerator(401,
                                                 'Complete',
                                                 '{{"status":200,"message":"success","data":[{"id":416,"extSysId":"a0j4B000001UgmpQAC","extSysTenantId":null,"expectedShipDate":"2019-09-04","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-08-30","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"34760","invoiceTotal":0,"carrierQuoteTotal":null,"carrierInvoiceTotal":null,"customerQuoteTotal":null,"customerTransportTotal":null,"hazMat":false,"loadStatusReq":null,"modeName":null,"orderDate":"2019-09-04T00:00:00","salesStatus":null,"loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Ugmp","shipperId":45,"distanceMiles":null}],"hashId":"d3a295184e03a91b7ea980757316f04f34517730e57311c81e1b8ef78de51696"}}',
                                                 null);
		Test.setMock(HttpCalloutMock.class, fakeResponse1);
        Test.getEventBus().deliver();
        
       test.stopTest();
    }
         
}
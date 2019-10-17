@IsTest
private class LoadChangeEventTest {
     
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
        Map<String, String> responseHeaders = new map<String,String>();
        responseHeaders.put('status','200');
        responseHeaders.put('message','success');
        
        MockHttpResponseGenerator fakeResponse = new MockHttpResponseGenerator(200,
                                                 'success',
                                                 '{"status":200,"message":"success","data":[{"id":29,"extSysId":"a0j4B000001HhbtQAC","extSysTenantId":null,"expectedShipDate":"2019-10-11T00:00:00","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-10-11T00:00:00","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"37536","carrierQuoteTotal":0,"carrierInvoiceTotal":0,"customerQuoteTotal":0,"customerInvoiceTotal":0,"hazMat":false,"modeName":null,"orderDate":"2019-10-11T00:00:00","salesStatus":"Pending","loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Hhbt","shipperId":4,"distanceMiles":0.0}],"hashId":"bd8a39476dfcf678c31b0c17b1f34c40284086371b0f3cd205c9153a663e3d78"}',
                                                 responseHeaders);
        Test.setMock(HttpCalloutMock.class, fakeResponse);
        Test.getEventBus().deliver();
        rtms__Load__c loadobj1 = [select id,Name from rtms__Load__c where id =: loadobj.id limit 1];
         
        update loadobj1; 
        MockHttpResponseGenerator fakeResponse1 = new MockHttpResponseGenerator(200,
                                                 'success',
                                                '{"status":200,"message":"success","data":[{"id":416,"extSysId":"a0j4B000001UgmpQAC","extSysTenantId":null,"expectedShipDate":"2019-09-04","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-08-30","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"34760","invoiceTotal":0,"carrierQuoteTotal":null,"carrierInvoiceTotal":null,"customerQuoteTotal":null,"customerTransportTotal":null,"hazMat":false,"loadStatusReq":null,"modeName":null,"orderDate":"2019-09-04T00:00:00","salesStatus":null,"loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Ugmp","shipperId":45,"distanceMiles":null}],"hashId":"d3a295184e03a91b7ea980757316f04f34517730e57311c81e1b8ef78de51696"}',
                                                 null);
        Test.setMock(HttpCalloutMock.class, fakeResponse1);
        Test.getEventBus().deliver();       
       test.stopTest();
    }
    /*
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
        
        rtms__Load__c loadobj1 = [select id,Name from rtms__Load__c where id =: loadobj.id limit 1];
          
        update loadobj1; 
        MockHttpResponseGenerator fakeResponse1 = new MockHttpResponseGenerator(200,
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
        
        rtms__Load__c loadobj1 = [select id,Name from rtms__Load__c where id =: loadobj.id limit 1];
        ServiceProxy sr = new ServiceProxy();
        HttpResponse response = sr.callRestService('http://example.com/example/test','PUT', '{"hashId":"test"}','test','test');
        
        delete loadobj1; 
        MockHttpResponseGenerator fakeResponse1 = new MockHttpResponseGenerator(200,
                                                 'Complete',
                                                 '{{"status":200,"message":"success","data":[{"id":416,"extSysId":"a0j4B000001UgmpQAC","extSysTenantId":null,"expectedShipDate":"2019-09-04","siteId":100,"carrier":null,"transportMode":null,"postedRate":null,"insuranceAmt":0,"equipmentTypeId":null,"loadStatus":1,"teamReq":false,"foodGradeTrailerReq":false,"tempControlReq":false,"expectedDeliveryDate":"2019-08-30","driver":null,"locationBasedSvcsReq":null,"referenceNumber":null,"billOfLadingNo":null,"name":"34760","invoiceTotal":0,"carrierQuoteTotal":null,"carrierInvoiceTotal":null,"customerQuoteTotal":null,"customerTransportTotal":null,"hazMat":false,"loadStatusReq":null,"modeName":null,"orderDate":"2019-09-04T00:00:00","salesStatus":null,"loadUrl":"https://carrier.riggo.io/carrier-quote-bid/a0j4B000001Ugmp","shipperId":45,"distanceMiles":null}],"hashId":"d3a295184e03a91b7ea980757316f04f34517730e57311c81e1b8ef78de51696"}}',
                                                 null);
        Test.setMock(HttpCalloutMock.class, fakeResponse1);
        Test.getEventBus().deliver();
        
       test.stopTest();
    }*/     
}
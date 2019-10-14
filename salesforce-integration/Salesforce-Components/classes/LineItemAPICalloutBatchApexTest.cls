@IsTest
private class LineItemAPICalloutBatchApexTest {
     
    static testMethod void testLoadsTriggerforAPI() {
		Test.enableChangeDataCapture(); 
        
        
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
        LineItemAPICalloutBatchApex obj = new LineItemAPICalloutBatchApex();
            DataBase.executeBatch(obj); 
        
    }
     
}
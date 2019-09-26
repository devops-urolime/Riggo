@IsTest
private class StopTriggerForAPITestCtrl {
     
    static testMethod void testStopTriggerForAPITest() {
		Test.enableChangeDataCapture(); 
        test.startTest();
        
		Apex_Trigger_On_Off__c trig = new Apex_Trigger_On_Off__c();
        trig.NAme = 'StopTriggerForAPIForDetentionEmail';
        trig.isEnable__c = true;
        insert trig;
        
        Apex_Trigger_On_Off__c trigEv = new Apex_Trigger_On_Off__c();
        trigEv.NAme = 'StopTriggerForAPIForDetentionEmail';
        trigEv.isEnable__c = true;
        insert trigEv;
        // Load Customer
         
        Account accCustomer = TestDataFactory.createTestRecordsforCustomer();
        insert accCustomer;
            
        // Stop Location
        Account accLocation = TestDataFactory.createTestRecordsforLocation();
        insert accLocation;
        
        // Load Test Record
        rtms__Load__c loadobj =   TestDataFactory.createTestRecordsforLoad();
        loadobj.rtms__Customer__c = accCustomer.Id;
        loadobj.rtms__Load_Status__c = 'Assigned';
        insert loadobj;
        
        rtms__Stop__c stopObj = TestDataFactory.createTestRecordsforStop(loadobj.Id);
        stopObj.rtms__Location2__c = accLocation.Id;
        stopObj.rtms__Appointment_Time__c = '10:30';
        insert stopObj;
        
       test.stopTest();
    }
}
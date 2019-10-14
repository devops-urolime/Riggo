/*
* Copyright 2014-2018 Revenova, LLC. All rights reserved.     
*/  
@isTest 
private class UnitTestLoadAccountJob {
    
    static testMethod void testLoadAccountJob() {
        Test.startTest();
        integer batchSizeX = LoadAccountJob.getBatchSizeX();
        test.stopTest();
        System.assertEquals(1,batchSizeX,'UnitTestLoadAccountJob - batchSizeX ');
    }
}
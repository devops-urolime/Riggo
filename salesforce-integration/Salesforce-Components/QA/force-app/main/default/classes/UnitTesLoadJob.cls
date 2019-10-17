/*
* Copyright 2014-2018 Revenova, LLC. All rights reserved.     
*/  
@isTest 
private class UnitTesLoadJob {
    
    static testMethod void testLoadLoadJob() {
        Test.startTest();
        integer batchSizeX = LoadLoadJob.getBatchSizeX();
        test.stopTest();
        System.assertEquals(1,batchSizeX,'UnitTesLoadJob - batchSizeX ');
    }
}
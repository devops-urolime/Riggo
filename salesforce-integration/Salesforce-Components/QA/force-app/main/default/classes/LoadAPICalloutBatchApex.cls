global class LoadAPICalloutBatchApex implements Database.Batchable<sObject>, Database.AllowsCallouts  {
    public String query;
    
    global Database.QueryLocator start(Database.BatchableContext BC) {
        query = 'select id,Marketplace_Records__c from rtms__Load__c order by createddate desc limit 5000';
        return Database.getQueryLocator(query);
    }
    global void execute(Database.BatchableContext BC, List<rtms__Load__c> loadList) {
        for(rtms__Load__c ld : loadList){
            // Load API Call
            if(ld.Marketplace_Records__c != 0 && ld.Marketplace_Records__c != null ){
                LoadAPICtrl.makePostCallout(ld.Id,'Update Batch');  
             }else{
                 LoadAPICtrl.makePostCallout(ld.Id,'Insert Batch');  
             }
            //LoadAPICtrl.makePostCallout(ld.Id,'Apex Batch'); 
        } 
    }
    global void finish(Database.BatchableContext BC) {
        if(!test.isrunningtest()){
            //Id batchJobId = Database.executeBatch(new LineItemAPICalloutBatchApex(), 1);
        }
    }
}
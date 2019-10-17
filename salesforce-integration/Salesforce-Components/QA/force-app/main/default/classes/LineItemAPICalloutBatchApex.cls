global class LineItemAPICalloutBatchApex implements Database.Batchable<sObject>, Database.AllowsCallouts {
    public String query;
    
    global Database.QueryLocator start(Database.BatchableContext BC) {
        query = 'select id,Marketplace_Records__c, (select id from rtms__Line_Items__r) from rtms__Load__c order by createddate desc limit 5000';
        return Database.getQueryLocator(query);
    }
    global void execute(Database.BatchableContext BC, List<rtms__Load__c> loadList) {
        // Stop API Call
        for(rtms__Load__c ld : loadList){  
            List<rtms__LineItem__c> LineItemList = ld.rtms__Line_Items__r;
            Set<Id> LineItemSetID = new Set<ID>();
            for(rtms__LineItem__c stp : LineItemList){
                LineItemSetID.add(stp.Id); 
            }
            if(LineItemSetID != null){
                LineItemAPICtrl.makePostCallout(LineItemSetID,'Update Batch');  
            }
        }
    }
    global void finish(Database.BatchableContext BC) {
        if(!test.isrunningtest()){
            //Id batchJobId = Database.executeBatch(new StopAPICalloutBatchApex(), 1);
        }
    }
}
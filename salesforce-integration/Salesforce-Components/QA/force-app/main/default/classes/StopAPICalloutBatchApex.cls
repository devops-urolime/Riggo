global class StopAPICalloutBatchApex implements Database.Batchable<sObject>, Database.AllowsCallouts  {
	public String query;
    global Database.QueryLocator start(Database.BatchableContext BC) {
        query = 'select id,Marketplace_Records__c,(select id from rtms__Stops__r) from rtms__Load__c order by createddate des limit 5000';
        return Database.getQueryLocator(query);
    }
     global void execute(Database.BatchableContext BC, List<rtms__Load__c> loadList) {
         // Stop API Call
         for(rtms__Load__c ld : loadList){
             List<rtms__Stop__c> stopList = ld.rtms__Stops__r;
             Set<Id> StopSetID = new Set<ID>();
             for(rtms__Stop__c stp : stopList){
                 StopSetID.add(stp.Id);
             }
             if(StopSetID != null){
				StopAPICtrl.makePostCallout(StopSetID,'Update Batch');  
             } 
         }
     }
     global void finish(Database.BatchableContext BC) {
         if(!test.isrunningtest()){
             //Id batchJobId = Database.executeBatch(new CustomerQuoteAPICalloutBatchApex(), 1); 
    	}
     }
}
global class CustomerQuoteAPICalloutBatchApex implements Database.Batchable<sObject>, Database.AllowsCallouts  {
    public String query;
    
    global Database.QueryLocator start(Database.BatchableContext BC) {
        query = 'select id,Marketplace_Records__c,(select id from rtms__Customer_Quotes1__r) from rtms__Load__c order by createddate desc limit 5000';
        return Database.getQueryLocator(query);
    }
    global void execute(Database.BatchableContext BC, List<rtms__Load__c> loadList) {
        // Stop API Call
         for(rtms__Load__c ld : loadList){  
             List<rtms__CustomerQuote__c> customerQuote = ld.rtms__Customer_Quotes1__r;
             Set<Id> customerQuoteSetID = new Set<ID>();
             for(rtms__CustomerQuote__c stp : customerQuote){
                 customerQuoteSetID.add(stp.Id);
             }
             if(customerQuoteSetID != null && customerQuoteSetID.size() > 0){
                 CustomerQuoteAPICtrl.makePostCallout(customerQuoteSetID,'Update Batch');  
            }
         }
        
    }
    global void finish(Database.BatchableContext BC) {
        if(!test.isrunningtest()){
            //Id batchJobId = Database.executeBatch(new StopAPICalloutBatchApex(), 1);
        }
    }
}
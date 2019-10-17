trigger CustomerQuoteChangeEvent on rtms__CustomerQuote__ChangeEvent (after insert) {
Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('CustomerQuoteChangeEvent');
    if(apxTriOnOff != null && apxTriOnOff.isEnable__c){	
        List<Id> CustomerQuoteInsertList = new List<Id>();
        List<Id> CustomerQuoteUpdateList = new List<Id>();
        
        Boolean insertHeader = false;
        Boolean updateHeader = false;
        Boolean deleteHeader = false;
        for(rtms__CustomerQuote__ChangeEvent lnIt : trigger.new) {
            EventBus.ChangeEventHeader header = lnIt.ChangeEventHeader; 
            if (header.changetype == 'CREATE') { 
                CustomerQuoteInsertList.addAll(header.recordIds);
                insertHeader = true;
            } 
            if (header.changetype == 'UPDATE') { 
                CustomerQuoteUpdateList.addAll(header.recordIds);
                UpdateHeader = true;
             }
        }
        
        if(CustomerQuoteInsertList != null){
            Set<Id> CustomerQuoteInsertSet = new Set<Id>();
            for(Id st : CustomerQuoteInsertList){
                CustomerQuoteInsertSet.add(st);
            }
             if(CustomerQuoteInsertSet != null){
                if(insertHeader){
                    CustomerQuoteAPICalloutCtrl.makePostCallout(CustomerQuoteInsertSet,'Insert');
                }
            }
            
        }
       
        if(CustomerQuoteUpdateList != null){
            Set<Id> CustomerQuoteUpdatetSet = new Set<Id>();
            for(Id st : CustomerQuoteUpdateList){
                CustomerQuoteUpdatetSet.add(st);
            }
            if(CustomerQuoteUpdatetSet != null){
                if(updateHeader){
                    CustomerQuoteAPICalloutCtrl.makePostCallout(CustomerQuoteUpdatetSet,'Update');
                }
            }
        }   
    }
}
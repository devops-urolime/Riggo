trigger LineItemChangeEvent on rtms__LineItem__ChangeEvent (after insert) {
	Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('LineItemChangeEvent');
    if(apxTriOnOff != null && apxTriOnOff.isEnable__c){	
        List<Id> LineItemInsertList = new List<Id>();
        List<Id> LineItemUpdateList = new List<Id>();
        List<Id> LineItemDeleteList = new List<Id>();
        Boolean insertHeader = false;
        Boolean updateHeader = false;
        Boolean deleteHeader = false;
        for(rtms__LineItem__ChangeEvent lnIt : trigger.new) {
            EventBus.ChangeEventHeader header = lnIt.ChangeEventHeader; 
            if (header.changetype == 'CREATE') { 
                LineItemInsertList.addAll(header.recordIds);
                insertHeader = true;
            }
            if (header.changetype == 'UPDATE') { 
                LineItemUpdateList.addAll(header.recordIds);
                UpdateHeader = true;
             }
        }
        
        if(LineItemInsertList != null){
            Set<Id> LineItemInsertSet = new Set<Id>();
            for(Id st : LineItemInsertList){
                LineItemInsertSet.add(st);
            }
             if(LineItemInsertSet != null){
                if(insertHeader){
                    LineItemAPICalloutCtrl.makePostCallout(LineItemInsertSet,'Insert');
                }
            }
        }
       
        if(LineItemUpdateList != null){
            Set<Id> LineItemUpdatetSet = new Set<Id>();
            for(Id st : LineItemUpdateList){
                LineItemUpdatetSet.add(st);
            }
            if(LineItemUpdatetSet != null){
                if(updateHeader){
                    LineItemAPICalloutCtrl.makePostCallout(LineItemUpdatetSet,'Update');
                }
            }
        }
    }
}
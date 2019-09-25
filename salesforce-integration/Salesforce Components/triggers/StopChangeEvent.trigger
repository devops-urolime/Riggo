trigger StopChangeEvent on rtms__Stop__ChangeEvent (after insert) {
	Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('StopChangeEvent');
    if(apxTriOnOff != null && apxTriOnOff.isEnable__c){	
        List<Id> StopInsertList = new List<Id>();
        List<Id> StopUpdateList = new List<Id>();
        List<Id> StopDeleteList = new List<Id>();
        Boolean insertHeader = false;
        Boolean updateHeader = false;
        Boolean deleteHeader = false;
        for(rtms__Stop__ChangeEvent lnIt : trigger.new) {
            EventBus.ChangeEventHeader header = lnIt.ChangeEventHeader; 
            if (header.changetype == 'CREATE') { 
                StopInsertList.addAll(header.recordIds);
                insertHeader = true;
            }
            if (header.changetype == 'UPDATE') { 
                StopUpdateList.addAll(header.recordIds);
                UpdateHeader = true;
             }
        }
        
        if(StopInsertList != null){
            Set<Id> StopInsertSet = new Set<Id>();
            for(Id st : StopInsertList){
                StopInsertSet.add(st);
            }
             if(StopInsertSet != null){
                if(insertHeader){
                    StopAPICalloutCtrl.makePostCallout(StopInsertSet,'Insert');
                }
            }
            
        }
       
        if(StopUpdateList != null){
            Set<Id> StopUpdatetSet = new Set<Id>();
            for(Id st : StopUpdateList){
                StopUpdatetSet.add(st);
            }
            if(StopUpdatetSet != null){
                if(updateHeader){
                    StopAPICalloutCtrl.makePostCallout(StopUpdatetSet,'Update');
                }
            }
        }   
    }
}
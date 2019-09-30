trigger LoadChangeEvent on rtms__Load__ChangeEvent (after insert) {
	Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('LoadChangeEvent');
    if(apxTriOnOff != null && apxTriOnOff.isEnable__c){	
        for(rtms__Load__ChangeEvent ld : trigger.new) {
            EventBus.ChangeEventHeader header = ld.ChangeEventHeader; 
            if (header.changetype == 'CREATE') {  
                //The trigger iterates all Load with changeType field is 'UPDATE'
                LoadAPICalloutCtrl.makePostCallout(header.recordIds[0],'Insert');
            }
            if (header.changetype == 'UPDATE') {  
                //The trigger iterates all Load with changeType field is 'UPDATE'
                LoadAPICalloutCtrl.makePostCallout(header.recordIds[0],'Update');
             }
        }
    } 
}
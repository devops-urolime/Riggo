trigger LoadsTriggerforAPI on rtms__Load__c (before delete) {
    Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('LoadsTriggerforAPI');
    if(apxTriOnOff != null && apxTriOnOff.isEnable__c){
        // Trigger is on before update event 
        if(trigger.isbefore && trigger.isdelete){
            for(rtms__Load__c ld : trigger.old){ 
                // Calling webservice callout passing parameters as Load Id and event name
                LoadAPICalloutCtrl.makePostCallout(trigger.old[0].id,'Delete');
            }
        }
    }
}
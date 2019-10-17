trigger LineItemTriggerForAPI on rtms__LineItem__c (before delete) {
    Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('LineItemTriggerForAPI');
    if(apxTriOnOff != null && apxTriOnOff.isEnable__c){	
        // Trigger is on before update event 
        if(trigger.isbefore && trigger.isdelete){
            Set<Id> deleteId = new Set<Id>();
            for(rtms__LineItem__c stp : trigger.old){ 
                // Calling webservice callout passing parameters as Load Id and event name
                deleteId.add(trigger.old[0].id);
            }
            if(deleteId != null){
                 LineItemAPICalloutCtrl.makePostCallout(deleteId,'Delete');
            }
        }
    }
}
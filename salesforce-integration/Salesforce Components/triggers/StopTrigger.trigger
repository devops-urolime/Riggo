trigger StopTrigger on rtms__Stop__c (after insert,after update) {
 
    if(trigger.isInsert ){
        Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('StopTriggerForAPIForDetentionEmail');
        if(apxTriOnOff != null && apxTriOnOff.isEnable__c){ 
            DetentionEmailsCtrl.detentionRecordCreation(trigger.newmap.keyset());
         }
    }
    if(trigger.isupdate){
        Apex_Trigger_On_Off__c apxTriOnOff = Apex_Trigger_On_Off__c.getValues('StopTriggerForAPIForDetentionEmail');
        if(apxTriOnOff != null && apxTriOnOff.isEnable__c){ 
            Set<Id> stopIdSet = new Set<Id>();
            for(rtms__Stop__c stp : trigger.new){
                if(stp.rtms__Stop_Status__c != Trigger.oldMap.get(stp.Id).rtms__Stop_Status__c || stp.LoadStatus__c != Trigger.oldMap.get(stp.Id).LoadStatus__c || stp.rtms__Expected_Date__c != Trigger.oldMap.get(stp.Id).rtms__Expected_Date__c || stp.rtms__Appointment_Time__c != Trigger.oldMap.get(stp.Id).rtms__Appointment_Time__c){
                    system.debug('---stp--'+stp);
                    stopIdSet.add(stp.Id);
                }
            }
        	if(stopIdSet != null && stopIdSet.size() > 0){
               DetentionEmailsCtrl.detentionRecordCreation(stopIdSet); 
            }    
         }
        
    }
}
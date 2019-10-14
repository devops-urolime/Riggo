trigger TriggerEmail on Task (after insert) {
    if (trigger.isAfter && trigger.isInsert) { 
        List<Task> newlyInsertedTasks = [SELECT Id From Task WHERE Id IN :trigger.new]; 
        Database.DMLOptions taskNotificationsOptions = new Database.DMLOptions(); 
        taskNotificationsOptions.EmailHeader.triggerUserEmail = true; 
        for (Task c : newlyInsertedTasks ) { 
            Database.update(c, taskNotificationsOptions); 
        } 
    } 
}
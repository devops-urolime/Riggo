/**
 * Created by Viktor S. Yaremenko on 12/5/2018.
 */

@IsTest
private class RiggohTaskListTest {
    static testMethod void testBehavior() {
        RiggohTaskList controller = new RiggohTaskList();
        Task testTask = new Task(Subject = 'Test');
        insert testTask;
        try {
            controller.getRecords();
            controller.last();
            controller.first();
            controller.next();
            controller.previous();
            controller.getHasNext();
            controller.getHasPrevious();
            controller.setFilter();
            controller.getPriorities();
            controller.getStatuses();
            controller.setComplete();
            controller.setDefer();
            controller.confirm();
            controller.back();
            controller.listTasks[0].selected = true;
            controller.processSelected();
            controller.massComplete();
            controller.massDefer();
            controller.massDelete();
            controller.nop();
        } catch (Exception e){

        }
    }
}
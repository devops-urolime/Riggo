/**
 * Created by Viktor S. Yaremenko on 4/17/2019.
 */

@IsTest
private class RiggohAdminTest {

    @IsTest(SeeAllData=true)
    static void testChainalyticsDataBase() {
        ChainalyticsDataBase.generateReport(Date.today(), Date.today().addDays(-7));
    }

    @IsTest(SeeAllData=true)
    static void testDATExportBase() {
        DATExportBase.generateReport(Date.today(), Date.today().addDays(-7));
    }

    static testMethod void testRiggohAdminCtrl() {
        RiggohAdminCtrl ctrl = new RiggohAdminCtrl();

        ctrl.chaina_rangeEnd = Date.today();
        ctrl.chaina_rangeStart = ctrl.chaina_rangeEnd.addDays(-30);

        ctrl.chaina_setCron();
        ctrl.getchaina_CurrentCronState();
        ctrl.chaina_getReport();
        ctrl.chaina_resetToDefault();

        ctrl.dat_rangeEnd = Date.today();
        ctrl.dat_rangeStart = ctrl.chaina_rangeEnd.addDays(-30);

        ctrl.dat_setCron();
        ctrl.getdat_CurrentCronState();
        ctrl.dat_getReport();
        ctrl.dat_resetToDefault();


        RiggohAdminConfig__c chaina = new RiggohAdminConfig__c(
                Name = ChainalyticsDataBase.CONFIG_KEY,
                Attach_Report_To_Email__c = false,
                Email_For_Reporting__c = UserInfo.getUserEmail(),
                Report_Range__c = 'week',
                Run_Day__c = 1, //sunday
                Save_To_External__c = true
        );
        insert chaina;

        RiggohAdminConfig__c dat = new RiggohAdminConfig__c(
                Name = DATExportBase.CONFIG_KEY,
                Attach_Report_To_Email__c = false,
                Email_For_Reporting__c = UserInfo.getUserEmail(),
                Report_Range__c = 'week',
                Run_Day__c = 1, //sunday
                Save_To_External__c = true
        );
        insert dat;

        Test.startTest();
        String chaina_jobId = System.schedule('testChainalyticsDataScheduler', '0 0 0 3 9 ? 2022', new ChainalyticsDataScheduler());
        CronTrigger chaina_ct = [SELECT Id, CronExpression, TimesTriggered, NextFireTime FROM CronTrigger WHERE id = :chaina_jobId];
        System.assertEquals('0 0 0 3 9 ? 2022', chaina_ct.CronExpression);
        System.assertEquals(0, chaina_ct.TimesTriggered);
        System.assertEquals('2022-09-03 00:00:00', String.valueOf(chaina_ct.NextFireTime));

        String dat_jobId = System.schedule('testDATExportScheduler', '0 0 0 3 9 ? 2022', new DATExportScheduler());
        CronTrigger dat_ct = [SELECT Id, CronExpression, TimesTriggered, NextFireTime FROM CronTrigger WHERE id = :dat_jobId];
        System.assertEquals('0 0 0 3 9 ? 2022', dat_ct.CronExpression);
        System.assertEquals(0, dat_ct.TimesTriggered);
        System.assertEquals('2022-09-03 00:00:00', String.valueOf(dat_ct.NextFireTime));

        Test.stopTest();
    }

}
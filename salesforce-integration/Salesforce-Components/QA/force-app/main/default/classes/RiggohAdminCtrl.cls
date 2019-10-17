/**
 * Created by Viktor S. Yaremenko on 3/29/2019.
 */

public with sharing class RiggohAdminCtrl {

    private Map<String, RiggohAdminConfig__c> defRigConfig = new Map<String, RiggohAdminConfig__c>();

    //Chainalitics
    public Date chaina_rangeStart { get; set; }
    public Date chaina_rangeEnd { get; set; }

    public String chaina_cronLogContent { get; set; }
    public String chaina_currentJobId { get; set; }
    public String chaina_genReportResult { get; set; }
    public String chaina_genDocumentUrl { get; set; }
    public Boolean chaina_isSaveToExternal { get; set; }

    public Integer chaina_cronRunDay { get; set; }
    public Integer chaina_cronRangeDays { get; set; }
    public Boolean chaina_isCronSendEmail { get; set; }
    public String chaina_cronEmailAddress { get; set; }
    public Boolean chaina_isCronSaveToExternal { get; set; }

    //DAT
    public Date dat_rangeStart { get; set; }
    public Date dat_rangeEnd { get; set; }

    public String dat_cronLogContent { get; set; }
    public String dat_currentJobId { get; set; }
    public String dat_genReportResult { get; set; }
    public String dat_genDocumentUrl { get; set; }
    public Boolean dat_isSaveToExternal { get; set; }
    public String dat_externalFolder { get; set; }

    public Integer dat_cronRunDay { get; set; }
    public Integer dat_cronRangeDays { get; set; }
    public Boolean dat_isCronSendEmail { get; set; }
    public String dat_cronEmailAddress { get; set; }
    public Boolean dat_isCronSaveToExternal { get; set; }

    public RiggohAdminCtrl() {

        RiggohAdminConfig__c chaina = RiggohAdminConfig__c.getValues(ChainalyticsDataBase.CONFIG_KEY);
        if (chaina == null) {
            chaina = new RiggohAdminConfig__c(
                    Attach_Report_To_Email__c = false,
                    Email_For_Reporting__c = UserInfo.getUserEmail(),
                    Report_Range__c = 'week',
                    Run_Day__c = 1, //sunday
                    Save_To_External__c = true,
                    External_Folder__c = ''
            );
        }
        defRigConfig.put(ChainalyticsDataBase.CONFIG_KEY, chaina);
        this.chaina_isSaveToExternal = false;
        this.chaina_isCronSendEmail = chaina.Attach_Report_To_Email__c;
        this.chaina_isCronSaveToExternal = chaina.Save_To_External__c;
        this.chaina_cronEmailAddress = chaina.Email_For_Reporting__c;
        this.chaina_cronRunDay = Integer.valueOf(chaina.Run_Day__c);
        this.chaina_cronRangeDays = 7; //Report_Range__c = 'week
        this.chaina_rangeEnd = Date.today().addDays(-1);
        this.chaina_rangeStart = Date.today().addDays(-7);
        this.chaina_cronLogContent = '';


        RiggohAdminConfig__c dat = RiggohAdminConfig__c.getValues(DATExportBase.CONFIG_KEY);
        if (dat == null) {
            dat = new RiggohAdminConfig__c(
                    Attach_Report_To_Email__c = false,
                    Email_For_Reporting__c = UserInfo.getUserEmail(),
                    Report_Range__c = 'day',
                    Run_Day__c = 1, //sunday
                    Save_To_External__c = true,
                    External_Folder__c = ''
            );
        }
        defRigConfig.put(DATExportBase.CONFIG_KEY, dat);

        this.dat_isSaveToExternal = false;
        this.dat_isCronSendEmail = dat.Attach_Report_To_Email__c;
        this.dat_isCronSaveToExternal = dat.Save_To_External__c;
        this.dat_cronEmailAddress = dat.Email_For_Reporting__c;
        this.dat_cronRunDay = Integer.valueOf(dat.Run_Day__c);
        this.dat_cronRangeDays = 0; //Report_Range__c = 'week
        this.dat_rangeEnd = Date.today().addDays(-1);
        this.dat_rangeStart = Date.today().addDays(-14);
        this.dat_cronLogContent = '';
        this.dat_externalFolder = dat.External_Folder__c;
    }

    //Chainalitics
    public String getchaina_CurrentCronState() {
        List<CronJobDetail> cjds = [
                SELECT Id, Name, JobType
                FROM CronJobDetail
                WHERE Name = 'ChainalyticsDataScheduler'
        ];
        if (cjds.isEmpty()) {
            return 'Not runned.';
        }
        List<CronTrigger> cts = [
                SELECT Id, State, CronExpression, TimesTriggered, NextFireTime
                FROM CronTrigger
                WHERE CronJobDetailId = :cjds[0].Id
        ];
        if (cts.isEmpty()) {
            return 'No trigger runned.';
        }
        this.chaina_currentJobId = cts[0].Id;
        return 'Cron Expression:' + cts[0].CronExpression + '<br />Next Fire Time: ' +
                cts[0].NextFireTime + '<br />Times Triggered: ' + cts[0].TimesTriggered;
    }

    public PageReference chaina_getReport() {
        this.chaina_genReportResult = 'Generating the Report';
        String docId = ChainalyticsDataBase.generateReport(this.chaina_rangeStart, this.chaina_rangeEnd);
        if (String.isNotBlank(docId)) {
            this.chaina_genReportResult = 'The Report has been generated';
            this.chaina_genDocumentUrl = '/' + docId;///servlet/servlet.FileDownload?file=
            if (this.chaina_isSaveToExternal) {
                ChainalyticsDataBase.sendToOneDriveAsync(docId);
            }
        } else {
            this.chaina_genReportResult = 'The Report has not been generated. Empty result.';
        }
        return null;
    }

    public PageReference chaina_setCron() {
        if (String.isNotBlank(chaina_currentJobId)) {
            system.abortJob(chaina_currentJobId);
        }

        RiggohAdminConfig__c chaina = defRigConfig.get(ChainalyticsDataBase.CONFIG_KEY);

        chaina.Name = ChainalyticsDataBase.CONFIG_KEY;
        chaina.Attach_Report_To_Email__c = this.chaina_isCronSendEmail;
        chaina.Save_To_External__c = this.chaina_isCronSaveToExternal;
        chaina.Email_For_Reporting__c = this.chaina_cronEmailAddress;
        chaina.Run_Day__c = (Decimal) this.chaina_cronRunDay;
        chaina.Report_Range__c = 'week'; //this.cronRangeDays = 7;
        upsert chaina;
        defRigConfig.put(ChainalyticsDataBase.CONFIG_KEY, chaina);
        if ( !Test.isRunningTest() ) {
            ChainalyticsDataScheduler cds = new ChainalyticsDataScheduler();
            // Seconds Minutes Hours Day_of_month Month Day_of_week Optional_year
            String sch = '0 5 0 ? * ' + this.chaina_cronRunDay;
            String jobID = system.schedule('ChainalyticsDataScheduler', sch, cds);
            this.chaina_cronLogContent = 'Saved. JobId: ' + jobID;
        }
        return null;
    }

    public PageReference chaina_resetToDefault() {
        RiggohAdminConfig__c chaina = defRigConfig.get(ChainalyticsDataBase.CONFIG_KEY);

        this.chaina_isCronSendEmail = chaina.Attach_Report_To_Email__c;
        this.chaina_isCronSaveToExternal = chaina.Save_To_External__c;
        this.chaina_cronEmailAddress = chaina.Email_For_Reporting__c;
        this.chaina_cronRunDay = Integer.valueOf(chaina.Run_Day__c);
        this.chaina_cronRangeDays = 7; //this.defConfig.Report_Range__c = 'week
        this.chaina_cronLogContent = '';

        return null;
    }

    //DAT
    public String getdat_CurrentCronState() {
        List<CronJobDetail> cjds = [
                SELECT Id, Name, JobType
                FROM CronJobDetail
                WHERE Name = 'DATExportScheduler'
        ];
        if (cjds.isEmpty()) {
            return 'Not runned.';
        }
        List<CronTrigger> cts = [
                SELECT Id, State, CronExpression, TimesTriggered, NextFireTime
                FROM CronTrigger
                WHERE CronJobDetailId = :cjds[0].Id
        ];
        if (cts.isEmpty()) {
            return 'No trigger runned.';
        }
        this.dat_currentJobId = cts[0].Id;
        return 'Cron Expression:' + cts[0].CronExpression + '<br />Next Fire Time: ' +
                cts[0].NextFireTime + '<br />Times Triggered: ' + cts[0].TimesTriggered;
    }

    public PageReference dat_getReport() {
        this.dat_genReportResult = 'Generating the Report';
        String docId = DATExportBase.generateReport(this.dat_rangeStart, this.dat_rangeEnd);
        if (String.isNotBlank(docId)) {
            this.dat_genReportResult = 'The Report has been generated';
            this.dat_genDocumentUrl = '/' + docId;///servlet/servlet.FileDownload?file=
            if (this.dat_isSaveToExternal) {
                DATExportBase.sendToFtpAsync(docId, this.dat_externalFolder);
            }
        } else {
            this.dat_genReportResult = 'The Report has not been generated. Empty result.';
        }

        return null;
    }

    public PageReference dat_setCron() {
        if (String.isNotBlank(dat_currentJobId)) {
            system.abortJob(dat_currentJobId);
        }

        RiggohAdminConfig__c dat = defRigConfig.get(DATExportBase.CONFIG_KEY);

        dat.Name = DATExportBase.CONFIG_KEY;
        dat.Attach_Report_To_Email__c = this.dat_isCronSendEmail;
        dat.Save_To_External__c = this.dat_isCronSaveToExternal;
        dat.Email_For_Reporting__c = this.dat_cronEmailAddress;
        dat.Run_Day__c = (Decimal) this.dat_cronRunDay;
        dat.Report_Range__c = 'week'; //this.cronRangeDays = 7;
        dat.External_Folder__c = this.dat_externalFolder;

        upsert dat;
        defRigConfig.put(DATExportBase.CONFIG_KEY, dat);

        if ( !Test.isRunningTest() ) {
            DATExportScheduler cds = new DATExportScheduler();
            // Seconds Minutes Hours Day_of_month Month Day_of_week Optional_year
            String sch = '0 5 0 ? * *';// + this.dat_cronRunDay;
            String jobID = system.schedule('DATExportScheduler', sch, cds);
            this.dat_cronLogContent = 'Saved. JobId: ' + jobID;
        }
        return null;
    }

    public PageReference dat_resetToDefault() {
        RiggohAdminConfig__c dat = defRigConfig.get(DATExportBase.CONFIG_KEY);

        this.dat_isCronSendEmail = dat.Attach_Report_To_Email__c;
        this.dat_isCronSaveToExternal = dat.Save_To_External__c;
        this.dat_cronEmailAddress = dat.Email_For_Reporting__c;
        this.dat_cronRunDay = Integer.valueOf(dat.Run_Day__c);
        this.dat_cronRangeDays = 7; //this.defConfig.Report_Range__c = 'week
        this.dat_cronLogContent = '';
        this.dat_externalFolder = '';

        return null;
    }

}
/**
 * Created by Viktor S. Yaremenko on 4/1/2019.
 */

global with sharing class DATExportScheduler implements Schedulable {

    global void execute(SchedulableContext SC) {
        RiggohAdminConfig__c dat = RiggohAdminConfig__c.getValues(DATExportBase.CONFIG_KEY);
        if (dat == null) {
            throw new DATExportBase.DATExportBaseException(DATExportBase.CONFIG_KEY + ' is empty.');
        }
        Boolean isCronSendEmail = dat.Attach_Report_To_Email__c;
        Boolean isCronSaveToOne = dat.Save_To_External__c;
        String cronEmailAddress = String.isNotBlank(dat.Email_For_Reporting__c) ?
                dat.Email_For_Reporting__c : UserInfo.getUserEmail();
        //cronRangeDays = 7; //Report_Range__c = 'week
        Date rangeEnd = Date.today().addDays(-1);
        Date rangeStart = Date.today().addDays(-14);
        String folderId = dat.External_Folder__c;
        String docId = DATExportBase.generateReport(rangeStart, rangeEnd);

        if (!Test.isRunningTest()) {
            if (String.isNotBlank(docId) && isCronSaveToOne) {
                DATExportBase.sendToFtpAsync(docId, folderId);
            }
            Messaging.SingleEmailMessage mail = new Messaging.SingleEmailMessage();
            mail.setToAddresses(new List<String>{
                    cronEmailAddress
            });
            mail.setSubject('DATExportBaseScheduler Report');
            if (isCronSendEmail) {
                mail.setEntityAttachments(new List<String>{
                        docId
                });
            }
            if (String.isNotBlank(docId)) {
                String docURL = URL.getSalesforceBaseUrl().toExternalForm() + '/' + docId;
                mail.setHtmlBody('<p>DATExportBaseScheduler done.</p><p>Document link: ' + docURL + '</p>');
            }
            mail.setPlainTextBody('DATExportBaseScheduler done.');
            Messaging.sendEmail(new Messaging.SingleEmailMessage[]{
                    mail
            });
        }
    }

}
/**
 * Created by Viktor S. Yaremenko on 11/10/2018.
 */

global class ChainalyticsDataScheduler implements Schedulable {

    global void execute(SchedulableContext SC) {
        RiggohAdminConfig__c chaina = RiggohAdminConfig__c.getValues(ChainalyticsDataBase.CONFIG_KEY);
        if (chaina == null) {
            throw new ChainalyticsDataBase.ChainalyticsDataBaseException(ChainalyticsDataBase.CONFIG_KEY + ' is empty.');
        }
        Boolean isCronSendEmail = chaina.Attach_Report_To_Email__c;
        Boolean isCronSaveToOne = chaina.Save_To_External__c;
        String cronEmailAddress = String.isNotBlank(chaina.Email_For_Reporting__c) ?
                chaina.Email_For_Reporting__c : UserInfo.getUserEmail();
        //cronRangeDays = 7; //Report_Range__c = 'week
        Date rangeEnd = Date.today().addDays(-1);
        Date rangeStart = Date.today().addDays(-7);
        String docId = ChainalyticsDataBase.generateReport(rangeStart, rangeEnd);

        if (!Test.isRunningTest()) {
            if (String.isNotBlank(docId) && isCronSaveToOne) {
                ChainalyticsDataBase.sendToOneDriveAsync(docId);
            }
            Messaging.SingleEmailMessage mail = new Messaging.SingleEmailMessage();
            mail.setToAddresses(new List<String>{
                    cronEmailAddress
            });
            mail.setSubject('ChainalyticsDataScheduler Report');
            if (isCronSendEmail) {
                mail.setEntityAttachments(new List<String>{
                        docId
                });
            }
            if (String.isNotBlank(docId)) {
                String docURL = URL.getSalesforceBaseUrl().toExternalForm() + '/' + docId;
                mail.setHtmlBody('<p>ChainalyticsDataScheduler done.</p><p>Document link: ' + docURL + '</p>');
            }
            mail.setPlainTextBody('ChainalyticsDataScheduler done.');
            Messaging.sendEmail(new Messaging.SingleEmailMessage[]{
                    mail
            });
        }
    }
}
public class APIExceptionHandler {
    public static void erroLogCreation(Exception exceptionMessage, HttpResponse http,string payload){
        Error_Log__c errorlog = new Error_Log__c();
        if(exceptionMessage != null){
            system.debug(LoggingLevel.ERROR,'Error when initiating the http Request in initiateHTTPCall():'+exceptionMessage.getMessage());
        	errorlog.Error__c = exceptionMessage.getMessage();    
        }
        errorlog.Payload__c = payload;
        errorlog.Description__c = http.getBody();
        errorlog.Error_Code__c = string.valueof(http.getStatusCode());
        insert errorlog; 
        system.debug('--errorlog--'+errorlog);
       
    }
}
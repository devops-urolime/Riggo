/**************************************************************************
-----------------------------------------------
Purpose:
This class contains the methods for HTTP Rest web service Callouts 
-----------------------------------------------------------------------
**************************************************************************/
public with sharing class ServiceProxy{
    /*********************************************************************
Purpose     : To initiate the the HTTP request 
Parameters  : endPoint- Target endpoint of the service
methodName- HTTP method to be used 
requestBody - JSON Body to be sent in request
Returns     : String in JSON format containing the response from
the HTTP service**
**********************************************************************/
    /* public string clientKey = 'tBI1EU8rbwZrUZ27giNcW1ecG2CLe0N7';
public string clientSecret ='Ucs3BK6GbhvQ4ALpYL5k_tz-8TGvYAwHrcFsL6COXi2b1ekz1jK0lXPxvui4BR1m';
public string setEndpoint = 'https://riggo-staging.auth0.com/oauth/token';
*/
    public static Integer errorCode ;
    public static integer calloutForGetAccessToken = 0;
    public HttpResponse callRestService(String endPointURL, String methodName, String requestBody,String objectName,String accessToken){
        String response=null;
        Boolean doesTokenReferesh = false;
        //string accessToken = null;
        HttpResponse httpResponse=null;
        try { 
            calloutForGetAccessToken++;
            system.debug('-accessToken--'+accessToken);
            if(accessToken == null){
            	accessToken = getAccessTokenFromConfiguration(); 
            }
            system.debug('--calloutForGetAccessToken---'+calloutForGetAccessToken);
            httpResponse=initiateHTTPCall(endPointURL,methodName,requestBody,accessToken,objectName); 
            response = httpResponse.getBody();
            errorCode = httpResponse.getStatusCode();
            system.debug('--errorCode---'+errorCode);
            if(errorCode == 401 ){
                system.debug('--errorCode---'+errorCode);
                accessToken =  refreshAccessToken(); 
                system.debug('--accessToken---'+accessToken);
                doesTokenReferesh = true;
                // Again calling end point url with valid token
                 if(calloutForGetAccessToken < 5){
                     system.debug('---Calling--'+calloutForGetAccessToken);
                    callRestService(endPointURL,methodName,requestBody,objectName,accessToken);
                }else{
                    if(errorCode == 401){
                        APIExceptionHandler.erroLogCreation(null,httpResponse,requestBody);   
                    }
                }
                errorCode = null;
            }
            if(errorCode == 403){
                String status = httpResponse.getStatus();
                if(status != null){
                    if(status.contains('Access is denied')){
                        APIExceptionHandler.erroLogCreation(null,httpResponse,requestBody);
                    }else{
                        APIExceptionHandler.erroLogCreation(null,httpResponse,requestBody);
                    }
                    //string.valueof(http.getStatusCode());
                }else{
                    //status is null
                    APIExceptionHandler.erroLogCreation(null,httpResponse,requestBody);
                }
                // write out error to ErrorLog.
                // write out response to the ErrorLog for each case.
                // 3 cases : Ed will provide you sample responses from each case. 
                //    403 is provided by the API Services
                //            write to LOG REFRESHTOKEN API Service returned a 403.  
                //    403 is provided by the API Gateway as a glomar response.
                //            write to LOG REFRESHTOKEN API Gateway glomar returned a 403.
		// ELSE no matched case
                //     write to LOG No matched case.
            }
            
            
        } catch(Exception e) {
            system.debug(LoggingLevel.ERROR,'Error when initiating the http Request in callRestService():'+e.getMessage());
        }
        system.debug(LoggingLevel.DEBUG,'ServiceProxy:callRestService'+response);
        if(doesTokenReferesh){
            Auth0_API_Token__c authAPITokenupdate = [select id,Client_Key__c,Client_Secret__c,End_Point__c,Token__c from Auth0_API_Token__c where Enable__c = true limit 1];
            authAPITokenupdate.Token__c = accessToken;
            authTokenUpdate(authAPITokenupdate);
        }
        return httpResponse;    
    }
    private void authTokenUpdate(Auth0_API_Token__c authUpdate){
        // Updating letest Authtoken in salesforce
        update authUpdate;
    }
    private string getAccessTokenFromConfiguration(){
        List<Auth0_API_Token__c> authAPIToken=null;
        authAPIToken = [select id,Client_Key__c,Client_Secret__c,End_Point__c,Token__c from Auth0_API_Token__c where Enable__c = true limit 1];
        if(authAPIToken.size() > 0 && authAPIToken[0].Token__c != null){
            system.debug('---token if already exist');
            return authAPIToken[0].Token__c;
        }else{
            system.debug('---token if refresh');
            return refreshAccessToken(); 
            
        } 
    }
    private string refreshAccessToken(){
        List<Auth0_API_Token__c> authAPIToken=null;
        authAPIToken = [select id,Client_Key__c,Client_Secret__c,End_Point__c,Token__c from Auth0_API_Token__c where Enable__c = true limit 1];
        
        String accessToken = null;
        String body = '{"client_id": "'+authAPIToken[0].Client_Key__c+'",'+
            '"client_secret": "'+authAPIToken[0].Client_Secret__c+'",'+
            '"audience":"load-resource-api","grant_type":"client_credentials"'+	'}';
        HttpRequest req = new HttpRequest(); 
        req.setMethod('POST');
        req.setEndpoint(authAPIToken[0].End_Point__c);
        req.setheader('content-type', 'application/json');
        req.setBody(body);
        
        Http http = new Http();        
        HTTPResponse res = http.send(req);
        Map<String, Object> cObjMap = (Map<String, Object>) JSON.deserializeUntyped(res.getBody());
        system.debug('res.getBody---'+res.getBody());
        accessToken  = string.valueof(cObjMap.get('access_token'));
        system.debug('--return 108 --accessToken '+accessToken);
        return accessToken; 
    }        
    private HttpResponse initiateHTTPCall(String endPointURL, String methodName, String requestBody,string accessToken,string objectName){
        String response=null;
        Http httpConnection=null;
        HttpRequest httpRequest=null; 
        HttpResponse httpResponse=null;
        List<Riggo_API_Configuration__c> riggoConfigList=null;
        Integer timeOut;
        String endPoint;
        String contentType;
        String authorizationHeader;
        try { 
            if(endPointURL!=null && methodName!=null){
                Riggo_API_Configuration__c riggoApipostCall = Riggo_API_Configuration__c.getValues('Riggo post');
                timeOut=Integer.valueOf(riggoApipostCall.Time_Out__c);
                contentType=riggoApipostCall.Content_Type__c; 
                endPoint=riggoApipostCall.End_Point_URL__c;
                authorizationHeader=riggoApipostCall.Authorization_Header__c;
                
                httpConnection=new Http();
                httpResponse = new HttpResponse();
                httpRequest= new HttpRequest();
                system.debug('--endPoint--'+endPoint);
                system.debug('--endPointURL--'+endPointURL);
                httpRequest.setEndpoint(endPoint+endPointURL);
                httpRequest.setEndpoint(endPoint+endPointURL);
                httpRequest.setMethod(methodName);    
                
                httpRequest.setTimeout(timeOut);
                httpRequest.setHeader('Content-Type',contentType);
                httpRequest.setHeader('Authorization', 'Bearer '+ accessToken); 
                if(requestBody !='' && requestBody!=null){
                    httpRequest.setBody(requestBody);
                }
                    system.debug('-httpRequest-'+httpRequest);
                httpResponse = httpConnection.send(httpRequest);
                system.debug('--httpResponse--'+httpResponse);
                if(httpResponse!=null){            		
                    response=httpResponse.getBody();
                    integer codeft = httpResponse.getStatusCode();
                    System.debug('web service response: '+response);
                }	      		
            }       		
        }catch(Exception e) {
            APIExceptionHandler.erroLogCreation(e,httpResponse,null);
        }
        system.debug(LoggingLevel.DEBUG,'ServiceProxy:initiateHTTPCall'+response);
        return HttpResponse;    
    } 
}
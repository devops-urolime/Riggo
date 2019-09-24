/**************************************************************************
-----------------------------------------------
Purpose:
This class contains the methods to call HTTP Rest web service and error handler for the Load Object
-----------------------------------------------------------------------
**************************************************************************/

//========================
/*
Class Name  : LoadAPICalloutCtrl
Note		: We are using this class for the calling webserive api to post Load object to market place using rest api.
*/
public class LoadAPICalloutCtrl {
    /*********************************************************************
Purpose     : To initiate the the HTTP request 
Parameters  : LoadID- Load Ojbect Id to generate payload
triggerAction  - To identify the event of the load object either Insert update or Delete
**********************************************************************/
    
    @future(callout=true)
    public static void makePostCallout(string LoadId,String triggerAction) { 
         LoadAPICtrl.makePostCallout(LoadId,triggerAction);
    }    
}
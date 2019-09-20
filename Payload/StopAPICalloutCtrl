/**************************************************************************
-----------------------------------------------
Purpose:
This class contains the methods to call HTTP Rest web service and error handler for the Stop object
-----------------------------------------------------------------------
**************************************************************************/

//========================
/*
Class Name  : StopAPICalloutCtrls
Note		: We are using this class for the calling webserive api to post Stop  object to market place using rest api.
*/
public class StopAPICalloutCtrl {
    /*********************************************************************
Purpose     : To initiate the the HTTP request 
Parameters  : Set of Stop Id
triggerAction  - To identify the event for the Stop object either Insert update or Delete
**********************************************************************/
    
    @future(callout=true)
    public static void makePostCallout(Set<Id> stopIdSet ,String triggerAction) { 
     StopAPICtrl.makePostCallout(stopIdSet,triggerAction);   
    }
}
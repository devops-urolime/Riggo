/**************************************************************************
-----------------------------------------------
Purpose:
This class contains the methods to call HTTP Rest web service and error handler for the LineItem object
-----------------------------------------------------------------------
**************************************************************************/

//========================
/*
Class Name  : LineItemAPICalloutCtrl
Note		: We are using this class for the calling webserive api to post Lineitem  object to market place using rest api.
*/
public class LineItemAPICalloutCtrl {
    /*********************************************************************
Purpose     : To initiate the the HTTP request 
Parameters  : Set of lineitem Id
triggerAction  - To identify the event for the Lineitem object either Insert update or Delete
**********************************************************************/
    
    @future(callout=true)
    public static void makePostCallout(Set<Id> LineItemIdSet ,String triggerAction) { 
        LineItemAPICtrl.makePostCallout(LineItemIdSet,triggerAction);
    }
}
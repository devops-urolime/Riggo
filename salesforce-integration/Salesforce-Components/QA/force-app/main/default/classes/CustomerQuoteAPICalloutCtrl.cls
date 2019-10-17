/**************************************************************************
-----------------------------------------------
Purpose:
This class contains the methods to call HTTP Rest web service and error handler for the Customer Quote object
-----------------------------------------------------------------------
**************************************************************************/

//========================
/*
Class Name  : CustomerQuoteAPICalloutCtrl
Note		: We are using this class for the calling webserive api to post Customer Quote object to market place using rest api.
*/
public class CustomerQuoteAPICalloutCtrl {
    /*********************************************************************
Purpose     : To initiate the the HTTP request 
Parameters  : Set of Stop Id
triggerAction  - To identify the event for the Customer Quote object either Insert update or Delete
**********************************************************************/
    
    @future(callout=true)
    public static void makePostCallout(Set<Id> CustomerQuoteIdSet ,String triggerAction) { 
        CustomerQuoteAPICtrl.makePostCallout(CustomerQuoteIdSet,triggerAction);
    }
}
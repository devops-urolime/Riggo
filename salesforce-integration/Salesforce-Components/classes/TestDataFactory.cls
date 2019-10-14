@isTest
public class TestDataFactory {
    
    public static Account createTestRecordsforCustomer() { 
        Account acc = new Account();
        acc.Name = 'Timezone Location testing';
        acc.Send_Detention_Email__c = true;
        acc.Detention_Policy_in_minutes__c = 30;
        return acc;  
    }
    
    public static Account createTestRecordsforLocation() { 
        Account acc = new Account();
        acc.Name = 'Timezone Location testing';
        acc.Time_Zone__c = 'MDT - Mountain Time';
        return acc;  
    }
    
    public static rtms__Load__c createTestRecordsforLoad() { 
        rtms__Load__c loadobj = new rtms__Load__c();
        
        loadobj.Name = 'Load Test ST';
        loadobj.rtms__Total_Weight__c = 5;
        loadobj.rtms__EDI_Reference_Numbers__c = 'abc ax   ad';
        return loadobj;  
    }
    public static rtms__LineItem__c createTestRecordsforLineItem(Id LoadId) { 
        rtms__LineItem__c LineItem = new rtms__LineItem__c();
        
        LineItem.Name = 'LineItem Test ST';
        LineItem.rtms__Item_Description__c  = 'Description';
        LineItem.rtms__Weight__c = 5;
        LineItem.rtms__Weight_Units__c = 'lbs';
         LineItem.rtms__Load__c = LoadId;
        return LineItem;  
    }
    
    public static rtms__CustomerQuote__c createTestRecordsforCustomerQuote(Id LoadId) { 
        rtms__CustomerQuote__c quote = new rtms__CustomerQuote__c();
        quote.rtms__Load2__c = LoadId;
        quote.rtms__Quote_Date__c = system.today();
        quote.rtms__Status__c = 'Accepted';
        quote.rtms__Customer_Quote_Total__c = 120;
        quote.rtms__Pricing__c = 'Spot';
        
        return quote;  
    }
    
    public static rtms__Stop__c createTestRecordsforStop(Id LoadId) { 
        rtms__Stop__c stop = new rtms__Stop__c();
        stop.Name = 'LineItem Test ST';
        stop.rtms__Load__c = LoadId;
        stop.rtms__Number__c = 3;
        stop.rtms__Expected_Date__c = system.today();
        return stop;  
    }
    
    public static Marketplace_Exposed_Data__c createTestRecordsforMarketPlace(Id LoadId) { 
        Marketplace_Exposed_Data__c mak = new Marketplace_Exposed_Data__c();
        mak.Marketplace_Exposed_Id__c = '1234';
        mak.extSysId__c = '1234';
        mak.Load__c = LoadId;
        
        return mak;  
    }
    
    public static Auth0_API_Token__c createTestRecordsforAPIToken() { 
     Auth0_API_Token__c api = new Auth0_API_Token__c();
        api.Client_Key__c = 'Keyvalye';
        api.Client_Secret__c = 'secretkey';
        api.End_Point__c    = 'http://example.com/example/test';
        api.Enable__c = true;
        return api;
    }
    
}
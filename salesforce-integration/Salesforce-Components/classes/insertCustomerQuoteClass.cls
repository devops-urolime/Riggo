public class insertCustomerQuoteClass{
   public void insertCustomerQuote(){
       rtms__Load__c ld = [select id from rtms__Load__c limit 1];
       rtms__CustomerQuote__c qu = new rtms__CustomerQuote__c();
        qu.rtms__Quote_Date__c = system.today();
        qu.rtms__Status__c = 'Pending';
        qu.rtms__Customer_Quote_Total__c = 120.00;
        qu.rtms__Load2__c = ld.ID;
        qu.rtms__Pricing__c = 'Spot';
        insert qu;
        }
        
   }
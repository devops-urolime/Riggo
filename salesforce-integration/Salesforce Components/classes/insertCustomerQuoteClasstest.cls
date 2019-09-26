@IsTest(SeeAllData=true)
private class insertCustomerQuoteClasstest{
     
    static testMethod void testLoadsTriggerforAPI() {
    insertCustomerQuoteClass cla = new insertCustomerQuoteClass();
    cla.insertCustomerQuote();
    }
    }
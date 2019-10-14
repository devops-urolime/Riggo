public class CustomerQuoteAPICtrl {
	public static void makePostCallout(Set<Id> CustomerQuoteIdSet ,String triggerAction) { 
        system.debug('--CustomerQuoteIdSet--'+CustomerQuoteIdSet); 
        List<rtms__CustomerQuote__c> ListOfCustomerQuote =[Select Id,rtms__Load2__r.Id FROM rtms__CustomerQuote__c where Id IN :CustomerQuoteIdSet];
        Set<Id> loadIdSet = new Set<Id>();
        Map<Id, Set<Id>> customerQuoteSet = new Map<Id, set<Id>>();
 
        for(rtms__CustomerQuote__c li : ListOfCustomerQuote){  
            loadIdSet.add(li.rtms__Load2__r.Id);
            
            if(customerQuoteSet.containsKey(li.rtms__Load2__r.Id)) {
                set<Id> QuoteIdId = customerQuoteSet.get(li.rtms__Load2__r.Id);
                QuoteIdId.add(li.Id);
                customerQuoteSet.put(li.rtms__Load2__r.Id, QuoteIdId);
            } else {
                customerQuoteSet.put(li.rtms__Load2__r.Id, new set<Id> { li.Id });
            }
                    
        }
        for(Id ldId : loadIdSet){
            WrapperClassForAPI.WrapperforMarketPlac wrappMarket = null;
            String methodDefine = 'PUT';
            string marketplaceId = '';
            string extSysId = '';
            
            Boolean isMarketplaceIdAvailable = true;
          /*  if(triggerAction == 'Update'){
                methodDefine = 'PUT';
            } */
            system.debug('--ldId---'+ldId);
        	wrappMarket = GetMarketPlaceDetails.getMarketPlaceDetails(ldId,triggerAction);
                system.debug('--wrappMarket--'+wrappMarket);
                if(wrappMarket != null){
                    isMarketplaceIdAvailable = wrappMarket.isMarketplaceIdAvailable;
                    marketplaceId = wrappMarket.marketplaceId;
                    extSysId = wrappMarket.extSysId;
                }
            
             if(isMarketplaceIdAvailable){
                responeClass resResult=null;
                CustomerQuotePayload palyloadCls = new CustomerQuotePayload();
                // Get the load payload by json formate.
                set<Id> QuoteIdId = customerQuoteSet.get(ldId);
                string CustomerInvoicePayloadBody=palyloadCls.getSavePayload(QuoteIdId,triggerAction); 
                system.debug('--Invoice Payload---'+CustomerInvoicePayloadBody);
                
                // Calling webserive to push the load object  Passing parameters like end pint url, Method Name and Load payload
                HttpResponse httpResponse=null;
                ServiceProxy proxy = new ServiceProxy(); 
                 if(extSysId != null && extSysId != ''){
                     system.debug('----inside if');
                    HttpResponse = proxy.callRestService('/load/'+extSysId+'/invoice', methodDefine, CustomerInvoicePayloadBody,'Load',null);     
                 }else{
                     // without extSysId id /api/v1/load/invoice
                     system.debug('----inside else');
                     HttpResponse = proxy.callRestService('/load/invoice', methodDefine, CustomerInvoicePayloadBody,'Load',null);
                 }
                
                 if(httpResponse != null){
                    String response =  httpResponse.getBody();
                    system.debug('--response'+response);
                    
                    // if there is any exception we are creating exception record in Error Log object
                    if(response!= null){
                        string StatusCode = string.valueof(httpResponse.getStatusCode());
                        if(response!= null && StatusCode != '200'){
                            APIExceptionHandler.erroLogCreation(null,httpResponse,CustomerInvoicePayloadBody);
                        }
                    }
                 }
            }    
        }
    }
    public class responeClass{
        public string id{get;set;}
        public string message{get;set;}
    }
}
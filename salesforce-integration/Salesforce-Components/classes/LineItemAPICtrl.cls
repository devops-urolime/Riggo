public class LineItemAPICtrl {
	public static void makePostCallout(Set<Id> LineItemIdSet ,String triggerAction) { 
    List<rtms__LineItem__c> ListOfLineitem =[Select Id,rtms__Load__r.Id FROM rtms__LineItem__c where Id IN :LineItemIdSet];
        Set<Id> loadIdSet = new Set<Id>();
        Map<Id, Set<Id>> lineItemSet = new Map<Id, set<Id>>();
        
        for(rtms__LineItem__c li : ListOfLineitem){  
            loadIdSet.add(li.rtms__Load__r.Id);
            
            if(lineItemSet.containsKey(li.rtms__Load__r.Id)) {
                set<Id> getlineitemId = lineItemSet.get(li.rtms__Load__r.Id);
                getlineitemId.add(li.Id);
                lineItemSet.put(li.rtms__Load__r.Id, getlineitemId);
            } else {
                lineItemSet.put(li.rtms__Load__r.Id, new set<Id> { li.Id });
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
                LineItemPayLoad palyloadCls = new LineItemPayLoad();
                // Get the load payload by json formate.
                set<Id> getLineitem = lineItemSet.get(ldId);
                string lineitemPayloadBody=palyloadCls.getSavePayload(getLineitem,triggerAction); 
                system.debug('--lineitem Payload---'+lineitemPayloadBody);
                
                // Calling webserive to push the load object  Passing parameters like end pint url, Method Name and Load payload
                HttpResponse httpResponse=null;
                ServiceProxy proxy = new ServiceProxy(); 
                if(extSysId != null && extSysId != ''){
                    system.debug('----inside if');
                    HttpResponse = proxy.callRestService('/load/'+extSysId+'/line-item', methodDefine, lineitemPayloadBody,'LineItem',null);     
                }else{
                    // without extSysId id /api/v1/load/lineitemf
                    system.debug('----inside else');
                    HttpResponse = proxy.callRestService('/load/line-item', methodDefine, lineitemPayloadBody,'LineItem',null);
                }
                
                if(httpResponse != null){
                    String response =  httpResponse.getBody();
                    // if there is any exception we are creating exception record in Error Log object
                    if(response!= null){
                        string StatusCode = string.valueof(httpResponse.getStatusCode());
                        if(response!= null && StatusCode != '200'){
                            APIExceptionHandler.erroLogCreation(null,httpResponse,lineitemPayloadBody);
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
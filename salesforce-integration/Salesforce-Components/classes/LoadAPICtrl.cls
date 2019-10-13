public class LoadAPICtrl {
	public static void makePostCallout(string LoadId,String triggerAction) { 
    	WrapperClassForAPI.WrapperforMarketPlac wrappMarket = null;
        String methodDefine = 'POST';
        String marketplaceId = '';
        Boolean isMarketplaceIdAvailable = true;
         
        if(triggerAction == 'Update' || triggerAction == 'Update Batch'){
			methodDefine = 'PUT';
        }  
         /*wrappMarket = GetMarketPlaceDetails.getMarketPlaceDetails(LoadId,triggerAction);
        system.debug('--wrappMarket--'+wrappMarket);
        if(wrappMarket != null){
            isMarketplaceIdAvailable = wrappMarket.isMarketplaceIdAvailable;
            marketplaceId = wrappMarket.MarketPlaceId;
            system.debug('--isMarketplaceIdAvailable-'+isMarketplaceIdAvailable);
            system.debug('--marketplaceId-'+marketplaceId);
        } */
      //  if(isMarketplaceIdAvailable){
            responeClass resResult=null;
            
            LoadPayload palyloadCls = new LoadPayload();
            // Get the load payload by json formate.
            string LoadPayloadBody=palyloadCls.getSavePayload(LoadId,triggerAction,methodDefine,marketplaceId); 
            system.debug('--Load Payload---'+LoadPayloadBody);
            
            // Calling webserive to push the load object  Passing parameters like end pint url, Method Name and Load payload
            HttpResponse httpResponse=null;
            ServiceProxy proxy = new ServiceProxy(); 
            HttpResponse = proxy.callRestService('/load', methodDefine, LoadPayloadBody,'Load',null);
            String response = null;

            Map< String, Object> resMap = new Map< String, Object>();
            if(HttpResponse != null && HttpResponse.getStatusCode() == 200 ){
            try{
                response =  httpResponse.getBody();
                system.debug('---response---'+response);
                if(response != null && response != ''){
                    system.debug('---response---'+response);
                    
                    resMap = (Map<String, Object>)JSON.deserializeUntyped(response);
                    
                    system.debug('---resMap---'+resMap);
                    
                    if( resMap != null && resMap.get('hashId') != null){
                        if(triggerAction == 'Insert' || triggerAction == 'Insert Batch'){
                            Marketplace_Exposed_Data__c makP = new Marketplace_Exposed_Data__c();
                            
                            system.debug('---response hash id---'+resMap.get('hashId'));
                            makP.Marketplace_Exposed_Id__c = String.valueOf(resMap.get('hashId'));
                            
                            List<Object> data = (List<Object>)resMap.get('data');
                            system.debug('---data size---'+data.size());
                            Map< String, Object > dataMap = (Map<String, Object>)data[0];
                            system.debug('---dataMap---'+dataMap);
                            if(data != null && data.size()>0 && dataMap != null ){
                                makP.extSysId__c = String.valueof(dataMap.get('id'));
                            }
                            makP.Load__c = LoadId;
                            insert makp;
                            system.debug('---makp'+makp);    
                        }
                    }
                    /*
                    resResult = (responeClass)JSON.deserialize(response, LoadAPICtrl.responeClass.class);
                    if(resResult != null && resResult.hashId != null){
                        if(triggerAction == 'Insert' || triggerAction == 'Insert Batch'){
                            Marketplace_Exposed_Data__c makP = new Marketplace_Exposed_Data__c();
                            makP.Marketplace_Exposed_Id__c = resResult.hashId;
                            if(resResult.Data != null){
                                makP.extSysId__c = String.valueof(resResult.Data[0].id);
                            }
                            makP.Load__c = LoadId;
                            insert makp;
                            system.debug('---makp'+makp);
                        }
                    }*/ 
                }
            }catch(Exception e){
               system.debug(e.getMessage());
            }
            }
            
            if(httpResponse != null){
                string StatusCode = string.valueof(httpResponse.getStatusCode());
                system.debug('--StatusCode-54-'+StatusCode);
                // if there is any exception we are creating exception record in Error Log object
                if(StatusCode != '200'){
                    APIExceptionHandler.erroLogCreation(null,httpResponse,LoadPayloadBody);
                }
            } 
       /* }else{
            system.debug('--Load does not have market place id in PUT request');
        }*/
    }
}
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
            if(HttpResponse != null && HttpResponse.getStatusCode() == 200 ){
                response =  httpResponse.getBody();
                system.debug('---response---'+response);
                if(response != null && response != ''){
                    system.debug('---response---'+response);
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
                    } 
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
    // return Resonse wrapper class 
    public class responeClass{
        public Integer status;
        public String message;
        public List<Data> data;
        public String hashId;
        
    }
    public class Data {
		public Integer id;
		public String extSysId;
		public String extSysTenantId;
		public String expectedShipDate;
		public decimal siteId;
		public String carrier;
		public String transportMode;
		public String postedRate;
		public decimal insuranceAmt;
		public String equipmentTypeId;
		public Integer loadStatus;
		public Boolean teamReq;
		public Boolean foodGradeTrailerReq;
		public Boolean tempControlReq;
		public String expectedDeliveryDate;
		public String driver;
		public String locationBasedSvcsReq;
		public String referenceNumber;
		public String billOfLadingNo;
		public String name;
		public decimal invoiceTotal;
		public String customerInvoiceTotal;       
		public String carrierQuoteTotal;
		public String carrierInvoiceTotal;
		public String customerQuoteTotal;
		public String customerTransportTotal;
		public Boolean hazMat;
		public String loadStatusReq;
		public String modeName;
		public String orderDate;
		public String salesStatus;
		public String loadUrl;
		public Integer shipperId;
		public String distanceMiles;
	}
}
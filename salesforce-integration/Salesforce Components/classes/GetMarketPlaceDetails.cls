public class GetMarketPlaceDetails {
    public static WrapperClassForAPI.WrapperforMarketPlac getMarketPlaceDetails(String LoadId,string triggerAction){
        
        string marketplaceId = '';
        List<Marketplace_Exposed_Data__c> marketPlaceList = [select id,Marketplace_Exposed_Id__c,extSysId__c from Marketplace_Exposed_Data__c where Load__c =:LoadId limit 1];
        
        List<rtms__Load__c> ListOfLoead =[Select Id, createdDate from rtms__Load__c where ID =:LoadId];
        
        if(marketPlaceList!= null && marketPlaceList.size() > 0 ){
            system.debug('---if----10');
            marketplaceId = marketPlaceList[0].Marketplace_Exposed_Id__c;
            WrapperClassForAPI.WrapperforMarketPlac wrapp = new WrapperClassForAPI.WrapperforMarketPlac();
            wrapp.marketplaceId = marketPlaceList[0].Marketplace_Exposed_Id__c;
            wrapp.extSysId = marketPlaceList[0].extSysId__c;
            wrapp.isMarketplaceIdAvailable = true;
            return wrapp;
        }else{
            if((marketplaceId == null || marketplaceId == '') && ListOfLoead != null && ListOfLoead.size() > 0){
                Integer decMinutes = Integer.valueof((((system.now().getTime())/1000/60) - (ListOfLoead[0].createdDate.getTime())/1000/60));
                system.debug('---decMinutes'+decMinutes);
                system.debug('--triggerAction---'+triggerAction);
         
                if(decMinutes < 5 && (triggerAction != 'Insert Batch' && triggerAction != 'Update Batch')){
                    system.debug('---decMinutes'+decMinutes); 
                     WrapperClassForAPI.WrapperforMarketPlac wrapp = new WrapperClassForAPI.WrapperforMarketPlac();
                    wrapp.marketplaceId = '';
                    wrapp.extSysId = '';
                    wrapp.isMarketplaceIdAvailable = false;
                    return wrapp;
                } 
            } 
        }
        
        
        return null;
    }
    
}
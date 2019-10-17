public class CustomerQuotePayload {
    public string getSavePayload(Set<ID> CustomerQuoteIdSet,String triggerAction){ 
        system.debug('CustomerQuoteIdSet'+CustomerQuoteIdSet);   
        List<rtms__Stop__c> stopList = null;
        String payload  = '';
        if(triggerAction == 'Insert' || triggerAction == 'Update'  || triggerAction == 'Apex Batch'   || triggerAction == 'Update Batch'){
           List<rtms__CustomerQuote__c> ListOfCustomerQuote =[Select Id,rtms__Distance_Miles__c,rtms__Quote_Date__c,rtms__Status__c,rtms__Comments__c,rtms__Net_Line_Haul__c,
                                                              rtms__Fuel_Surcharge__c,rtms__Accessorial_Charges__c,rtms__Transportation_Total__c,
                                                              rtms__Customer_Quote_Total__c,rtms__Load2__c,rtms__Load2__r.Id  FROM rtms__CustomerQuote__c  
                                          where Id  IN :CustomerQuoteIdSet]; 
            
           
            Integer customerQuoteRecords = 1;	
            payload = '{"Quote": [';
			for(rtms__CustomerQuote__c st : ListOfCustomerQuote){  
            	payLoad = payLoad + '{"id": "'+st.Id+'",'+
                        	'"quote_date": "'+st.rtms__Quote_Date__c+ '",'+
                        	'"status": "'+st.rtms__Status__c+ '",'+
                            '"comments": "'+st.rtms__Comments__c+ '",'+
                            '"load_id": "'+st.rtms__Load2__c+ '",'+
                        	'"net_freight_charges": "'+st.rtms__Net_Line_Haul__c+ '",'+
                        	'"fuel_surcharges": "'+st.rtms__Fuel_Surcharge__c+ '",'+
                        	'"accessorial_charge": "'+st.rtms__Accessorial_Charges__c+ '",'+
                        	'"transportation_total": "'+st.rtms__Transportation_Total__c+ '",'+
                     		'"customer_quote_total": "'+st.rtms__Customer_Quote_Total__c+ '"'+
                    '}';
                if(ListOfCustomerQuote.size() != customerQuoteRecords){
                    payLoad = payLoad + ',';
                }
                customerQuoteRecords++;
            } 
            payLoad = payLoad +']}';	
        }
        /* if(triggerAction == 'Delete'){
            system.debug('--delete--');
            Integer customerQuoteRecords = 1;	
            payload = '{"LineItem": [';
            for(String li : customerQuoteSetId){  
                payLoad = payLoad + '{"Id": "'+LoadId+'",'+
                    '}';
                if(customerQuoteSetId.size() != customerQuoteRecords){
                    payLoad = payLoad + ',';
                }
                customerQuoteRecords++;
            } 
            payLoad = payLoad +']}';
            
        }*/
        return payload;
    } 
}
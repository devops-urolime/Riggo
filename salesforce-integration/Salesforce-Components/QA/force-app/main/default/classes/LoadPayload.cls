public class LoadPayload {
    public string getSavePayload(ID loadId,String triggerAction,String methodName,String marketplaceId){ 
        system.debug('loadId'+loadId);   
        
        String payload  = '';
        if(triggerAction == 'Insert' || triggerAction == 'Update' || triggerAction == 'Insert Batch' || triggerAction == 'Update Batch' ){
            List<rtms__Load__c> ListOfLoead =[Select Id, OwnerId,rtms__Bill_To__r.Name,rtms__Bill_To__r.rtms__TMS_Type__c,rtms__Bill_To__r.ShippingStreet,
                        rtms__Bill_To__r.ShippingCity,rtms__Bill_To__r.ShippingPostalCode,rtms__Bill_To__r.ShippingState,rtms__Bill_To__r.ShippingCountry,Owner.Name, Name, CreatedDate, 
                                        rtms__Bill_To__c, rtms__Bill_of_Lading_Number__c,rtms__Carrier_Invoice_Total__c, rtms__Carrier_Load__c, rtms__Carrier_Payment_Total__c, rtms__Carrier_Quote_Total__c,
                                        rtms__Carrier_Remit_To__c,rtms__Carrier_Remit_To__r.Id, rtms__Carrier_Remit_To__r.Name,rtms__Carrier_Remit_To__r.rtms__TMS_Type__c,rtms__Carrier_Remit_To__r.ShippingStreet,
                        				rtms__Carrier_Remit_To__r.ShippingCity,rtms__Carrier_Remit_To__r.ShippingPostalCode,rtms__Carrier_Remit_To__r.ShippingState,
                                        rtms__Carrier_Remit_To__r.ShippingCountry, 
                                        rtms__Carrier_Service__c,rtms__Carrier_Service__r.Name,
                                        rtms__Carrier_Uploads__c, 
                                        rtms__Carrier__c,rtms__Carrier__r.Id,rtms__Carrier__r.Name,rtms__Carrier__r.rtms__TMS_Type__c,rtms__Carrier__r.ShippingStreet,
                        				rtms__Carrier__r.ShippingCity,rtms__Carrier__r.ShippingPostalCode,rtms__Carrier__r.ShippingState,rtms__Carrier__r.ShippingCountry,
                                        rtms__City_Lane__c, rtms__Country_Lane__c, rtms__Customer_Check_Acceptable__c, rtms__Customer_Invoice_Tax_Amount__c, rtms__Customer_Invoice_Total__c, rtms__Customer_Invoice_Transportation_Total__c, rtms__Customer_Load__c,rtms__Customer_Quote_Tax_Amount__c, rtms__Customer_Quote_Total__c, rtms__Customer_Quote_Transportation_Total__c, 
                                        rtms__Customer__c, rtms__Customer__r.Id,rtms__Customer__r.Name,rtms__Customer__r.rtms__TMS_Type__c,rtms__Customer__r.ShippingStreet,
            							rtms__Customer__r.ShippingCity,rtms__Customer__r.ShippingPostalCode,rtms__Customer__r.ShippingState,rtms__Customer__r.ShippingCountry,
                                        rtms__Delivery_Status__c, rtms__Destination__c, rtms__Distance_Kilometers__c, rtms__Distance_Miles__c, 
                                        rtms__Document_Options__c, 
                                        rtms__Driver__c,rtms__Driver__r.Id,rtms__Driver__r.Name,rtms__Driver__r.rtms__Transportation_Role__c,
                        				rtms__Driver__r.Phone,rtms__Driver__r.MobilePhone,rtms__Driver__r.MailingStreet,
                        				rtms__Driver__r.MailingCity,rtms__Driver__r.MailingPostalCode,rtms__Driver__r.MailingState,rtms__Driver__r.MailingCountry,
                                        rtms__EDI_Reference_Numbers__c, rtms__EDI_Status__c,
                                        rtms__Equipment_Type__c,
                                        rtms__Equipment_Type__r.Name,rtms__Equipment_Type__r.Id,rtms__Equipment_Type__r.rtms__Category__c,rtms__Equipment_Type__r.rtms__Max_Volume__c,
                                        rtms__Equipment_Type__r.rtms__Volume_Units__c,rtms__Equipment_Type__r.rtms__Max_Pallets__c,rtms__Equipment_Type__r.rtms__Enabled__c,
                                        rtms__Equipment_Type__r.rig_Truckstop_Type__c,rtms__Equipment_Type__r.rtms__ISO_Type_Group__c,
                                        rtms__Equipment_Type__r.rtms__ISO_Size_Type__c,rtms__Equipment_Type__r.rtms__Tare_Weight__c,
                                        rtms__Equipment_Type__r.rtms__Max_Weight__c,rtms__Equipment_Type__r.rtms__Weight_Units__c,
                                        rtms__Expected_Delivery_Date2__c, rtms__Expected_Ship_Date2__c, 
                                        rtms__First_Stop__c,
                                        rtms__First_Stop__r.rtms__Location2__r.Id,
                                        rtms__First_Stop__r.rtms__Location2__r.Name,
                                        rtms__First_Stop__r.rtms__Location2__r.ShippingStreet,
                                        rtms__First_Stop__r.rtms__Location2__r.ShippingCity,
                                        rtms__First_Stop__r.rtms__Location2__r.ShippingState,
                                        rtms__First_Stop__r.rtms__Location2__r.ShippingPostalCode,
                                        rtms__First_Stop__r.rtms__Instructions__c,
                                       	rtms__First_Stop__r.rtms__City__c,rtms__First_Stop__r.rtms__State_Province__c,rtms__First_Stop__r.rtms__Postal_Code__c,
										rtms__First_Stop__r.rtms__Country__c,
                                        rtms__First_Stop__r.Id,rtms__First_Stop__r.Name,
                                        rtms__First_Stop__r.rtms__P_D__c,rtms__First_Stop__r.rig_Load_Customer_Name__c,
                                        rtms__First_Stop__r.rtms__Number__c,rtms__First_Stop__r.rtms__Address2__c,
                                        rtms__First_Stop__r.rtms__Shipping_Receiving_Hours__c,rtms__First_Stop__r.rtms__Expected_Date__c,
                                        rtms__First_Stop__r.rtms__Expected_Day__c,rtms__First_Stop__r.rtms__Appointment_Required__c,
                                        rtms__First_Stop__r.rtms__Appointment_Time__c,rtms__First_Stop__r.rtms__Stop_Status__c,
                                        rtms__First_Stop__r.rtms__Departure_Date__c,rtms__First_Stop__r.rtms__Departure_Time__c,
                                        rtms__First_Stop__r.rtms__Carrier_Status2__c,rtms__Hazardous_Materials__c, rtms__Insurance_Amount__c, rtms__Last_Reported_City__c, rtms__Last_Reported_Country__c, rtms__Last_Reported_Geolocation__Latitude__s, rtms__Last_Reported_Geolocation__Longitude__s, rtms__Last_Reported_Geolocation__c, rtms__Last_Reported_Location2__c, rtms__Last_Reported_State_Province__c, 
                                        rtms__Last_Stop__c, 
                                        rtms__Last_Stop__r.rtms__Instructions__c,
                                        rtms__Last_Stop__r.rtms__Location2__r.Id,
                                        rtms__Last_Stop__r.rtms__Location2__r.Name,
                                        rtms__Last_Stop__r.rtms__Location2__r.ShippingStreet,
                                        rtms__Last_Stop__r.rtms__Location2__r.ShippingCity,
                                        rtms__Last_Stop__r.rtms__Location2__r.ShippingState,
                                        rtms__Last_Stop__r.rtms__Location2__r.ShippingPostalCode,
                                        rtms__Last_Stop__r.Id,
                                        rtms__Last_Stop__r.Name,
                                    	rtms__Last_Stop__r.rtms__City__c,rtms__Last_Stop__r.rtms__State_Province__c,rtms__Last_Stop__r.rtms__Postal_Code__c,
										rtms__Last_Stop__r.rtms__Country__c,
                                        rtms__Last_Stop__r.rtms__P_D__c,
                                        rtms__Last_Stop__r.rig_Load_Customer_Name__c,
                                        rtms__Last_Stop__r.rtms__Number__c,
                                        rtms__Last_Stop__r.rtms__Address2__c,
                                        rtms__Last_Stop__r.rtms__Shipping_Receiving_Hours__c,
                                        rtms__Last_Stop__r.rtms__Expected_Date__c,
                                        rtms__Last_Stop__r.rtms__Expected_Day__c,
                                        rtms__Last_Stop__r.rtms__Appointment_Required__c,
                                        rtms__Last_Stop__r.rtms__Appointment_Time__c,
                                        rtms__Last_Stop__r.rtms__Stop_Status__c,
                                        rtms__Last_Stop__r.rtms__Departure_Date__c,rtms__Last_Stop__r.rtms__Departure_Time__c,
                                        rtms__Last_Stop__r.rtms__Carrier_Status2__c,
                                        rtms__Load_Sequence_Number__c, rtms__Load_Status_Comments__c, rtms__Load_Status_Requested__c, rtms__Load_Status__c, rtms__Margin_Invoiced__c, rtms__Margin_Paid__c, rtms__Margin_Pct_Invoiced__c,rtms__Margin_Pct_Quoted__c, rtms__Margin_Quoted__c, 
                                          rtms__Mode__c,rtms__Mode__r.Id,rtms__Mode__r.Name,rtms__Mode__r.rtms__Enabled__c,rtms__Mode__r.rtms__LTL__c,rtms__Mode__r.rtms__Order__c,rtms__Mode__r.rtms__Required_Application_Documents__c,
                                          rtms__OTD_Counter__c, rtms__Order_Date__c, rtms__Order_Number__c, rtms__Order_Total__c, rtms__Origin__c,rtms__Other_Instructions__c, rtms__POD_Received__c, rtms__PO_Number__c, rtms__PRO_Number__c, rtms__Payment_Terms__c, rtms__Postal_Code_Lane__c,rtms__Sales_Status__c, rtms__Schedule_Status__c,rtms__Ship_From_Address__c, rtms__Ship_Status__c, rtms__Ship_To_Address__c,rtms__Site_URL__c, rtms__State_Lane__c, rtms__Tax_Exempt__c, rtms__Tender_Accepted_Date__c,rtms__Total_Weight__c, rtms__Tracking_Number__c,rtms__Weight_Units__c, rtms__Zip3_Lane__c, rtms__Zip5_Lane__c,rtms__Mode_Name__c,rtms__Temperature_Controlled__c, rtms__Tracking_Provider__c,
                                          rtms__Vendor__c,rtms__Vendor__r.Id,rtms__Vendor__r.Name,rtms__Vendor__r.rtms__TMS_Type__c,rtms__Vendor__r.ShippingStreet,rtms__Vendor__r.ShippingCity,rtms__Vendor__r.ShippingPostalCode,rtms__Vendor__r.ShippingState,rtms__Vendor__r.ShippingCountry,
                                          Posted_Notes__c, Pickup_Delivery_Number__c, Stop_Reference_Numbers__c, Tractor_Number__c, Trailer_Number__c, Historical_Load__c, Legacy_Load_Number__c, Pickup_Appointment_Time__c, Delivery_Appointment_Time__c, rig_riggoh_status__c, EDI_Shipment_ID__c, food_grade_trailer_required__c, riggoh_Formatted_Expected_Delivery_Date__c, riggoh_Formatted_Expected_Ship_Date__c,
                                          rtms__Carrier_Sales_Rep__c, rtms__Carrier_Sales_Rep__r.id,rtms__Carrier_Sales_Rep__r.Name,rtms__Carrier_Sales_Rep__r.Email,
                                          rtms__Master_BOL_Option__c, rig_Expected_TTT__c, rig_Previous_Cost__c, riggoh_Required_Temperature__c, rig_Lead_Source__c, Pickup_Location__c, Delivery_Location__c, rigPostedRate__c, rig_Completed_Date__c, rig_Load_URL__c, rig_Team_Required__c, rtms__Pickup_Delivery_Status__c, rtms__Carrier_Portal__c, rtms__EDI_Provider__c, Send_Status_Updates__c, rig_Email_Automation__c, rig_Load_Board__c FROM rtms__Load__c  
                                          where Id =: loadId]; 
            
            system.debug('marketplaceId'+marketplaceId);
            payload = '{"LoadDetails":';
                for(rtms__Load__c ld : ListOfLoead){  
                    payload = payload + '{'+ 
                        '"Id": "'+ld.Id+'",';
                         if(methodName == 'PUT' && marketplaceId != '' && marketplaceId != null){	
                            payload = payload +'"MarketplaceExposedId": "'+marketplaceId+'",';
                         }else{
                             payload = payload +'"MarketplaceExposedId": "null",';
                         }
                        payload = payload +  '"OwnerDetails": {'+
							'"OwnerId": "'+ld.OwnerId+'",'+
                        	'"OwnerName": "'+convertedString(ld.Owner.Name)+'"'+
                        '},'+
                        
                        '"Name": "'+ld.Name+'",'+
                        '"CreatedDate": "'+ld.CreatedDate+'",'+
                         '"CustomerBillTo": {'+
							'"billToId": "'+convertedString(ld.rtms__Bill_To__c)+'",'+
                        	'"BillToName": "'+convertedString(ld.rtms__Bill_To__r.Name)+'",'+
                        	'"billToTMSType": "'+convertedString(ld.rtms__Bill_To__r.rtms__TMS_Type__c)+'",'+
                        	'"billToShippingStreet": "'+convertedString(ld.rtms__Bill_To__r.ShippingStreet)+'",'+
                        	'"billToShippingCity": "'+convertedString(ld.rtms__Bill_To__r.ShippingCity)+'",'+
                        	'"billToShippingPostalCode": "'+convertedString(ld.rtms__Bill_To__r.ShippingPostalCode)+'",'+
                        	'"billToShippingState": "'+convertedString(ld.rtms__Bill_To__r.ShippingState)+'",'+
                        	'"billToShippingCountry": "'+convertedString(ld.rtms__Bill_To__r.ShippingCountry)+'"'+ 
                        '},'+            
                        '"rtms__Bill_of_Lading_Number__c": "'+convertedString(ld.rtms__Bill_of_Lading_Number__c)+'",'+
                        '"rtms__Carrier_Invoice_Total__c": '+ld.rtms__Carrier_Invoice_Total__c+','+
                        '"rtms__Carrier_Load__c": '+ld.rtms__Carrier_Load__c+','+
                        '"rtms__Carrier_Payment_Total__c": '+ld.rtms__Carrier_Payment_Total__c+','+
                        '"rtms__Carrier_Quote_Total__c": '+ld.rtms__Carrier_Quote_Total__c+','+
                        '"CarrierRemitTo": {'+
							'"CarrierRemitToId": "'+convertedString(ld.rtms__Carrier_Remit_To__r.Id)+'",'+
                        	'"CarrierRemitToName": "'+convertedString(ld.rtms__Carrier_Remit_To__r.Name)+ '",'+
                        	'"CarrierRemitToTMSType": "'+convertedString(ld.rtms__Carrier_Remit_To__r.rtms__TMS_Type__c)+'",'+
                        	'"CarrierRemitToShippingStreet": "'+convertedString(ld.rtms__Carrier_Remit_To__r.ShippingStreet)+'",'+
                        	'"CarrierRemitToShippingCity": "'+convertedString(ld.rtms__Carrier_Remit_To__r.ShippingCity)+'",'+
                        	'"CarrierRemitToShippingPostalCode": "'+convertedString(ld.rtms__Carrier_Remit_To__r.ShippingPostalCode)+'",'+
                        	'"CarrierRemitToShippingState": "'+convertedString(ld.rtms__Carrier_Remit_To__r.ShippingState)+'",'+
                        	'"CarrierRemitToShippingCountry": "'+convertedString(ld.rtms__Carrier_Remit_To__r.ShippingCountry)+'"'+ 
                        '},'+ 
                         '"CarrierService": {'+
							'"CarrierServiceId": "'+convertedString(ld.rtms__Carrier_Service__c)+'",'+
                        	'"CarrierServiceName": "'+convertedString(ld.rtms__Carrier_Service__r.Name)+'"'+                        	
                        '},'+ 
                        '"rtms__Carrier_Uploads__c": "'+convertedString(ld.rtms__Carrier_Uploads__c)+'",'+
                        '"Carrier": {'+
							'"CarrierId": "'+convertedString(ld.rtms__Carrier__r.Id)+'",'+
                        	'"CarrierName": "'+convertedString(ld.rtms__Carrier__r.Name)+ '",'+
                        	'"CarrierTMSType": "'+convertedString(ld.rtms__Carrier__r.rtms__TMS_Type__c)+'",'+
                        	'"CarrierShippingStreet": "'+convertedString(ld.rtms__Carrier__r.ShippingStreet)+'",'+
                        	'"CarrierShippingCity": "'+convertedString(ld.rtms__Carrier__r.ShippingCity)+'",'+
                        	'"CarrierShippingPostalCode": "'+convertedString(ld.rtms__Carrier__r.ShippingPostalCode)+'",'+
                        	'"CarrierShippingState": "'+convertedString(ld.rtms__Carrier__r.ShippingState)+'",'+
                        	'"CarrierShippingCountry": "'+convertedString(ld.rtms__Carrier__r.ShippingCountry)+'"'+
                        '},'+ 
                        
                        '"rtms__City_Lane__c": "'+convertedString(ld.rtms__City_Lane__c)+'",'+
                        '"rtms__Country_Lane__c": "'+convertedString(ld.rtms__Country_Lane__c)+'",'+
                        '"rtms__Customer_Check_Acceptable__c": '+ld.rtms__Customer_Check_Acceptable__c+','+
                        '"rtms__Customer_Invoice_Tax_Amount__c": '+ld.rtms__Customer_Invoice_Tax_Amount__c+','+
                        '"rtms__Customer_Invoice_Total__c": '+ld.rtms__Customer_Invoice_Total__c+','+
                        '"rtms__Customer_Invoice_Transportation_Total__c": '+ld.rtms__Customer_Invoice_Transportation_Total__c+','+
                        '"rtms__Customer_Load__c": '+ld.rtms__Customer_Load__c+','+
						'"rtms__Customer_Quote_Tax_Amount__c": '+ld.rtms__Customer_Quote_Tax_Amount__c+','+
                        '"rtms__Customer_Quote_Total__c": '+ld.rtms__Customer_Quote_Total__c+','+
                        '"rtms__Customer_Quote_Transportation_Total__c": '+ld.rtms__Customer_Quote_Transportation_Total__c+','+
                        '"Customer": {'+
							'"CustomerId": "'+convertedString(ld.rtms__Customer__r.Id)+'",'+
                        	'"CustomerName": "'+convertedString(ld.rtms__Customer__r.Name)+ '",'+
                        	'"CustomerTMSType": "'+convertedString(ld.rtms__Customer__r.rtms__TMS_Type__c)+'",'+
                        	'"CustomerShippingStreet": "'+convertedString(ld.rtms__Customer__r.ShippingStreet)+'",'+
                        	'"CustomerShippingCity": "'+convertedString(ld.rtms__Customer__r.ShippingCity)+'",'+
                        	'"CustomerShippingPostalCode": "'+convertedString(ld.rtms__Customer__r.ShippingPostalCode)+'",'+
                        	'"CustomerShippingState": "'+convertedString(ld.rtms__Customer__r.ShippingState)+'",'+
                        	'"CustomerShippingCountry": "'+convertedString(ld.rtms__Customer__r.ShippingCountry)+'"'+
                        '},'+
                        '"rtms__Delivery_Status__c": "'+convertedString(ld.rtms__Delivery_Status__c)+'",'+
                        '"rtms__Destination__c": "'+convertedString(ld.rtms__Destination__c)+'",'+
                        '"rtms__Distance_Kilometers__c": '+ld.rtms__Distance_Kilometers__c+','+
                        '"rtms__Distance_Miles__c": '+ld.rtms__Distance_Miles__c+','+
                       	'"Driver": {'+
							'"DriverId": "'+ld.rtms__Driver__r.Id+'",'+
                        	'"DriverName": "'+convertedString(ld.rtms__Driver__r.Name)+ '",'+
                        	'"DriverTransportationRole": "'+convertedString(ld.rtms__Driver__r.rtms__Transportation_Role__c)+'",'+
                        	'"DriverPhone": "'+convertedString(ld.rtms__Driver__r.Phone)+'",'+
                        	'"DriverMobilePhone": "'+convertedString(ld.rtms__Driver__r.MobilePhone)+'",'+
                        	'"DriverMailingStreet": "'+convertedString(ld.rtms__Driver__r.MailingStreet)+'",'+
                        	'"DriverMailingCity": "'+convertedString(ld.rtms__Driver__r.MailingCity)+'",'+
                        	'"DriverMailingPostalCode": "'+convertedString(ld.rtms__Driver__r.MailingPostalCode)+'",'+
                        	'"DriverMailingState": "'+convertedString(ld.rtms__Driver__r.MailingState)+'",'+
                        	'"DriverMailingCountry": "'+convertedString(ld.rtms__Driver__r.MailingCountry)+'"'+ 
                        '},'+ 
                        '"rtms__EDI_Reference_Numbers__c": "'+convertedString(ld.rtms__EDI_Reference_Numbers__c)+'",'+
                        '"rtms__EDI_Status__c": "'+convertedString(ld.rtms__EDI_Status__c)+'",'+
                        '"EquipmentType": {'+
							'"EquipmentTypeId": "'+ld.rtms__Equipment_Type__r.Id+'",'+
                        	'"EquipmentTypeName": "'+convertedString(ld.rtms__Equipment_Type__r.Name)+ '",'+
                        	'"EquipmentType_rtms__Category__c": "'+convertedString(ld.rtms__Equipment_Type__r.rtms__Category__c)+ '",'+
                        	'"EquipmentType_rtms__Max_Volume__c": "'+ld.rtms__Equipment_Type__r.rtms__Max_Volume__c+ '",'+
                            '"EquipmentType_rtms__Volume_Units__c": "'+convertedString(ld.rtms__Equipment_Type__r.rtms__Volume_Units__c)+ '",'+
                            '"EquipmentType_rtms__Max_Pallets__c": "'+ld.rtms__Equipment_Type__r.rtms__Max_Pallets__c+ '",'+
                            '"EquipmentType_rtms__Enabled__c": "'+ld.rtms__Equipment_Type__r.rtms__Enabled__c+ '",'+
                            '"EquipmentType_rig_Truckstop_Type__c": "'+convertedString(ld.rtms__Equipment_Type__r.rig_Truckstop_Type__c)+ '",'+
                            '"EquipmentType_rtms__ISO_Type_Group__c": "'+convertedString(ld.rtms__Equipment_Type__r.rtms__ISO_Type_Group__c)+ '",'+
                            '"EquipmentType_rtms__ISO_Size_Type__c": "'+convertedString(ld.rtms__Equipment_Type__r.rtms__ISO_Size_Type__c)+ '",'+
                            '"EquipmentType_rtms__Tare_Weight__c": "'+ld.rtms__Equipment_Type__r.rtms__Tare_Weight__c+ '",'+
                            '"EquipmentType_rtms__Max_Weight__c": "'+ld.rtms__Equipment_Type__r.rtms__Max_Weight__c+ '",'+
                            '"EquipmentType_rtms__Weight_Units__c": "'+convertedString(ld.rtms__Equipment_Type__r.rtms__Weight_Units__c)+'"'+
                        '},'+ 
                        '"rtms__Expected_Delivery_Date2__c": "'+ld.rtms__Expected_Delivery_Date2__c+'",'+
                        '"rtms__Expected_Ship_Date2__c": "'+ld.rtms__Expected_Ship_Date2__c+'",'+
                        '"FirstStop": {'+
							'"FirstStopId": "'+ld.rtms__First_Stop__r.Id+'",'+
                        	'"FirstStopName": "'+convertedString(ld.rtms__First_Stop__r.Name)+ '",'+
                        	'"FirstStoprtms__P_D__c": "'+convertedString(ld.rtms__First_Stop__r.rtms__P_D__c)+ '",'+
                            '"FirstStoprig_Load_Customer_Name__c": "'+convertedString(ld.rtms__First_Stop__r.rig_Load_Customer_Name__c)+ '",'+
                            '"FirstStoprtms__Number__c": "'+ld.rtms__First_Stop__r.rtms__Number__c+ '",'+
                            '"FirstStoprtms__Instructions__c": "'+convertedString(ld.rtms__First_Stop__r.rtms__Instructions__c)+ '",'+
                            '"FirstStopLocation": {'+
                            	'"FirstStopLocationId": "'+convertedString(ld.rtms__First_Stop__r.rtms__Location2__r.Id)+'",'+
                            	'"FirstStopLocationName": "'+convertedString(ld.rtms__First_Stop__r.rtms__Location2__r.Name)+'",'+
                                '"FirstStopLocationShippingStreet": "'+convertedString(ld.rtms__First_Stop__r.rtms__Location2__r.ShippingStreet)+'",'+
                                '"FirstStopLocationShippingShippingCity": "'+convertedString(ld.rtms__First_Stop__r.rtms__Location2__r.ShippingCity)+'",'+
                            	'"FirstStopLocationShippingShippingState": "'+convertedString(ld.rtms__First_Stop__r.rtms__Location2__r.ShippingState)+'",'+
                            	'"FirstStopLocationShippingShippingPostalCode": "'+convertedString(ld.rtms__First_Stop__r.rtms__Location2__r.ShippingPostalCode)+'"'+
                            '},'+ 
                            '"FirstStoprtms__Shipping_Receiving_Hours__c": "'+convertedString(ld.rtms__First_Stop__r.rtms__Shipping_Receiving_Hours__c)+ '",'+
                            '"FirstStoprtms__Expected_Date__c": "'+ld.rtms__First_Stop__r.rtms__Expected_Date__c+ '",'+
                            '"FirstStoprtms__Expected_Day__c": "'+convertedString(ld.rtms__First_Stop__r.rtms__Expected_Day__c)+ '",'+
                            '"FirstStoprtms__Appointment_Required__c": "'+ld.rtms__First_Stop__r.rtms__Appointment_Required__c+ '",'+
                            '"FirstStoprtms__Appointment_Time__c": "'+convertedString(ld.rtms__First_Stop__r.rtms__Appointment_Time__c)+ '",'+
                            '"FirstStoprtms__Stop_Status__c": "'+convertedString(ld.rtms__First_Stop__r.rtms__Stop_Status__c)+ '",'+
                            '"FirstStoprtms__Carrier_Status2__c": "'+convertedString(ld.rtms__First_Stop__r.rtms__Carrier_Status2__c)+'",'+
                            '"FirstStoprtms__Departure_Date": "' + ld.rtms__First_Stop__r.rtms__Departure_Date__c + ' ' + ld.rtms__First_Stop__r.rtms__Departure_Time__c + '"' +
                        '},'+ 
                        '"rtms__Hazardous_Materials__c": '+ld.rtms__Hazardous_Materials__c+','+
                        '"rtms__Insurance_Amount__c": '+ld.rtms__Insurance_Amount__c+','+
                        '"rtms__Last_Reported_City__c": "'+convertedString(ld.rtms__Last_Reported_City__c)+'",'+
                        '"rtms__Last_Reported_Country__c": "'+convertedString(ld.rtms__Last_Reported_Country__c)+'",'+
                        '"rtms__Last_Reported_Geolocation__c": "'+ld.rtms__Last_Reported_Geolocation__c+'",'+
                        '"rtms__Last_Reported_Location2__c": "'+convertedString(ld.rtms__Last_Reported_Location2__c)+'",'+
                        '"rtms__Last_Reported_State_Province__c": "'+convertedString(ld.rtms__Last_Reported_State_Province__c)+'",'+
                        '"LastStop": {'+
							'"LastStopId": "'+ld.rtms__Last_Stop__r.Id+'",'+
                        	'"LastStopName": "'+convertedString(ld.rtms__Last_Stop__r.Name)+ '",'+
                        	'"LastStoprtms__P_D__c": "'+convertedString(ld.rtms__Last_Stop__r.rtms__P_D__c)+ '",'+
                            '"LastStoprig_Load_Customer_Name__c": "'+convertedString(ld.rtms__Last_Stop__r.rig_Load_Customer_Name__c)+ '",'+
                            '"LastStoprtms__Number__c": "'+ld.rtms__Last_Stop__r.rtms__Number__c+ '",'+
                            '"LastStoprtms__Instructions__c": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Instructions__c)+ '",'+
                            '"LastStopLocation": {'+
                            	'"LastStopLocationId": "'+ld.rtms__Last_Stop__r.rtms__Location2__r.Id+'",'+
                            	'"LastStopLocationName": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Location2__r.Name)+'",'+
                                '"LastStopLocationShippingStreet": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Location2__r.ShippingStreet)+'",'+
                                '"LastStopLocationShippingShippingCity": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Location2__r.ShippingCity)+'",'+
                            	'"LastStopLocationShippingShippingState": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Location2__r.ShippingState)+'",'+
                            	'"LastStopLocationShippingShippingPostalCode": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Location2__r.ShippingPostalCode)+'"'+
                            '},'+ 
                            '"LastStoprtms__Shipping_Receiving_Hours__c": "'+ld.rtms__Last_Stop__r.rtms__Shipping_Receiving_Hours__c+ '",'+
                            '"LastStoprtms__Expected_Date__c": "'+ld.rtms__Last_Stop__r.rtms__Expected_Date__c+ '",'+
                            '"LastStoprtms__Expected_Day__c": "'+ld.rtms__Last_Stop__r.rtms__Expected_Day__c+ '",'+
                            '"LastStoprtms__Appointment_Required__c": "'+ld.rtms__Last_Stop__r.rtms__Appointment_Required__c+ '",'+
                            '"LastStoprtms__Appointment_Time__c": "'+ld.rtms__Last_Stop__r.rtms__Appointment_Time__c+ '",'+
                            '"LastStoprtms__Stop_Status__c": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Stop_Status__c)+ '",'+
                            '"LastStoprtms__Carrier_Status2__c": "'+convertedString(ld.rtms__Last_Stop__r.rtms__Carrier_Status2__c)+'",'+
                            '"LastStoprtms__Departure_Date": "' + ld.rtms__Last_Stop__r.rtms__Departure_Date__c + ' ' + ld.rtms__Last_Stop__r.rtms__Departure_Time__c + '"' +
                        '},'+                         
                        '"rtms__Load_Sequence_Number__c": "'+ld.rtms__Load_Sequence_Number__c+'",'+
                        '"rtms__Load_Status_Comments__c": "'+convertedString(ld.rtms__Load_Status_Comments__c)+'",'+
                        '"rtms__Load_Status_Requested__c": '+ld.rtms__Load_Status_Requested__c+','+
                        '"rtms__Load_Status__c": "'+convertedString(ld.rtms__Load_Status__c)+'",'+
                        '"rtms__Margin_Invoiced__c": '+ld.rtms__Margin_Invoiced__c+','+
                        '"rtms__Margin_Paid__c": '+ld.rtms__Margin_Paid__c+','+
                        '"rtms__Margin_Pct_Invoiced__c": '+ld.rtms__Margin_Pct_Invoiced__c+','+
                        '"rtms__Margin_Pct_Quoted__c": '+ld.rtms__Margin_Pct_Quoted__c+','+
                        '"rtms__Margin_Quoted__c": '+ld.rtms__Margin_Quoted__c+','+
                        '"Mode": {'+
							'"ModeId": "'+ld.rtms__Mode__r.Id+'",'+ 
                        	'"ModeName": "'+convertedString(ld.rtms__Mode__r.Name)+'",'+ 
                        	'"ModeEnabled": "'+ld.rtms__Mode__r.rtms__Enabled__c+'",'+ 
                        	'"ModeLTL": "'+ld.rtms__Mode__r.rtms__LTL__c+'",'+ 
                        	'"ModeOrder": "'+ld.rtms__Mode__r.rtms__Order__c+'",'+ 
                        	'"ModeRequiredApplication": "'+convertedString(ld.rtms__Mode__r.rtms__Required_Application_Documents__c)+'"'+ 
                         '},'+  
                        '"rtms__OTD_Counter__c": '+ld.rtms__OTD_Counter__c+','+
                        '"rtms__Order_Date__c": "'+ld.rtms__Order_Date__c+'",'+
                        '"rtms__Order_Number__c": "'+ld.rtms__Order_Number__c+'",'+
                        '"rtms__Order_Total__c": '+ld.rtms__Order_Total__c+','+
                        '"rtms__Origin__c": "'+ld.rtms__Origin__c+'",'+
                        '"rtms__Other_Instructions__c": "'+convertedString(ld.rtms__Other_Instructions__c)+'",'+
                        '"rtms__POD_Received__c": '+ld.rtms__POD_Received__c+','+
                        '"rtms__PO_Number__c": "'+ld.rtms__PO_Number__c+'",'+
                        '"rtms__PRO_Number__c": "'+ld.rtms__PRO_Number__c+'",'+
                        '"rtms__Payment_Terms__c": "'+convertedString(ld.rtms__Payment_Terms__c)+'",'+
                        '"rtms__Postal_Code_Lane__c": "'+ld.rtms__Postal_Code_Lane__c+'",'+
                        '"rtms__Sales_Status__c": "'+convertedString(ld.rtms__Sales_Status__c)+'",'+
                        '"rtms__Schedule_Status__c": "'+ld.rtms__Schedule_Status__c+'",'+
                        '"rtms__Ship_From_Address__c": "'+convertedString(ld.rtms__Ship_From_Address__c)+'",'+
                        '"rtms__Ship_Status__c": "'+convertedString(ld.rtms__Ship_Status__c)+'",'+
                        '"rtms__Ship_To_Address__c": "'+convertedString(ld.rtms__Ship_To_Address__c)+'",'+
                        '"rtms__Site_URL__c": "'+ld.rtms__Site_URL__c+'",'+
                        '"rtms__State_Lane__c": "'+ld.rtms__State_Lane__c+'",'+
                        '"rtms__Tax_Exempt__c": '+ld.rtms__Tax_Exempt__c+','+
                        '"rtms__Tender_Accepted_Date__c": "'+ld.rtms__Tender_Accepted_Date__c+'",'+                       
                        '"rtms__Total_Weight__c":'+ld.rtms__Total_Weight__c+','+
                        '"rtms__Tracking_Number__c": "'+ld.rtms__Tracking_Number__c+'",'+
                        '"rtms__Zip3_Lane__c": "'+ld.rtms__Zip3_Lane__c+'",'+
                        '"rtms__Zip5_Lane__c": "'+ld.rtms__Zip5_Lane__c+'",'+
                        '"rtms__Weight_Units__c": "'+ld.rtms__Weight_Units__c+'",'+
                        '"rtms__Mode_Name__c": "'+convertedString(ld.rtms__Mode_Name__c)+'",'+
                        '"rtms__Temperature_Controlled__c": '+ld.rtms__Temperature_Controlled__c+','+
                        '"rtms__Tracking_Provider__c": "'+ld.rtms__Tracking_Provider__c+'",'+
                        '"Vendor": {'+
							'"VendorId": "'+ld.rtms__Vendor__r.Id+'",'+
                        	'"VendorName": "'+convertedString(ld.rtms__Vendor__r.Name)+ '",'+
                        	'"VendorTMSType": "'+convertedString(ld.rtms__Vendor__r.rtms__TMS_Type__c)+'",'+
                        	'"VendorShippingStreet": "'+convertedString(ld.rtms__Vendor__r.ShippingStreet)+'",'+
                        	'"VendorShippingCity": "'+convertedString(ld.rtms__Vendor__r.ShippingCity)+'",'+
                        	'"VendorShippingPostalCode": "'+convertedString(ld.rtms__Vendor__r.ShippingPostalCode)+'",'+
                        	'"VendorShippingState": "'+convertedString(ld.rtms__Vendor__r.ShippingState)+'",'+
                        	'"VendorShippingCountry": "'+convertedString(ld.rtms__Vendor__r.ShippingCountry)+'"'+
                         '},'+
                        '"Posted_Notes__c": "'+convertedString(ld.Posted_Notes__c)+'",'+
                        '"Pickup_Delivery_Number__c": "'+convertedString(ld.Pickup_Delivery_Number__c)+'",'+
                        '"Stop_Reference_Numbers__c": "'+convertedString(ld.Stop_Reference_Numbers__c)+'",'+
                        '"Tractor_Number__c": "'+ld.Tractor_Number__c+'",'+
                        '"Trailer_Number__c": "'+ld.Trailer_Number__c+'",'+
                        '"Historical_Load__c": '+ld.Historical_Load__c+','+
                        '"Legacy_Load_Number__c": "'+ld.Legacy_Load_Number__c+'",'+
                        '"Pickup_Appointment_Time__c": "'+ld.Pickup_Appointment_Time__c+'",'+
                        '"Delivery_Appointment_Time__c": "'+ld.Delivery_Appointment_Time__c+'",'+
                        '"rig_riggoh_status__c": "'+ld.rig_riggoh_status__c+'",'+
                        '"EDI_Shipment_ID__c": "'+ld.EDI_Shipment_ID__c+'",'+
                        '"food_grade_trailer_required__c": '+ld.food_grade_trailer_required__c+','+
                        '"riggoh_Formatted_Expected_Delivery_Date__c": "'+ld.riggoh_Formatted_Expected_Delivery_Date__c+'",'+
                        '"riggoh_Formatted_Expected_Ship_Date__c": "'+ld.riggoh_Formatted_Expected_Ship_Date__c+'",'+
                        '"CarrierSalesRep": {'+
							'"CarrierSalesRepId": "'+ld.rtms__Carrier_Sales_Rep__r.Id+'",'+
                        	'"CarrierSalesRepName": "'+convertedString(ld.rtms__Carrier_Sales_Rep__r.Name)+ '",'+
                        	'"CarrierSalesRepEmail": "'+ld.rtms__Carrier_Sales_Rep__r.Email+ '"'+ 
                         '},'+
                        '"rtms__Master_BOL_Option__c": "'+ld.rtms__Master_BOL_Option__c+'",'+
                        '"rig_Expected_TTT__c": '+ld.rig_Expected_TTT__c+','+
                        '"rig_Previous_Cost__c": '+ld.rig_Previous_Cost__c+','+
                        '"riggoh_Required_Temperature__c": '+ld.riggoh_Required_Temperature__c+','+
                        '"rig_Lead_Source__c": "'+ld.rig_Lead_Source__c+'",'+
                        '"Pickup_Location__c": "'+convertedString(ld.Pickup_Location__c)+'",'+
                        '"Delivery_Location__c": "'+convertedString(ld.Delivery_Location__c)+'",'+
                        '"rigPostedRate__c": '+ld.rigPostedRate__c+','+
                        '"rig_Completed_Date__c": "'+ld.rig_Completed_Date__c+'",'+
                        '"rig_Load_URL__c": "'+ld.rig_Load_URL__c+'",'+
                        '"rig_Team_Required__c": '+ld.rig_Team_Required__c+','+
                        '"rtms__Pickup_Delivery_Status__c": "'+convertedString(ld.rtms__Pickup_Delivery_Status__c)+'",'+
                        '"rtms__Carrier_Portal__c": '+ld.rtms__Carrier_Portal__c+','+
                        '"rtms__EDI_Provider__c": "'+ld.rtms__EDI_Provider__c+'",'+
                        '"Send_Status_Updates__c": '+ld.Send_Status_Updates__c+','+
                         '"rig_Email_Automation__c": '+ld.rig_Email_Automation__c+','+
                        '"rig_Load_Board__c": '+ld.rig_Load_Board__c+''+
                        '}}';
        	}
        }
      /*  if(triggerAction == 'Delete'){
            payload = '{"LoadDetails":';
                payload = payload + '{'+ 
                		'"Id": "'+loadId+'"'+ 
                    '}}';
        	
        }*/
        return payload;
    }
    private string convertedString(String LoadFildName){
        if(LoadFildName != null){
        LoadFildName = LoadFildName.replace('\\n\\r', ' ');	// remove CRLF
        LoadFildName = LoadFildName.replace('\\n', ' ');		// remove LF	
        LoadFildName = LoadFildName.replace('\\r', ' ');		// remove CR
        LoadFildName = LoadFildName.replace('\\', ' ');		// remove slashes
            LoadFildName = LoadFildName.replace('"',''); // Remve double quotes
        LoadFildName = LoadFildName.normalizeSpace();	// Remove all repeating white space
        }
        return LoadFildName;
    }
}
package io.riggo.web.integration.parser;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.riggo.data.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SalesforceRevenovaRequestBodyParserPostPutLoadTest {

    private SalesforceRevenovaRequestBodyParserPostPutLoad salesforceRevenovaRequestBodyParserPostPutLoad;
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;

    @Before
    public void setupTests(){
        salesforceRevenovaRequestBodyParserPostPutLoad = new SalesforceRevenovaRequestBodyParserPostPutLoad();
        salesforceRevenovaRequestBodyParserHelper = new SalesforceRevenovaRequestBodyParserHelper();
        salesforceRevenovaRequestBodyParserPostPutLoad.setSalesforceRevenovaRequestBodyParserHelper(salesforceRevenovaRequestBodyParserHelper);
                dataHashMap = new Gson().fromJson(
                payloadJson, new TypeToken<HashMap<String, Object>>() {}.getType()
        );
    }

    @Test
    public void resolveFirstStop() {
        LoadStop firstLoadStop = salesforceRevenovaRequestBodyParserPostPutLoad.resolveFirstStop(dataHashMap);

        assertTrue(StringUtils.equals(firstLoadStop.getExtSysId(), "a0tg0000003QpxmAAC"));
        assertTrue(StringUtils.equals(firstLoadStop.getName(), "Stop 1"));
        assertTrue(StringUtils.equals(firstLoadStop.getShippingReceivingHours(), "01:01-02:02"));
        assertTrue(firstLoadStop.getStopNumber().intValue() == 1);
        assertTrue(firstLoadStop.getType().intValue() == 1);
        assertTrue(firstLoadStop.getExpectedDateTime().equals(LocalDateTime.of(2019, 8, 5, 0, 0)));
        assertTrue(firstLoadStop.getAppointmentRequired());

        Location location = firstLoadStop.getLocation();
        assertTrue(StringUtils.equals(location.getExtSysId(), "001g000001ywr1BAAQ"));
        assertTrue(StringUtils.equals(location.getName(), "DK Shipper"));

        Address address = location.getAddress();
        assertTrue(StringUtils.equals(address.getAddress1(), "175 Bleecker st"));
        assertTrue(StringUtils.equals(address.getCity(), "New York"));
        assertTrue(StringUtils.equals(address.getState(), "New York"));
        assertTrue(StringUtils.equals(address.getPostalCode(), "10012"));
    }

    @Test
    public void resolveLastStop() {
        LoadStop lastLoadStop = salesforceRevenovaRequestBodyParserPostPutLoad.resolveLastStop(dataHashMap);

        assertTrue(StringUtils.equals(lastLoadStop.getExtSysId(), "a0tg0000003QpxnAAC"));
        assertTrue(StringUtils.equals(lastLoadStop.getName(), "Stop 2"));
        assertTrue(StringUtils.equals(lastLoadStop.getShippingReceivingHours(), "10:10-11:11"));
        assertTrue(lastLoadStop.getStopNumber().intValue() == 2);
        assertTrue(lastLoadStop.getType().intValue() == 2);
        assertTrue(lastLoadStop.getExpectedDateTime().equals(LocalDateTime.of(2019, 8, 5, 0, 0)));
        assertTrue(lastLoadStop.getAppointmentRequired());

        Location location = lastLoadStop.getLocation();
        assertTrue(StringUtils.equals(location.getExtSysId(), "001g000001ywr1CAAQ"));
        assertTrue(StringUtils.equals(location.getName(), "DK Receiver"));

        Address address = location.getAddress();
        assertTrue(StringUtils.equals(address.getAddress1(), "1300 Oak side circle"));
        assertTrue(StringUtils.equals(address.getCity(), "Chanhassen"));
        assertTrue(StringUtils.equals(address.getState(), "Minnesota"));
        assertTrue(StringUtils.equals(address.getPostalCode(), "55317"));
    }

    @Test
    public void resolveLoad() {
        Load load = salesforceRevenovaRequestBodyParserPostPutLoad.resolveLoad(dataHashMap);

        assertTrue(StringUtils.equals(load.getExtSysId(), "a0jg000000EzBFdAAN"));
        assertTrue(StringUtils.equals(load.getName(), "28944"));
        assertTrue(StringUtils.equals(load.getTransportMode(), "Truckload"));
        assertTrue(StringUtils.equals(load.getLoadUrl(), "https://stage.carrier.riggoh.name/carrier-quote-bid/a0jg000000EzBFd"));
        assertTrue(load.getLoadStatus().intValue() == 1);
        assertTrue(StringUtils.equals(load.getModeName(), "Truckload"));
    }

    @Test
    public void resolveShipper() {
        Shipper shipper = salesforceRevenovaRequestBodyParserPostPutLoad.resolveShipper(dataHashMap);

        assertTrue(StringUtils.equals(shipper.getExtSysId(), "001g000001ywr1BAAQ"));
        assertTrue(StringUtils.equals(shipper.getName(), "DK Shipper"));
    }

    @Test
    public void resolveEquipmentType() {
        EquipmentType equipmentType  = salesforceRevenovaRequestBodyParserPostPutLoad.resolveEquipmentType(dataHashMap);

        //TODO: write test cases for EquipmentType - current payload does not specify an equipmentType
        //assertTrue(StringUtils.equals(equipmentType.getExtSysId(), "001g000001ywr1BAAQ"));

    }


    private Map<String, Object> dataHashMap;
    private String payloadJson = "{\n" +
            "   \"LoadDetails\":{\n" +
            "      \"Id\":\"a0jg000000EzBFdAAN\",\n" +
            "      \"MarketplaceExposedId\":\"null\",\n" +
            "      \"OwnerDetails\":{\n" +
            "         \"OwnerId\":\"0056A000000cQFIQA2\",\n" +
            "         \"OwnerName\":\"Admin Riggo\"\n" +
            "      },\n" +
            "      \"Name\":\"28944\",\n" +
            "      \"CreatedDate\":\"2019-08-05 15:37:19\",\n" +
            "      \"CustomerBillTo\":{\n" +
            "         \"billToId\":\"001g000001ywr1BAAQ\",\n" +
            "         \"BillToName\":\"DK Shipper\",\n" +
            "         \"billToTMSType\":\"Shipper/Consignee\",\n" +
            "         \"billToShippingStreet\":\"175 Bleecker st\",\n" +
            "         \"billToShippingCity\":\"New York\",\n" +
            "         \"billToShippingPostalCode\":10012,\n" +
            "         \"billToShippingState\":\"New York\",\n" +
            "         \"billToShippingCountry\":\"United States\"\n" +
            "      },\n" +
            "      \"rtms__Bill_of_Lading_Number__c\":\"null\",\n" +
            "      \"rtms__Carrier_Invoice_Total__c\":null,\n" +
            "      \"rtms__Carrier_Load__c\":true,\n" +
            "      \"rtms__Carrier_Payment_Total__c\":null,\n" +
            "      \"rtms__Carrier_Quote_Total__c\":null,\n" +
            "      \"CarrierRemitTo\":{\n" +
            "         \"CarrierRemitToId\":\"null\",\n" +
            "         \"CarrierRemitToName\":\"null\",\n" +
            "         \"CarrierRemitToTMSType\":\"null\",\n" +
            "         \"CarrierRemitToShippingStreet\":\"null\",\n" +
            "         \"CarrierRemitToShippingCity\":\"null\",\n" +
            "         \"CarrierRemitToShippingPostalCode\":\"null\",\n" +
            "         \"CarrierRemitToShippingState\":\"null\",\n" +
            "         \"CarrierRemitToShippingCountry\":\"null\"\n" +
            "      },\n" +
            "      \"CarrierService\":{\n" +
            "         \"CarrierServiceId\":\"null\",\n" +
            "         \"CarrierServiceName\":\"null\"\n" +
            "      },\n" +
            "      \"rtms__Carrier_Uploads__c\":\"null\",\n" +
            "      \"Carrier\":{\n" +
            "         \"CarrierId\":\"null\",\n" +
            "         \"CarrierName\":\"null\",\n" +
            "         \"CarrierTMSType\":\"null\",\n" +
            "         \"CarrierShippingStreet\":\"null\",\n" +
            "         \"CarrierShippingCity\":\"null\",\n" +
            "         \"CarrierShippingPostalCode\":\"null\",\n" +
            "         \"CarrierShippingState\":\"null\",\n" +
            "         \"CarrierShippingCountry\":\"null\"\n" +
            "      },\n" +
            "      \"rtms__City_Lane__c\":\"New York, NY:Chanhassen, MN\",\n" +
            "      \"rtms__Country_Lane__c\":\"US:US\",\n" +
            "      \"rtms__Customer_Check_Acceptable__c\":false,\n" +
            "      \"rtms__Customer_Invoice_Tax_Amount__c\":null,\n" +
            "      \"rtms__Customer_Invoice_Total__c\":null,\n" +
            "      \"rtms__Customer_Invoice_Transportation_Total__c\":0.00,\n" +
            "      \"rtms__Customer_Load__c\":true,\n" +
            "      \"rtms__Customer_Quote_Tax_Amount__c\":null,\n" +
            "      \"rtms__Customer_Quote_Total__c\":null,\n" +
            "      \"rtms__Customer_Quote_Transportation_Total__c\":0.00,\n" +
            "      \"Customer\":{\n" +
            "         \"CustomerId\":\"001g000001ywr1BAAQ\",\n" +
            "         \"CustomerName\":\"DK Shipper\",\n" +
            "         \"CustomerTMSType\":\"Shipper/Consignee\",\n" +
            "         \"CustomerShippingStreet\":\"175 Bleecker st\",\n" +
            "         \"CustomerShippingCity\":\"New York\",\n" +
            "         \"CustomerShippingPostalCode\":\"10012\",\n" +
            "         \"CustomerShippingState\":\"New York\",\n" +
            "         \"CustomerShippingCountry\":\"United States\"\n" +
            "      },\n" +
            "      \"rtms__Delivery_Status__c\":\"null\",\n" +
            "      \"rtms__Destination__c\":\"Chanhassen, Minnesota\",\n" +
            "      \"rtms__Distance_Kilometers__c\":1984.00311569094792000270369875646,\n" +
            "      \"rtms__Distance_Miles__c\":1232.802,\n" +
            "      \"Driver\":{\n" +
            "         \"DriverId\":\"null\",\n" +
            "         \"DriverName\":\"null\",\n" +
            "         \"DriverTransportationRole\":\"null\",\n" +
            "         \"DriverPhone\":\"null\",\n" +
            "         \"DriverMobilePhone\":\"null\",\n" +
            "         \"DriverMailingStreet\":\"null\",\n" +
            "         \"DriverMailingCity\":\"null\",\n" +
            "         \"DriverMailingPostalCode\":\"null\",\n" +
            "         \"DriverMailingState\":\"null\",\n" +
            "         \"DriverMailingCountry\":\"null\"\n" +
            "      },\n" +
            "      \"rtms__EDI_Reference_Numbers__c\":\"null\",\n" +
            "      \"rtms__EDI_Status__c\":\"null\",\n" +
            "      \"EquipmentType\":{\n" +
            "         \"EquipmentTypeId\":\"null\",\n" +
            "         \"EquipmentTypeName\":\"null\",\n" +
            "         \"EquipmentType_rtms__Category__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__Max_Volume__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__Volume_Units__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__Max_Pallets__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__Enabled__c\":\"false\",\n" +
            "         \"EquipmentType_rig_Truckstop_Type__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__ISO_Type_Group__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__ISO_Size_Type__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__Tare_Weight__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__Max_Weight__c\":\"null\",\n" +
            "         \"EquipmentType_rtms__Weight_Units__c\":\"null\"\n" +
            "      },\n" +
            "      \"rtms__Expected_Delivery_Date2__c\":\"2019-08-05 00:00:00\",\n" +
            "      \"rtms__Expected_Ship_Date2__c\":\"2019-08-05 00:00:00\",\n" +
            "      \"FirstStop\":{\n" +
            "         \"FirstStopId\":\"a0tg0000003QpxmAAC\",\n" +
            "         \"FirstStopName\":\"Stop 1\",\n" +
            "         \"FirstStoprtms__P_D__c\":\"Pickup\",\n" +
            "         \"FirstStoprig_Load_Customer_Name__c\":\"DK Shipper\",\n" +
            "         \"FirstStoprtms__Number__c\":\"1\",\n" +
            "         \"FirstStoprtms__Instructions__c\":\"null\",\n" +
            "         \"FirstStopLocation\":{\n" +
            "            \"FirstStopLocationId\":\"001g000001ywr1BAAQ\",\n" +
            "            \"FirstStopLocationName\":\"DK Shipper\",\n" +
            "            \"FirstStopLocationShippingStreet\":\"175 Bleecker st\",\n" +
            "            \"FirstStopLocationShippingShippingCity\":\"New York\",\n" +
            "            \"FirstStopLocationShippingShippingState\":\"New York\",\n" +
            "            \"FirstStopLocationShippingShippingPostalCode\":\"10012\"\n" +
            "         },\n" +
            "         \"FirstStoprtms__Shipping_Receiving_Hours__c\":\"01:01-02:02\",\n" +
            "         \"FirstStoprtms__Expected_Date__c\":\"2019-08-05 00:00:00\",\n" +
            "         \"FirstStoprtms__Expected_Day__c\":\"Monday\",\n" +
            "         \"FirstStoprtms__Appointment_Required__c\":\"true\",\n" +
            "         \"FirstStoprtms__Appointment_Time__c\":\"null\",\n" +
            "         \"FirstStoprtms__Stop_Status__c\":\"null\",\n" +
            "         \"FirstStoprtms__Carrier_Status2__c\":\"null\"\n" +
            "      },\n" +
            "      \"rtms__Hazardous_Materials__c\":false,\n" +
            "      \"rtms__Insurance_Amount__c\":null,\n" +
            "      \"rtms__Last_Reported_City__c\":\"null\",\n" +
            "      \"rtms__Last_Reported_Country__c\":\"null\",\n" +
            "      \"rtms__Last_Reported_Geolocation__c\":\"null\",\n" +
            "      \"rtms__Last_Reported_Location2__c\":\",\",\n" +
            "      \"rtms__Last_Reported_State_Province__c\":\"null\",\n" +
            "      \"LastStop\":{\n" +
            "         \"LastStopId\":\"a0tg0000003QpxnAAC\",\n" +
            "         \"LastStopName\":\"Stop 2\",\n" +
            "         \"LastStoprtms__P_D__c\":\"Delivery\",\n" +
            "         \"LastStoprig_Load_Customer_Name__c\":\"DK Shipper\",\n" +
            "         \"LastStoprtms__Number__c\":\"2\",\n" +
            "         \"LastStoprtms__Instructions__c\":\"null\",\n" +
            "         \"LastStopLocation\":{\n" +
            "            \"LastStopLocationId\":\"001g000001ywr1CAAQ\",\n" +
            "            \"LastStopLocationName\":\"DK Receiver\",\n" +
            "            \"LastStopLocationShippingStreet\":\"1300 Oak side circle\",\n" +
            "            \"LastStopLocationShippingShippingCity\":\"Chanhassen\",\n" +
            "            \"LastStopLocationShippingShippingState\":\"Minnesota\",\n" +
            "            \"LastStopLocationShippingShippingPostalCode\":\"55317\"\n" +
            "         },\n" +
            "         \"LastStoprtms__Shipping_Receiving_Hours__c\":\"10:10-11:11\",\n" +
            "         \"LastStoprtms__Expected_Date__c\":\"2019-08-05 00:00:00\",\n" +
            "         \"LastStoprtms__Expected_Day__c\":\"Monday\",\n" +
            "         \"LastStoprtms__Appointment_Required__c\":\"true\",\n" +
            "         \"LastStoprtms__Appointment_Time__c\":\"null\",\n" +
            "         \"LastStoprtms__Stop_Status__c\":\"null\",\n" +
            "         \"LastStoprtms__Carrier_Status2__c\":\"null\"\n" +
            "      },\n" +
            "      \"rtms__Load_Sequence_Number__c\":\"3705\",\n" +
            "      \"rtms__Load_Status_Comments__c\":\"null\",\n" +
            "      \"rtms__Load_Status_Requested__c\":false,\n" +
            "      \"rtms__Load_Status__c\":\"Unassigned\",\n" +
            "      \"rtms__Margin_Invoiced__c\":0.00,\n" +
            "      \"rtms__Margin_Paid__c\":0.00,\n" +
            "      \"rtms__Margin_Pct_Invoiced__c\":null,\n" +
            "      \"rtms__Margin_Pct_Quoted__c\":null,\n" +
            "      \"rtms__Margin_Quoted__c\":0.00,\n" +
            "      \"Mode\":{\n" +
            "         \"ModeId\":\"a0k6A0000035SUxQAM\",\n" +
            "         \"ModeName\":\"Truckload\",\n" +
            "         \"ModeEnabled\":\"true\",\n" +
            "         \"ModeLTL\":\"false\",\n" +
            "         \"ModeOrder\":\"1\",\n" +
            "         \"ModeRequiredApplication\":\"Completed W9 Tax Form;Certificate of Insurance;Operating Authority\"\n" +
            "      },\n" +
            "      \"rtms__OTD_Counter__c\":0,\n" +
            "      \"rtms__Order_Date__c\":\"2019-08-05 00:00:00\",\n" +
            "      \"rtms__Order_Number__c\":\"null\",\n" +
            "      \"rtms__Order_Total__c\":0.00,\n" +
            "      \"rtms__Origin__c\":\"New York, New York\",\n" +
            "      \"rtms__Other_Instructions__c\":\"null\",\n" +
            "      \"rtms__POD_Received__c\":false,\n" +
            "      \"rtms__PO_Number__c\":\"null\",\n" +
            "      \"rtms__PRO_Number__c\":\"null\",\n" +
            "      \"rtms__Payment_Terms__c\":\"Third Party\",\n" +
            "      \"rtms__Postal_Code_Lane__c\":\"10012:55317\",\n" +
            "      \"rtms__Sales_Status__c\":\"Pending\",\n" +
            "      \"rtms__Schedule_Status__c\":\"null\",\n" +
            "      \"rtms__Ship_From_Address__c\":\"175 Bleecker st, New York, New York 10012\",\n" +
            "      \"rtms__Ship_Status__c\":\"null\",\n" +
            "      \"rtms__Ship_To_Address__c\":\"1300 Oak side circle, Chanhassen, Minnesota 55317\",\n" +
            "      \"rtms__Site_URL__c\":\"null\",\n" +
            "      \"rtms__State_Lane__c\":\"NY:MN\",\n" +
            "      \"rtms__Tax_Exempt__c\":false,\n" +
            "      \"rtms__Tender_Accepted_Date__c\":\"null\",\n" +
            "      \"rtms__Total_Weight__c\":133332,\n" +
            "      \"rtms__Tracking_Number__c\":\"null\",\n" +
            "      \"rtms__Zip3_Lane__c\":\"100:553\",\n" +
            "      \"rtms__Zip5_Lane__c\":\"10012:55317\",\n" +
            "      \"rtms__Weight_Units__c\":\"lbs\",\n" +
            "      \"rtms__Mode_Name__c\":\"Truckload\",\n" +
            "      \"rtms__Temperature_Controlled__c\":false,\n" +
            "      \"rtms__Tracking_Provider__c\":\"null\",\n" +
            "      \"Vendor\":{\n" +
            "         \"VendorId\":\"null\",\n" +
            "         \"VendorName\":\"null\",\n" +
            "         \"VendorTMSType\":\"null\",\n" +
            "         \"VendorShippingStreet\":\"null\",\n" +
            "         \"VendorShippingCity\":\"null\",\n" +
            "         \"VendorShippingPostalCode\":\"null\",\n" +
            "         \"VendorShippingState\":\"null\",\n" +
            "         \"VendorShippingCountry\":\"null\"\n" +
            "      },\n" +
            "      \"Posted_Notes__c\":\"null\",\n" +
            "      \"Pickup_Delivery_Number__c\":\"null\",\n" +
            "      \"Stop_Reference_Numbers__c\":\"null\",\n" +
            "      \"Tractor_Number__c\":\"null\",\n" +
            "      \"Trailer_Number__c\":\"null\",\n" +
            "      \"Historical_Load__c\":false,\n" +
            "      \"Legacy_Load_Number__c\":\"null\",\n" +
            "      \"Pickup_Appointment_Time__c\":\"null\",\n" +
            "      \"Delivery_Appointment_Time__c\":\"null\",\n" +
            "      \"rig_riggoh_status__c\":\"Unassigned\",\n" +
            "      \"EDI_Shipment_ID__c\":\"null\",\n" +
            "      \"food_grade_trailer_required__c\":false,\n" +
            "      \"riggoh_Formatted_Expected_Delivery_Date__c\":\"2019-08-05\",\n" +
            "      \"riggoh_Formatted_Expected_Ship_Date__c\":\"2019-08-05\",\n" +
            "      \"CarrierSalesRep\":{\n" +
            "         \"CarrierSalesRepId\":\"null\",\n" +
            "         \"CarrierSalesRepName\":\"null\",\n" +
            "         \"CarrierSalesRepEmail\":\"null\"\n" +
            "      },\n" +
            "      \"rtms__Master_BOL_Option__c\":\"Shipper to Consignee\",\n" +
            "      \"rig_Expected_TTT__c\":null,\n" +
            "      \"rig_Previous_Cost__c\":null,\n" +
            "      \"riggoh_Required_Temperature__c\":null,\n" +
            "      \"rig_Lead_Source__c\":\"null\",\n" +
            "      \"Pickup_Location__c\":\"New York, NY\",\n" +
            "      \"Delivery_Location__c\":\"ChanhassenMN\",\n" +
            "      \"rigPostedRate__c\":null,\n" +
            "      \"rig_Completed_Date__c\":\"null\",\n" +
            "      \"rig_Load_URL__c\":\"https://stage.carrier.riggoh.name/carrier-quote-bid/a0jg000000EzBFd\",\n" +
            "      \"rig_Team_Required__c\":false,\n" +
            "      \"rtms__Pickup_Delivery_Status__c\":\"Stop 1 - Pickup --- No tracking updates available for this stop. Stop 2 - Delivery - No tracking updates available for this stop.\",\n" +
            "      \"rtms__Carrier_Portal__c\":false,\n" +
            "      \"rtms__EDI_Provider__c\":\"null\",\n" +
            "      \"Send_Status_Updates__c\":false,\n" +
            "      \"rig_Email_Automation__c\":true,\n" +
            "      \"rig_Load_Board__c\":true\n" +
            "   }\n" +
            "}";
}
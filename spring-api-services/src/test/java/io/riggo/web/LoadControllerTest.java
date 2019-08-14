package io.riggo.web;

import io.riggo.data.domain.EquipmentType;
import io.riggo.data.domain.Load;
import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.Shipper;
import io.riggo.data.services.*;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserForPatchLoadStop;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserPostPutLoad;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoadController.class)
public class LoadControllerTest {

    private static Logger logger = LoggerFactory.getLogger(LoadControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoadService loadService;

    @MockBean
    private ShipperService shipperService;

    @MockBean
    private EquipmentTypeService equipmentTypeService;

    @MockBean
    private LoadStopService loadStopService;

    @MockBean
    private LoadLineItemService loadLineItemService;

    @MockBean
    private LocationService locationService;

    @MockBean
    private AddressService addressService;

    @MockBean
    private SalesforceRevenovaRequestBodyParserPostPutLoad salesforceRevenovaRequestBodyParserPostPutLoad;

    @MockBean
    private SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem salesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem;

    @MockBean
    private SalesforceRevenovaRequestBodyParserForPatchLoadStop salesforceRevenovaRequestBodyParserForPatchLoadStop;


    @WithMockUser(value = "spring", authorities = {"read:load"})
    @Test
    public void getLoadById() throws Exception {

        Load load = new Load();
        load.setId(1);
        load.setName("load");

        given(loadService.findById(1)).willReturn(java.util.Optional.of(load));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.load.id").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.load.id", is(1)))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @Test
    public void getLoadByIdUnauthenticated() throws Exception {
        Load load = new Load();
        load.setId(1);
        load.setName("load");

        given(loadService.findById(1)).willReturn(java.util.Optional.of(load));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:load"})
    @Test
    public void postLoadHappyPath() throws Exception {

        Load load = new Load();
        load.setId(1);
        load.setName("load");
        load.setExtSysId("1");

        Shipper shipper = new Shipper();
        shipper.setId(1);
        shipper.setName("shipper");
        shipper.setExtSysId("1");

        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setId(1);
        equipmentType.setName("equipmentType");
        equipmentType.setExtSysId("1");

        LoadStop firstStop = new LoadStop();
        firstStop.setId(1);
        firstStop.setName("firstStop");
        firstStop.setExtSysId("1");

        LoadStop lastStop = new LoadStop();
        lastStop.setId(1);
        lastStop.setName("firstStop");
        lastStop.setExtSysId("1");

        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveLoad(any(Map.class))).willReturn(load);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveShipper(any(Map.class))).willReturn(shipper);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveEquipmentType(any(Map.class))).willReturn(equipmentType);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveFirstStop(any(Map.class))).willReturn(firstStop);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveLastStop(any(Map.class))).willReturn(lastStop);

        given(loadService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(loadService.save(load)).willReturn(load);

        MvcResult result = mvc.perform(post(Paths.API_VERSION_LOAD)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:load"})
    @Test
    public void putLoad() throws Exception {
        Load load = new Load();
        load.setId(1);
        load.setExtSysId("1");
        load.setName("load");

        Shipper shipper = new Shipper();
        shipper.setId(1);
        shipper.setName("shipper");
        shipper.setExtSysId("1");

        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setId(1);
        equipmentType.setName("equipmentType");
        equipmentType.setExtSysId("1");

        LoadStop firstStop = new LoadStop();
        firstStop.setId(1);
        firstStop.setName("firstStop");
        firstStop.setExtSysId("1");

        LoadStop lastStop = new LoadStop();
        lastStop.setId(1);
        lastStop.setName("firstStop");
        lastStop.setExtSysId("1");

        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveLoad(any(Map.class))).willReturn(load);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveShipper(any(Map.class))).willReturn(shipper);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveEquipmentType(any(Map.class))).willReturn(equipmentType);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveFirstStop(any(Map.class))).willReturn(firstStop);
        given(salesforceRevenovaRequestBodyParserPostPutLoad.resolveLastStop(any(Map.class))).willReturn(lastStop);

        given(loadService.findByExtSysId("1")).willReturn(java.util.Optional.of(load));
        given(loadService.save(load)).willReturn(load);

        MvcResult result = mvc.perform(put(Paths.API_VERSION_LOAD)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andReturn();


        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void getLoadByIdRequiresReadLoadPermission() throws Exception {
        Load load = new Load();
        load.setId(1);
        load.setName("load");

        given(loadService.findById(1)).willReturn(java.util.Optional.of(load));

        mvc.perform(get(Paths.API_VERSION_LOAD + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    private static String jsonPostLoadFor200 = "{\n" +
            "\t\"LoadDetails\": {\n" +
            "\t\t\"Id\": \"a0jg000000ExRFcAAN\",\n" +
            "\t\t\"MarketplaceExposedId\": \"null\",\n" +
            "\t\t\"OwnerDetails\": {\n" +
            "\t\t\t\"OwnerId\": \"0056A000000cQFIQA2\",\n" +
            "\t\t\t\"OwnerName\": \"Admin Riggo\"\n" +
            "\t\t},\n" +
            "\t\t\"Name\": \"28828\",\n" +
            "\t\t\"CreatedDate\": \"2019-07-17 14:11:15\",\n" +
            "\t\t\"CustomerBillTo\": {\n" +
            "\t\t\t\"billToId\": \"null\",\n" +
            "\t\t\t\"BillToName\": \"null\",\n" +
            "\t\t\t\"billToTMSType\": \"null\",\n" +
            "\t\t\t\"billToShippingStreet\": \"null\",\n" +
            "\t\t\t\"billToShippingCity\": \"null\",\n" +
            "\t\t\t\"billToShippingPostalCode\": null,\n" +
            "\t\t\t\"billToShippingState\": \"null\",\n" +
            "\t\t\t\"billToShippingCountry\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__Bill_of_Lading_Number__c\": \"null\",\n" +
            "\t\t\"rtms__Carrier_Invoice_Total__c\": null,\n" +
            "\t\t\"rtms__Carrier_Load__c\": true,\n" +
            "\t\t\"rtms__Carrier_Payment_Total__c\": null,\n" +
            "\t\t\"rtms__Carrier_Quote_Total__c\": null,\n" +
            "\t\t\"CarrierRemitTo\": {\n" +
            "\t\t\t\"CarrierRemitToId\": \"null\",\n" +
            "\t\t\t\"CarrierRemitToName\": \"null\",\n" +
            "\t\t\t\"CarrierRemitToTMSType\": \"null\",\n" +
            "\t\t\t\"CarrierRemitToShippingStreet\": \"null\",\n" +
            "\t\t\t\"CarrierRemitToShippingCity\": \"null\",\n" +
            "\t\t\t\"CarrierRemitToShippingPostalCode\": \"null\",\n" +
            "\t\t\t\"CarrierRemitToShippingState\": \"null\",\n" +
            "\t\t\t\"CarrierRemitToShippingCountry\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"CarrierService\": {\n" +
            "\t\t\t\"CarrierServiceId\": \"null\",\n" +
            "\t\t\t\"CarrierServiceName\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__Carrier_Uploads__c\": \"null\",\n" +
            "\t\t\"Carrier\": {\n" +
            "\t\t\t\"CarrierId\": \"null\",\n" +
            "\t\t\t\"CarrierName\": \"null\",\n" +
            "\t\t\t\"CarrierTMSType\": \"null\",\n" +
            "\t\t\t\"CarrierShippingStreet\": \"null\",\n" +
            "\t\t\t\"CarrierShippingCity\": \"null\",\n" +
            "\t\t\t\"CarrierShippingPostalCode\": \"null\",\n" +
            "\t\t\t\"CarrierShippingState\": \"null\",\n" +
            "\t\t\t\"CarrierShippingCountry\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__City_Lane__c\": \"New York, NY:CHATTANOOGA, TN\",\n" +
            "\t\t\"rtms__Country_Lane__c\": \"US:US\",\n" +
            "\t\t\"rtms__Customer_Check_Acceptable__c\": false,\n" +
            "\t\t\"rtms__Customer_Invoice_Tax_Amount__c\": null,\n" +
            "\t\t\"rtms__Customer_Invoice_Total__c\": null,\n" +
            "\t\t\"rtms__Customer_Invoice_Transportation_Total__c\": 0.00,\n" +
            "\t\t\"rtms__Customer_Load__c\": true,\n" +
            "\t\t\"rtms__Customer_Quote_Tax_Amount__c\": null,\n" +
            "\t\t\"rtms__Customer_Quote_Total__c\": null,\n" +
            "\t\t\"rtms__Customer_Quote_Transportation_Total__c\": 0.00,\n" +
            "\t\t\"Customer\": {\n" +
            "\t\t\t\"CustomerId\": \"null\",\n" +
            "\t\t\t\"CustomerName\": \"null\",\n" +
            "\t\t\t\"CustomerTMSType\": \"null\",\n" +
            "\t\t\t\"CustomerShippingStreet\": \"null\",\n" +
            "\t\t\t\"CustomerShippingCity\": \"null\",\n" +
            "\t\t\t\"CustomerShippingPostalCode\": \"null\",\n" +
            "\t\t\t\"CustomerShippingState\": \"null\",\n" +
            "\t\t\t\"CustomerShippingCountry\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__Delivery_Status__c\": \"null\",\n" +
            "\t\t\"rtms__Destination__c\": \"CHATTANOOGA, Tennessee\",\n" +
            "\t\t\"rtms__Distance_Kilometers__c\": 1329.95263699142702185972631487469,\n" +
            "\t\t\"rtms__Distance_Miles__c\": 826.394,\n" +
            "\t\t\"Driver\": {\n" +
            "\t\t\t\"DriverId\": \"null\",\n" +
            "\t\t\t\"DriverName\": \"null\",\n" +
            "\t\t\t\"DriverTransportationRole\": \"null\",\n" +
            "\t\t\t\"DriverPhone\": \"null\",\n" +
            "\t\t\t\"DriverMobilePhone\": \"null\",\n" +
            "\t\t\t\"DriverMailingStreet\": \"null\",\n" +
            "\t\t\t\"DriverMailingCity\": \"null\",\n" +
            "\t\t\t\"DriverMailingPostalCode\": \"null\",\n" +
            "\t\t\t\"DriverMailingState\": \"null\",\n" +
            "\t\t\t\"DriverMailingCountry\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__EDI_Reference_Numbers__c\": \"null\",\n" +
            "\t\t\"rtms__EDI_Status__c\": \"null\",\n" +
            "\t\t\"EquipmentType\": {\n" +
            "\t\t\t\"EquipmentTypeId\": \"null\",\n" +
            "\t\t\t\"EquipmentTypeName\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Category__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Max_Volume__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Volume_Units__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Max_Pallets__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Enabled__c\": \"false\",\n" +
            "\t\t\t\"EquipmentType_rig_Truckstop_Type__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__ISO_Type_Group__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__ISO_Size_Type__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Tare_Weight__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Max_Weight__c\": \"null\",\n" +
            "\t\t\t\"EquipmentType_rtms__Weight_Units__c\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__Expected_Delivery_Date2__c\": \"2019-07-17 00:00:00\",\n" +
            "\t\t\"rtms__Expected_Ship_Date2__c\": \"2019-07-17 00:00:00\",\n" +
            "\t\t\"FirstStop\": {\n" +
            "\t\t\t\"FirstStopId\": \"a0tg0000003QOQJAA4\",\n" +
            "\t\t\t\"FirstStopName\": \"Stop 1\",\n" +
            "\t\t\t\"FirstStoprtms__P_D__c\": \"Pickup\",\n" +
            "\t\t\t\"FirstStoprig_Load_Customer_Name__c\": \"null\",\n" +
            "\t\t\t\"FirstStoprtms__Number__c\": \"1\",\n" +
            "\t\t\t\"FirstStoprtms__Instructions__c\": \"*TRAILER REQUIRED TO BE CLEAN, DRY, NO ODOR, NO HOLES, NO DEBRIS, NO DUNNAGE DRIVER MUST CHECK-IN WITH FACILITY AT OR PRIOR TO APPOINTMENT MUST NOTIFY RIGGOH 30 MINUTES PRIOR TO DETENTION STARTING *DETENTION STARTS AFTER 2 HOURS, IN AND OUT TIMES MUST BE DOCUMENTED BY SHIPPER\",\n" +
            "\t\t\t\"FirstStopLocation\": {\n" +
            "\t\t\t\t\"FirstStopLocationId\": \"0016A00000NKPItQAP\",\n" +
            "\t\t\t\t\"FirstStopLocationName\": \"Prompt Logistics Customer\",\n" +
            "\t\t\t\t\"FirstStopLocationShippingStreet\": \"25 Broadway\",\n" +
            "\t\t\t\t\"FirstStopLocationShippingShippingCity\": \"New York\",\n" +
            "\t\t\t\t\"FirstStopLocationShippingShippingState\": \"New York\",\n" +
            "\t\t\t\t\"FirstStopLocationShippingShippingPostalCode\": \"10004\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"FirstStoprtms__Shipping_Receiving_Hours__c\": \"null\",\n" +
            "\t\t\t\"FirstStoprtms__Expected_Date__c\": \"2019-07-17 00:00:00\",\n" +
            "\t\t\t\"FirstStoprtms__Expected_Day__c\": \"Wednesday\",\n" +
            "\t\t\t\"FirstStoprtms__Appointment_Required__c\": \"false\",\n" +
            "\t\t\t\"FirstStoprtms__Appointment_Time__c\": \"null\",\n" +
            "\t\t\t\"FirstStoprtms__Stop_Status__c\": \"null\",\n" +
            "\t\t\t\"FirstStoprtms__Carrier_Status2__c\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__Hazardous_Materials__c\": false,\n" +
            "\t\t\"rtms__Insurance_Amount__c\": null,\n" +
            "\t\t\"rtms__Last_Reported_City__c\": \"null\",\n" +
            "\t\t\"rtms__Last_Reported_Country__c\": \"null\",\n" +
            "\t\t\"rtms__Last_Reported_Geolocation__c\": \"null\",\n" +
            "\t\t\"rtms__Last_Reported_Location2__c\": \",\",\n" +
            "\t\t\"rtms__Last_Reported_State_Province__c\": \"null\",\n" +
            "\t\t\"LastStop\": {\n" +
            "\t\t\t\"LastStopId\": \"a0tg0000003QOQKAA4\",\n" +
            "\t\t\t\"LastStopName\": \"Stop 2\",\n" +
            "\t\t\t\"LastStoprtms__P_D__c\": \"Delivery\",\n" +
            "\t\t\t\"LastStoprig_Load_Customer_Name__c\": \"null\",\n" +
            "\t\t\t\"LastStoprtms__Number__c\": \"2\",\n" +
            "\t\t\t\"LastStoprtms__Instructions__c\": \"null\",\n" +
            "\t\t\t\"LastStopLocation\": {\n" +
            "\t\t\t\t\"LastStopLocationId\": \"0016A00000cexDOQAY\",\n" +
            "\t\t\t\t\"LastStopLocationName\": \"FIRST LOGISTICS LLC\",\n" +
            "\t\t\t\t\"LastStopLocationShippingStreet\": \"3326 ROBERTS ROAD\",\n" +
            "\t\t\t\t\"LastStopLocationShippingShippingCity\": \"CHATTANOOGA\",\n" +
            "\t\t\t\t\"LastStopLocationShippingShippingState\": \"Tennessee\",\n" +
            "\t\t\t\t\"LastStopLocationShippingShippingPostalCode\": \"37416\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"LastStoprtms__Shipping_Receiving_Hours__c\": \"null\",\n" +
            "\t\t\t\"LastStoprtms__Expected_Date__c\": \"2019-07-17 00:00:00\",\n" +
            "\t\t\t\"LastStoprtms__Expected_Day__c\": \"Wednesday\",\n" +
            "\t\t\t\"LastStoprtms__Appointment_Required__c\": \"false\",\n" +
            "\t\t\t\"LastStoprtms__Appointment_Time__c\": \"null\",\n" +
            "\t\t\t\"LastStoprtms__Stop_Status__c\": \"null\",\n" +
            "\t\t\t\"LastStoprtms__Carrier_Status2__c\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__Load_Sequence_Number__c\": \"3589\",\n" +
            "\t\t\"rtms__Load_Status_Comments__c\": \"null\",\n" +
            "\t\t\"rtms__Load_Status_Requested__c\": false,\n" +
            "\t\t\"rtms__Load_Status__c\": \"Unassigned\",\n" +
            "\t\t\"rtms__Margin_Invoiced__c\": 0.00,\n" +
            "\t\t\"rtms__Margin_Paid__c\": 0.00,\n" +
            "\t\t\"rtms__Margin_Pct_Invoiced__c\": null,\n" +
            "\t\t\"rtms__Margin_Pct_Quoted__c\": null,\n" +
            "\t\t\"rtms__Margin_Quoted__c\": 0.00,\n" +
            "\t\t\"Mode\": {\n" +
            "\t\t\t\"ModeId\": \"null\",\n" +
            "\t\t\t\"ModeName\": \"null\",\n" +
            "\t\t\t\"ModeEnabled\": \"false\",\n" +
            "\t\t\t\"ModeLTL\": \"false\",\n" +
            "\t\t\t\"ModeOrder\": \"null\",\n" +
            "\t\t\t\"ModeRequiredApplication\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__OTD_Counter__c\": 0,\n" +
            "\t\t\"rtms__Order_Date__c\": \"2019-07-17 00:00:00\",\n" +
            "\t\t\"rtms__Order_Number__c\": \"null\",\n" +
            "\t\t\"rtms__Order_Total__c\": null,\n" +
            "\t\t\"rtms__Origin__c\": \"New York, New York\",\n" +
            "\t\t\"rtms__Other_Instructions__c\": \"null\",\n" +
            "\t\t\"rtms__POD_Received__c\": false,\n" +
            "\t\t\"rtms__PO_Number__c\": \"null\",\n" +
            "\t\t\"rtms__PRO_Number__c\": \"null\",\n" +
            "\t\t\"rtms__Payment_Terms__c\": \"Third Party\",\n" +
            "\t\t\"rtms__Postal_Code_Lane__c\": \"10004:37416\",\n" +
            "\t\t\"rtms__Sales_Status__c\": \"Pending\",\n" +
            "\t\t\"rtms__Schedule_Status__c\": \"null\",\n" +
            "\t\t\"rtms__Ship_From_Address__c\": \"25 Broadway, New York, New York 10004\",\n" +
            "\t\t\"rtms__Ship_Status__c\": \"null\",\n" +
            "\t\t\"rtms__Ship_To_Address__c\": \"3326 ROBERTS ROAD, CHATTANOOGA, Tennessee 37416\",\n" +
            "\t\t\"rtms__Site_URL__c\": \"null\",\n" +
            "\t\t\"rtms__State_Lane__c\": \"NY:TN\",\n" +
            "\t\t\"rtms__Tax_Exempt__c\": false,\n" +
            "\t\t\"rtms__Tender_Accepted_Date__c\": \"null\",\n" +
            "\t\t\"rtms__Total_Weight__c\": 1,\n" +
            "\t\t\"rtms__Tracking_Number__c\": \"null\",\n" +
            "\t\t\"rtms__Zip3_Lane__c\": \"100:374\",\n" +
            "\t\t\"rtms__Zip5_Lane__c\": \"10004:37416\",\n" +
            "\t\t\"rtms__Weight_Units__c\": \"lbs\",\n" +
            "\t\t\"rtms__Mode_Name__c\": \"null\",\n" +
            "\t\t\"rtms__Temperature_Controlled__c\": false,\n" +
            "\t\t\"rtms__Tracking_Provider__c\": \"null\",\n" +
            "\t\t\"Vendor\": {\n" +
            "\t\t\t\"VendorId\": \"null\",\n" +
            "\t\t\t\"VendorName\": \"null\",\n" +
            "\t\t\t\"VendorTMSType\": \"null\",\n" +
            "\t\t\t\"VendorShippingStreet\": \"null\",\n" +
            "\t\t\t\"VendorShippingCity\": \"null\",\n" +
            "\t\t\t\"VendorShippingPostalCode\": \"null\",\n" +
            "\t\t\t\"VendorShippingState\": \"null\",\n" +
            "\t\t\t\"VendorShippingCountry\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"Posted_Notes__c\": \"null\",\n" +
            "\t\t\"Pickup_Delivery_Number__c\": \"null\",\n" +
            "\t\t\"Stop_Reference_Numbers__c\": \"null\",\n" +
            "\t\t\"Tractor_Number__c\": \"null\",\n" +
            "\t\t\"Trailer_Number__c\": \"null\",\n" +
            "\t\t\"Historical_Load__c\": false,\n" +
            "\t\t\"Legacy_Load_Number__c\": \"null\",\n" +
            "\t\t\"Pickup_Appointment_Time__c\": \"null\",\n" +
            "\t\t\"Delivery_Appointment_Time__c\": \"null\",\n" +
            "\t\t\"rig_riggoh_status__c\": \"Unassigned\",\n" +
            "\t\t\"EDI_Shipment_ID__c\": \"null\",\n" +
            "\t\t\"food_grade_trailer_required__c\": false,\n" +
            "\t\t\"riggoh_Formatted_Expected_Delivery_Date__c\": \"2019-07-17\",\n" +
            "\t\t\"riggoh_Formatted_Expected_Ship_Date__c\": \"2019-07-17\",\n" +
            "\t\t\"CarrierSalesRep\": {\n" +
            "\t\t\t\"CarrierSalesRepId\": \"null\",\n" +
            "\t\t\t\"CarrierSalesRepName\": \"null\",\n" +
            "\t\t\t\"CarrierSalesRepEmail\": \"null\"\n" +
            "\t\t},\n" +
            "\t\t\"rtms__Master_BOL_Option__c\": \"Shipper to Consignee\",\n" +
            "\t\t\"rig_Expected_TTT__c\": null,\n" +
            "\t\t\"rig_Previous_Cost__c\": null,\n" +
            "\t\t\"riggoh_Required_Temperature__c\": null,\n" +
            "\t\t\"rig_Lead_Source__c\": \"null\",\n" +
            "\t\t\"Pickup_Location__c\": \"New York, NY\",\n" +
            "\t\t\"Delivery_Location__c\": \"CHATTANOOGATN\",\n" +
            "\t\t\"rigPostedRate__c\": null,\n" +
            "\t\t\"rig_Completed_Date__c\": \"null\",\n" +
            "\t\t\"rig_Load_URL__c\": \"https://stage.carrier.riggoh.name/carrier-quote-bid/a0jg000000ExRFc\",\n" +
            "\t\t\"rig_Team_Required__c\": false,\n" +
            "\t\t\"rtms__Pickup_Delivery_Status__c\": \"Stop 1 - Pickup --- No tracking updates available for this stop. Stop 2 - Delivery - No tracking updates available for this stop.\",\n" +
            "\t\t\"rtms__Carrier_Portal__c\": false,\n" +
            "\t\t\"rtms__EDI_Provider__c\": \"null\",\n" +
            "\t\t\"Send_Status_Updates__c\": false,\n" +
            "\t\t\"rig_Email_Automation__c\": true,\n" +
            "\t\t\"rig_Load_Board__c\": true\n" +
            "\t}\n" +
            "}";
}
package io.riggo.web;

import io.riggo.data.domain.Load;
import io.riggo.data.domain.LoadStop;
import io.riggo.data.services.AddressService;
import io.riggo.data.services.LoadService;
import io.riggo.data.services.LoadStopService;
import io.riggo.data.services.LocationService;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserForPatchLoadStop;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoadStopController.class)
public class LoadStopControllerTest {

    private static Logger logger = LoggerFactory.getLogger(LoadStopControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoadService loadService;

    @MockBean
    private LoadStopService loadStopService;

    @MockBean
    private LocationService locationService;

    @MockBean
    private AddressService addressService;


    @MockBean
    private SalesforceRevenovaRequestBodyParserForPatchLoadStop salesforceRevenovaRequestBodyParserForPatchLoadStop;

    @MockBean
    private AuthenticationFacade authenticationFacade;


    @WithMockUser(value = "spring", authorities = {"write:load"})
    @Test
    public void putInsertLoadStopHappyPath() throws Exception {
        Load load = new Load();
        load.setId(1);
        load.setSiteId(100);
        load.setName("load");
        load.setExtSysId("1");

        LoadStop firstStop = new LoadStop();
        firstStop.setId(1);
        firstStop.setName("firstStop");
        firstStop.setExtSysId("1");
        firstStop.setLoadExtSysId("1");

        LoadStop lastStop = new LoadStop();
        lastStop.setId(1);
        lastStop.setName("firstStop");
        lastStop.setExtSysId("2");
        lastStop.setLoadExtSysId("1");

        List<LoadStop> loadStopList = new ArrayList<>();
        loadStopList.add(firstStop);
        loadStopList.add(lastStop);

        given(salesforceRevenovaRequestBodyParserForPatchLoadStop.resolveLoadStopsList(any(Map.class))).willReturn(loadStopList);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(loadService.findByExtSysId("1", 100)).willReturn(java.util.Optional.of(load));

        given(loadStopService.findByExtSysId("1", 100)).willReturn(java.util.Optional.empty());
        given(loadStopService.findByExtSysId("2", 100)).willReturn(java.util.Optional.empty());

        given(loadStopService.save(firstStop)).willReturn(firstStop);
        given(loadStopService.save(lastStop)).willReturn(lastStop);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD_STOP)
                .content(jsonLoad)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(2)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:load"})
    @Test
    public void putInsertLoadStopWithFailures() throws Exception {
        Load load = new Load();
        load.setId(1);
        load.setSiteId(100);
        load.setName("load");
        load.setExtSysId("1");

        LoadStop firstStop = new LoadStop();
        firstStop.setId(1);
        firstStop.setName("firstStop");
        firstStop.setExtSysId("1");

        LoadStop lastStop = new LoadStop();
        lastStop.setId(1);
        lastStop.setName("firstStop");
        lastStop.setExtSysId("2");

        List<LoadStop> loadStopList = new ArrayList<>();
        loadStopList.add(firstStop);
        loadStopList.add(lastStop);

        given(salesforceRevenovaRequestBodyParserForPatchLoadStop.resolveLoadStopsList(any(Map.class))).willReturn(loadStopList);
        given(authenticationFacade.getSiteId()).willReturn(100);
        given(loadService.save(load)).willReturn(load);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD_STOP)
                .content(jsonLoad)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.failures", is(2)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void unauthorized() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD_STOP)
                .content(jsonLoad)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }

    @WithMockUser(value = "spring")
    @Test
    public void unauthorizededWithLoadId() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD + "/1" + Paths.STOP)
                .content(jsonLoad)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }

    @Test
    public void unauthenticated() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD_STOP)
                .content(jsonLoad)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @Test
    public void unauthenticatedWithLoadId() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD + "/1" + Paths.STOP)
                .content(jsonLoad)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    private final String jsonLoad = "{\n"+
            "\t\"LoadStops\": [{\n"+
            "\t\t\"Id\": \"a0t4B00000DUeDZQA1\",\n"+
            "\t\t\"StopName\": \"Stop 1\",\n"+
            "\t\t\"Stoprtms__P_D__c\": \"Pickup\",\n"+
            "\t\t\"Stoprig_Load_Customer_Name__c\": \"Emerge TMS Customer\",\n"+
            "\t\t\"Stoprtms__Number__c\": \"1\",\n"+
            "\t\t\"Stoprtms__City__c\": \"Phoenix\",\n"+
            "\t\t\"Stoprtms__State_Province__c\": \"AZ\",\n"+
            "\t\t\"Stoprtms__Postal_Code__c\": \"85009\",\n"+
            "\t\t\"Stoprtms__Country__c\": \"US\",\n"+
            "\t\t\"Stoprtms__Address2__c\": \"Phoenix, AZ 85009\",\n"+
            "\t\t\"Stoprtms__Shipping_Receiving_Hours__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Expected_Date__c\": \"2019-09-10 00:00:00\",\n"+
            "\t\t\"Stoprtms__Expected_Day__c\": \"Tuesday\",\n"+
            "\t\t\"Stoprtms__Appointment_Required__c\": \"false\",\n"+
            "\t\t\"Stoprtms__Appointment_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Stop_Status__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status2__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Arrival_Date__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Status__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Arrival_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_ETA_Date__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_ETA_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status_as_of__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status_Details__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status_Reason__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Cumulative_Miles__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Departure_Date__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Departure_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__EDI_Provider__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Geolocation__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Instructions__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Is_Dropoff__c\": \"false\",\n"+
            "\t\t\"Stoprtms__Is_Pickup__c\": \"true\",\n"+
            "\t\t\"Stoprtms__Kilometers_Away__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Load__c\": \"a0j4B000001UoTQQA0\",\n"+
            "\t\t\"stoptemLoadMarketplaceExposedId\": \"null\",\n"+
            "\t\t\"StopLocation\": {\n"+
            "\t\t\t\"LocationId\": \"null\",\n"+
            "\t\t\t\"LocationName\": \"null\",\n"+
            "\t\t\t\"LocationShippingStreet\": \"null\",\n"+
            "\t\t\t\"LocationShippingCity\": \"null\",\n"+
            "\t\t\t\"LocationShippingState\": \"null\",\n"+
            "\t\t\t\"LocationShippingPostalCode\": \"null\"\n"+
            "\t\t},\n"+
            "\t\t\"Stoprtms__Location_Name__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Miles_Away__c\": \"null\",\n"+
            "\t\t\"StopModify_Boolean__c\": \"false\",\n"+
            "\t\t\"Stoprtms__OTD_Counter__c\": \"0\",\n"+
            "\t\t\"Stoprtms__P_DAS__c\": \"On Time\",\n"+
            "\t\t\"Stoprtms__Pickup_Delivery_Number__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Pickup_Delivery_Status__c\": \"No tracking updates available for this stop.\",\n"+
            "\t\t\"Stoprtms__References__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Shipping_Receiving_Contact__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Tracking_Provider_Customer__c\": \"null\"\n"+
            "\t}, {\n"+
            "\t\t\"Id\": \"a0t4B00000DUeDaQAL\",\n"+
            "\t\t\"StopName\": \"Stop 2\",\n"+
            "\t\t\"Stoprtms__P_D__c\": \"Delivery\",\n"+
            "\t\t\"Stoprig_Load_Customer_Name__c\": \"Emerge TMS Customer\",\n"+
            "\t\t\"Stoprtms__Number__c\": \"2\",\n"+
            "\t\t\"Stoprtms__City__c\": \"Mission\",\n"+
            "\t\t\"Stoprtms__State_Province__c\": \"TX\",\n"+
            "\t\t\"Stoprtms__Postal_Code__c\": \"78572\",\n"+
            "\t\t\"Stoprtms__Country__c\": \"US\",\n"+
            "\t\t\"Stoprtms__Address2__c\": \"Mission, TX 78572\",\n"+
            "\t\t\"Stoprtms__Shipping_Receiving_Hours__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Expected_Date__c\": \"2019-09-13 00:00:00\",\n"+
            "\t\t\"Stoprtms__Expected_Day__c\": \"Friday\",\n"+
            "\t\t\"Stoprtms__Appointment_Required__c\": \"false\",\n"+
            "\t\t\"Stoprtms__Appointment_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Stop_Status__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status2__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Arrival_Date__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Status__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Arrival_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_ETA_Date__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_ETA_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status_as_of__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status_Details__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Carrier_Status_Reason__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Cumulative_Miles__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Departure_Date__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Departure_Time__c\": \"null\",\n"+
            "\t\t\"Stoprtms__EDI_Provider__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Geolocation__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Instructions__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Is_Dropoff__c\": \"true\",\n"+
            "\t\t\"Stoprtms__Is_Pickup__c\": \"false\",\n"+
            "\t\t\"Stoprtms__Kilometers_Away__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Load__c\": \"a0j4B000001UoTQQA0\",\n"+
            "\t\t\"stoptemLoadMarketplaceExposedId\": \"null\",\n"+
            "\t\t\"StopLocation\": {\n"+
            "\t\t\t\"LocationId\": \"null\",\n"+
            "\t\t\t\"LocationName\": \"null\",\n"+
            "\t\t\t\"LocationShippingStreet\": \"null\",\n"+
            "\t\t\t\"LocationShippingCity\": \"null\",\n"+
            "\t\t\t\"LocationShippingState\": \"null\",\n"+
            "\t\t\t\"LocationShippingPostalCode\": \"null\"\n"+
            "\t\t},\n"+
            "\t\t\"Stoprtms__Location_Name__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Miles_Away__c\": \"null\",\n"+
            "\t\t\"StopModify_Boolean__c\": \"false\",\n"+
            "\t\t\"Stoprtms__OTD_Counter__c\": \"0\",\n"+
            "\t\t\"Stoprtms__P_DAS__c\": \"On Time\",\n"+
            "\t\t\"Stoprtms__Pickup_Delivery_Number__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Pickup_Delivery_Status__c\": \"No tracking updates available for this stop.\",\n"+
            "\t\t\"Stoprtms__References__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Shipping_Receiving_Contact__c\": \"null\",\n"+
            "\t\t\"Stoprtms__Tracking_Provider_Customer__c\": \"null\"\n"+
            "\t}]\n"+
            "}";
}
package io.riggo.web.integration.parser;

import io.riggo.data.domain.*;
import io.riggo.web.integration.exception.PayloadParseException;
import io.riggo.web.integration.resolver.SalesforceRevenovaLoadStatusResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SalesforceRevenovaRequestBodyParserPostPutLoad implements RequestBodyParserPostPutLoad {


    @Autowired
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;

    //For unit tests
    public void setSalesforceRevenovaRequestBodyParserHelper(SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper){
        this.salesforceRevenovaRequestBodyParserHelper = salesforceRevenovaRequestBodyParserHelper;

    }

    @Override
    public LoadStop resolveFirstStop(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Map<String, Object> firstStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("FirstStop", loadDetailsMap);
        LoadStop loadStop = new LoadStop();
        loadStop.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopId", firstStopMap));
        loadStop.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopName", firstStopMap));
        loadStop.setType(getFirstStopType(firstStopMap));
        loadStop.setStopNumber(salesforceRevenovaRequestBodyParserHelper.getMapValueAsInteger("FirstStoprtms__Number__c", firstStopMap));
        loadStop.setShippingReceivingHours(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStoprtms__Shipping_Receiving_Hours__c", firstStopMap));
        loadStop.setExpectedDateTime(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("FirstStoprtms__Expected_Date__c", firstStopMap));
        loadStop.setAppointmentRequired(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("FirstStoprtms__Appointment_Required__c", firstStopMap));
        loadStop.setAppointmentTime(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStoprtms__Appointment_Time__c", firstStopMap));
        loadStop.setStopStatus(getFirstStopStopStatus(firstStopMap));
        loadStop.setCarrierStatus(getFirstStopCarrierStatus(firstStopMap));

        Location location = resolveFirstStopLocation(firstStopMap);
        loadStop.setLocation(location);
        return loadStop;
    }


    private Location resolveFirstStopLocation(Map<String, Object> firstStopMap) {
        Map<String, Object> firstStopLocationMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("FirstStopLocation", firstStopMap);
        Location location = new Location();
        location.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationId", firstStopLocationMap));
        location.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationName", firstStopLocationMap));

        Address address = new Address();
        address.setAddress1(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingStreet", firstStopLocationMap));
        address.setCity(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingShippingCity", firstStopLocationMap));
        address.setState(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingShippingState", firstStopLocationMap));
        address.setPostalCode(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingShippingPostalCode", firstStopLocationMap));

        location.setAddress(address);
        return location;
    }


    @Override
    public LoadStop resolveLastStop(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Map<String, Object> lastStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStop", loadDetailsMap);
        LoadStop loadStop = new LoadStop();
        loadStop.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopId", lastStopMap));
        loadStop.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopName", lastStopMap));
        loadStop.setType(getLastStopType(lastStopMap));
        loadStop.setStopNumber(salesforceRevenovaRequestBodyParserHelper.getMapValueAsInteger("LastStoprtms__Number__c", lastStopMap));
        loadStop.setShippingReceivingHours(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStoprtms__Shipping_Receiving_Hours__c", lastStopMap));
        loadStop.setExpectedDateTime(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("LastStoprtms__Expected_Date__c", lastStopMap));
        loadStop.setAppointmentRequired(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("LastStoprtms__Appointment_Required__c", lastStopMap));
        loadStop.setAppointmentTime(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStoprtms__Appointment_Time__c", lastStopMap));
        loadStop.setStopStatus(getLastStopStopStatus(lastStopMap));
        loadStop.setCarrierStatus(getLastStopCarrierStatus(lastStopMap));

        Location location = resolveLastStopLocation(lastStopMap);
        loadStop.setLocation(location);
        return loadStop;
    }


    private Location resolveLastStopLocation(Map<String, Object> firstStopMap) {
        Map<String, Object> lastStopLocationMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStopLocation", firstStopMap);
        Location location = new Location();
        location.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationId", lastStopLocationMap));
        location.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationName", lastStopLocationMap));

        Address address = new Address();
        address.setAddress1(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingStreet", lastStopLocationMap));
        address.setCity(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingShippingCity", lastStopLocationMap));
        address.setState(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingShippingState", lastStopLocationMap));
        address.setPostalCode(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingShippingPostalCode", lastStopLocationMap));

        location.setAddress(address);
        return location;
    }


    @Override
    public Load resolveLoad(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Load load = new Load();
        load.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Id", loadDetailsMap));
        load.setTransportMode(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Mode_Name__c", loadDetailsMap));
        load.setPostedRate(salesforceRevenovaRequestBodyParserHelper.getMapValueIntegerAsBigDecimal("rigPostedRate__c", loadDetailsMap));
        load.setInsuranceAmt(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Insurance_Amount__c", loadDetailsMap));
        load.setSalesStatus(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Sales_Status__c", loadDetailsMap));

        HashMap<String, Object> loadStatusMaps = new HashMap<>();
        Map<String, Object> firstStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("FirstStop", loadDetailsMap);
        Map<String, Object> lastStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStop", loadDetailsMap);
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Load_Status__c", loadDetailsMap));
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Sales_Status__c", loadDetailsMap));
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.FIRST_STOP_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStoprtms__Stop_Status__c", firstStopMap));
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.LAST_STOP_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStoprtms__Stop_Status__c", lastStopMap));

        SalesforceRevenovaLoadStatusResolver salesforceRevenovaLoadStatusResolver = new SalesforceRevenovaLoadStatusResolver();
        LoadSubStatus loadSubStatus = salesforceRevenovaLoadStatusResolver.resolveLoadStatus(loadStatusMaps);
        load.setLoadStatus(loadSubStatus.getColVal());

        Map<String, Object> modeMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("Mode", loadDetailsMap);
        load.setTeamReq(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("rig_Team_Required__c", loadDetailsMap));
        load.setFoodGradeTrailerReq(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("food_grade_trailer_required__c", loadDetailsMap));
        load.setTempControlReq(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("rtms__Temperature_Controlled__c", loadDetailsMap));
        load.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Name", loadDetailsMap));
        load.setCarrierQuoteTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Carrier_Quote_Total__c", loadDetailsMap));
        load.setCarrierInvoiceTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Carrier_Invoice_Total__c", loadDetailsMap));
        load.setCustomerQuoteTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Customer_Quote_Total__c", loadDetailsMap));
        load.setCustomerInvoiceTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Customer_Invoice_Total__c", loadDetailsMap));

        load.setHazMat(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("rtms__Hazardous_Materials__c", loadDetailsMap));
        load.setModeName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("ModeName", modeMap));
        load.setOrderDate(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("rtms__Order_Date__c", loadDetailsMap));
        load.setExpectedShipDate(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("rtms__Expected_Ship_Date2__c", loadDetailsMap));
        load.setExpectedDeliveryDate(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("rtms__Expected_Delivery_Date2__c", loadDetailsMap ));
        load.setLoadUrl(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rig_Load_URL__c", loadDetailsMap));
        load.setReferenceNumber(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__EDI_Reference_Numbers__c", loadDetailsMap));
        load.setBillOfLadingNo(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Bill_of_Lading_Number__c", loadDetailsMap));
        load.setDistanceMiles(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Distance_Miles__c", loadDetailsMap));

        return load;
    }


    @Override
    public Shipper resolveShipper(Map<String, Object> dataHashMap, Integer siteId) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Map<String, Object> customerMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("Customer", loadDetailsMap);
        Shipper shipper = new Shipper();
        shipper.setSiteId(siteId);
        shipper.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("CustomerId", customerMap));
        shipper.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("CustomerName", customerMap));
        return shipper;
    }


    @Override
    public EquipmentType resolveEquipmentType(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Map<String, Object> equipmentTypeMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("EquipmentType", loadDetailsMap);
        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("EquipmentTypeId", equipmentTypeMap));
        equipmentType.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("EquipmentTypeName", equipmentTypeMap));
        return equipmentType;
    }


    private Integer getFirstStopType(Map<String, Object> firstStopMap) {
        LoadStopType loadStopType = getLoadStopType(firstStopMap, "FirstStoprtms__P_D__c");
        return loadStopType != null ? loadStopType.getColVal() : null;
    }


    private LoadStopType getLoadStopType(Map<String, Object> stopMap, String key){
        String stopType = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString(key, stopMap);
        LoadStopType loadStopType = LoadStopType.fromDisplayName(stopType);
        return loadStopType != null ? loadStopType : null;
    }


    private Integer getFirstStopStopStatus(Map<String, Object> firstStopMap) {
        LoadStopStatus loadStopStatus = getLoadStopStatus(firstStopMap, "FirstStoprtms__Stop_Status__c");
        return loadStopStatus != null ? loadStopStatus.getColVal() : null;
    }

    private Integer getFirstStopCarrierStatus(Map<String, Object> firstStopMap) {
        LoadStopCarrierStatus loadStopCarrierStatus = getLoadStopCarrierStatus(firstStopMap, "FirstStoprtms__Carrier_Status2__c");
        return loadStopCarrierStatus != null ? loadStopCarrierStatus.getColVal() : null;
    }

    private LoadStopStatus getLoadStopStatus(Map<String, Object> firstStopMap, String key) {
        String firstStopStopStatusValue = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString(key, firstStopMap);
        if(firstStopStopStatusValue != null) {
            return LoadStopStatus.fromDisplayName(firstStopStopStatusValue);
        }
        return null;
    }

    private LoadStopCarrierStatus getLoadStopCarrierStatus(Map<String, Object> firstStopMap, String key) {
        String firstStopStopStatusValue = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString(key, firstStopMap);
        if(firstStopStopStatusValue != null) {
            return LoadStopCarrierStatus.fromDisplayName(firstStopStopStatusValue);
        }
        return null;
    }

    private Integer getLastStopType(Map<String, Object> lastStopMap) {
        LoadStopType loadStopType = getLoadStopType(lastStopMap, "LastStoprtms__P_D__c");
        return loadStopType != null ? loadStopType.getColVal() : null;
    }


    private Integer getLastStopStopStatus(Map<String, Object> lastStopMap) {
        LoadStopStatus loadStopStatus = getLoadStopStatus(lastStopMap, "LastStoprtms__Stop_Status__c");
        return loadStopStatus != null ? loadStopStatus.getColVal() : null;
    }

    private Integer getLastStopCarrierStatus(Map<String, Object> lastStopMap) {
        LoadStopStatus loadStopStatus = getLoadStopStatus(lastStopMap, "LastStoprtms__Carrier_Status2__c");
        return loadStopStatus != null ? loadStopStatus.getColVal() : null;
    }


    private Map<String, Object> getLoadDetailsMap(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = (Map<String, Object>) dataHashMap.get("LoadDetails");
        if (loadDetailsMap != null) {
            return loadDetailsMap;
        }
        throw new PayloadParseException("LoadDetails");
    }
}
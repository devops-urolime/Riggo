package io.riggo.web.integration.parser;

import io.riggo.data.domain.*;
import io.riggo.web.integration.exception.PayloadParseException;
import io.riggo.web.integration.resolver.SalesforceRevenovaLoadStatusResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SalesforceRevenovaRequestBodyParserPostPutLoad implements RequestBodyParserPostPutLoad {

    private String loadExtSysId;
    private String loadName;
    private String shipperExtSysId;
    private String shipperName;

    private String equipmentTypeExtSysId;
    private String equipmentTypeName;
    private String transportMode;
    private BigDecimal postedRate;
    private BigDecimal insuranceAmount;

    private String loadStatusString;
    private String salesStatusString;
    private String firstStopStatusString;
    private String lastStopStatusString;
    private String modeName;

    private Boolean teamRequired;
    private Boolean foodTrailerRequired;
    private Boolean temperatureControlRequired;
    private Boolean hazMat;

    private BigDecimal invoiceTotal;

    private LocalDateTime orderDate;
    private LocalDate expectedShipDate;
    private LocalDate expectedDeliveryDate;

    private String loadUrl;
    private String firstStopExtSysId;
    private String firstStopName;
    private String firstStopShippingReceivingHours;
    private Integer firstStopNumber;
    private Integer firstStopType;
    private LocalDateTime firstStopExpectedDateTime;
    private Boolean firstStopAppointmentRequired;
    private String firstStopAppointmentTime;
    private Integer firstStopStopStatus;
    private Integer firstStopCarrierStatus;
    private String firstStopLocationExtSysId;
    private String firstStopLocationName;
    private String firstStopLocationAddress;
    private String firstStopLocationAddressCity;
    private String firstStopLocationAddressState;
    private String firstStopLocationAddressPostalCode;


    private String lastStopExtSysId;
    private String lastStopName;
    private String lastStopShippingReceivingHours;
    private Integer lastStopNumber;
    private Integer lastStopType;
    private LocalDateTime lastStopExpectedDateTime;
    private Boolean lastStopAppointmentRequired;
    private String lastStopAppointmentTime;
    private Integer lastStopStopStatus;
    private Integer lastStopCarrierStatus;
    private String lastStopLocationExtSysId;
    private String lastStopLocationName;
    private String lastStopLocationAddress;
    private String lastStopLocationAddressCity;
    private String lastStopLocationAddressState;
    private String lastStopLocationAddressPostalCode;



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
        loadStop.setExtSysId(getFirstStopExtSysId(firstStopMap));
        loadStop.setName(getFirstStopName(firstStopMap));
        loadStop.setType(getFirstStopType(firstStopMap));
        loadStop.setStopNumber(getFirstStopNumber(firstStopMap));
        loadStop.setShippingReceivingHours(getFirstStopShippingReceivingHours(firstStopMap));
        loadStop.setExpectedDateTime(getFirstStopExpectedDateTime(firstStopMap));
        loadStop.setAppointmentRequired(getFirstStopAppointmentRequired(firstStopMap));
        loadStop.setAppointmentTime(getFirstStopAppointmentTime(firstStopMap));
        loadStop.setStopStatus(getFirstStopStopStatus(firstStopMap));
        loadStop.setCarrierStatus(getFirstStopCarrierStatus(firstStopMap));

        Location location = resolveFirstStopLocation(firstStopMap);
        loadStop.setLocation(location);
        return loadStop;
    }


    private Location resolveFirstStopLocation(Map<String, Object> firstStopMap) {
        Map<String, Object> firstStopLocationMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("FirstStopLocation", firstStopMap);
        Location location = new Location();
        location.setExtSysId(getFirstStopLocationExtSysId(firstStopLocationMap));
        location.setName(getFirstStopLocationName(firstStopLocationMap));

        Address address = new Address();
        address.setAddress1(getFirstStopLocationAddress(firstStopLocationMap));
        address.setCity(getFirstStopLocationAddressCity(firstStopLocationMap));
        address.setState(getFirstStopLocationAddressState(firstStopLocationMap));
        address.setPostalCode(getFirstStopLocationAddressPostalCode(firstStopLocationMap));

        location.setAddress(address);
        return location;
    }


    @Override
    public LoadStop resolveLastStop(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Map<String, Object> lastStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStop", loadDetailsMap);
        LoadStop loadStop = new LoadStop();
        loadStop.setExtSysId(getLastStopExtSysId(lastStopMap));
        loadStop.setName(getLastStopName(lastStopMap));
        loadStop.setType(getLastStopType(lastStopMap));
        loadStop.setStopNumber(getLastStopNumber(lastStopMap));
        loadStop.setShippingReceivingHours(getLastStopShippingReceivingHours(lastStopMap));
        loadStop.setExpectedDateTime(getLastStopExpectedDateTime(lastStopMap));
        loadStop.setAppointmentRequired(getLastStopAppointmentRequired(lastStopMap));
        loadStop.setAppointmentTime(getLastStopAppointmentTime(lastStopMap));
        loadStop.setStopStatus(getLastStopStopStatus(lastStopMap));
        loadStop.setCarrierStatus(getLastStopCarrierStatus(lastStopMap));

        Location location = resolveLastStopLocation(lastStopMap);
        loadStop.setLocation(location);
        return loadStop;
    }


    private Location resolveLastStopLocation(Map<String, Object> firstStopMap) {
        Map<String, Object> lastStopLocationMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStopLocation", firstStopMap);
        Location location = new Location();
        location.setExtSysId(getLastStopLocationExtSysId(lastStopLocationMap));
        location.setName(getLastStopLocationName(lastStopLocationMap));

        Address address = new Address();
        address.setAddress1(getLastStopLocationAddress(lastStopLocationMap));
        address.setCity(getLastStopLocationAddressCity(lastStopLocationMap));
        address.setState(getLastStopLocationAddressState(lastStopLocationMap));
        address.setPostalCode(getLastStopLocationAddressPostalCode(lastStopLocationMap));

        location.setAddress(address);
        return location;
    }


    @Override
    public Load resolveLoad(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Load load = new Load();
        load.setExtSysId(getLoadExtSysId(loadDetailsMap));
        load.setTransportMode(getTransportMode(loadDetailsMap));
        load.setPostedRate(getPostedRate(loadDetailsMap));
        load.setInsuranceAmt(getInsuranceAmount(loadDetailsMap));


        HashMap<String, Object> loadStatusMaps = new HashMap<>();
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, getLoadStatusString(loadDetailsMap));
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, getSalesStatusString(loadDetailsMap));
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.FIRST_STOP_STATUS, getFirstStopStatusString(loadDetailsMap));
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.LAST_STOP_STATUS, getLastStopStatusString(loadDetailsMap));

        SalesforceRevenovaLoadStatusResolver salesforceRevenovaLoadStatusResolver = new SalesforceRevenovaLoadStatusResolver();
        LoadSubStatus loadSubStatus = salesforceRevenovaLoadStatusResolver.resolveLoadStatus(loadStatusMaps);
        load.setLoadStatus(loadSubStatus.getColVal());

        load.setTeamReq(getTeamRequired(loadDetailsMap));
        load.setFoodGradeTrailerReq(getFoodGradeTrailerRequired(loadDetailsMap));
        load.setTempControlReq(getTemperatureControlRequired(loadDetailsMap));
        load.setName(getLoadName(loadDetailsMap));
        load.setInvoiceTotal(getInvoiceTotal(loadDetailsMap));
        load.setHazMat(getHazMat(loadDetailsMap));
        load.setModeName(getModeName(loadDetailsMap));
        load.setOrderDate(getOrderDate(loadDetailsMap));
        load.setExpectedShipDate(getExpectedShipDate(loadDetailsMap));
        load.setExpectedDeliveryDate(getExpectedDeliveryDate(loadDetailsMap));
        load.setLoadUrl(getLoadUrl(loadDetailsMap));

        return load;
    }


    @Override
    public Shipper resolveShipper(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        Shipper shipper = new Shipper();
        shipper.setExtSysId(getShipperExtSysId(loadDetailsMap));
        shipper.setName(getShipperName(loadDetailsMap));
        return shipper;
    }


    @Override
    public EquipmentType resolveEquipmentType(Map<String, Object> dataHashMap) {
        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setExtSysId(getEquipmentTypeExtSysId(getLoadDetailsMap(dataHashMap)));
        equipmentType.setName(getEquipmentTypeName(getLoadDetailsMap(dataHashMap)));
        return equipmentType;
    }


    private String getLoadExtSysId(Map<String, Object> loadDetailsMap) {
        if (loadExtSysId == null) {
            loadExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Id", loadDetailsMap);
        }
        return loadExtSysId;
    }


    private String getShipperExtSysId(Map<String, Object> loadDetailsMap) {
        if (shipperExtSysId == null) {
            Map<String, Object> customerMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("Customer", loadDetailsMap);
            shipperExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("CustomerId", customerMap);
        }
        return shipperExtSysId;
    }


    private String getEquipmentTypeExtSysId(Map<String, Object> loadDetailsMap) {
        if (equipmentTypeExtSysId == null) {
            Map<String, Object> equipmentTypeMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("EquipmentType", loadDetailsMap);
            equipmentTypeExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("EquipmentTypeId", equipmentTypeMap);
        }
        return equipmentTypeExtSysId;
    }


    private String getEquipmentTypeName(Map<String, Object> loadDetailsMap) {
        if (equipmentTypeName == null) {
            Map<String, Object> equipmentTypeMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("EquipmentType", loadDetailsMap);
            equipmentTypeName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("EquipmentTypeName", equipmentTypeMap);
        }
        return equipmentTypeName;
    }


    private String getTransportMode(Map<String, Object> loadDetailsMap) {
        if (transportMode == null) {
            transportMode = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Mode_Name__c", loadDetailsMap);
        }
        return transportMode;
    }


    private BigDecimal getPostedRate(Map<String, Object> loadDetailsMap) {
        if (postedRate == null) {
            postedRate = salesforceRevenovaRequestBodyParserHelper.getMapValueIntegerAsBigDecimal("rigPostedRate__c", loadDetailsMap);
        }
        return postedRate;
    }


    private BigDecimal getInsuranceAmount(Map<String, Object> loadDetailsMap) {
        if (insuranceAmount == null) {
            insuranceAmount = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Insurance_Amount__c", loadDetailsMap);

        }
        return insuranceAmount;
    }


    private String getLoadStatusString(Map<String, Object> loadDetailsMap) {
        if (loadStatusString == null) {
            loadStatusString = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Load_Status__c", loadDetailsMap);
        }
        return loadStatusString;
    }


    private String getSalesStatusString(Map<String, Object> loadDetailsMap) {
        if (salesStatusString == null) {
            salesStatusString = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rtms__Sales_Status__c", loadDetailsMap);
        }
        return salesStatusString;
    }


    private String getFirstStopStatusString(Map<String, Object> loadDetailsMap) {
        if (firstStopStatusString == null) {
            Map<String, Object> firstStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("FirstStop", loadDetailsMap);
            firstStopStatusString = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStoprtms__Stop_Status__c", firstStopMap);
        }
        return firstStopStatusString;
    }


    private String getLastStopStatusString(Map<String, Object> loadDetailsMap) {
        if (lastStopStatusString == null) {
            Map<String, Object> lastStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStop", loadDetailsMap);
            lastStopStatusString = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStoprtms__Stop_Status__c", lastStopMap);
        }
        return lastStopStatusString;
    }


    private Boolean getTeamRequired(Map<String, Object> loadDetailsMap) {
        if (teamRequired == null) {
            teamRequired = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("rig_Team_Required__c", loadDetailsMap);
        }
        return teamRequired;
    }


    private Boolean getFoodGradeTrailerRequired(Map<String, Object> loadDetailsMap) {
        if (foodTrailerRequired == null) {
            foodTrailerRequired = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("food_grade_trailer_required__c", loadDetailsMap);
        }
        return foodTrailerRequired;
    }


    private Boolean getTemperatureControlRequired(Map<String, Object> loadDetailsMap) {
        if (temperatureControlRequired == null) {
            temperatureControlRequired = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("rtms__Temperature_Controlled__c", loadDetailsMap);
        }
        return temperatureControlRequired;
    }


    private Boolean getHazMat(Map<String, Object> loadDetailsMap) {
        if (hazMat == null) {
            hazMat = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("rtms__Hazardous_Materials__c", loadDetailsMap);
        }
        return hazMat;
    }


    private String getLoadName(Map<String, Object> loadDetailsMap) {
        if (loadName == null) {
            loadName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Name", loadDetailsMap);
        }
        return loadName;
    }


    private String getLoadUrl(Map<String, Object> loadDetailsMap) {
        if (loadUrl == null) {
            loadUrl = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("rig_Load_URL__c", loadDetailsMap);
        }
        return loadUrl;
    }


    private String getModeName(Map<String, Object> loadDetailsMap) {
        if (modeName == null) {
            Map<String, Object> modeMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("Mode", loadDetailsMap);
            modeName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("ModeName", modeMap);
        }
        return modeName;
    }


    private BigDecimal getInvoiceTotal(Map<String, Object> loadDetailsMap) {
        if (invoiceTotal == null) {
            invoiceTotal = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rtms__Carrier_Invoice_Total__c", loadDetailsMap);
        }
        return invoiceTotal;
    }


    private String getShipperName(Map<String, Object> loadDetailsMap) {
        if (shipperName == null) {
            Map<String, Object> customerMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("Customer", loadDetailsMap);
            shipperName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("CustomerName", customerMap);
        }
        return shipperName;
    }


    private LocalDateTime getOrderDate(Map<String, Object> loadDetailsMap) {
        if (orderDate == null) {
            orderDate = salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("rtms__Order_Date__c", loadDetailsMap);
        }
        return orderDate;
    }


    private LocalDate getExpectedShipDate(Map<String, Object> loadDetailsMap) {
        if (expectedShipDate == null) {
            expectedShipDate = salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDate("riggoh_Formatted_Expected_Ship_Date__c", loadDetailsMap);
        }
        return expectedShipDate;
    }


    private LocalDate getExpectedDeliveryDate(Map<String, Object> loadDetailsMap) {
        if (expectedDeliveryDate == null) {
            expectedDeliveryDate = salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDate("riggoh_Formatted_Expected_Delivery_Date__c", loadDetailsMap );
        }
        return expectedDeliveryDate;
    }


    private String getFirstStopExtSysId(Map<String, Object> firstStopMap) {
        if (firstStopExtSysId == null) {
            firstStopExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopId", firstStopMap);
        }
        return firstStopExtSysId;
    }


    private String getFirstStopName(Map<String, Object> firstStopMap) {
        if (firstStopName == null) {
            firstStopName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopName", firstStopMap);
        }
        return firstStopName;
    }


    private Integer getFirstStopNumber(Map<String, Object> firstStopMap) {
        if (firstStopNumber == null) {
            firstStopNumber = salesforceRevenovaRequestBodyParserHelper.getMapValueAsInteger("FirstStoprtms__Number__c", firstStopMap);
        }
        return firstStopNumber;
    }

    private Integer getFirstStopType(Map<String, Object> firstStopMap) {
        if (firstStopType == null) {
            LoadStopType loadStopType = getLoadStopType(firstStopMap, "FirstStoprtms__P_D__c");
            firstStopType =  loadStopType != null ? loadStopType.getColVal() : null;
        }
        return firstStopType;
    }

    private LoadStopType getLoadStopType(Map<String, Object> stopMap, String key){
        String stopType = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString(key, stopMap);
        LoadStopType loadStopType = LoadStopType.fromDisplayName(stopType);
        return loadStopType != null ? loadStopType : null;
    }

    private String getFirstStopShippingReceivingHours(Map<String, Object> firstStopMap) {
        if (firstStopShippingReceivingHours == null) {
            firstStopShippingReceivingHours = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStoprtms__Shipping_Receiving_Hours__c", firstStopMap);
        }
        return firstStopShippingReceivingHours;
    }

    private LocalDateTime getFirstStopExpectedDateTime(Map<String, Object> firstStopMap) {
        if (firstStopExpectedDateTime == null) {
            firstStopExpectedDateTime = salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("FirstStoprtms__Expected_Date__c", firstStopMap);
        }
        return firstStopExpectedDateTime;
    }

    private Boolean getFirstStopAppointmentRequired(Map<String, Object> firstStopMap) {
        if (firstStopAppointmentRequired == null) {
            firstStopAppointmentRequired = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("FirstStoprtms__Appointment_Required__c", firstStopMap);
        }
        return firstStopAppointmentRequired;
    }

    private String getFirstStopAppointmentTime(Map<String, Object> firstStopMap) {
        if (firstStopAppointmentTime == null) {
            firstStopAppointmentTime = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStoprtms__Appointment_Time__c", firstStopMap);
        }
        return firstStopAppointmentTime;
    }

    private Integer getFirstStopStopStatus(Map<String, Object> firstStopMap) {
        if(firstStopStopStatus == null){
            LoadStopStatus loadStopStatus = getLoadStopStatus(firstStopMap, "FirstStoprtms__Stop_Status__c");
            firstStopStopStatus = loadStopStatus != null ? loadStopStatus.getColVal() : null;
        }
        return firstStopStopStatus;
    }

    private Integer getFirstStopCarrierStatus(Map<String, Object> firstStopMap) {
        if(firstStopCarrierStatus == null){
            LoadStopStatus loadStopStatus = getLoadStopStatus(firstStopMap, "FirstStoprtms__Carrier_Status2__c");
            firstStopCarrierStatus = loadStopStatus != null ? loadStopStatus.getColVal() : null;
        }
        return firstStopCarrierStatus;
    }

    private LoadStopStatus getLoadStopStatus(Map<String, Object> firstStopMap, String key) {
        String firstStopStopStatusValue = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString(key, firstStopMap);
        if(firstStopStopStatusValue != null) {
            return LoadStopStatus.fromDisplayName(firstStopStopStatusValue);
        }
        return null;
    }

    private Integer getLastStopType(Map<String, Object> lastStopMap) {
        if (lastStopType == null) {
            LoadStopType loadStopType = getLoadStopType(lastStopMap, "LastStoprtms__P_D__c");
            lastStopType =  loadStopType != null ? loadStopType.getColVal() : null;
        }
        return lastStopType;
    }

    private String getLastStopShippingReceivingHours(Map<String, Object> lastStopMap) {
        if (lastStopShippingReceivingHours == null) {
            lastStopShippingReceivingHours = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStoprtms__Shipping_Receiving_Hours__c", lastStopMap);
        }
        return lastStopShippingReceivingHours;
    }

    private LocalDateTime getLastStopExpectedDateTime(Map<String, Object> lastStopMap) {
        if (lastStopExpectedDateTime == null) {
            lastStopExpectedDateTime = salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("LastStoprtms__Expected_Date__c", lastStopMap);
        }
        return lastStopExpectedDateTime;
    }

    private Boolean getLastStopAppointmentRequired(Map<String, Object> lastStopMap) {
        if (lastStopAppointmentRequired == null) {
            lastStopAppointmentRequired = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("LastStoprtms__Appointment_Required__c", lastStopMap);
        }
        return lastStopAppointmentRequired;
    }

    private String getLastStopAppointmentTime(Map<String, Object> lastStopMap) {
        if (lastStopAppointmentTime == null) {
            lastStopAppointmentTime = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStoprtms__Appointment_Time__c", lastStopMap);
        }
        return lastStopAppointmentTime;
    }

    private Integer getLastStopStopStatus(Map<String, Object> lastStopMap) {
        if(lastStopStopStatus == null){
            LoadStopStatus loadStopStatus = getLoadStopStatus(lastStopMap, "LastStoprtms__Stop_Status__c");
            lastStopStopStatus = loadStopStatus != null ? loadStopStatus.getColVal() : null;
        }
        return lastStopStopStatus;
    }

    private Integer getLastStopCarrierStatus(Map<String, Object> lastStopMap) {
        if(lastStopCarrierStatus == null){
            LoadStopStatus loadStopStatus = getLoadStopStatus(lastStopMap, "LastStoprtms__Carrier_Status2__c");
            lastStopCarrierStatus = loadStopStatus != null ? loadStopStatus.getColVal() : null;
        }
        return lastStopCarrierStatus;
    }

    private Integer getLastStopNumber(Map<String, Object> lastStopMap) {
        if (lastStopNumber == null) {
            lastStopNumber = salesforceRevenovaRequestBodyParserHelper.getMapValueAsInteger("LastStoprtms__Number__c", lastStopMap);
        }
        return lastStopNumber;
    }

    private String getLastStopLocationExtSysId(Map<String, Object> lastStopLocationMap) {
        if (lastStopLocationExtSysId == null) {
            lastStopLocationExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationId", lastStopLocationMap);
        }
        return lastStopLocationExtSysId;
    }

    private String getLastStopLocationName(Map<String, Object> lastStopLocationMap) {
        if (lastStopLocationName == null) {
            lastStopLocationName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationName", lastStopLocationMap);
        }
        return lastStopLocationName;
    }

    private String getLastStopLocationAddress(Map<String, Object> lastStopLocationMap) {
        if (lastStopLocationAddress == null) {
            lastStopLocationAddress = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingStreet", lastStopLocationMap);
        }
        return lastStopLocationAddress;
    }

    private String getLastStopLocationAddressCity(Map<String, Object> lastStopLocationMap) {
        if (lastStopLocationAddressCity == null) {
            lastStopLocationAddressCity = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingShippingCity", lastStopLocationMap);
        }
        return lastStopLocationAddressCity;
    }

    private String getLastStopLocationAddressState(Map<String, Object> lastStopLocationMap) {
        if (lastStopLocationAddressState == null) {
            lastStopLocationAddressState = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingShippingState", lastStopLocationMap);
        }
        return lastStopLocationAddressState;
    }

    private String getLastStopLocationAddressPostalCode(Map<String, Object> lastStopLocationMap) {
        if (lastStopLocationAddressPostalCode == null) {
            lastStopLocationAddressPostalCode = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopLocationShippingShippingPostalCode", lastStopLocationMap);
        }
        return lastStopLocationAddressPostalCode;
    }

    private String getFirstStopLocationExtSysId(Map<String, Object> firstStopLocationMap) {
        if (firstStopLocationExtSysId == null) {
            firstStopLocationExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationId", firstStopLocationMap);
        }
        return firstStopLocationExtSysId;
    }

    private String getFirstStopLocationName(Map<String, Object> firstStopLocationMap) {
        if (firstStopLocationName == null) {
            firstStopLocationName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationName", firstStopLocationMap);
        }
        return firstStopLocationName;
    }

    private String getFirstStopLocationAddress(Map<String, Object> firstStopLocationMap) {
        if (firstStopLocationAddress == null) {
            firstStopLocationAddress = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingStreet", firstStopLocationMap);
        }
        return firstStopLocationAddress;
    }

    private String getFirstStopLocationAddressCity(Map<String, Object> firstStopLocationMap) {
        if (firstStopLocationAddressCity == null) {
            firstStopLocationAddressCity = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingShippingCity", firstStopLocationMap);
        }
        return firstStopLocationAddressCity;
    }

    private String getFirstStopLocationAddressState(Map<String, Object> firstStopLocationMap) {
        if (firstStopLocationAddressState == null) {
            firstStopLocationAddressState = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingShippingState", firstStopLocationMap);
        }
        return firstStopLocationAddressState;
    }

    private String getFirstStopLocationAddressPostalCode(Map<String, Object> firstStopLocationMap) {
        if (firstStopLocationAddressPostalCode == null) {
            firstStopLocationAddressPostalCode = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopLocationShippingShippingPostalCode", firstStopLocationMap);
        }
        return firstStopLocationAddressPostalCode;
    }


    private String getLastStopExtSysId(Map<String, Object> lastStopMap) {
        if (lastStopExtSysId == null) {
            lastStopExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopId", lastStopMap);
        }
        return lastStopExtSysId;
    }

    private String getLastStopName(Map<String, Object> lastStopMap) {
        if (lastStopName == null) {
            lastStopName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopName", lastStopMap);
        }
        return lastStopName;
    }


    private Map<String, Object> getLoadDetailsMap(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = (Map<String, Object>) dataHashMap.get("LoadDetails");
        if (loadDetailsMap != null) {
            return loadDetailsMap;
        }
        throw new PayloadParseException("LoadDetails");
    }
}

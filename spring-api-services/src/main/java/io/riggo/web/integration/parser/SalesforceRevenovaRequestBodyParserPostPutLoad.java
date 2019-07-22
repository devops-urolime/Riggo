package io.riggo.web.integration.parser;

import io.riggo.data.domain.*;
import io.riggo.web.integration.exception.PayloadParseException;
import io.riggo.web.integration.resolver.SalesforceRevenovaLoadStatusResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SalesforceRevenovaRequestBodyParserPostPutLoad implements RequestBodyParserPostPutLoad {

    private Map<String, Object> dataHashMap;
    private String loadExtSysId;
    private String loadName;
    private String shipperExtSysId;
    private String shipperName;

    private String equipmentTypeExtSysId;
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
    private String lastStopExtSysId;

    public SalesforceRevenovaRequestBodyParserPostPutLoad(Map<String, Object> dataHashMap) {
        this.dataHashMap = dataHashMap;
    }

    @Override
    public LoadStop resolveFirstStop() {
        LoadStop loadStop = new LoadStop();
        loadStop.setExtSysId(getFirstStopExtSysId());
        loadStop.setStopNumber(1);
        loadStop.setType(LoadStopType.FIRST_STOP.getColVal());
        return loadStop;
    }

    @Override
    public LoadStop resolveLastStop() {
        LoadStop loadStop = new LoadStop();
        loadStop.setExtSysId(getLastStopExtSysId());
        loadStop.setStopNumber(2);
        loadStop.setType(LoadStopType.LAST_STOP.getColVal());
        return loadStop;
    }

    @Override
    public Load resolveLoad() {
        Load load = new Load();
        load.setExtSysId(getLoadExtSysId());
        load.setTransportMode(getTransportMode());
        load.setPostedRate(getPostedRate());
        load.setInsuranceAmt(getInsuranceAmount());


        HashMap<String, Object> loadStatusMaps = new HashMap<>();
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, getLoadStatusString());
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, getSalesStatusString());
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.FIRST_STOP_STATUS, getFirstStopStatusString());
        loadStatusMaps.put(SalesforceRevenovaLoadStatusResolver.LAST_STOP_STATUS, getLastStopStatusString());

        SalesforceRevenovaLoadStatusResolver salesforceRevenovaLoadStatusResolver = new SalesforceRevenovaLoadStatusResolver();
        LoadSubStatus loadSubStatus = salesforceRevenovaLoadStatusResolver.resolveLoadStatus(loadStatusMaps);
        load.setLoadStatus(loadSubStatus.getColVal());

        load.setTeamReq(getTeamRequired());
        load.setFoodGradeTrailerReq(getFoodGradeTrailerRequired());
        load.setTempControlReq(getTemperatureControlRequired());
        load.setName(getLoadName());
        load.setInvoiceTotal(getInvoiceTotal());
        load.setHazMat(getHazMat());
        load.setModeName(getModeName());
        load.setOrderDate(getOrderDate());
        load.setExpectedShipDate(getExpectedShipDate());
        load.setExpectedDeliveryDate(getExpectedDeliveryDate());
        load.setLoadUrl(getLoadUrl());

        return load;
    }


    @Override
    public Shipper resolveShipper() {
        Shipper shipper = new Shipper();
        shipper.setExtSysId(getShipperExtSysId());
        shipper.setName(getShipperName());
        return shipper;
    }


    @Override
    public EquipmentType resolveEquipmentType() {
        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setExtSysId(getEquipmentTypeExtSysId());
        return equipmentType;
    }


    private String getLoadExtSysId() {
        if (loadExtSysId == null) {
            String exceptionMessage = "load.extSysId was not found";
            loadExtSysId = getMapValueAsString("Id", getLoadDetailsMap(exceptionMessage), exceptionMessage);
        }
        return loadExtSysId;
    }


    private String getShipperExtSysId() {
        if (shipperExtSysId == null) {
            String exceptionMessage = "shipper.extSysId was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> customerMap = getMapValueAsMap("Customer", loadDetailsMap, exceptionMessage);
            shipperExtSysId = getMapValueAsString("CustomerId", customerMap, exceptionMessage);
        }
        return shipperExtSysId;
    }


    private String getEquipmentTypeExtSysId() {
        if (equipmentTypeExtSysId == null) {
            String exceptionMessage = "equipmentType.extSysId was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> equipmentTypeMap = getMapValueAsMap("EquipmentType", loadDetailsMap, exceptionMessage);
            equipmentTypeExtSysId = getMapValueAsString("EquipmentTypeId", equipmentTypeMap, exceptionMessage);
        }
        return equipmentTypeExtSysId;
    }


    private String getTransportMode() {
        if (transportMode == null) {
            String exceptionMessage = "load.transportMode was not found";
            transportMode = getMapValueAsString("rtms__Mode_Name__c", getLoadDetailsMap(exceptionMessage), exceptionMessage);
        }
        return transportMode;
    }


    private BigDecimal getPostedRate() {
        if (postedRate == null) {
            String exceptionMessage = "load.postedRate was not found";
            postedRate = getMapValueAsBigDecimal("rigPostedRate__c", getLoadDetailsMap(exceptionMessage), exceptionMessage);

        }
        return postedRate;
    }


    private BigDecimal getInsuranceAmount() {
        if (insuranceAmount == null) {
            String exceptionMessage = "load.insuranceAmount was not found";
            insuranceAmount = getMapValueAsBigDecimal("rtms__Insurance_Amount__c", getLoadDetailsMap(exceptionMessage), exceptionMessage);

        }
        return insuranceAmount;
    }


    private String getLoadStatusString() {
        if (loadStatusString == null) {
            String exceptionMessage = "load.rtms__Load_Status__c was not found";
            loadStatusString = getMapValueAsString("rtms__Load_Status__c", getLoadDetailsMap(exceptionMessage), exceptionMessage);
        }
        return loadStatusString;
    }


    private String getSalesStatusString() {
        if (salesStatusString == null) {
            String exceptionMessage = "load.rtms__Sales_Status__c was not found";
            salesStatusString = getMapValueAsString("rtms__Sales_Status__c", getLoadDetailsMap(exceptionMessage), exceptionMessage);
        }
        return salesStatusString;
    }


    private String getFirstStopStatusString() {
        if (firstStopStatusString == null) {
            String exceptionMessage = "firstStop.status was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> firstStopMap = getMapValueAsMap("FirstStop", loadDetailsMap, exceptionMessage);
            firstStopStatusString = getMapValueAsString("FirstStoprtms__Stop_Status__c", firstStopMap, exceptionMessage);
        }
        return firstStopStatusString;
    }


    private String getLastStopStatusString() {
        if (lastStopStatusString == null) {
            String exceptionMessage = "lastStop.status was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> lastStopMap = getMapValueAsMap("LastStop", loadDetailsMap, exceptionMessage);
            lastStopStatusString = getMapValueAsString("LastStoprtms__Stop_Status__c", lastStopMap, exceptionMessage);
        }
        return lastStopStatusString;
    }


    private Boolean getTeamRequired() {
        if (teamRequired == null) {
            String exceptionMessage = "load.teamRequired was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            teamRequired = (Boolean) loadDetailsMap.get("rig_Team_Required__c");
        }
        return teamRequired;
    }


    private Boolean getFoodGradeTrailerRequired() {
        if (foodTrailerRequired == null) {
            String exceptionMessage = "load.foodTrailerRequired was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            foodTrailerRequired = (Boolean) loadDetailsMap.get("food_grade_trailer_required__c");
        }
        return foodTrailerRequired;
    }


    private Boolean getTemperatureControlRequired() {
        if (temperatureControlRequired == null) {
            String exceptionMessage = "load.temperatureControlRequired was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            temperatureControlRequired = (Boolean) loadDetailsMap.get("rtms__Temperature_Controlled__c");
        }
        return temperatureControlRequired;
    }


    private Boolean getHazMat() {
        if (hazMat == null) {
            String exceptionMessage = "load.temperatureControlRequired was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            hazMat = (Boolean) loadDetailsMap.get("rtms__Hazardous_Materials__c");
        }
        return hazMat;
    }


    private String getLoadName() {
        if (loadName == null) {
            String exceptionMessage = "load.name was not found";
            loadName = getMapValueAsString("Name", getLoadDetailsMap(exceptionMessage), exceptionMessage);
        }
        return loadName;
    }


    private String getLoadUrl() {
        if (loadUrl == null) {
            String exceptionMessage = "load.url was not found";
            loadUrl = getMapValueAsString("rig_Load_URL__c", getLoadDetailsMap(exceptionMessage), exceptionMessage);
        }
        return loadUrl;
    }


    private String getModeName() {
        if (modeName == null) {
            String exceptionMessage = "load.modeName was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> modeMap = getMapValueAsMap("Mode", loadDetailsMap, exceptionMessage);
            modeName = getMapValueAsString("ModeName", modeMap, exceptionMessage);
        }
        return modeName;
    }


    private BigDecimal getInvoiceTotal() {
        if (invoiceTotal == null) {
            String exceptionMessage = "load.carrierInvoiceTotal was not found";
            invoiceTotal = getMapValueAsBigDecimal("rtms__Carrier_Invoice_Total__c", getLoadDetailsMap(exceptionMessage), exceptionMessage);
        }
        return invoiceTotal;
    }


    private String getShipperName() {
        if (shipperName == null) {
            String exceptionMessage = "shipper.name was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> customerMap = getMapValueAsMap("Customer", loadDetailsMap, exceptionMessage);
            shipperName = getMapValueAsString("CustomerName", customerMap, exceptionMessage);
        }
        return shipperName;
    }


    private LocalDateTime getOrderDate() {
        if (orderDate == null) {
            String exceptionMessage = "load.orderDate was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            orderDate = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .parse(getMapValueAsString("rtms__Order_Date__c", loadDetailsMap, exceptionMessage)));
        }
        return orderDate;
    }


    private LocalDate getExpectedShipDate() {
        if (expectedShipDate == null) {
            String exceptionMessage = "load.expectedShipDate was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            expectedShipDate = LocalDate.parse(getMapValueAsString("riggoh_Formatted_Expected_Ship_Date__c", loadDetailsMap, exceptionMessage), formatter);
        }
        return expectedShipDate;
    }


    private LocalDate getExpectedDeliveryDate() {
        if (expectedDeliveryDate == null) {
            String exceptionMessage = "load.expectedDeliveryDate was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            expectedDeliveryDate = LocalDate.parse(getMapValueAsString("riggoh_Formatted_Expected_Delivery_Date__c", loadDetailsMap, exceptionMessage), formatter);
        }
        return expectedDeliveryDate;
    }


    private String getFirstStopExtSysId() {
        if (firstStopExtSysId == null) {
            String exceptionMessage = "loadStop.firstStop was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> firstStopMap = getMapValueAsMap("FirstStop", loadDetailsMap, exceptionMessage);
            firstStopExtSysId = getMapValueAsString("FirstStopId", firstStopMap, exceptionMessage);
        }
        return firstStopExtSysId;
    }


    private String getLastStopExtSysId() {
        if (lastStopExtSysId == null) {
            String exceptionMessage = "loadStop.lastStop was not found";
            HashMap<String, Object> loadDetailsMap = getLoadDetailsMap(exceptionMessage);
            HashMap<String, Object> firstStopMap = getMapValueAsMap("LastStop", loadDetailsMap, exceptionMessage);
            lastStopExtSysId = getMapValueAsString("LastStopId", firstStopMap, exceptionMessage);
        }
        return lastStopExtSysId;
    }


    private HashMap<String, Object> getLoadDetailsMap(String exceptionMessage) {
        HashMap<String, Object> loadDetailsMap = (LinkedHashMap<String, Object>) dataHashMap.get("LoadDetails");
        if (loadDetailsMap != null) {
            return loadDetailsMap;
        }
        throw new PayloadParseException(exceptionMessage);
    }

    private String getMapValueAsString(String key, HashMap<String, Object> map, String exceptionMessage) {
        String value = (String) map.get(key);
        if (StringUtils.isNotBlank(value)) {
            return StringUtils.equals("null", value) ? null : value;
        }
        throw new PayloadParseException(exceptionMessage);
    }

    private BigDecimal getMapValueAsBigDecimal(String key, HashMap<String, Object> map, String exceptionMessage) {
        String value = (String) map.get(key);
        if (value == null) {
            return null;
        }
        if (StringUtils.isNotBlank(value)) {
            try {
                return NumberUtils.parseNumber(value, BigDecimal.class);
            } catch (IllegalArgumentException iae) {
                throw new PayloadParseException(exceptionMessage, iae);
            }
        }
        throw new PayloadParseException(exceptionMessage);
    }


    private HashMap<String, Object> getMapValueAsMap(String key, HashMap<String, Object> map, String exceptionMessage) {
        HashMap<String, Object> returnMap = (HashMap<String, Object>) map.get(key);
        if (returnMap.isEmpty()) {
            throw new PayloadParseException(exceptionMessage);
        }
        return returnMap;
    }
}

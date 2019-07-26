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
import java.util.LinkedHashMap;
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
    private String lastStopExtSysId;
    private String lastStopName;

    @Autowired
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;


    @Override
    public LoadStop resolveFirstStop(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        LoadStop loadStop = new LoadStop();
        loadStop.setExtSysId(getFirstStopExtSysId(loadDetailsMap));
        loadStop.setName(getFirstStopName(loadDetailsMap));
        loadStop.setStopNumber(1);
        loadStop.setType(LoadStopType.PICKUP.getColVal());
        return loadStop;
    }

    @Override
    public LoadStop resolveLastStop(Map<String, Object> dataHashMap) {
        Map<String, Object> loadDetailsMap = getLoadDetailsMap(dataHashMap);
        LoadStop loadStop = new LoadStop();
        loadStop.setExtSysId(getLastStopExtSysId(loadDetailsMap));
        loadStop.setStopNumber(2);
        loadStop.setName(getLastStopName(loadDetailsMap));
        loadStop.setType(LoadStopType.DELIVERY.getColVal());
        return loadStop;
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
            postedRate = salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("rigPostedRate__c", loadDetailsMap);
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


    private String getFirstStopExtSysId(Map<String, Object> loadDetailsMap) {
        if (firstStopExtSysId == null) {
            Map<String, Object> firstStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("FirstStop", loadDetailsMap);
            firstStopExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopId", firstStopMap);
        }
        return firstStopExtSysId;
    }

    private String getFirstStopName(Map<String, Object> loadDetailsMap) {
        if (firstStopName == null) {
            Map<String, Object> firstStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("FirstStop", loadDetailsMap);
            firstStopName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("FirstStopName", firstStopMap);
        }
        return firstStopName;
    }


    private String getLastStopExtSysId(Map<String, Object> loadDetailsMap) {
        if (lastStopExtSysId == null) {
            Map<String, Object> firstStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStop", loadDetailsMap);
            lastStopExtSysId = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopId", firstStopMap);
        }
        return lastStopExtSysId;
    }

    private String getLastStopName(Map<String, Object> loadDetailsMap) {
        if (lastStopName == null) {
            Map<String, Object> firstStopMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("LastStop", loadDetailsMap);
            lastStopName = salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LastStopName", firstStopMap);
        }
        return lastStopName;
    }


    private Map<String, Object> getLoadDetailsMap(Map<String, Object> dataHashMap) {
        HashMap<String, Object> loadDetailsMap = (LinkedHashMap<String, Object>) dataHashMap.get("LoadDetails");
        if (loadDetailsMap != null) {
            return loadDetailsMap;
        }
        throw new PayloadParseException("LoadDetails");
    }
}

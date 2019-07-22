package io.riggo.web.integration.parser;

import io.riggo.data.domain.LoadLineItem;
import io.riggo.web.integration.exception.PayloadParseException;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesforceRevenovaRequestBodyParserForPatchLoad implements RequestBodyParserPatchLoadLineItem {

    private Map<String, Object> dataHashMap;

    public SalesforceRevenovaRequestBodyParserForPatchLoad(Map<String, Object> dataHashMap) {
        this.dataHashMap = dataHashMap;
    }

    public List<LoadLineItem> resolveLoadLineItemsList() {
        List<Map<String, Object>> loadLineItemsArray = getLoadLineItemsArray();
        return loadLineItemsArray.stream().map(loadLineItemMap -> {
            LoadLineItem loadLineItem = new LoadLineItem();
            loadLineItem.setDescription((String) loadLineItemMap.get("name"));
            loadLineItem.setHazMat(BooleanUtils.toBoolean((String) loadLineItemMap.get("Lineitemrtms__Hazardous_Materials__c")));
            loadLineItem.setDeliveryStopExtSysId((String) loadLineItemMap.get("Lineitemrtms__Delivery_Stop__c"));
            loadLineItem.setPickupStopExtSysId((String) loadLineItemMap.get("Lineitemrtms__Pickup_Stop__c"));
            loadLineItem.setLoadExtSysId((String) loadLineItemMap.get("Lineitemrtms__Load__c"));
            return loadLineItem;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getLoadLineItemsArray() {
        List<Map<String, Object>> lineItemsArray = (ArrayList<Map<String, Object>>) dataHashMap.get("LineItem");
        if (lineItemsArray != null) {
            return lineItemsArray;
        }
        throw new PayloadParseException("lineItemsList was not found");
    }
}

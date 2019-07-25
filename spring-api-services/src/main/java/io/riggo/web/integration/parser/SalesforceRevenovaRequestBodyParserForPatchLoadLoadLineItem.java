package io.riggo.web.integration.parser;

import io.riggo.data.domain.LoadLineItem;
import io.riggo.web.integration.exception.PayloadParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(value="prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem implements RequestBodyParserPatchLoadLineItem {

    @Autowired
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;


    public List<LoadLineItem> resolveLoadLineItemsList(Map<String, Object> dataHashMap) {
        List<Map<String, Object>> loadLineItemsArray = getLoadLineItemsArray(dataHashMap);
        return loadLineItemsArray.stream().map(loadLineItemMap -> {
            LoadLineItem loadLineItem = new LoadLineItem();
            loadLineItem.setDescription(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Lineitemrtms__Item_Description__c",  loadLineItemMap));
            loadLineItem.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Id",  loadLineItemMap));
            loadLineItem.setHazMat(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("Lineitemrtms__Hazardous_Materials__c", loadLineItemMap));
            loadLineItem.setDeliveryStopExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Lineitemrtms__Delivery_Stop__c", loadLineItemMap));
            loadLineItem.setPickupStopExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Lineitemrtms__Pickup_Stop__c", loadLineItemMap));
            loadLineItem.setLoadExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Lineitemrtms__Load__c", loadLineItemMap));
            loadLineItem.setWeight(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("Lineitemrtms__Weight__c", loadLineItemMap));
            return loadLineItem;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getLoadLineItemsArray(Map<String, Object> dataHashMap) {
        List<Map<String, Object>> lineItemsArray = (ArrayList<Map<String, Object>>) dataHashMap.get("LineItem");
        if (lineItemsArray != null) {
            return lineItemsArray;
        }
        throw new PayloadParseException("lineItemsList was not found");
    }
}

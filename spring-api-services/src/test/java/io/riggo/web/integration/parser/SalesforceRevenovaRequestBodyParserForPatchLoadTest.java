package io.riggo.web.integration.parser;

import io.riggo.data.domain.LoadLineItem;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SalesforceRevenovaRequestBodyParserForPatchLoadTest {

    @Test
    public void resolveLoadLineItemsList() {
        //given
        Map<String, Object> dataHashMap = new HashMap<>();
        List<Map<String, Object>> lineItemsArray = new ArrayList<>(0);

        Map<String, Object> firstLoadLineItem = new HashMap<>();
        firstLoadLineItem.put("Lineitemrtms__Hazardous_Materials__c", "true");
        firstLoadLineItem.put("Lineitemrtms__Delivery_Stop__c", "aSalesForceLoadStopId1");
        firstLoadLineItem.put("Lineitemrtms__Pickup_Stop__c", "aSalesForceLoadStopId2");
        firstLoadLineItem.put("Lineitemrtms__Load__c", "aSalesForceLoadId3");
        lineItemsArray.add(firstLoadLineItem);

        Map<String, Object> secondLoadLineItem = new HashMap<>();
        secondLoadLineItem.put("Lineitemrtms__Hazardous_Materials__c", "true");
        secondLoadLineItem.put("Lineitemrtms__Delivery_Stop__c", "aSalesForceLoadStopId4");
        secondLoadLineItem.put("Lineitemrtms__Pickup_Stop__c", "aSalesForceLoadStopId5");
        secondLoadLineItem.put("Lineitemrtms__Load__c", "aSalesForceLoadId6");
        lineItemsArray.add(secondLoadLineItem);

        dataHashMap.put("LineItem", lineItemsArray);

        //when
        SalesforceRevenovaRequestBodyParserForPatchLoad salesforceRevenovaRequestBodyParserForPatchLoad = new SalesforceRevenovaRequestBodyParserForPatchLoad(dataHashMap);
        List<LoadLineItem> loadLineItems = salesforceRevenovaRequestBodyParserForPatchLoad.resolveLoadLineItemsList();
        LoadLineItem firstLoadLineItemObject = loadLineItems.get(0);
        LoadLineItem secondLoadLineItemObject = loadLineItems.get(1);

        //then
        assertTrue(firstLoadLineItemObject.getHazMat());
        assertTrue(StringUtils.equals(firstLoadLineItemObject.getDeliveryStopExtSysId(), "aSalesForceLoadStopId1"));
        assertTrue(StringUtils.equals(firstLoadLineItemObject.getPickupStopExtSysId(), "aSalesForceLoadStopId2"));
        assertTrue(StringUtils.equals(firstLoadLineItemObject.getLoadExtSysId(), "aSalesForceLoadId3"));

        assertTrue(secondLoadLineItemObject.getHazMat());
        assertTrue(StringUtils.equals(secondLoadLineItemObject.getDeliveryStopExtSysId(), "aSalesForceLoadStopId4"));
        assertTrue(StringUtils.equals(secondLoadLineItemObject.getPickupStopExtSysId(), "aSalesForceLoadStopId5"));
        assertTrue(StringUtils.equals(secondLoadLineItemObject.getLoadExtSysId(), "aSalesForceLoadId6"));
    }
}
package io.riggo.web.integration.parser;

import io.riggo.RiggoConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RiggoConfig.class)
public class SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItemTest {

    @MockBean
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;

    @MockBean
    private SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem salesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem;


    @Test
    public void resolveLoadLineItemsListBROKENTEST() {
        //TODO: FIX THIS TEST
        //given
        Map<String, Object> dataHashMap = new HashMap<>();
        List<Map<String, Object>> lineItemsArray = new ArrayList<>(0);

        Map<String, Object> firstLoadLineItem = new HashMap<>();
        firstLoadLineItem.put("Lineitemrtms__Hazardous_Materials__c", "true");
        firstLoadLineItem.put("Id", "aSalesForceLoadLineItemId0");
        firstLoadLineItem.put("Lineitemrtms__Item_Description__c", "itemDescription");
        firstLoadLineItem.put("Lineitemrtms__Delivery_Stop__c", "aSalesForceLoadStopId1");
        firstLoadLineItem.put("Lineitemrtms__Pickup_Stop__c", "aSalesForceLoadStopId2");
        firstLoadLineItem.put("Lineitemrtms__Load__c", "aSalesForceLoadId3");
        firstLoadLineItem.put("Lineitemrtms__Weight__c", "50");

        lineItemsArray.add(firstLoadLineItem);

        Map<String, Object> secondLoadLineItem = new HashMap<>();
        secondLoadLineItem.put("Lineitemrtms__Hazardous_Materials__c", "true");
        secondLoadLineItem.put("Id", "aSalesForceLoadLineItemId4");
        secondLoadLineItem.put("Lineitemrtms__Item_Description__c", "itemDescription");
        secondLoadLineItem.put("Lineitemrtms__Delivery_Stop__c", "aSalesForceLoadStopId5");
        secondLoadLineItem.put("Lineitemrtms__Pickup_Stop__c", "aSalesForceLoadStopId6");
        secondLoadLineItem.put("Lineitemrtms__Load__c", "aSalesForceLoadId7");
        secondLoadLineItem.put("Lineitemrtms__Weight__c", "60");
        lineItemsArray.add(secondLoadLineItem);

        dataHashMap.put("LineItem", lineItemsArray);

        //when
//        List<LoadLineItem> loadLineItems = salesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem.resolveLoadLineItemsList(dataHashMap);
//        LoadLineItem firstLoadLineItemObject = loadLineItems.get(0);
//        LoadLineItem secondLoadLineItemObject = loadLineItems.get(1);
        //then
//        assertTrue(firstLoadLineItemObject.getHazMat());
//        assertTrue(StringUtils.equals(firstLoadLineItemObject.getDescription(), "itemDescription"));
//        assertTrue(StringUtils.equals(firstLoadLineItemObject.getExtSysId(), "aSalesForceLoadLineItemId0"));
//        assertTrue(StringUtils.equals(firstLoadLineItemObject.getDeliveryStopExtSysId(), "aSalesForceLoadStopId1"));
//        assertTrue(StringUtils.equals(firstLoadLineItemObject.getPickupStopExtSysId(), "aSalesForceLoadStopId2"));
//        assertTrue(StringUtils.equals(firstLoadLineItemObject.getLoadExtSysId(), "aSalesForceLoadId3"));
//        assertTrue(firstLoadLineItemObject.getWeight().intValue() == 50);
//
//        assertTrue(secondLoadLineItemObject.getHazMat());
//        assertTrue(StringUtils.equals(secondLoadLineItemObject.getDescription(), "itemDescription"));
//        assertTrue(StringUtils.equals(secondLoadLineItemObject.getExtSysId(), "aSalesForceLoadLineItemId4"));
//        assertTrue(StringUtils.equals(secondLoadLineItemObject.getDeliveryStopExtSysId(), "aSalesForceLoadStopId5"));
//        assertTrue(StringUtils.equals(secondLoadLineItemObject.getPickupStopExtSysId(), "aSalesForceLoadStopId6"));
//        assertTrue(StringUtils.equals(secondLoadLineItemObject.getLoadExtSysId(), "aSalesForceLoadId7"));
//        assertTrue(secondLoadLineItemObject.getWeight().intValue() == 60);

    }
}
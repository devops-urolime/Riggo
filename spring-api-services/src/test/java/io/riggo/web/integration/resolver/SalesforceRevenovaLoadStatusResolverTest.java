package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadSubStatus;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SalesforceRevenovaLoadStatusResolverTest {

    @Test
    public void resolveLoadStatusQuoted() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Pending");

        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.QUOTED);
    }

    @Test
    public void resolveLoadStatusBooked() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "Unassigned");

        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.BOOKED);
    }

    @Test
    public void resolveLoadStatusBookedAssigned() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "Assigned");

        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.BOOKED);
    }

    @Test
    public void resolveLoadStatusDispatched() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "Dispatched");

        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.DISPATCHED);
    }

    @Test
    public void resolveLoadStatusAtPickup() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "In Transit");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.FIRST_STOP_STATUS, "Arrived");


        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.AT_PICKUP);
    }


    @Test
    public void resolveLoadStatusInTransit() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "In Transit");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.FIRST_STOP_STATUS, "Departed");

        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.IN_TRANSIT);
    }


    @Test
    public void resolveLoadStatusAtDelivery() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "In Transit");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LAST_STOP_STATUS, "Arrived");

        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.AT_DELIVERY);
    }

    @Test
    public void resolveLoadStatusPendingDocuments() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "Delivered");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.HAS_DOCUMENTS, false);


        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.PENDING_DOCUMENTS);
    }

    @Test
    public void resolveLoadStatusDocumentsReceived() {
        //given
        Map<String, Object> resolverMap = new HashMap<>();
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.SALES_STATUS, "Won");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.LOAD_STATUS, "Delivered");
        resolverMap.put(SalesforceRevenovaLoadStatusResolver.HAS_DOCUMENTS, true);

        //when
        LoadSubStatus loadSubStatus = new SalesforceRevenovaLoadStatusResolver().resolveLoadStatus(resolverMap);

        //then
        assertTrue(loadSubStatus == LoadSubStatus.DOCUMENTS_RECEIVED);
    }
}
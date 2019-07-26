package io.riggo.web.integration.parser;

import io.riggo.data.domain.Address;
import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.Location;
import io.riggo.web.integration.exception.PayloadParseException;
import io.riggo.web.integration.resolver.SalesforceRevenovaLoadStopCarrierStatusResolver;
import io.riggo.web.integration.resolver.SalesforceRevenovaLoadStopStatusResolver;
import io.riggo.web.integration.resolver.SalesforceRevenovaLoadStopTypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(value="prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SalesforceRevenovaRequestBodyParserForPatchLoadStop implements RequestBodyParserPatchLoadStop {

    @Autowired
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;

    @Autowired
    private SalesforceRevenovaLoadStopTypeResolver salesforceRevenovaLoadStopTypeResolver;

    @Autowired
    private SalesforceRevenovaLoadStopStatusResolver salesforceRevenovaLoadStopStatusResolver;

    @Autowired
    private SalesforceRevenovaLoadStopStatusResolver salesforceRevenovaLoadStopCarrierStatusResolver;


    @Override
    public List<LoadStop> resolveLoadStopsList(Map<String, Object> dataHashMap) {
        List<Map<String, Object>> loadStopsArray = getLoadStopsArray(dataHashMap);
        return loadStopsArray.stream().map(loadStopMap -> {
            LoadStop loadStop = new LoadStop();
            loadStop.setLoadExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Stoprtms__Load__c", loadStopMap));
            loadStop.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Id", loadStopMap));
            loadStop.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("StopName", loadStopMap));
            loadStop.setShippingReceivingHours(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Stoprtms__Shipping_Receiving_Hours__c", loadStopMap));

            Location location = new Location();
            Map<String, Object> locationMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("StopLocation", loadStopMap);
            location.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LocationId", locationMap));
            location.setName(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LocationName", locationMap));

            Address address = new Address();
            address.setAddress1(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LocationShippingStreet", locationMap));
            address.setCity(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LocationShippingCity", locationMap));
            address.setState(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LocationShippingState", locationMap));
            address.setPostalCode(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("LocationShippingPostalCode", locationMap));
            location.setAddress(address);
            loadStop.setLocation(location);

            loadStop.setStopNumber(salesforceRevenovaRequestBodyParserHelper.getMapValueAsInteger("Stoprtms__Number__c", loadStopMap));

            Map<String, Object> loadStopTypeResolverMap = new HashMap<>();
            loadStopTypeResolverMap.put(SalesforceRevenovaLoadStopTypeResolver.STOP_TYPE, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Stoprtms__P_D__c", loadStopMap));
            loadStop.setType(salesforceRevenovaLoadStopTypeResolver.resolveLoadStopType(loadStopTypeResolverMap).getColVal());

            loadStop.setExpectedDateTime(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("Stoprtms__Expected_Date__c", loadStopMap));
            loadStop.setAppointmentRequired(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBoolean("Stoprtms__Appointment_Required__c", loadStopMap));
            loadStop.setAppointmentTime(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Stoprtms__Appointment_Time__c", loadStopMap));

            Map<String, Object> loadStopStatusResolverMap = new HashMap<>();
            loadStopStatusResolverMap.put(SalesforceRevenovaLoadStopStatusResolver.LOAD_STOP_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Stoprtms__Stop_Status__c", loadStopMap));
            loadStop.setStopStatus(salesforceRevenovaLoadStopStatusResolver.resolveLoadStopStatus(loadStopStatusResolverMap).getColVal());

            Map<String, Object> loadStopCarrierStatusResolverMap = new HashMap<>();
            loadStopCarrierStatusResolverMap.put(SalesforceRevenovaLoadStopCarrierStatusResolver.LOAD_STOP_CARRIER_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("Stoprtms__Carrier_Status2__c", loadStopMap));
            loadStop.setCarrierStatus(salesforceRevenovaLoadStopCarrierStatusResolver.resolveLoadStopStatus(loadStopCarrierStatusResolverMap).getColVal());

            //TODO: arrivalStatus
            //TODO: departureStatus
            loadStop.setArrivalDate(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDate("Stoprtms__Arrival_Date__c", loadStopMap));
            loadStop.setDepartureDateTime(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTimeFrom2Keys ("Stoprtms__Departure_Date__c", "Stoprtms__Departure_Time__c", loadStopMap));
            return loadStop;
        }).collect(Collectors.toList());
    }


    private List<Map<String, Object>> getLoadStopsArray(Map<String, Object> dataHashMap) {
        List<Map<String, Object>> loadStopsArray = (ArrayList<Map<String, Object>>) dataHashMap.get("LoadStops");
        if (loadStopsArray != null) {
            return loadStopsArray;
        }
        throw new PayloadParseException("loadStopsList was not found");
    }

}

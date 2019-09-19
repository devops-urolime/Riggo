package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopArrivalStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SalesforceRevenovaLoadStopArrivalStatusResolver implements LoadStopArrivalStatusResolver{

    public final static String LOAD_STOP_ARRIVAL_STATUS = "load_stop_arrival_status";

    @Override
    public LoadStopArrivalStatus resolveLoadStopArrivalStatus(Map<String, Object> parameters) {
        return LoadStopArrivalStatus.fromDisplayName((String) parameters.get(LOAD_STOP_ARRIVAL_STATUS));
    }
}

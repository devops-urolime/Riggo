package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopCarrierStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SalesforceRevenovaLoadStopCarrierStatusResolver implements LoadStopCarrierStatusResolver{

    public final static String LOAD_STOP_CARRIER_STATUS = "load_stop_carrier_status";

    @Override
    public LoadStopCarrierStatus resolveLoadStopCarrierStatus(Map<String, Object> parameters) {
        return LoadStopCarrierStatus.fromDisplayName((String) parameters.get(LOAD_STOP_CARRIER_STATUS));
    }
}

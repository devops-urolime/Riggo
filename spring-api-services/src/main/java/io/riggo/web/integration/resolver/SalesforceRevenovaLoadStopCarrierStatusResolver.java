package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopCarrierStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SalesforceRevenovaLoadStopCarrierStatusResolver implements LoadStopCarrierStatusResolver{

    public final static String LOAD_STOP_CARRIER_STATUS = "load_stop_carrier_status";

    @Override
    public LoadStopCarrierStatus resolveLoadStopCarrierStatus(Map<String, Object> parameters) {
        LoadStopCarrierStatus loadStopCarrierStatus = null;
        if(StringUtils.isNotBlank((String) parameters.get(LOAD_STOP_CARRIER_STATUS))){
            loadStopCarrierStatus = LoadStopCarrierStatus.fromDisplayName((String) parameters.get(LOAD_STOP_CARRIER_STATUS));
        }
        return loadStopCarrierStatus != null ? loadStopCarrierStatus : LoadStopCarrierStatus.NO_STATUS;
    }
}

package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SalesforceRevenovaLoadStopStatusResolver implements LoadStopStatusResolver{

    public final static String LOAD_STOP_STATUS = "load_stop_status";

    @Override
    public LoadStopStatus resolveLoadStopStatus(Map<String, Object> parameters) {
        if(StringUtils.isNotBlank((String) parameters.get(LOAD_STOP_STATUS))) {
            return LoadStopStatus.fromDisplayName((String) parameters.get(LOAD_STOP_STATUS));
        }
        return LoadStopStatus.NO_STATUS;
    }
}
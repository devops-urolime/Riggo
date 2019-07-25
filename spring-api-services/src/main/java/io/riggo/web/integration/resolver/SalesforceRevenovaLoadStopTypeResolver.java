package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SalesforceRevenovaLoadStopTypeResolver implements LoadStopTypeResolver{

    public final static String STOP_TYPE = "stop_type";

    public LoadStopType resolveLoadStopType(Map<String, Object> parameters){
        return LoadStopType.fromDisplayName((String) parameters.get(STOP_TYPE));
    }
}

package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopCarrierStatus;

import java.util.Map;

public interface LoadStopCarrierStatusResolver {

    LoadStopCarrierStatus resolveLoadStopCarrierStatus(Map<String, Object> parameters);
}

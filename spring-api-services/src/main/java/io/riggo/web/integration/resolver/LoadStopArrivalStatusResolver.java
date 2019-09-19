package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopArrivalStatus;

import java.util.Map;

public interface LoadStopArrivalStatusResolver {

    LoadStopArrivalStatus resolveLoadStopArrivalStatus(Map<String, Object> parameters);
}

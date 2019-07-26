package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopStatus;

import java.util.Map;

public interface LoadStopStatusResolver {

    LoadStopStatus resolveLoadStopStatus(Map<String, Object> parameters);
}

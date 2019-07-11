package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadSubStatus;

import java.util.Map;

public interface LoadStatusResolver {

    LoadSubStatus resolveLoadStatus(Map<String, Object> parameters);
}

package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadStopType;

import java.util.Map;

public interface LoadStopTypeResolver {

    LoadStopType resolveLoadStopType(Map<String, Object> parameters);
}

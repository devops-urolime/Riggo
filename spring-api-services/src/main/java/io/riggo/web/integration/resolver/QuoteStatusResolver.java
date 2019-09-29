package io.riggo.web.integration.resolver;

import io.riggo.data.domain.QuoteStatus;

import java.util.Map;

public interface QuoteStatusResolver {

    QuoteStatus resolveQuoteStatus(Map<String, Object> parameters);
}

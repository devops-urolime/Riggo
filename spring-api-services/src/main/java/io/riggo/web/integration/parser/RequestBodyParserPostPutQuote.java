package io.riggo.web.integration.parser;

import io.riggo.data.domain.Quote;

import java.util.List;
import java.util.Map;

public interface RequestBodyParserPostPutQuote {

    List<Quote> resolveQuote(Map<String, Object> dataHashMap);
}

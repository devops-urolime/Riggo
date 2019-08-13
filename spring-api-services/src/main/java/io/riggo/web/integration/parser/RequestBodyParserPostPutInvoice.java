package io.riggo.web.integration.parser;

import io.riggo.data.domain.Invoice;

import java.util.Map;

public interface RequestBodyParserPostPutInvoice {

    Invoice resolveInvoice(Map<String, Object> dataHashMap);
}

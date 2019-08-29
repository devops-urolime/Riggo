package io.riggo.web.integration.parser;

import io.riggo.data.domain.Invoice;

import java.util.List;
import java.util.Map;

public interface RequestBodyParserPostPutInvoice {

    List<Invoice> resolveInvoice(Map<String, Object> dataHashMap);
}

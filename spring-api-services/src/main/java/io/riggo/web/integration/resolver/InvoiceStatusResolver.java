package io.riggo.web.integration.resolver;

import io.riggo.data.domain.InvoiceStatus;

import java.util.Map;

public interface InvoiceStatusResolver {

    InvoiceStatus resolveInvoiceStatus(Map<String, Object> parameters);
}

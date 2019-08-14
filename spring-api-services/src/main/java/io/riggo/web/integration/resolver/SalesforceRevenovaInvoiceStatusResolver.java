package io.riggo.web.integration.resolver;

import io.riggo.data.domain.InvoiceStatus;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class SalesforceRevenovaInvoiceStatusResolver implements InvoiceStatusResolver{

    public final static String INVOICE_STATUS = "invoice_status";


    public InvoiceStatus resolveInvoiceStatus(Map<String, Object> parameters){
        String invoiceStatus = (String) parameters.get(INVOICE_STATUS);
        return InvoiceStatus.fromDisplayName(invoiceStatus);
    }
}
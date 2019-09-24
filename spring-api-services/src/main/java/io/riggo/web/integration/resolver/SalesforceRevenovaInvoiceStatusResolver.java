package io.riggo.web.integration.resolver;

import io.riggo.data.domain.InvoiceStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class SalesforceRevenovaInvoiceStatusResolver implements InvoiceStatusResolver{

    public final static String INVOICE_STATUS = "invoice_status";

    public InvoiceStatus resolveInvoiceStatus(Map<String, Object> parameters){
        InvoiceStatus invoiceStatus = null;
        if(StringUtils.isNotBlank((String) parameters.get(INVOICE_STATUS))){
            invoiceStatus = InvoiceStatus.fromDisplayName((String) parameters.get(INVOICE_STATUS));
        }
        return invoiceStatus != null ? invoiceStatus : InvoiceStatus.NO_STATUS;
    }
}
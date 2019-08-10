package io.riggo.web.integration.parser;

import io.riggo.data.domain.Invoice;
import io.riggo.web.integration.resolver.SalesforceRevenovaInvoiceStatusResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SalesforceRevenovaRequestBodyParserPostPutInvoice implements RequestBodyParserPostPutInvoice {


    @Autowired
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;

    @Autowired
    private SalesforceRevenovaInvoiceStatusResolver salesforceRevenovaInvoiceStatusResolver = new SalesforceRevenovaInvoiceStatusResolver();

    //For unit tests
    public void setSalesforceRevenovaRequestBodyParserHelper(SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper){
        this.salesforceRevenovaRequestBodyParserHelper = salesforceRevenovaRequestBodyParserHelper;
    }

    //For unit tests
    public void setSalesforceRevenovaInvoiceStatusResolver(SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper){
        this.salesforceRevenovaRequestBodyParserHelper = salesforceRevenovaRequestBodyParserHelper;
    }


    @Override
    public Invoice resolveInvoice(Map<String, Object> dataHashMap) {
        Map<String, Object> invoiceMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsMap("Invoice", dataHashMap);
        Invoice invoice = new Invoice();
        invoice.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("id", invoiceMap));
        invoice.setQuoteDate(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("quote_date", invoiceMap));


        Map<String, Object> invoiceStatusMap = new HashMap<>();
        invoiceStatusMap.put(SalesforceRevenovaInvoiceStatusResolver.INVOICE_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("status", invoiceMap));
        invoice.setStatus(salesforceRevenovaInvoiceStatusResolver.resolveInvoiceStatus(invoiceStatusMap).getColVal());

        invoice.setComments(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("comments", invoiceMap));
        invoice.setLoadExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("load_id", invoiceMap));
        invoice.setNetFreightCharges(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("net_freight_charges", invoiceMap));
        invoice.setFuelSurcharge(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("fuel_surcharges", invoiceMap));
        invoice.setAccessorialCharges(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("accessorial_charge", invoiceMap));
        invoice.setTransportationTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("transportation_total", invoiceMap));
        invoice.setCustomerQuoteTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("customer_quote_total", invoiceMap));
        return invoice;
    }
}
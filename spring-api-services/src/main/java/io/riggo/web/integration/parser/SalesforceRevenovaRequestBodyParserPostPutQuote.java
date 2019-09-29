package io.riggo.web.integration.parser;

import io.riggo.data.domain.Quote;
import io.riggo.web.integration.resolver.SalesforceRevenovaQuoteStatusResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SalesforceRevenovaRequestBodyParserPostPutQuote implements RequestBodyParserPostPutQuote {


    @Autowired
    private SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper;

    @Autowired
    private SalesforceRevenovaQuoteStatusResolver salesforceRevenovaQuoteStatusResolver = new SalesforceRevenovaQuoteStatusResolver();

    //For unit tests
    public void setSalesforceRevenovaRequestBodyParserHelper(SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper){
        this.salesforceRevenovaRequestBodyParserHelper = salesforceRevenovaRequestBodyParserHelper;
    }

    //For unit tests
    public void setSalesforceRevenovaQuoteStatusResolver(SalesforceRevenovaRequestBodyParserHelper salesforceRevenovaRequestBodyParserHelper){
        this.salesforceRevenovaRequestBodyParserHelper = salesforceRevenovaRequestBodyParserHelper;
    }


    @Override
    public List<Quote> resolveQuote(Map<String, Object> dataHashMap) {
        List<Map<String, Object>> quoteListMap = salesforceRevenovaRequestBodyParserHelper.getMapValueAsListOfMap("Quote", dataHashMap);
        List<Quote> quotes = quoteListMap.stream().map(quoteMap -> {
            Quote quote = new Quote();
            quote.setExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("id", quoteMap));
            quote.setQuoteDate(salesforceRevenovaRequestBodyParserHelper.getMapValueAsLocalDateTime("quote_date", quoteMap));


            Map<String, Object> quoteStatusMap = new HashMap<>();
            quoteStatusMap.put(SalesforceRevenovaQuoteStatusResolver.QUOTE_STATUS, salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("status", quoteMap));
            quote.setStatus(salesforceRevenovaQuoteStatusResolver.resolveQuoteStatus(quoteStatusMap).getColVal());

            quote.setComments(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("comments", quoteMap));
            quote.setLoadExtSysId(salesforceRevenovaRequestBodyParserHelper.getMapValueAsString("load_id", quoteMap));
            quote.setNetFreightCharges(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("net_freight_charges", quoteMap));
            quote.setFuelSurcharge(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("fuel_surcharges", quoteMap));
            quote.setAccessorialCharges(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("accessorial_charge", quoteMap));
            quote.setTransportationTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("transportation_total", quoteMap));
            quote.setCustomerQuoteTotal(salesforceRevenovaRequestBodyParserHelper.getMapValueAsBigDecimal("customer_quote_total", quoteMap));
            return quote;
        }).collect(Collectors.toList());
        return quotes;
    }
}
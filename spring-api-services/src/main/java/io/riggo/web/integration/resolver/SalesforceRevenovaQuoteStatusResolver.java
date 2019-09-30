package io.riggo.web.integration.resolver;

import io.riggo.data.domain.QuoteStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class SalesforceRevenovaQuoteStatusResolver implements QuoteStatusResolver {

    public final static String QUOTE_STATUS = "quote_status";

    public QuoteStatus resolveQuoteStatus(Map<String, Object> parameters){
        QuoteStatus quoteStatus = null;
        if(StringUtils.isNotBlank((String) parameters.get(QUOTE_STATUS))){
            quoteStatus = QuoteStatus.fromDisplayName((String) parameters.get(QUOTE_STATUS));
        }
        return quoteStatus != null ? quoteStatus : QuoteStatus.NO_STATUS;
    }
}
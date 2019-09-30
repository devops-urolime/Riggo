package io.riggo.web;

import io.riggo.data.domain.Quote;
import io.riggo.data.domain.Load;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.BadRequestException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.QuoteService;
import io.riggo.data.services.LoadService;
import io.riggo.web.integration.SalesforceRevenovaConstants;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserPostPutQuote;
import io.riggo.web.response.BaseAPIResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(value = Paths.API_VERSION)
public class QuoteController {

    Logger logger = LoggerFactory.getLogger(QuoteController.class);

    @Autowired
    private LoadService loadService;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private SalesforceRevenovaRequestBodyParserPostPutQuote salesforceRevenovaRequestBodyParserPostPutQuote;

    @Autowired
    private AuthenticationFacade authenticationFacade;


    @PutMapping(value = Paths.LOAD_QUOTE, produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Quote> putQuote(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        List<Quote> quoteList = processPutQuote(dataHashMap);
        BaseAPIResponse baseApiResponse = new BaseAPIResponse();
        baseApiResponse.setData(quoteList);
        return baseApiResponse;
    }


    @PutMapping(value = Paths.LOAD_LOADID_PARAM_QUOTE, produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Quote> putQuoteWithLoadId(@PathVariable final String loadId, @RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException, BadRequestException{
        if(NumberUtils.isDigits(loadId)) {
            validateLoad(NumberUtils.toInt(loadId), authenticationFacade.getSiteId());
            List<Quote> quoteList = processPutQuote(dataHashMap);

            BaseAPIResponse baseApiResponse = new BaseAPIResponse();
            baseApiResponse.setData(quoteList);
            return baseApiResponse;
        }
        throw new BadRequestException();
    }


    private List<Quote> processPutQuote(Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        List<Quote> quoteList = salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(dataHashMap);
        for(Quote quote : quoteList) {
            Optional<Quote> quoteFromDb = quoteService.findByExtSysId(quote.getExtSysId());
            validateLoadExists(quote, authenticationFacade.getSiteId());
            if (quoteFromDb.isPresent()) {
                BeanUtils.copyProperties(quoteFromDb.get(), quote, SalesforceRevenovaConstants.POST_PUT_QUOTE_IGNORE_PROPERTIES);
                quoteService.save(quote);
            }else{
                quoteService.save(quote);
            }
        }
        return quoteList;
    }


    private void validateLoad(Integer loadId, Integer siteId) throws ResourceNotFoundException{
        Optional<Load> checkLoadExists = loadService.findById(loadId, siteId);
        if (!checkLoadExists.isPresent()) {
            throw new ResourceNotFoundException(ResourceType.LOAD, loadId);
        }
    }


    private void validateLoadExists(Quote quote, Integer siteId) throws ResourceNotFoundException{
        if(StringUtils.isNotBlank(quote.getLoadExtSysId())) {
            Optional<Load> load = loadService.findByExtSysId(quote.getLoadExtSysId(), siteId);
            if(load.isPresent() && load.get().getId() != null) {
                quote.setLoadId(load.get().getId());
            }else{
                throw new ResourceNotFoundException(ResourceType.LOAD, quote.getLoadExtSysId());
            }
        }
    }
}
package io.riggo.web;

import io.riggo.data.domain.Quote;
import io.riggo.data.domain.Load;
import io.riggo.data.services.QuoteService;
import io.riggo.data.services.LoadService;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserPostPutQuote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(QuoteController.class)
public class QuoteControllerTest {

    private static Logger logger = LoggerFactory.getLogger(QuoteControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoadService loadService;

    @MockBean
    private QuoteService quoteService;

    @MockBean
    private SalesforceRevenovaRequestBodyParserPostPutQuote salesforceRevenovaRequestBodyParserPostPutQuote;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putInsertQuoteHappyPath() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        given(quoteService.findById(1)).willReturn(java.util.Optional.of(quote));
        given(quoteService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/quote")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andReturn()
        ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @Test
    public void postQuoteUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(post(Paths.API_VERSION + Paths.LOAD_QUOTE)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void putInsertQuoteRequireWriteLoadInvoicePermission() throws Exception {
        mvc.perform(put(Paths.API_VERSION + Paths.LOAD_QUOTE)
                .contentType(APPLICATION_JSON)
                .content(jsonPostLoadFor200))
                .andExpect(status().isForbidden())
                .andReturn();
    }



    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putInsertQuoteUnrecognizedLoad() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        Load load = new Load();
        load.setId(1);
        load.setSiteId(100);


        given(quoteService.findById(1)).willReturn(java.util.Optional.of(quote));
        given(loadService.findByExtSysId("1", 100)).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD_QUOTE)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putInsertQuoteWithParamHappyPath() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        Load load = new Load();
        load.setId(1);
        load.setSiteId(100);

        given(authenticationFacade.getSiteId()).willReturn(100);
        given(quoteService.findById(1)).willReturn(java.util.Optional.of(quote));
        given(loadService.findById(1, 100)).willReturn(java.util.Optional.of(load));
        given(quoteService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/1/quote")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @Test
    public void postQuoteWithParamUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/1/quote")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void putInsertQuoteWithRequireWriteLoadInvoicePermission() throws Exception {
        mvc.perform(put(Paths.API_VERSION + "/load/1/quote")
                .contentType(APPLICATION_JSON)
                .content(jsonPostLoadFor200))
                .andExpect(status().isForbidden())
                .andReturn();
    }





    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putInsertQuoteWithParamUnrecognizedLoad() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        Load load = new Load();
        load.setId(1);
        load.setSiteId(100);

        given(quoteService.findById(1)).willReturn(java.util.Optional.of(quote));
        given(loadService.findByExtSysId("1", 100)).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/1/quote")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }



    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putQuoteHappyPath() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        given(quoteService.findByExtSysId("1")).willReturn(java.util.Optional.of(quote));
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD_QUOTE)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @Test
    public void putQuoteUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION+ Paths.LOAD_QUOTE)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void putQuoteRequireWriteLoadQuotePermission() throws Exception {
        mvc.perform(put(Paths.API_VERSION+ Paths.LOAD_QUOTE)
                .contentType(APPLICATION_JSON)
                .content(jsonPostLoadFor200))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putQuoteUnrecognizedLoad() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        Load load = new Load();
        load.setId(1);
        load.setSiteId(100);

        given(quoteService.findById(1)).willReturn(java.util.Optional.of(quote));
        given(loadService.findByExtSysId("1", 100)).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION+ Paths.LOAD_QUOTE)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putQuoteWithParamHappyPath() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        Load load = new Load();
        load.setSiteId(100);
        load.setId(1);

        given(authenticationFacade.getSiteId()).willReturn(100);
        given(loadService.findById(1, 100)).willReturn(java.util.Optional.of(load));
        given(quoteService.findByExtSysId("1")).willReturn(java.util.Optional.of(quote));
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD + "/1" + Paths.QUOTE)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @Test
    public void putQuoteWithParamUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD + "/1" + Paths.QUOTE)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void putQuoteWithRequireWriteLoadInvoicePermission() throws Exception {
        mvc.perform(put(Paths.API_VERSION + Paths.LOAD + "/1" + Paths.QUOTE)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putQuoteWithParamInvoiceNotFound() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        Load load = new Load();
        load.setSiteId(100);
        load.setId(1);

        given(loadService.findById(1, 100)).willReturn(java.util.Optional.of(load));
        given(quoteService.findById(1)).willReturn(java.util.Optional.of(quote));
        given(quoteService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD + "/1" + Paths.QUOTE)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:quote"})
    @Test
    public void putQuoteWithParamUnrecognizedLoad() throws Exception {
        List<Quote> quoteList = new ArrayList<>();
        Quote quote = new Quote();
        quote.setId(1);
        quote.setExtSysId("1");
        quote.setLoadExtSysId("1");
        quote.setLoadId(1);
        quoteList.add(quote);

        Load load = new Load();
        load.setId(1);
        load.setSiteId(100);

        given(quoteService.findById(1)).willReturn(java.util.Optional.of(quote));
        given(loadService.findByExtSysId("1", 100)).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutQuote.resolveQuote(any(Map.class))).willReturn(quoteList);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + Paths.LOAD + "/1" + Paths.QUOTE)
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    private static String jsonPostLoadFor200 = "{\n" +
            "\t\"Quote\" : {\n" +
            "\t\t\"id\" : \"a0jg000000ExlfrAsq\",\n" +
            "\t\t\"quote_date\" : \"2019-08-09 13:05:19\",\n" +
            "\t\t\"status\" : \"Accepted\",\n" +
            "\t\t\"comments\" : \"Load\",\n" +
            "\t\t\"load_id\" : \"a0jg000000ExlfrAAB\",\n" +
            "\t\t\"net_freight_charges\" : \"1200.22\",\n" +
            "\t\t\"fuel_surcharges\" : \"1300.22\",\n" +
            "\t\t\"accessorial_charge\" : \"1400.22\",\n" +
            "\t\t\"transportation_total\" : \"1500.22\",\n" +
            "\t\t\"customer_quote_total\" : \"1600.22\"\n" +
            "\t}\n" +
            "}\n";
}
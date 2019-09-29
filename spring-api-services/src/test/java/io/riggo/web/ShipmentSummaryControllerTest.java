package io.riggo.web;

import io.riggo.data.domain.*;
import io.riggo.data.services.FiscalPeriodService;
import io.riggo.data.services.QuoteLoadService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShipmentSummaryController.class)
public class ShipmentSummaryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private QuoteLoadService quoteLoadService;

    @MockBean
    private FiscalPeriodService fiscalPeriodService;


    private static Logger logger = LoggerFactory.getLogger(ShipmentSummaryControllerTest.class);

    @WithMockUser(value = "spring")
    @Test
    public void getSummaryHappyPathSuperAdminMonths() throws Exception {
        Integer fiscalYear = 2019;
        Integer fiscalMonth = 9;
        Integer startFiscalMonth = 6;

        FiscalPeriod startFiscalPeriod = new FiscalPeriod();
        startFiscalPeriod.setYear_actual(fiscalYear);
        startFiscalPeriod.setMonthActual(startFiscalMonth);
        startFiscalPeriod.setFirstDayOfQuarter(LocalDate.of(2019, 4 ,1));
        startFiscalPeriod.setFirstDayOfMonth(LocalDate.of(2019, 6 ,1));
        startFiscalPeriod.setQuarterActual(2);

        FiscalPeriod endFiscalPeriod = new FiscalPeriod();
        endFiscalPeriod.setYear_actual(fiscalYear);
        endFiscalPeriod.setMonthActual(fiscalMonth);
        endFiscalPeriod.setLastDayOfQuarter(LocalDate.of(2019, 9 ,30));
        endFiscalPeriod.setFirstDayOfMonth(LocalDate.of(2019, 9 ,1));
        endFiscalPeriod.setQuarterActual(3);

        QuoteLoad quote = new QuoteLoad();
        quote.setQuoteDate(LocalDateTime.of(2019, 5, 2, 0, 0,0 ));
        quote.setNetFreightCharges(new BigDecimal(11));

        QuoteLoad quote2 = new QuoteLoad();
        quote2.setQuoteDate(LocalDateTime.of(2019, 6, 2, 0, 0,0 ));
        quote2.setNetFreightCharges(new BigDecimal(22));

        QuoteLoad quote3 = new QuoteLoad();
        quote3.setQuoteDate(LocalDateTime.of(2019, 7, 2, 0, 0,0 ));
        quote3.setNetFreightCharges(new BigDecimal(22));

        List<QuoteLoad> quoteList = new ArrayList<>();
        quoteList.add(quote);
        quoteList.add(quote2);
        quoteList.add(quote3);

        List<Integer> quoteStatusList = Arrays.asList(new Integer[]{QuoteStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});

        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(3))).willReturn(Optional.of(startFiscalPeriod));
        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1))).willReturn(Optional.of(endFiscalPeriod));

        given(quoteLoadService.findQuotesBySite(100, quoteStatusList, loadStatusList, startFiscalPeriod.getFirstDayOfQuarter().atStartOfDay(), endFiscalPeriod.getLastDayOfQuarter().atStartOfDay().plusDays(1))).willReturn(Optional.of(quoteList));

        MvcResult result = mvc.perform(get(Paths.API_VERSION + Paths.LOAD_SHIPMENT_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title", is("Q2 2019 - Q3 2019")))
                .andExpect(jsonPath("$.data[0].shipmentData[0].fiscalMonth", is(4)))
                .andExpect(jsonPath("$.data[0].shipmentData[1].fiscalMonth", is(5)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }


    @Test
    public void shipmentSummaryUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(get(Paths.API_VERSION + Paths.LOAD_SHIPMENT_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void getSummaryHappyPathSuperAdminWeeks() throws Exception {
        Integer fiscalYear = 2019;
        Integer fiscalMonth = 9;

        FiscalPeriod startFiscalPeriod = new FiscalPeriod();
        startFiscalPeriod.setYear_actual(fiscalYear);
        startFiscalPeriod.setMonthActual(fiscalMonth);
        startFiscalPeriod.setFirstDayOfMonth(LocalDate.of(2019, 9 ,1));
        startFiscalPeriod.setLastDayOfMonth(LocalDate.of(2019, 9 ,30));
        startFiscalPeriod.setMonthName("September");

        QuoteLoad quote = new QuoteLoad();
        quote.setQuoteDate(LocalDateTime.of(2019, 9, 1, 0, 0,0 ));
        quote.setNetFreightCharges(new BigDecimal(11));

        QuoteLoad quote2 = new QuoteLoad();
        quote2.setQuoteDate(LocalDateTime.of(2019, 9, 2, 0, 0,0 ));
        quote2.setNetFreightCharges(new BigDecimal(22));

        QuoteLoad quote3 = new QuoteLoad();
        quote3.setQuoteDate(LocalDateTime.of(2019, 9, 3, 0, 0,0 ));
        quote3.setNetFreightCharges(new BigDecimal(22));

        List<QuoteLoad> quoteList = new ArrayList<>();
        quoteList.add(quote);
        quoteList.add(quote2);
        quoteList.add(quote3);

        List<Integer> quoteStatusList = Arrays.asList(new Integer[]{QuoteStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});

        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1))).willReturn(Optional.of(startFiscalPeriod));

        given(quoteLoadService.findQuotesBySite(100, quoteStatusList, loadStatusList, startFiscalPeriod.getFirstDayOfMonth().atStartOfDay(), startFiscalPeriod.getLastDayOfMonth().atStartOfDay().plusDays(1))).willReturn(Optional.of(quoteList));

        MvcResult result = mvc.perform(get(Paths.API_VERSION + Paths.LOAD_SHIPMENT_SUMMARY + "?units=weeks")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title", is("September 2019")))
                .andExpect(jsonPath("$.data[0].shipmentData[0].fiscalMonth", is(9)))
                .andExpect(jsonPath("$.data[0].shipmentData[0].fiscalWeek", is(1)))
                .andExpect(jsonPath("$.data[0].shipmentData[1].fiscalWeek", is(2)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void getSummaryHappyPathSuperAdminDays() throws Exception {
        Integer fiscalYear = 2019;
        Integer fiscalMonth = 9;

        FiscalPeriod startFiscalPeriod = new FiscalPeriod();
        startFiscalPeriod.setYear_actual(fiscalYear);
        startFiscalPeriod.setMonthActual(fiscalMonth);
        startFiscalPeriod.setFirstDayOfWeek(LocalDate.of(2019, 9 ,1));
        startFiscalPeriod.setLastDayOfWeek(LocalDate.of(2019, 9 ,7));
        startFiscalPeriod.setMonthName("September");

        QuoteLoad quote = new QuoteLoad();
        quote.setQuoteDate(LocalDateTime.of(2019, 9, 1, 0, 0,0 ));
        quote.setNetFreightCharges(new BigDecimal(11));
        quote.setDistanceMiles(new BigDecimal(12.12));

        QuoteLoad quote2 = new QuoteLoad();
        quote2.setQuoteDate(LocalDateTime.of(2019, 9, 2, 0, 0,0 ));
        quote2.setNetFreightCharges(new BigDecimal(22));
        quote2.setDistanceMiles(new BigDecimal(12.12));

        QuoteLoad quote3 = new QuoteLoad();
        quote3.setQuoteDate(LocalDateTime.of(2019, 9, 3, 0, 0,0 ));
        quote3.setNetFreightCharges(new BigDecimal(22));
        quote3.setDistanceMiles(new BigDecimal(12.12));

        List<QuoteLoad> quoteList = new ArrayList<>();
        quoteList.add(quote);
        quoteList.add(quote2);
        quoteList.add(quote3);

        List<Integer> quoteStatusList = Arrays.asList(new Integer[]{QuoteStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});

        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1))).willReturn(Optional.of(startFiscalPeriod));

        given(quoteLoadService.findQuotesBySite(100, quoteStatusList, loadStatusList, startFiscalPeriod.getFirstDayOfWeek().atStartOfDay(), startFiscalPeriod.getLastDayOfWeek().atStartOfDay().plusDays(1))).willReturn(Optional.of(quoteList));

        MvcResult result = mvc.perform(get(Paths.API_VERSION + Paths.LOAD_SHIPMENT_SUMMARY + "?units=days&fiscalWeek=1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title", is("September 2019 Week 1")))
                .andExpect(jsonPath("$.data[0].shipmentData[0].fiscalMonth", is(9)))
                .andExpect(jsonPath("$.data[0].shipmentData[0].fiscalWeek", is(1)))
                .andExpect(jsonPath("$.data[0].shipmentData[1].fiscalWeek", is(1)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }
}

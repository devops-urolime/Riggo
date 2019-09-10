package io.riggo.web;

import io.riggo.data.domain.*;
import io.riggo.data.services.FiscalPeriodService;
import io.riggo.data.services.InvoiceLoadService;
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
    private InvoiceLoadService invoiceLoadService;

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

        InvoiceLoad invoice = new InvoiceLoad();
        invoice.setQuoteDate(LocalDateTime.of(2019, 5, 2, 0, 0,0 ));
        invoice.setNetFreightCharges(new BigDecimal(11));

        InvoiceLoad invoice2 = new InvoiceLoad();
        invoice2.setQuoteDate(LocalDateTime.of(2019, 6, 2, 0, 0,0 ));
        invoice2.setNetFreightCharges(new BigDecimal(22));

        InvoiceLoad invoice3 = new InvoiceLoad();
        invoice3.setQuoteDate(LocalDateTime.of(2019, 7, 2, 0, 0,0 ));
        invoice3.setNetFreightCharges(new BigDecimal(22));

        List<InvoiceLoad> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);
        invoiceList.add(invoice2);
        invoiceList.add(invoice3);

        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});

        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(3))).willReturn(Optional.of(startFiscalPeriod));
        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1))).willReturn(Optional.of(endFiscalPeriod));

        given(invoiceLoadService.findInvoicesBySite(100, invoiceStatusList, loadStatusList, startFiscalPeriod.getFirstDayOfQuarter().atStartOfDay(), endFiscalPeriod.getLastDayOfQuarter().atStartOfDay().plusDays(1))).willReturn(Optional.of(invoiceList));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_SHIPMENT_SUMMARY)
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
        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_SHIPMENT_SUMMARY)
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

        InvoiceLoad invoice = new InvoiceLoad();
        invoice.setQuoteDate(LocalDateTime.of(2019, 9, 1, 0, 0,0 ));
        invoice.setNetFreightCharges(new BigDecimal(11));

        InvoiceLoad invoice2 = new InvoiceLoad();
        invoice2.setQuoteDate(LocalDateTime.of(2019, 9, 2, 0, 0,0 ));
        invoice2.setNetFreightCharges(new BigDecimal(22));

        InvoiceLoad invoice3 = new InvoiceLoad();
        invoice3.setQuoteDate(LocalDateTime.of(2019, 9, 3, 0, 0,0 ));
        invoice3.setNetFreightCharges(new BigDecimal(22));

        List<InvoiceLoad> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);
        invoiceList.add(invoice2);
        invoiceList.add(invoice3);

        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});

        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1))).willReturn(Optional.of(startFiscalPeriod));

        given(invoiceLoadService.findInvoicesBySite(100, invoiceStatusList, loadStatusList, startFiscalPeriod.getFirstDayOfMonth().atStartOfDay(), startFiscalPeriod.getLastDayOfMonth().atStartOfDay().plusDays(1))).willReturn(Optional.of(invoiceList));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_SHIPMENT_SUMMARY + "?units=weeks")
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

        InvoiceLoad invoice = new InvoiceLoad();
        invoice.setQuoteDate(LocalDateTime.of(2019, 9, 1, 0, 0,0 ));
        invoice.setNetFreightCharges(new BigDecimal(11));
        invoice.setDistanceMiles(new BigDecimal(12.12));

        InvoiceLoad invoice2 = new InvoiceLoad();
        invoice2.setQuoteDate(LocalDateTime.of(2019, 9, 2, 0, 0,0 ));
        invoice2.setNetFreightCharges(new BigDecimal(22));
        invoice2.setDistanceMiles(new BigDecimal(12.12));

        InvoiceLoad invoice3 = new InvoiceLoad();
        invoice3.setQuoteDate(LocalDateTime.of(2019, 9, 3, 0, 0,0 ));
        invoice3.setNetFreightCharges(new BigDecimal(22));
        invoice3.setDistanceMiles(new BigDecimal(12.12));

        List<InvoiceLoad> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);
        invoiceList.add(invoice2);
        invoiceList.add(invoice3);

        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});

        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1))).willReturn(Optional.of(startFiscalPeriod));

        given(invoiceLoadService.findInvoicesBySite(100, invoiceStatusList, loadStatusList, startFiscalPeriod.getFirstDayOfWeek().atStartOfDay(), startFiscalPeriod.getLastDayOfWeek().atStartOfDay().plusDays(1))).willReturn(Optional.of(invoiceList));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_SHIPMENT_SUMMARY + "?units=days&fiscalWeek=1")
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

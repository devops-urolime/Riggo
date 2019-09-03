package io.riggo.web;

import io.riggo.data.domain.FiscalPeriod;
import io.riggo.data.domain.Invoice;
import io.riggo.data.domain.InvoiceStatus;
import io.riggo.data.domain.LoadSubStatus;
import io.riggo.data.services.FiscalPeriodService;
import io.riggo.data.services.InvoiceService;
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
    private InvoiceService invoiceService;

    @MockBean
    private FiscalPeriodService fiscalPeriodService;


    private static Logger logger = LoggerFactory.getLogger(ShipmentSummaryControllerTest.class);

    @WithMockUser(value = "spring", authorities = {"read:invoice"})
    @Test
    public void getSummaryHappyPathSuperAdmin() throws Exception {

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

        Invoice invoice = new Invoice();
        invoice.setQuoteDate(LocalDateTime.of(2019, 5, 2, 0, 0,0 ));
        invoice.setNetFreightCharges(new BigDecimal(11));

        Invoice invoice2 = new Invoice();
        invoice2.setQuoteDate(LocalDateTime.of(2019, 6, 2, 0, 0,0 ));
        invoice2.setNetFreightCharges(new BigDecimal(22));

        Invoice invoice3 = new Invoice();
        invoice3.setQuoteDate(LocalDateTime.of(2019, 7, 2, 0, 0,0 ));
        invoice3.setNetFreightCharges(new BigDecimal(22));

        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);
        invoiceList.add(invoice2);
        invoiceList.add(invoice3);

        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});


        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);

        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(3))).willReturn(Optional.of(startFiscalPeriod));
        given(fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1))).willReturn(Optional.of(endFiscalPeriod));

        given(invoiceService.findInvoicesBySite(100, invoiceStatusList, loadStatusList, startFiscalPeriod.getFirstDayOfQuarter(), endFiscalPeriod.getLastDayOfQuarter().plusDays(1))).willReturn(Optional.of(invoiceList));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_SHIPMENT_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                //.andExpect(jsonPath("$.data[0].subStatuses[0].id", is(loadPipeline.getId())))
                //.andExpect(jsonPath("$.data[0].subStatuses[0].count", is(loadPipeline.getCount())))
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
}

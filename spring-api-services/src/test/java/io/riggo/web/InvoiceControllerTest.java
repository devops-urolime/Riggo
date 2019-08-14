package io.riggo.web;

import io.riggo.data.domain.Invoice;
import io.riggo.data.domain.Load;
import io.riggo.data.services.InvoiceService;
import io.riggo.data.services.LoadService;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserPostPutInvoice;
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

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    private static Logger logger = LoggerFactory.getLogger(InvoiceControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoadService loadService;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private SalesforceRevenovaRequestBodyParserPostPutInvoice salesforceRevenovaRequestBodyParserPostPutInvoice;

    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void postInvoiceHappyPath() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);

        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/invoice")
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
    public void postInvoiceUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void postInvoicedRequireWriteLoadInvvoicePermission() throws Exception {
        mvc.perform(post(Paths.API_VERSION + "/load/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void postInvoiceExistingInvoiceConflict() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);

        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.of(invoice));
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void postInvoiceUnrecognizedLoad() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadExtSysId("1");
        invoice.setLoadId(1);

        Load load = new Load();
        load.setId(1);

        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(loadService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void postInvoiceWithParamHappyPath() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);

        Load load = new Load();
        load.setId(1);

        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(loadService.findById(1)).willReturn(java.util.Optional.of(load));
        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/1/invoice")
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
    public void postInvoiceWithParamUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/1/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void postInvoicedWithRequireWriteLoadInvvoicePermission() throws Exception {
        mvc.perform(post(Paths.API_VERSION + "/load/1/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void postInvoiceWithParamExistingInvoiceConflict() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);


        Load load = new Load();
        load.setId(1);

        given(loadService.findById(1)).willReturn(java.util.Optional.of(load));
        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.of(invoice));
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/1/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void postInvoiceWithParamUnrecognizedLoad() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadExtSysId("1");
        invoice.setLoadId(1);

        Load load = new Load();
        load.setId(1);

        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(loadService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(post(Paths.API_VERSION + "/load/1/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }



    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void putInvoiceHappyPath() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);

        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.of(invoice));
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/invoice")
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
    public void putInvoiceUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void putInvoicedRequireWriteLoadInvvoicePermission() throws Exception {
        mvc.perform(put(Paths.API_VERSION + "/load/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void putInvoiceNotFound() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);

        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void putInvoiceUnrecognizedLoad() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadExtSysId("1");
        invoice.setLoadId(1);

        Load load = new Load();
        load.setId(1);

        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(loadService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void putInvoiceWithParamHappyPath() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);

        Load load = new Load();
        load.setId(1);

        given(loadService.findById(1)).willReturn(java.util.Optional.of(load));
        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.of(invoice));
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/1/invoice")
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
    public void putInvoiceWithParamUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/1/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring")
    @Test
    public void putInvoicedWithRequireWriteLoadInvvoicePermission() throws Exception {
        mvc.perform(put(Paths.API_VERSION + "/load/1/invoice")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void putInvoiceWithParamInvoiceNotFound() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadId(1);


        Load load = new Load();
        load.setId(1);

        given(loadService.findById(1)).willReturn(java.util.Optional.of(load));
        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(invoiceService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/1/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring", authorities = {"write:loadInvoice"})
    @Test
    public void putInvoiceWithParamUnrecognizedLoad() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setExtSysId("1");
        invoice.setLoadExtSysId("1");
        invoice.setLoadId(1);

        Load load = new Load();
        load.setId(1);

        given(invoiceService.findById(1)).willReturn(java.util.Optional.of(invoice));
        given(loadService.findByExtSysId("1")).willReturn(java.util.Optional.empty());
        given(salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(any(Map.class))).willReturn(invoice);

        MvcResult result = mvc.perform(put(Paths.API_VERSION + "/load/1/invoice")
                .content(jsonPostLoadFor200)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                ;
        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    private static String jsonPostLoadFor200 = "{\n" +
            "\t\"Invoice\" : {\n" +
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
package io.riggo.web;

import io.riggo.data.domain.Invoice;
import io.riggo.data.domain.Load;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceAlreadyExistsException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.InvoiceService;
import io.riggo.data.services.LoadService;
import io.riggo.web.integration.SalesforceRevenovaConstants;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserPostPutInvoice;
import io.riggo.web.response.BaseAPIResponse;
import org.apache.commons.lang3.StringUtils;
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
public class InvoiceController {

    Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private LoadService loadService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SalesforceRevenovaRequestBodyParserPostPutInvoice salesforceRevenovaRequestBodyParserPostPutInvoice;

    @Autowired
    private AuthenticationFacade authenticationFacade;


    @PutMapping(value = "/load/invoice", produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Invoice> putInvoice(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        List<Invoice> invoiceList = processPutInvoice(dataHashMap);
        BaseAPIResponse baseApiResponse = new BaseAPIResponse();
        baseApiResponse.setData(invoiceList);
        return baseApiResponse;
    }


    @PutMapping(value = "/load/{loadId}/invoice", produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Invoice> putInvoiceWithLoadId(@PathVariable final Integer loadId, @RequestBody Map<String, Object> dataHashMap)  throws ResourceNotFoundException{
        validateLoad(loadId, authenticationFacade.getSiteId());
        List<Invoice> invoiceList = processPutInvoice(dataHashMap);

        BaseAPIResponse baseApiResponse = new BaseAPIResponse();
        baseApiResponse.setData(invoiceList);
        return baseApiResponse;
    }


    private List<Invoice> processPutInvoice(Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        List<Invoice> invoiceList = salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(dataHashMap);
        for(Invoice invoice : invoiceList) {
            Optional<Invoice> invoiceFromDb = invoiceService.findByExtSysId(invoice.getExtSysId());
            validateLoadExists(invoice, authenticationFacade.getSiteId());
            if (invoiceFromDb.isPresent()) {
                BeanUtils.copyProperties(invoice, invoiceFromDb.get(), SalesforceRevenovaConstants.POST_PUT_INVOICE_IGNORE_PROPERTIES);
                invoiceService.save(invoiceFromDb.get());
            }else{
                invoiceService.save(invoice);
            }
        }
        return invoiceList;
    }


    private void validateLoad(Integer loadId, Integer siteId) throws ResourceNotFoundException{
        Optional<Load> checkLoadExists = loadService.findById(loadId, siteId);
        if (!checkLoadExists.isPresent()) {
            throw new ResourceNotFoundException(ResourceType.LOAD, loadId);
        }
    }


    private void validateLoadExists(Invoice invoice, Integer siteId) throws ResourceNotFoundException{
        if(StringUtils.isNotBlank(invoice.getLoadExtSysId())) {
            Optional<Load> load = loadService.findByExtSysId(invoice.getLoadExtSysId(), siteId);
            if(load.isPresent() && load.get().getId() != null) {
                invoice.setLoadId(load.get().getId());
            }else{
                throw new ResourceNotFoundException(ResourceType.LOAD, invoice.getLoadExtSysId());
            }
        }
    }

}

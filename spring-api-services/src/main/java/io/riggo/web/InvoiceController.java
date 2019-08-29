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


    @PostMapping(path = "/load/invoice", produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Invoice> postInvoice( @RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        List<Invoice> invoiceList = processPostAPI(dataHashMap, true);
        BaseAPIResponse baseApiResponse = new BaseAPIResponse();
        baseApiResponse.setData(invoiceList);
        return baseApiResponse;
    }


    @PostMapping(path = "/load/{loadId}/invoice", produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Invoice> postInvoiceWithLoadId(@PathVariable final Integer loadId, @RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        validateLoad(loadId, authenticationFacade.getSiteId());
        return postInvoice(dataHashMap);  //will validate load twice
    }


    private List<Invoice> processPostAPI(Map<String, Object> dataHashMap, boolean validateLoad) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        List<Invoice> invoiceList = salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(dataHashMap);
        for(Invoice invoice : invoiceList) {
            validateInvoiceDoesNotAlreadyExist(invoice.getExtSysId());
            if (validateLoad) {
                validateLoadExists(invoice, authenticationFacade.getSiteId());
            }
            invoiceService.save(invoice);
        }
        return invoiceList;
    }


    @PutMapping(value = "/load/invoice", produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Invoice> putInvoice(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        List<Invoice> invoiceList = processPutInvoice(dataHashMap, false);
        BaseAPIResponse baseApiResponse = new BaseAPIResponse();
        baseApiResponse.setData(invoiceList);
        return baseApiResponse;
    }


    @PutMapping(value = "/load/{loadId}/invoice", produces = "application/json")
    @PreAuthorize("hasAuthority('write:loadInvoice')")
    public BaseAPIResponse<Invoice> putInvoiceWithLoadId(@PathVariable final Integer loadId, @RequestBody Map<String, Object> dataHashMap)  throws ResourceNotFoundException{
        validateLoad(loadId, authenticationFacade.getSiteId());
        List<Invoice> invoiceList = processPutInvoice(dataHashMap, false);

        BaseAPIResponse baseApiResponse = new BaseAPIResponse();
        baseApiResponse.setData(invoiceList);
        return baseApiResponse;
    }


    private List<Invoice> processPutInvoice(Map<String, Object> dataHashMap, boolean validateLoad) throws ResourceNotFoundException{
        List<Invoice> invoiceList = salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(dataHashMap);
        for(Invoice invoice : invoiceList) {
            Invoice invoiceFromDb = validateInvoiceExists(invoice.getExtSysId());

            if (validateLoad) {
                validateLoadExists(invoice, authenticationFacade.getSiteId());
            }

            BeanUtils.copyProperties(invoice, invoiceFromDb, SalesforceRevenovaConstants.POST_PUT_INVOICE_IGNORE_PROPERTIES);
            invoiceService.save(invoiceFromDb);
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


    private Invoice validateInvoiceExists(String extSysId) throws ResourceNotFoundException{
        Optional<Invoice> invoiceFromDb = invoiceService.findByExtSysId(extSysId);
        if (!invoiceFromDb.isPresent()) {
            throw new ResourceNotFoundException(ResourceType.INVOICE, extSysId);
        }
        return invoiceFromDb.get();
    }


    private void validateInvoiceDoesNotAlreadyExist(String extSysId) throws ResourceAlreadyExistsException{
        Optional<Invoice> invoiceFromDb = invoiceService.findByExtSysId(extSysId);
        if (invoiceFromDb.isPresent()) {
            throw new ResourceAlreadyExistsException(ResourceType.INVOICE, extSysId);
        }
    }
}

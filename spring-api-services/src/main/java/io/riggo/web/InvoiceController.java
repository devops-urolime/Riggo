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
import org.springframework.web.bind.annotation.*;

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


    @PostMapping(path = "/load/invoice", produces = "application/json")
    public BaseAPIResponse<Invoice> postInvoice( @RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        return processPostAPI(dataHashMap, true);
    }


    @PostMapping(path = "/load/{loadId}/invoice", produces = "application/json")
    public BaseAPIResponse<Invoice> postInvoiceWithLoadId(@PathVariable final Integer loadId, @RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        validateLoad(loadId);
        return postInvoice(dataHashMap);  //will validate load twice
    }


    private BaseAPIResponse<Invoice> processPostAPI(Map<String, Object> dataHashMap, boolean validateLoad) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        Invoice invoice = salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(dataHashMap);
        validateInvoiceDoesNotAlreadyExist(invoice.getExtSysId());
        if(validateLoad) {
            validateLoadExists(invoice);
        }
        invoiceService.save(invoice);

        return new BaseAPIResponse(invoice);
    }


    @PutMapping(value = "/load/invoice", produces = "application/json")
    public BaseAPIResponse<Invoice> putInvoice(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        return processPutInvoice(dataHashMap, false);
    }


    @PutMapping(value = "/load/{loadId}/invoice", produces = "application/json")
    public BaseAPIResponse<Invoice> putInvoiceWithLoadId(@PathVariable final Integer loadId, @RequestBody Map<String, Object> dataHashMap)  throws ResourceNotFoundException{
        validateLoad(loadId);
        return processPutInvoice(dataHashMap, false);
    }


    private BaseAPIResponse<Invoice> processPutInvoice(Map<String, Object> dataHashMap, boolean validateLoad) throws ResourceNotFoundException{
        Invoice invoice = salesforceRevenovaRequestBodyParserPostPutInvoice.resolveInvoice(dataHashMap);
        Invoice invoiceFromDb = validateInvoiceExists(invoice.getExtSysId());

        if(validateLoad) {
            validateLoadExists(invoice);
        }

        BeanUtils.copyProperties(invoice, invoiceFromDb, SalesforceRevenovaConstants.POST_PUT_INVOICE_IGNORE_PROPERTIES);
        invoiceService.save(invoiceFromDb);

        return new BaseAPIResponse(invoiceFromDb);
    }



    private void validateLoad(Integer loadId) throws ResourceNotFoundException{
        Optional<Load> checkLoadExists = loadService.findById(loadId);
        if (!checkLoadExists.isPresent()) {
            throw new ResourceNotFoundException(ResourceType.LOAD, loadId);
        }
    }

    private void validateLoadExists(Invoice invoice) throws ResourceNotFoundException{
        if(StringUtils.isNotBlank(invoice.getLoadExtSysId())) {
            Optional<Load> load = loadService.findByExtSysId(invoice.getLoadExtSysId());
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

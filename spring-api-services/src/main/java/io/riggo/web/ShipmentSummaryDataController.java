package io.riggo.web;

import io.riggo.data.domain.FiscalPeriod;
import io.riggo.data.domain.Invoice;
import io.riggo.data.domain.InvoiceStatus;
import io.riggo.data.domain.LoadSubStatus;
import io.riggo.data.exception.BadRequestException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.FiscalPeriodService;
import io.riggo.data.services.InvoiceService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.ShipmentSummaryData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = Paths.API_VERSION)
public class ShipmentSummaryDataController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private FiscalPeriodService fiscalPeriodService;

    @GetMapping(value = Paths.LOAD_SHIPMENT_SUMMARY, produces = "application/json")
    @ResponseBody
    public BaseAPIResponse<ShipmentSummaryData> getShipmentSummary(
            @RequestParam(required = false, name = "offset", value = "0") Integer offset,
            @RequestParam(required = false, name = "units", value = "month") String units,
            @RequestParam(required = false, name = "fiscalYear", value = "0") Integer fiscalYear,
            @RequestParam(required = false, name = "fiscalMonth", value = "0") Integer fiscalMonth,
            @RequestParam(required = false, name = "fiscalWeek", value = "0") Integer fiscalWeek
    )
            throws ResourceNotFoundException, BadRequestException {
        if(fiscalYear == 0){ fiscalYear = LocalDate.now().getYear(); }
        if(fiscalMonth == 0){ fiscalMonth = LocalDate.now().getMonthValue(); }
        if(fiscalWeek == 0){ fiscalWeek = LocalDate.now().getDayOfMonth()%7; }

        Optional<List<Invoice>> invoices = null;
        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});
        LocalDate startDate = null;
        LocalDate endDate = null;

        if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.MONTHS.getDisplayName()))
        {
            Optional<FiscalPeriod> priorFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1));
            Optional<FiscalPeriod> fiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(3));
            if(fiscalPeriod.isPresent() && priorFiscalPeriod.isPresent()) {
                startDate = priorFiscalPeriod.get().getFirstDayOfQuarter();
                endDate = fiscalPeriod.get().getLastDayOfQuarter();
            }
        }
        else if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.WEEKS.getDisplayName()))
        {
            Optional<FiscalPeriod> fiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1));
            if(fiscalPeriod.isPresent()) {
                startDate = fiscalPeriod.get().getFirstDayOfMonth();
                endDate = fiscalPeriod.get().getLastDayOfMonth();
            }
        }
        else if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.DAYS.getDisplayName()))
        {
            Optional<FiscalPeriod> fiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, fiscalWeek*7).minusMonths(offset*1));
            if(fiscalPeriod.isPresent()) {
                startDate = fiscalPeriod.get().getFirstDayOfWeek();
                endDate = fiscalPeriod.get().getLastDayOfWeek();
            }
        }

        if(startDate != null && endDate != null)
        {
            if(authenticationFacade.isSuperAdmin() || authenticationFacade.isSiteAdmin() ) {
                invoices = invoiceService.findInvoicesBySite(authenticationFacade.getSiteId(), invoiceStatusList, loadStatusList, startDate, endDate);
            }
            else if(authenticationFacade.isShipperExecutive()) {
                invoices = invoiceService.findInvoicesBySiteUser(authenticationFacade.getSiteId(), authenticationFacade.getUsername(), invoiceStatusList, loadStatusList, startDate, endDate);
            }
        }else{
            throw new BadRequestException();
        }

        if(invoices.isPresent()){

        }

        Optional<List<ShipmentSummaryData>> shipmentSummaryDataList = null;
        if (shipmentSummaryDataList != null && shipmentSummaryDataList.isPresent()) {
        }
        return null;
    }
}
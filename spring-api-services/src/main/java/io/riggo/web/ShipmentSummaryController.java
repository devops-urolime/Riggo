package io.riggo.web;

import io.riggo.data.domain.*;
import io.riggo.data.exception.BadRequestException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.FiscalPeriodService;
import io.riggo.data.services.InvoiceLoadService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.ShipmentVizData;
import io.riggo.web.response.ShipmentVizDataContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = Paths.API_VERSION)
public class ShipmentSummaryController {

    @Autowired
    private InvoiceLoadService invoiceLoadService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private FiscalPeriodService fiscalPeriodService;

    @GetMapping(value = Paths.LOAD_SHIPMENT_SUMMARY, produces = "application/json")
    @ResponseBody
    public BaseAPIResponse<ShipmentVizDataContainer> getShipmentSummary(
            @RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset,
            @RequestParam(required = false, name = "units", defaultValue = "months") String units,
            @RequestParam(required = false, name = "fiscalYear", defaultValue = "0") Integer fiscalYear,
            @RequestParam(required = false, name = "fiscalMonth", defaultValue = "0") Integer fiscalMonth,
            @RequestParam(required = false, name = "fiscalWeek", defaultValue = "0") Integer fiscalWeek
    )
            throws ResourceNotFoundException, BadRequestException {
        LocalDate now = LocalDate.now();
        if(fiscalYear == 0){ fiscalYear = now.getYear(); }
        if(fiscalMonth == 0){ fiscalMonth = now.getMonthValue(); }
        if(fiscalWeek == 0){ fiscalWeek = (((now.getDayOfMonth()-1)%7)+1); }
        Integer currentDayOfMonth = now.getDayOfMonth();

        Optional<List<InvoiceLoad>> invoices = null;
        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});
        LocalDate startDate = null;
        LocalDate endDate = null;

        Optional<FiscalPeriod> startFiscalPeriod = null;
        Optional<FiscalPeriod> endFiscalPeriod = null;
        String vizTitle = "";

        if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.MONTHS.getDisplayName()))
        {
            startFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(3));
            endFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1));
            if(startFiscalPeriod.isPresent() && endFiscalPeriod.isPresent()) {
                startDate = startFiscalPeriod.get().getFirstDayOfQuarter();
                endDate = endFiscalPeriod.get().getLastDayOfQuarter().plusDays(1);
                vizTitle = startFiscalPeriod.get().getFiscalQuarter() + " - " + endFiscalPeriod.get().getFiscalQuarter();
            }
        }
        else if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.WEEKS.getDisplayName()))
        {
            startFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1));
            if(startFiscalPeriod.isPresent()) {
                startDate = startFiscalPeriod.get().getFirstDayOfMonth();
                endDate = startFiscalPeriod.get().getLastDayOfMonth().plusDays(1);
                vizTitle = startFiscalPeriod.get().getMonthName() + " " +  startFiscalPeriod.get().getYear_actual();
            }
        }
        else if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.DAYS.getDisplayName()))
        {
            startFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, ((fiscalWeek-1)*7)+1));
            if(startFiscalPeriod.isPresent()) {
                startDate = startFiscalPeriod.get().getFirstDayOfWeek();
                endDate = startFiscalPeriod.get().getLastDayOfWeek().plusDays(1);
                vizTitle = startFiscalPeriod.get().getMonthName() + " " + startFiscalPeriod.get().getYear_actual() + " Week " + fiscalWeek;
            }
        }

        if(startDate != null && endDate != null)
        {
            if(authenticationFacade.isSuperAdmin() || authenticationFacade.isSiteAdmin() ) {
                invoices = invoiceLoadService.findInvoicesBySite(authenticationFacade.getSiteId(), invoiceStatusList, loadStatusList, startDate, endDate);
            }
            else if(authenticationFacade.isShipperExecutive()) {
                invoices = invoiceLoadService.findInvoicesBySiteUser(authenticationFacade.getSiteId(), authenticationFacade.getUsername(), invoiceStatusList, loadStatusList, startDate, endDate);
            }
        }else{
            throw new BadRequestException();
        }



        if (invoices != null && invoices.isPresent()) {
            ShipmentVizDataContainer shipmentVizDataContainer = new ShipmentVizDataContainer();
            List<ShipmentVizData> shipmentVizDataList = new ArrayList<>();
            shipmentVizDataContainer.setTitle(vizTitle);
            shipmentVizDataContainer.setUnits(units);

            if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.MONTHS.getDisplayName())) {
                for(int i = 0; i < 6; i++) {
                    LocalDate periodStartDate = startFiscalPeriod.get().getFirstDayOfQuarter().plusMonths(i);
                    LocalDate periodEndDate = startFiscalPeriod.get().getFirstDayOfQuarter().plusMonths(i+1);
                    if(periodStartDate.isBefore(endDate)) {

                        ShipmentVizData shipmentVizData = new ShipmentVizData();
                        shipmentVizData.setLabel(periodStartDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + periodStartDate.getYear());

                        populateShipmentVizData(shipmentVizData, periodStartDate.minusDays(1).atTime(23, 59, 59, 999999999), periodEndDate.atTime(0,0,0), invoices, periodStartDate.getMonthValue(), 0, offset);
                        shipmentVizDataList.add(shipmentVizData);
                    }
                }
                shipmentVizDataContainer.setShipmentData(shipmentVizDataList);
            }
            if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.WEEKS.getDisplayName())) {
                for(int i = 0; i < 5; i++) {
                    LocalDate periodStartDate = startFiscalPeriod.get().getFirstDayOfMonth().plusWeeks(i);
                    LocalDate periodEndDate = startFiscalPeriod.get().getFirstDayOfMonth().plusWeeks(i+1);
                    if(periodEndDate.getMonthValue() > periodStartDate.getMonthValue())
                    {
                        periodEndDate = periodEndDate.minusDays(periodEndDate.getDayOfMonth()-1);
                    }
                    if(periodStartDate.isBefore(endDate)) {
                        ShipmentVizData shipmentVizData = new ShipmentVizData();
                        shipmentVizData.setLabel("Week " + (i+1));
                        shipmentVizData.setFiscalMonth(periodStartDate.getMonthValue());
                        shipmentVizData.setFiscalYear(periodStartDate.getYear());

                        populateShipmentVizData(shipmentVizData, periodStartDate.minusDays(1).atTime(23, 59, 59, 999999999), periodEndDate.atTime(0,0,0), invoices, periodStartDate.getMonthValue(), i+1, offset);
                        shipmentVizDataList.add(shipmentVizData);
                    }
                }
            }
            if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.DAYS.getDisplayName())) {
                for(int i = 0; i < 7; i++) {
                    LocalDate periodStartDate = startFiscalPeriod.get().getFirstDayOfWeek().plusDays(i);
                    LocalDate periodEndDate = startFiscalPeriod.get().getFirstDayOfWeek().plusDays(i+1);
                    if(periodStartDate.isBefore(endDate)) {
                        ShipmentVizData shipmentVizData = new ShipmentVizData();
                        shipmentVizData.setLabel(periodStartDate.format(DateTimeFormatter.ofPattern("MM/dd/YYYY")));

                        populateShipmentVizData(shipmentVizData, periodStartDate.minusDays(1).atTime(23, 59, 59, 999999999), periodEndDate.atTime(0,0,0), invoices, periodStartDate.getMonthValue(), fiscalWeek, offset);
                        shipmentVizDataList.add(shipmentVizData);
                    }
                }
            }
            shipmentVizDataContainer.setShipmentData(shipmentVizDataList);
            BaseAPIResponse<ShipmentVizDataContainer> shipmentVizDataContainerBaseAPIResponse = new BaseAPIResponse<>();
            shipmentVizDataContainerBaseAPIResponse.addData(shipmentVizDataContainer);
            return shipmentVizDataContainerBaseAPIResponse;
        }
        throw new ResourceNotFoundException(ResourceType.SHIPMENT_SUMMARY, 0);
    }

    private void populateShipmentVizData(ShipmentVizData shipmentVizData, LocalDateTime periodStartDate, LocalDateTime periodEndDate, Optional<List<InvoiceLoad>> invoices, Integer fiscalMonth, Integer fiscalWeek, Integer offset){
        shipmentVizData.setFiscalMonth(periodStartDate.getMonthValue()+1);
        shipmentVizData.setFiscalYear(periodStartDate.getYear());
        shipmentVizData.setFiscalWeek(fiscalWeek);
        shipmentVizData.setOffset(offset);

        Long shipments = invoices.get().stream()
                .filter(invoice ->
                        invoice.getQuoteDate().isAfter(ChronoLocalDateTime.from(periodStartDate)) &&
                                invoice.getQuoteDate().isBefore(ChronoLocalDateTime.from(periodEndDate))
                ).count();

        Double totalFreightCharges = invoices.get().stream()
                .filter(invoice ->
                        invoice.getQuoteDate().isAfter(ChronoLocalDateTime.from(periodStartDate)) &&
                                invoice.getQuoteDate().isBefore(ChronoLocalDateTime.from(periodEndDate))
                ).collect(Collectors.summingDouble(InvoiceLoad::getNetFreightChargesDoubleValue));

        Double totalMiles = invoices.get().stream()
                .filter(invoice ->
                        invoice.getQuoteDate().isAfter(ChronoLocalDateTime.from(periodStartDate)) &&
                                invoice.getQuoteDate().isBefore(ChronoLocalDateTime.from(periodEndDate))
                ).collect(Collectors.summingDouble(InvoiceLoad::getDistanceMilesDoubleValue));

        shipmentVizData.setShipments(shipments.intValue());
        shipmentVizData.setTotalCost(new BigDecimal(totalFreightCharges));
        shipmentVizData.setTotalMiles(new BigDecimal(totalMiles));
        if(totalMiles > 0.0)
        {
            shipmentVizData.setCostPerMile(new BigDecimal(totalFreightCharges/totalMiles));
        }
    }
}
package io.riggo.web;

import io.riggo.data.domain.*;
import io.riggo.data.exception.BadRequestException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.FiscalPeriodService;
import io.riggo.data.services.QuoteLoadService;
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
    private QuoteLoadService quoteLoadService;

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
        if(fiscalWeek <= 0){ fiscalWeek = 1; }
        if(fiscalWeek > 5){ fiscalWeek = 5; }

        Optional<List<QuoteLoad>> quotes = null;
        List<Integer> quoteStatusList = Arrays.asList(new Integer[]{QuoteStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        Optional<FiscalPeriod> startFiscalPeriod = null;
        Optional<FiscalPeriod> endFiscalPeriod;
        String vizTitle = "";

        if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.MONTHS.getDisplayName()))
        {
            startFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(3*(offset+1)));
            endFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(3*offset));
            if(startFiscalPeriod.isPresent() && endFiscalPeriod.isPresent()) {
                startDate = startFiscalPeriod.get().getFirstDayOfQuarter().atStartOfDay();
                endDate = endFiscalPeriod.get().getLastDayOfQuarter().plusDays(1).atStartOfDay();
                vizTitle = StringUtils.trim(startFiscalPeriod.get().getFiscalQuarter()) + " - " + StringUtils.trim(endFiscalPeriod.get().getFiscalQuarter());
            }
        }
        else if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.WEEKS.getDisplayName()))
        {
            startFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, 1).minusMonths(offset));
            if(startFiscalPeriod.isPresent()) {
                startDate = startFiscalPeriod.get().getFirstDayOfMonth().atStartOfDay();
                endDate = startFiscalPeriod.get().getLastDayOfMonth().plusDays(1).atStartOfDay();
                vizTitle = StringUtils.trim(startFiscalPeriod.get().getMonthName()) + " " +  StringUtils.trim(startFiscalPeriod.get().getYear_actual().toString());
            }
        }
        else if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.DAYS.getDisplayName()))
        {
            startFiscalPeriod = fiscalPeriodService.findByDateActual(LocalDate.of(fiscalYear, fiscalMonth, ((fiscalWeek-1)*7)+1));
            if(startFiscalPeriod.isPresent()) {
                startDate = startFiscalPeriod.get().getFirstDayOfMonth().plusDays(fiscalWeek <= 0 ? 0 : (fiscalWeek-1)*7 ).atStartOfDay();
                endDate = startDate.plusDays(7).minusNanos(1);
                vizTitle = StringUtils.trim(startFiscalPeriod.get().getMonthName()) + " " + StringUtils.trim(startFiscalPeriod.get().getYear_actual().toString()) + " Week " + fiscalWeek;
            }
        }

        if(startDate != null && endDate != null)
        {
            if(authenticationFacade.isSuperAdmin() || authenticationFacade.isSiteAdmin() || authenticationFacade.isMachine() ) {
                quotes = quoteLoadService.findQuotesBySite(authenticationFacade.getSiteId(), quoteStatusList, loadStatusList, startDate, endDate);
            }
            else if(authenticationFacade.isShipperExecutive()) {
                quotes = quoteLoadService.findQuotesBySiteUser(authenticationFacade.getSiteId(), authenticationFacade.getUsername(), quoteStatusList, loadStatusList, startDate, endDate);
            }
        }else{
            throw new BadRequestException();
        }



        ShipmentVizDataContainer shipmentVizDataContainer = new ShipmentVizDataContainer();
        List<ShipmentVizData> shipmentVizDataList = new ArrayList<>();
        shipmentVizDataContainer.setTitle(vizTitle);
        shipmentVizDataContainer.setUnits(units);

        if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.MONTHS.getDisplayName())) {
            for(int i = 0; i < 6; i++) {
                LocalDateTime periodStartDate = startFiscalPeriod.get().getFirstDayOfQuarter().plusMonths(i).atStartOfDay();
                LocalDateTime periodEndDate = startFiscalPeriod.get().getFirstDayOfQuarter().plusMonths(i+1).atStartOfDay();
                if(periodStartDate.isBefore(endDate)) {
                    ShipmentVizData shipmentVizData = new ShipmentVizData();
                    shipmentVizData.setLabel(periodStartDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + periodStartDate.getYear());

                    populateShipmentVizData(shipmentVizData, periodStartDate.minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999), periodEndDate, quotes, periodStartDate.getMonthValue(), 0, offset);
                    shipmentVizDataList.add(shipmentVizData);
                }
            }
            shipmentVizDataContainer.setShipmentData(shipmentVizDataList);
        }
        if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.WEEKS.getDisplayName())) {
            for(int i = 0; i < 5; i++) {
                LocalDateTime periodStartDate = startFiscalPeriod.get().getFirstDayOfMonth().atStartOfDay().plusWeeks(i);
                LocalDateTime periodEndDate = startFiscalPeriod.get().getFirstDayOfMonth().atStartOfDay().plusWeeks(i+1);
                if(periodEndDate.getMonthValue() > periodStartDate.getMonthValue())
                {
                    periodEndDate = periodEndDate.minusDays(periodEndDate.getDayOfMonth()-1);
                }
                if(periodStartDate.isBefore(endDate)) {
                    ShipmentVizData shipmentVizData = new ShipmentVizData();
                    shipmentVizData.setLabel("Week " + (i+1));
                    shipmentVizData.setFiscalYear(periodStartDate.getYear());

                    populateShipmentVizData(shipmentVizData, periodStartDate.minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999), periodEndDate, quotes, periodStartDate.getMonthValue(), i+1, offset);
                    shipmentVizData.setFiscalMonth(periodStartDate.getMonthValue());
                    shipmentVizDataList.add(shipmentVizData);
                }
            }
        }
        if(StringUtils.equalsIgnoreCase(units, ShipmentVizPeriod.DAYS.getDisplayName())) {
            for(int i = 0; i < 7; i++) {
                LocalDateTime periodStartDate = startDate.plusDays(i);
                LocalDateTime periodEndDate = startDate.plusDays(i+1).minusNanos(1);
                if(periodStartDate.getMonthValue() == startDate.getMonthValue()) {
                    ShipmentVizData shipmentVizData = new ShipmentVizData();
                    shipmentVizData.setLabel(periodStartDate.format(DateTimeFormatter.ofPattern("MM/dd")));

                    populateShipmentVizData(shipmentVizData, periodStartDate.minusNanos(1), periodEndDate, quotes, startDate.getMonthValue(), fiscalWeek, offset);
                    shipmentVizData.setFiscalMonth(startDate.getMonthValue());
                    shipmentVizDataList.add(shipmentVizData);
                }
            }
        }
        shipmentVizDataContainer.setShipmentData(shipmentVizDataList);
        BaseAPIResponse<ShipmentVizDataContainer> shipmentVizDataContainerBaseAPIResponse = new BaseAPIResponse<>();
        shipmentVizDataContainerBaseAPIResponse.addData(shipmentVizDataContainer);
        return shipmentVizDataContainerBaseAPIResponse;
    }

    private void populateShipmentVizData(ShipmentVizData shipmentVizData, LocalDateTime periodStartDate, LocalDateTime periodEndDate, Optional<List<QuoteLoad>> quotes, Integer fiscalMonth, Integer fiscalWeek, Integer offset){
        shipmentVizData.setFiscalMonth(periodStartDate.getMonthValue()+1);
        shipmentVizData.setFiscalYear(periodStartDate.getYear());
        shipmentVizData.setFiscalWeek(fiscalWeek);
        shipmentVizData.setOffset(offset);

        if(quotes != null && quotes.isPresent()) {
            Long shipments = quotes.get().stream()
                    .filter(quote ->
                            quote.getQuoteDate().isAfter(ChronoLocalDateTime.from(periodStartDate)) &&
                                    quote.getQuoteDate().isBefore(ChronoLocalDateTime.from(periodEndDate))
                    ).count();
            shipmentVizData.setShipments(shipments.intValue());

            Double totalFreightCharges = quotes.get().stream()
                    .filter(quote ->
                            quote.getQuoteDate().isAfter(ChronoLocalDateTime.from(periodStartDate)) &&
                                    quote.getQuoteDate().isBefore(ChronoLocalDateTime.from(periodEndDate))
                    ).collect(Collectors.summingDouble(QuoteLoad::getNetFreightChargesDoubleValue));
            shipmentVizData.setTotalCost(new BigDecimal(totalFreightCharges));

            Double totalMiles = quotes.get().stream()
                    .filter(quote ->
                            quote.getQuoteDate().isAfter(ChronoLocalDateTime.from(periodStartDate)) &&
                                    quote.getQuoteDate().isBefore(ChronoLocalDateTime.from(periodEndDate))
                    ).collect(Collectors.summingDouble(QuoteLoad::getDistanceMilesDoubleValue));
            shipmentVizData.setTotalMiles(new BigDecimal(totalMiles));

            if(totalMiles > 0.0){
                shipmentVizData.setCostPerMile(new BigDecimal(totalFreightCharges/totalMiles));
            }else{
                shipmentVizData.setCostPerMile(new BigDecimal(0));
            }
        }
        else{
            shipmentVizData.setShipments(0);
            shipmentVizData.setTotalCost(new BigDecimal(0));
            shipmentVizData.setTotalMiles(new BigDecimal(0));
            shipmentVizData.setCostPerMile(new BigDecimal(0));
        }
    }
}
package io.riggo.web.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class ShipmentVizData implements Serializable {

    private String label;
    private Integer shipments;
    private BigDecimal costPerMile;
    private BigDecimal totalCost;
    private BigDecimal totalMiles;
    private Integer fiscalMonth;
    private Integer fiscalYear;
    private Integer fiscalWeek;
    private Integer offset;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getShipments() {
        return shipments;
    }

    public void setShipments(Integer shipments) {
        this.shipments = shipments;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getCostPerMile() {
        return costPerMile;
    }

    public void setCostPerMile(BigDecimal costPerMile) {
        this.costPerMile = costPerMile;
    }

    public Integer getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(Integer fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Integer getFiscalWeek() {
        return fiscalWeek;
    }

    public void setFiscalWeek(Integer fiscalWeek) {
        this.fiscalWeek = fiscalWeek;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public BigDecimal getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(BigDecimal totalMiles) {
        this.totalMiles = totalMiles;
    }
}

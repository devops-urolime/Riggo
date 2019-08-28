package io.riggo.web.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class ShipmentSummarySubsetData implements Serializable {

    private String label;
    private Integer shipments;
    private BigDecimal costPerMile;

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

    public BigDecimal getCostPerMile() {
        return costPerMile;
    }

    public void setCostPerMile(BigDecimal costPerMile) {
        this.costPerMile = costPerMile;
    }
}

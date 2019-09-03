package io.riggo.web.response;

import java.io.Serializable;
import java.util.List;

public class ShipmentVizDataContainer implements Serializable {

    private String title;
    private String units;
    private List<ShipmentVizData> shipmentData;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public List<ShipmentVizData> getShipmentData() {
        return shipmentData;
    }

    public void setShipmentData(List<ShipmentVizData> shipmentData) {
        this.shipmentData = shipmentData;
    }
}
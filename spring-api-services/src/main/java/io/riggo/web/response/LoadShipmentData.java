package io.riggo.web.response;

import java.io.Serializable;
import java.util.List;

public class LoadShipmentData implements Serializable {

    private String date;
    private Integer fiscalMonth;
    private Integer fiscalYear;
    private Integer week;
    private Integer day;
    private List<ShipmentSummaryData> data;


}

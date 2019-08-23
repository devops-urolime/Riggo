package io.riggo.web.response;

import java.io.Serializable;
import java.util.List;

public class ShipmentSummaryData implements Serializable {

    private String date;
    private Integer fiscalMonth;
    private Integer fiscalYear;
    private Integer week;
    private List<ShipmentSummarySubsetData> data;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public List<ShipmentSummarySubsetData> getData() {
        return data;
    }

    public void setData(List<ShipmentSummarySubsetData> data) {
        this.data = data;
    }
}

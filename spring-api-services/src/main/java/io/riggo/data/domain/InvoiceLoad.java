package io.riggo.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")

public class InvoiceLoad implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    private Integer id;

    @Column(name = "ext_sys_id")
    private String extSysId;

    @Column(name = "load_id")
    private Integer loadId;

    @Column(name = "quote_date")
    private LocalDateTime quoteDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "comments")
    private String comments;

    @Column(name = "net_freight_charges")
    private BigDecimal netFreightCharges;

    @Column(name = "fuel_surcharge")
    private BigDecimal fuelSurcharge;

    @Column(name = "accessorial_charges")
    private BigDecimal accessorialCharges;

    @Column(name = "transportation_total")
    private BigDecimal transportationTotal;

    @Column(name = "customer_quote_total")
    private BigDecimal customerQuoteTotal;

    @Column(name = "distance_miles")
    private BigDecimal distanceMiles;


    public InvoiceLoad(){}

    public InvoiceLoad(Integer id, String extSysId, Integer loadId, LocalDateTime quoteDate, Integer status, String comments, BigDecimal netFreightCharges, BigDecimal fuelSurcharge, BigDecimal accessorialCharges, BigDecimal transportationTotal, BigDecimal customerQuoteTotal, BigDecimal distanceMiles) {
        this.id = id;
        this.extSysId = extSysId;
        this.loadId = loadId;
        this.quoteDate = quoteDate;
        this.status = status;
        this.comments = comments;
        this.netFreightCharges = netFreightCharges;
        this.fuelSurcharge = fuelSurcharge;
        this.accessorialCharges = accessorialCharges;
        this.transportationTotal = transportationTotal;
        this.customerQuoteTotal = customerQuoteTotal;
        this.distanceMiles = distanceMiles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtSysId() {
        return extSysId;
    }

    public void setExtSysId(String extSysId) {
        this.extSysId = extSysId;
    }

    public Integer getLoadId() {
        return loadId;
    }

    public void setLoadId(Integer loadId) {
        this.loadId = loadId;
    }

    public LocalDateTime getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(LocalDateTime quoteDate) {
        this.quoteDate = quoteDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getNetFreightCharges() {
        return netFreightCharges;
    }

    public Double getNetFreightChargesDoubleValue() {
        return netFreightCharges == null ? 0.0 : netFreightCharges.doubleValue();
    }


    public void setNetFreightCharges(BigDecimal netFreightCharges) {
        this.netFreightCharges = netFreightCharges;
    }

    public BigDecimal getFuelSurcharge() {
        return fuelSurcharge;
    }

    public void setFuelSurcharge(BigDecimal fuelSurcharge) {
        this.fuelSurcharge = fuelSurcharge;
    }

    public BigDecimal getAccessorialCharges() {
        return accessorialCharges;
    }

    public void setAccessorialCharges(BigDecimal accessorialCharges) {
        this.accessorialCharges = accessorialCharges;
    }

    public BigDecimal getTransportationTotal() {
        return transportationTotal;
    }

    public void setTransportationTotal(BigDecimal transportationTotal) {
        this.transportationTotal = transportationTotal;
    }

    public BigDecimal getCustomerQuoteTotal() {
        return customerQuoteTotal;
    }

    public void setCustomerQuoteTotal(BigDecimal customerQuoteTotal) {
        this.customerQuoteTotal = customerQuoteTotal;
    }

    public BigDecimal getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(BigDecimal distanceMiles) {
        this.distanceMiles = distanceMiles;
    }

    public Double getDistanceMilesDoubleValue() {
        return distanceMiles == null ? 0.0 : distanceMiles.doubleValue();
    }

}


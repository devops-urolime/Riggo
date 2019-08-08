package io.riggo.data.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "invoice")

public class Invoice implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ext_sys_id")
    private String extSysId;

    @Column(name = "load_id")
    private String loadId;

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

    @Transient
    @Column(name = "created_at")
    private Date createdAt;

    @Transient
    @Column(name = "updated_at")
    private Date updatedAt;

    @Transient
    @Column(name = "deleted")
    private Date deleted;

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

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}

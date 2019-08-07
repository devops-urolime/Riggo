package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "load")
public class Load implements Serializable {


    private static final long serialVersionUID = -4133343645808769934L;
    private Integer id;
    private String extSysId;
    private String extSysTenantId;

    @Column(name = "expected_ship_date", columnDefinition = "DATE")
    private LocalDate expectedShipDate;

    private Integer carrier;
    private String transportMode;
    private BigDecimal postedRate;
    private BigDecimal insuranceAmt;
    private Integer equipmentTypeId;
    private Integer loadStatus;
    private Boolean teamReq;
    private Boolean foodGradeTrailerReq;
    private Boolean tempControlReq;
    private LocalDate expectedDeliveryDate;
    private Integer driver;
    private Boolean locationBasedSvcsReq;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
    private java.util.Date deleted;
    private String referenceNumber;
    private String billOfLadingNo;
    private String name;
    private BigDecimal invoiceTotal;
    private BigDecimal carrierQuoteTotal;
    private BigDecimal carrierInvoiceTotal;
    private BigDecimal customerQuoteTotal;
    private BigDecimal customerTransportTotal;
    private Boolean hazMat;
    private Boolean loadStatusReq;
    private String modeName;
    private LocalDateTime orderDate;
    private String salesStatus;
    private String loadUrl;
    private Integer shipperId;


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @JsonAlias({"CustomerId", "LastStopId", "FirstStopId", "billToId", "CarrierRemitToId"})
    @Column(name = "ext_sys_id")
    public String getExtSysId() {
        return extSysId;
    }

    public void setExtSysId(String extSysId) {
        this.extSysId = extSysId;
    }

    @Basic
    @Column(name = "ext_sys_tenant_id" )
    public String getExtSysTenantId() {
        return extSysTenantId;
    }

    public void setExtSysTenantId(String extSysTenantId) {
        this.extSysTenantId = extSysTenantId;
    }

    public LocalDate getExpectedShipDate() {
        return expectedShipDate;
    }

    public void setExpectedShipDate(LocalDate expectedShipDate) {
        this.expectedShipDate = expectedShipDate;
    }


    @Basic
    @Column(name = "carrier")
    public Integer getCarrier() {
        return carrier;
    }

    public void setCarrier(Integer carrier) {
        this.carrier = carrier;
    }

    @Basic
    @Column(name = "transport_mode")
    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    @Basic
    @Column(name = "posted_rate")
    public BigDecimal getPostedRate() {
        return postedRate;
    }

    public void setPostedRate(BigDecimal postedRate) {
        this.postedRate = postedRate;
    }

    @Basic
    @Column(name = "insurance_amt")
    public BigDecimal getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(BigDecimal insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }


    @Basic
    @Column(name = "equipment_type_id")
    public Integer getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(Integer equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    @Basic
    @Column(name = "load_status")
    public Integer getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(Integer loadStatus) {
        this.loadStatus = loadStatus;
    }

    @Basic
    @Column(name = "team_req")
    public Boolean getTeamReq() {
        return teamReq;
    }

    public void setTeamReq(Boolean teamReq) {
        this.teamReq = teamReq;
    }

    @Basic
    @Column(name = "food_grade_trailer_req")
    public Boolean getFoodGradeTrailerReq() {
        return foodGradeTrailerReq;
    }

    public void setFoodGradeTrailerReq(Boolean foodGradeTrailerReq) {
        this.foodGradeTrailerReq = foodGradeTrailerReq;
    }

    @Basic
    @Column(name = "temp_control_req")
    public Boolean getTempControlReq() {
        return tempControlReq;
    }

    public void setTempControlReq(Boolean tempControlReq) {
        this.tempControlReq = tempControlReq;
    }

    @Basic
    @Column(name = "expected_delivery_date")
    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    @Basic
    @Column(name = "driver")
    public Integer getDriver() {
        return driver;
    }

    public void setDriver(Integer driver) {
        this.driver = driver;
    }


    @Basic
    @Column(name = "location_based_svcs_req")
    public Boolean getLocationBasedSvcsReq() {
        return locationBasedSvcsReq;
    }

    public void setLocationBasedSvcsReq(Boolean locationBasedSvcsReq) {
        this.locationBasedSvcsReq = locationBasedSvcsReq;
    }

    @Transient
    @Basic
    @Column(name = "created_at")
    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    @Basic
    @Column(name = "updated_at")
    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Transient
    @Basic
    @Column(name = "deleted")
    public java.util.Date getDeleted() {
        return deleted;
    }

    public void setDeleted(java.util.Date deleted) {
        this.deleted = deleted;
    }

    @Basic
    @Column(name = "reference_number")
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    @Basic
    @Column(name = "bill_of_lading_no")
    public String getBillOfLadingNo() {
        return billOfLadingNo;
    }

    public void setBillOfLadingNo(String billOfLadingNo) {
        this.billOfLadingNo = billOfLadingNo;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "invoice_total")
    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(BigDecimal invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    @Basic
    @Column(name = "carrier_quote_total")
    public BigDecimal getCarrierQuoteTotal() {
        return carrierQuoteTotal;
    }

    public void setCarrierQuoteTotal(BigDecimal carrierQuoteTotal) {
        this.carrierQuoteTotal = carrierQuoteTotal;
    }

    @Basic
    @Column(name = "carrier_invoice_total")
    public BigDecimal getCarrierInvoiceTotal() {
        return carrierInvoiceTotal;
    }

    public void setCarrierInvoiceTotal(BigDecimal carrierInvoiceTotal) {
        this.carrierInvoiceTotal = carrierInvoiceTotal;
    }

    @Basic
    @Column(name = "customer_quote_total")
    public BigDecimal getCustomerQuoteTotal() {
        return customerQuoteTotal;
    }

    public void setCustomerQuoteTotal(BigDecimal customerQuoteTotal) {
        this.customerQuoteTotal = customerQuoteTotal;
    }

    @Basic
    @Column(name = "customer_transport_total")
    public BigDecimal getCustomerTransportTotal() {
        return customerTransportTotal;
    }

    public void setCustomerTransportTotal(BigDecimal customerTransportTotal) {
        this.customerTransportTotal = customerTransportTotal;
    }

    @Basic
    @Column(name = "haz_mat" )
    public Boolean getHazMat() {
        return hazMat;
    }

    public void setHazMat(Boolean hazMat) {
        this.hazMat = hazMat;
    }

    @Basic
    @Column(name = "load_status_req" )
    public Boolean getLoadStatusReq() {
        return loadStatusReq;
    }

    public void setLoadStatusReq(Boolean loadStatusReq) {
        this.loadStatusReq = loadStatusReq;
    }

    @Basic
    @Column(name = "mode_name"  )
    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    @Basic
    @Column(name = "order_date")
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Basic
    @Column(name = "sales_status" )
    public String getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(String salesStatus) {
        this.salesStatus = salesStatus;
    }

    @Basic
    @Column(name = "load_url")
    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }


    @Basic
    @Column(name = "shipper_id")
    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }
}

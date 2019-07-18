package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "load")
public class Load extends RiggoBaseEntity implements Serializable {


    private static final long serialVersionUID = -4133343645808769934L;
    private Long id;
    private String extSysId;
    private String extSysTenantId;
    private BigDecimal distanceMiles;

    @Column(name = "expected_ship_date", columnDefinition = "DATE")
    private LocalDate expectedShipDate;

    private Integer carrier;
    private String transportMode;
    private BigDecimal postedRate;
    private Short postedCurrency;
    private BigDecimal insuranceAmt;
    private Short insurnaceCurrency;
    private BigDecimal totalWeight;
    private Short weightUom;
    private Long equipmentTypeId;
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
    private String deliveryStatus;
    private BigDecimal distanceKilometers;
    private Boolean hazMat;
    private Boolean loadStatusReq;
    private BigDecimal marginInvoiced;
    private BigDecimal marginPiad;
    private BigDecimal marginPctInvoiced;
    private String modeName;
    private String onTimeDeliveryCounter;
    private LocalDateTime orderDate;
    private String salesStatus;
    private String salesScheduleStatus;
    private String loadShippingStatus;
    private String siteUrl;
    private String pickupDevlieryNumber;
    private String stopReferenceNumber;
    private String loadUrl;
    private Long shipperId;


    public Load() {

        this.setPrefix("load");
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Basic
    @Column(name = "distance_miles", precision = 3)
    public BigDecimal getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(BigDecimal distanceMiles) {
        this.distanceMiles = distanceMiles;
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
    @Column(name = "posted_currency")
    public Short getPostedCurrency() {
        return postedCurrency;
    }

    public void setPostedCurrency(Short postedCurrency) {
        this.postedCurrency = postedCurrency;
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
    @Column(name = "insurnace_currency")
    public Short getInsurnaceCurrency() {
        return insurnaceCurrency;
    }

    public void setInsurnaceCurrency(Short insurnaceCurrency) {
        this.insurnaceCurrency = insurnaceCurrency;
    }

    @Basic
    @Column(name = "total_weight")
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    @Basic
    @Column(name = "weight_uom")
    public Short getWeightUom() {
        return weightUom;
    }

    public void setWeightUom(Short weightUom) {
        this.weightUom = weightUom;
    }

    @Basic
    @Column(name = "equipment_type_id")
    public Long getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(Long equipmentTypeId) {
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
    @Column(name = "delivery_status"  )
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    @Basic
    @Column(name = "distance_kilometers" )
    @JsonAlias("distance_kilometers")
    public BigDecimal getDistanceKilometers() {
        return distanceKilometers;
    }

    public void setDistanceKilometers(BigDecimal distanceKilometers) {
        this.distanceKilometers = distanceKilometers;
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
    @Column(name = "margin_invoiced" )
    public BigDecimal getMarginInvoiced() {
        return marginInvoiced;
    }

    public void setMarginInvoiced(BigDecimal marginInvoiced) {
        this.marginInvoiced = marginInvoiced;
    }

    @Basic
    @Column(name = "margin_piad"  )
    public BigDecimal getMarginPiad() {
        return marginPiad;
    }

    public void setMarginPiad(BigDecimal marginPiad) {
        this.marginPiad = marginPiad;
    }

    @Basic
    @Column(name = "margin_pct_invoiced"  )
    public BigDecimal getMarginPctInvoiced() {
        return marginPctInvoiced;
    }

    public void setMarginPctInvoiced(BigDecimal marginPctInvoiced) {
        this.marginPctInvoiced = marginPctInvoiced;
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
    @Column(name = "on_time_delivery_counter")
    public String getOnTimeDeliveryCounter() {
        return onTimeDeliveryCounter;
    }

    public void setOnTimeDeliveryCounter(String onTimeDeliveryCounter) {
        this.onTimeDeliveryCounter = onTimeDeliveryCounter;
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
    @Column(name = "sales_schedule_status" )
    public String getSalesScheduleStatus() {
        return salesScheduleStatus;
    }

    public void setSalesScheduleStatus(String salesScheduleStatus) {
        this.salesScheduleStatus = salesScheduleStatus;
    }

    @Basic
    @Column(name = "load_shipping_status"  )
    public String getLoadShippingStatus() {
        return loadShippingStatus;
    }

    public void setLoadShippingStatus(String loadShippingStatus) {
        this.loadShippingStatus = loadShippingStatus;
    }

    @Basic
    @Column(name = "site_url" )
    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    @Basic
    @Column(name = "pickup_devliery_number" )
    public String getPickupDevlieryNumber() {
        return pickupDevlieryNumber;
    }

    public void setPickupDevlieryNumber(String pickupDevlieryNumber) {
        this.pickupDevlieryNumber = pickupDevlieryNumber;
    }

    @Basic
    @Column(name = "stop_reference_number" )
    public String getStopReferenceNumber() {
        return stopReferenceNumber;
    }

    public void setStopReferenceNumber(String stopReferenceNumber) {
        this.stopReferenceNumber = stopReferenceNumber;
    }

    @Basic
    @Column(name = "load_url")
    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }


    //    @ManyToOne
    @Basic
    @Column(name = "shipper_id")
    public Long getShipperId() {
        return shipperId;
    }

    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }


}

package io.riggo.data.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "load")
public class Load implements Serializable {

    private static final long serialVersionUID = -4133343645808769934L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ext_sys_id")
    private String extSysId;

    @Column(name = "ext_sys_tenant_id" )
    private String extSysTenantId;

    @Column(name = "expected_ship_date", columnDefinition = "DATE")
    private LocalDateTime expectedShipDate;

    @Column(name = "site_id")
    private Integer siteId;

    @Column(name = "carrier")
    private Integer carrier;

    @Column(name = "transport_mode")
    private String transportMode;

    @Column(name = "posted_rate")
    private BigDecimal postedRate;

    @Column(name = "insurance_amt")
    private BigDecimal insuranceAmt;

    @Column(name = "equipment_type_id")
    private Integer equipmentTypeId;

    @Column(name = "load_status")
    private Integer loadStatus;

    @Column(name = "team_req")
    private Boolean teamReq;

    @Column(name = "food_grade_trailer_req")
    private Boolean foodGradeTrailerReq;

    @Column(name = "temp_control_req")
    private Boolean tempControlReq;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "driver")
    private Integer driver;

    @Column(name = "location_based_svcs_req")
    private Boolean locationBasedSvcsReq;

    @Transient
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    @Column(name = "deleted")
    private LocalDateTime deleted;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "bill_of_lading_no")
    private String billOfLadingNo;

    @Column(name = "name")
    private String name;

    @Column(name = "carrier_quote_total")
    private BigDecimal carrierQuoteTotal;

    @Column(name = "carrier_invoice_total")
    private BigDecimal carrierInvoiceTotal;

    @Column(name = "customer_quote_total")
    private BigDecimal customerQuoteTotal;

    @Column(name = "customer_invoice_total")
    private BigDecimal customerInvoiceTotal;

    @Column(name = "haz_mat" )
    private Boolean hazMat;

    @Column(name = "mode_name"  )
    private String modeName;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "sales_status" )
    private String salesStatus;

    @Column(name = "load_url")
    private String loadUrl;

    @Column(name = "shipper_id")
    private Integer shipperId;

    @Column(name = "distance_miles")
    private BigDecimal distanceMiles;


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

    public String getExtSysTenantId() {
        return extSysTenantId;
    }

    public void setExtSysTenantId(String extSysTenantId) {
        this.extSysTenantId = extSysTenantId;
    }

    public LocalDateTime getExpectedShipDate() {
        return expectedShipDate;
    }

    public void setExpectedShipDate(LocalDateTime expectedShipDate) {
        this.expectedShipDate = expectedShipDate;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getCarrier() {
        return carrier;
    }

    public void setCarrier(Integer carrier) {
        this.carrier = carrier;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public BigDecimal getPostedRate() {
        return postedRate;
    }

    public void setPostedRate(BigDecimal postedRate) {
        this.postedRate = postedRate;
    }

    public BigDecimal getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(BigDecimal insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }

    public Integer getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(Integer equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    public Integer getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(Integer loadStatus) {
        this.loadStatus = loadStatus;
    }

    public Boolean getTeamReq() {
        return teamReq;
    }

    public void setTeamReq(Boolean teamReq) {
        this.teamReq = teamReq;
    }

    public Boolean getFoodGradeTrailerReq() {
        return foodGradeTrailerReq;
    }

    public void setFoodGradeTrailerReq(Boolean foodGradeTrailerReq) {
        this.foodGradeTrailerReq = foodGradeTrailerReq;
    }

    public Boolean getTempControlReq() {
        return tempControlReq;
    }

    public void setTempControlReq(Boolean tempControlReq) {
        this.tempControlReq = tempControlReq;
    }

    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Integer getDriver() {
        return driver;
    }

    public void setDriver(Integer driver) {
        this.driver = driver;
    }

    public Boolean getLocationBasedSvcsReq() {
        return locationBasedSvcsReq;
    }

    public void setLocationBasedSvcsReq(Boolean locationBasedSvcsReq) {
        this.locationBasedSvcsReq = locationBasedSvcsReq;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getBillOfLadingNo() {
        return billOfLadingNo;
    }

    public void setBillOfLadingNo(String billOfLadingNo) {
        this.billOfLadingNo = billOfLadingNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCustomerInvoiceTotal() {
        return customerInvoiceTotal;
    }

    public void setCustomerInvoiceTotal(BigDecimal customerInvoiceTotal) {
        this.customerInvoiceTotal = customerInvoiceTotal;
    }

    public BigDecimal getCarrierQuoteTotal() {
        return carrierQuoteTotal;
    }

    public void setCarrierQuoteTotal(BigDecimal carrierQuoteTotal) {
        this.carrierQuoteTotal = carrierQuoteTotal;
    }

    public BigDecimal getCarrierInvoiceTotal() {
        return carrierInvoiceTotal;
    }

    public void setCarrierInvoiceTotal(BigDecimal carrierInvoiceTotal) {
        this.carrierInvoiceTotal = carrierInvoiceTotal;
    }

    public BigDecimal getCustomerQuoteTotal() {
        return customerQuoteTotal;
    }

    public void setCustomerQuoteTotal(BigDecimal customerQuoteTotal) {
        this.customerQuoteTotal = customerQuoteTotal;
    }

    public Boolean getHazMat() {
        return hazMat;
    }

    public void setHazMat(Boolean hazMat) {
        this.hazMat = hazMat;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(String salesStatus) {
        this.salesStatus = salesStatus;
    }

    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public BigDecimal getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(BigDecimal distanceMiles) {
        this.distanceMiles = distanceMiles;
    }
}

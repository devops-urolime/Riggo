package io.riggo.data.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "load_stop")
public class LoadStop implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ext_sys_id")
    private String extSysId;

    @Column(name = "ext_sys_tenant_id")
    private String extSysTenantId;

    @Column(name = "name")
    private String name;

    @Column(name = "shipping_receiving_hours")
    private String shippingReceivingHours;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "stop_number")
    private Integer stopNumber;

    @Column(name = "type")
    private Integer type;

    @Column(name = "expected_date_time")
    private LocalDateTime expectedDateTime;

    @Column(name = "appointment_required")
    private Boolean appointmentRequired;

    @Column(name = "appointment_time")
    private String appointmentTime;

    @Column(name = "stop_status")
    private Integer stopStatus;

    @Column(name = "carrier_status")
    private Integer carrierStatus;

    @Column(name = "arrival_status")
    private Integer arrivalStatus;

    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;

    @Column(name = "departure_date_time")
    private LocalDateTime departureDateTime;

    @Transient
    @Column(name = "created_at")
    private java.util.Date createdAt;

    @Transient
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    @Transient
    @Column(name = "deleted")
    private java.util.Date deleted;

    @Column(name = "load_id")
    private Integer loadId;


    @Transient
    private String loadExtSysId;

    @Transient
    private Location location;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShippingReceivingHours() {
        return shippingReceivingHours;
    }

    public void setShippingReceivingHours(String shippingReceivingHours) {
        this.shippingReceivingHours = shippingReceivingHours;
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

    public Integer getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(Integer stopNumber) {
        this.stopNumber = stopNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LocalDateTime getExpectedDateTime() {
        return expectedDateTime;
    }

    public void setExpectedDateTime(LocalDateTime expectedDateTime) {
        this.expectedDateTime = expectedDateTime;
    }

    public Boolean getAppointmentRequired() {
        return appointmentRequired;
    }

    public void setAppointmentRequired(Boolean appointmentRequired) {
        this.appointmentRequired = appointmentRequired;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(Integer stopStatus) {
        this.stopStatus = stopStatus;
    }

    public Integer getCarrierStatus() {
        return carrierStatus;
    }

    public void setCarrierStatus(Integer carrierStatus) {
        this.carrierStatus = carrierStatus;
    }

    public Integer getArrivalStatus() {
        return arrivalStatus;
    }

    public void setArrivalStatus(Integer arrivalStatus) {
        this.arrivalStatus = arrivalStatus;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public Integer getLoadId() {
        return loadId;
    }

    public void setLoadId(Integer loadId) {
        this.loadId = loadId;
    }

    public String getLoadExtSysId() {
        return loadExtSysId;
    }

    public void setLoadExtSysId(String loadExtSysId) {
        this.loadExtSysId = loadExtSysId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

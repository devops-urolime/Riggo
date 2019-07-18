package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "load_stop")

public class LoadStop implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ext_sys_id")
    @JsonAlias({"LastStopId", "FirstStopId","StopId"})
    private String extSysId;

    @Column(name = "ext_sys_tenant_id")
    private String extSysTenantId = "0";


    @Column(name = "stop_number")
    @JsonAlias({"Stoprtms__Number__c","FirstStoprtms__Number__c","LastStoprtms__Number__c"})
    private Integer stopNumber = -1;



    @Column(name = "type")
    private Integer type = 1;

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
    private Long loadId;

    @Column(name = "location_id")
    private Long locationId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getLoadId() {
        return loadId;
    }

    public void setLoadId(Long loadId) {
        this.loadId = loadId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}

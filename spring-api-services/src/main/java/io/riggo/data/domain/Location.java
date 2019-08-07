package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "location")

public class Location implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ext_sys_id")
    @JsonAlias({"LastStopId", "FirstStopId","StopId"})
    private String extSysId;

    @Column(name = "ext_sys_tenant_id")
    private String extSysTenantId = "0";

    @Column(name = "name")
    @JsonAlias({"LastStopName", "FirstStopName","StopName"})
    private String name;

    @Transient
    @Column(name = "created_at")
    private java.util.Date createdAt;

    @Transient
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    @Transient
    @Column(name = "deleted")
    private java.util.Date deleted;

    @Column(name = "address_id")
    private Integer addressId;

    @Transient
    private Address address;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer address_id) {
        this.addressId = address_id;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
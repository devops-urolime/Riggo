package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "address")

public class Address implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ext_sys_id")
    @JsonAlias({"CustomerId", "LastStopId", "FirstStopId", "billToId", "CarrierRemitToId"})
    private String extSysId;

    @Column(name = "ext_sys_tenant_id")
    private String extSysTenantId;

    @JsonAlias({"CustomerShippingStreet", "FirstStoprtms__Address2__c", "LastStoprtms__Address2__c", "billToShippingStreet", "CarrierRemitToShippingStreet"})
    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2 = "";
    @JsonAlias({"CustomerShippingCity", "FirstStoprtms__City__c", "LastStoprtms__City__c", "LastStoprtms__City__c", "billToShippingCity", "CarrierRemitToShippingCity"})
    @Column(name = "city")
    private String city;

    @JsonAlias({"CustomerShippingState", "LastStoprtms__State_Province__c", "FirstStoprtms__State_Province__c", "LastStoprtms__State_Province__c", "billToShippingState"})
    @Column(name = "state")
    private String state;

    @Column(name = "country")
    @JsonAlias({"CustomerShippingCountry", "FirstStoprtms__Country__c", "LastStoprtms__Country__c", "CarrierRemitToShippingCountry"})
    private String country;

    @Column(name = "postal_code")
    @JsonAlias({"CustomerShippingPostalCode", "FirstStoprtms__Postal_Code__c", "LastStoprtms__Postal_Code__c", "billToShippingPostalCode", "CarrierRemitToShippingPostalCode"})
    private String postalCode;

    @Column(name = "is_primary")
    private Boolean primary;

    @Transient
    @Column(name = "created_at")
    private java.util.Date createdAt;

    @Transient
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    @Transient
    @Column(name = "deleted")
    private java.util.Date deleted;

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
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

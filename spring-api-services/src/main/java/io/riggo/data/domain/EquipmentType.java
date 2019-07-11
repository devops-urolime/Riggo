package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "equipment_type")


public class EquipmentType implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;


    /*
    "EquipmentTypeId": "a0V6A0000020EUsUAM",
            "EquipmentTypeName": "Dry Van 48'",
            "EquipmentType_rtms__Category__c": "Van",
            "EquipmentType_rtms__Max_Volume__c": "3350",
            "EquipmentType_rtms__Volume_Units__c": "Cubic Feet",
            "EquipmentType_rtms__Max_Pallets__c": "26",
            "EquipmentType_rtms__Enabled__c": "true",
            "EquipmentType_rig_Truckstop_Type__c": "null",
            "EquipmentType_rtms__ISO_Type_Group__c": "null",
            "EquipmentType_rtms__ISO_Size_Type__c": "null",
            "EquipmentType_rtms__Tare_Weight__c": "null",
            "EquipmentType_rtms__Max_Weight__c": "45000",
            "EquipmentType_rtms__Weight_Units__c": "lbs"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonAlias({"EquipmentTypeId"})
    @Column(name = "ext_sys_id")
    private String extSysId;


    @Column(name = "site_id")
    private Long siteId = 100L;

    @Column(name = "type")
    @JsonAlias({"EquipmentType_rtms__Category__c"})
    private Integer type = 1;

    @JsonAlias({"EquipmentTypeName"})
    @Column(name = "name")
    private String name;

    @Column(name = "rank")
    private Integer rank = 0;

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

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

/*enum EQ_TYPES {
    VAN, FLATBED, REEFER
}*/
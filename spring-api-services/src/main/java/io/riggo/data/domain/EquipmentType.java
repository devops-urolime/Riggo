package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "equipment_type")


public class EquipmentType implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonAlias({"EquipmentTypeId"})
    @Column(name = "ext_sys_id")
    private String extSysId;


    @Column(name = "site_id")
    private Long siteId;

    @Column(name = "type")
    @JsonAlias({"EquipmentType_rtms__Category__c"})
    private Integer type;

    @JsonAlias({"EquipmentTypeName"})
    @Column(name = "name")
    private String name;

    @Column(name = "rank")
    private Integer rank;

    @Transient
    @Column(name = "created_at")
    private java.util.Date createdAt;

    @Transient
    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    @Transient
    @Column(name = "deleted")
    private java.util.Date deleted;


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
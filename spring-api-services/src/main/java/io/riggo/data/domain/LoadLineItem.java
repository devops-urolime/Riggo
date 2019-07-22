package io.riggo.data.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "load_line_item")

public class LoadLineItem implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "ext_sys_id")
    private String extSysId;

    @Column(name = "ext_sys_tenant_id")
    private String extSysTenantId;

    @Column(name = "pickup_stop")  //references load_stop.id
    private Integer pickupStop;

    @Column(name = "delivery_stop")  //references load_stop.id
    private Integer deliveryStop;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "haz_mat")
    private Boolean hazMat;

    @Column(name = "rank")  //references load_stop.id
    private Integer rank;

    @Column(name = "load_id")  //references load.id
    private Integer loadId;

    @Transient
    @Column(name = "created_at")
    private Date createdAt;
    @Transient
    @Column(name = "updated_at")
    private Date updatedAt;

    @Transient
    @Column(name = "deleted")
    private Date deleted;

    private String deliveryStopExtSysId;
    private String pickupStopExtSysId;
    private String loadExtSysId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getPickupStop() {
        return pickupStop;
    }

    public void setPickupStop(Integer pickupStop) {
        this.pickupStop = pickupStop;
    }

    public Integer getDeliveryStop() {
        return deliveryStop;
    }

    public void setDeliveryStop(Integer deliveryStop) {
        this.deliveryStop = deliveryStop;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Boolean getHazMat() {
        return hazMat;
    }

    public void setHazMat(Boolean hazMat) {
        this.hazMat = hazMat;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getLoadId() {
        return loadId;
    }

    public void setLoadId(Integer loadId) {
        this.loadId = loadId;
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

    public String getDeliveryStopExtSysId() {
        return deliveryStopExtSysId;
    }

    public void setDeliveryStopExtSysId(String deliveryStopExtSysId) {
        this.deliveryStopExtSysId = deliveryStopExtSysId;
    }

    public String getPickupStopExtSysId() {
        return pickupStopExtSysId;
    }

    public void setPickupStopExtSysId(String pickupStopExtSysId) {
        this.pickupStopExtSysId = pickupStopExtSysId;
    }

    public String getLoadExtSysId() {
        return loadExtSysId;
    }

    public void setLoadExtSysId(String loadExtSysId) {
        this.loadExtSysId = loadExtSysId;
    }
}

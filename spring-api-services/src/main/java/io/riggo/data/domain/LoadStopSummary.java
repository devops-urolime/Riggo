package io.riggo.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class LoadStopSummary implements Serializable {

    @Id
    @Column(name = "id")
    private String id;


    @Column(name = "type")
    private Integer type;

    @Column(name = "arrival_status")
    private Integer arrivalStatus;

    @Column(name = "count")
    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public Integer getArrivalStatus() {
        return arrivalStatus;
    }

    public int getCount() {
        return count;
    }

}
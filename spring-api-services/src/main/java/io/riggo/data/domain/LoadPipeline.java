package io.riggo.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "LOAD")
public class LoadPipeline implements Serializable {


    @Id
    private long id;

    @Column(name="PENDING")
    private int pending;

    @Column(name="INTRANSIT")
    private int inTransit;

    @Column(name="DELIVERED")
    private int delivered;


    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }
}
package io.riggo.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class LoadPipeline implements Serializable {

    @Id
    @Column(name = "load_status")
    private int id;

    @Column(name = "count")
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

}
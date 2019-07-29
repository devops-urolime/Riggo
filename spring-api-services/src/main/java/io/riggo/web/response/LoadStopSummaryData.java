package io.riggo.web.response;

import java.io.Serializable;
import java.util.List;

public class LoadStopSummaryData implements Serializable {

    private Integer id;
    private String name;
    private List<IdNameCountData> data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IdNameCountData> getData() {
        return data;
    }

    public void setData(List<IdNameCountData> data) {
        this.data = data;
    }
}

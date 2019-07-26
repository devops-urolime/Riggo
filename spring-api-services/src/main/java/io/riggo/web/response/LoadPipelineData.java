package io.riggo.web.response;

import java.io.Serializable;
import java.util.List;

public class LoadPipelineData implements Serializable {

    private Integer id;
    private String name;
    private Integer count;

    private List<LoadPipelineStatusData> subStatuses;

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

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<LoadPipelineStatusData> getSubStatuses() {
        return subStatuses;
    }

    public void setSubStatuses(List<LoadPipelineStatusData> subStatuses) {
        this.subStatuses = subStatuses;
    }
}

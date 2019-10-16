package io.riggo.data.domain;

import java.io.Serializable;

public class LoadDataJson implements Serializable {

    private String loadStatus;
    private String salesStatus;

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(String salesStatus) {
        this.salesStatus = salesStatus;
    }
}

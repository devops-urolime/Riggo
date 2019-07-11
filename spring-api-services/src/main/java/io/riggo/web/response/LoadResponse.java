package io.riggo.web.response;

import io.riggo.data.domain.Load;

import java.io.Serializable;


public class LoadResponse implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;
    public LoadResponse(Load load) {
        this.load = load;
    }

    private Load load;

    public Load getLoad() {
        return load;
    }

    public void setLoad(Load load) {
        this.load = load;
    }
}

package io.riggo.web.response;

import io.riggo.data.domain.Load;

public class LoadAPIResponse extends BaseAPIResponse<Load> {

    private String hashId;

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }
}

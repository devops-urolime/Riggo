package io.riggo.data.exception;

public class LoadNotFoundExceptionResponse {

    private String LoadNotFound;

    public LoadNotFoundExceptionResponse(String LoadNotFound) {
        this.LoadNotFound = LoadNotFound;
    }

    public String getLoadNotFound() {
        return LoadNotFound;
    }

    public void setLoadNotFound(String LoadNotFound) {
        this.LoadNotFound = LoadNotFound;
    }
}

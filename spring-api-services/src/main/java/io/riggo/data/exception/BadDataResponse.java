package io.riggo.data.exception;

public class BadDataResponse {

    private String message;

    public BadDataResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

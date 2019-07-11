package io.riggo.data.exception;


public class LoadObjectConflictResponse {

    private String conflictMsg;

    public LoadObjectConflictResponse(String msg) {
        conflictMsg = msg;
    }

    public String getLoadNotFound() {
        return conflictMsg;
    }

    public void setLoadNotFound(String msg) {
        conflictMsg = msg;
    }
}

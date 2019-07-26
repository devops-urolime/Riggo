package io.riggo.web.response;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NestedBaseAPIResponse<T> implements Serializable {

    private int status = HttpStatus.OK.value();

    private String message = "success";

    private int success = 0;
    private int failures = 0;

    private List<BaseAPIResponse<T>> data = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BaseAPIResponse<T>> getData() {
        return data;
    }

    public void setData(List<BaseAPIResponse<T>> data) {
        this.data = data;
    }

    public void addData(BaseAPIResponse<T> data) {
        this.data.add(data);
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }
}

package io.riggo.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LoadNotFoundException extends RuntimeException {
    public LoadNotFoundException(String message) {
        super(message);
    }
}

package io.riggo.web.integration.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PayloadParseException extends RuntimeException {

    public PayloadParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayloadParseException(String property) {
        super(property + " was not found");
    }
}
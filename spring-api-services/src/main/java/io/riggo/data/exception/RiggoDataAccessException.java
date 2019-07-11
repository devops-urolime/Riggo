package io.riggo.data.exception;

import org.springframework.dao.DataAccessException;

/** Encapsulates Exceptions thrown by the Data Access Tiers. */
public class RiggoDataAccessException extends DataAccessException {

    public RiggoDataAccessException(Throwable cause) {
        super("A DataAccessException has occurred", cause);
    }

    public RiggoDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

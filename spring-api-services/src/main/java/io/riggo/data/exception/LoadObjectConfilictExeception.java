package io.riggo.data.exception;

/**
 * Thrown when 409 Conflict needs to be raised.
 */
public class LoadObjectConfilictExeception extends Exception {

    public LoadObjectConfilictExeception(Exception cause) {
        super(cause);
    }

    public LoadObjectConfilictExeception(String cause) {
        super(cause);
    }

}

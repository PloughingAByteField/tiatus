package org.tiatus.service;

/**
 * Created by johnreynolds on 26/06/2016.
 */
public class ServiceException extends Exception {
    private final Exception exception;

    public ServiceException(Exception exception) {
        super();
        this.exception = exception;
    }

    public Exception getSuppliedException() {
        return exception;
    }
}

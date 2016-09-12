package org.tiatus.service;

import org.tiatus.api.exception.EnclosingException;

/**
 * Wrap an exception or message for passing up the boundaries
 */
public class ServiceException extends EnclosingException {

    /**
     * Constructor to wrap the exception
     * @param exception Exception to be wrapped
     */
    public ServiceException(Exception exception) {
        super(exception);
    }

    /**
     * Constructor to wrap the message
     * @param message Message to be wrapped
     */
    public ServiceException(String message) {
        super(message);
    }

}

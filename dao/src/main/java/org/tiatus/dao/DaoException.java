package org.tiatus.dao;

import org.tiatus.api.exception.EnclosingException;

/**
 * Wrap an exception or message for passing up the boundaries
 */
public class DaoException extends EnclosingException {

    /**
     * Constructor to wrap the exception
     * @param exception Exception to be wrapped
     */
    public DaoException(Exception exception) {
        super(exception);
    }

    /**
     * Constructor to wrap the message
     * @param message Message to be wrapped
     */
    public DaoException(String message) {
        super(message);
    }
}

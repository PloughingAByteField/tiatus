package org.tiatus.dao;

import org.tiatus.api.exception.EnclosingException;

/**
 * Created by johnreynolds on 26/06/2016.
 */
public class DaoException extends EnclosingException {

    public DaoException(Exception exception) {
        super(exception);
    }

    public DaoException(String message) {
        super(message);
    }
}

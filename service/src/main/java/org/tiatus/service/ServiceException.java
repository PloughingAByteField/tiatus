package org.tiatus.service;

import org.tiatus.api.exception.EnclosingException;

/**
 * Created by johnreynolds on 26/06/2016.
 */
public class ServiceException extends EnclosingException {

    public ServiceException(Exception exception) {
        super(exception);
    }

    public ServiceException(String message) {
        super(message);
    }

}

package org.tiatus.server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.service.ServiceException;

/**
 * Created by johnreynolds on 15/05/2017.
 */
public abstract class RestBase {
    private static final Logger LOG = LoggerFactory.getLogger(RestBase.class);

    protected void logError(Exception e) {
        if (e instanceof ServiceException) {
            LOG.warn("Got service exception: ", ((ServiceException)e).getSuppliedException());
        } else {
            LOG.warn("Got general exception ", e);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

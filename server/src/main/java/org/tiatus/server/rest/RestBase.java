package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.service.ServiceException;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;

/**
 * Created by johnreynolds on 15/05/2017.
 */
public abstract class RestBase {
    private static final Logger LOG = LoggerFactory.getLogger(RestBase.class);

    protected Response logError(Exception e) {
        if (e instanceof ServiceException) {
            LOG.warn("Got service exception: ", ((ServiceException)e).getSuppliedException());
        } else {
            LOG.warn("Got general exception ", e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}

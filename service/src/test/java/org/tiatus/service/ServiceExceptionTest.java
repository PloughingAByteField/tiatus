package org.tiatus.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class ServiceExceptionTest {

    @Test
    public void testException() {
        Exception exception = new Exception("message");
        ServiceException serviceException = new ServiceException(exception);
        Assert.assertEquals(serviceException.getSuppliedException(), exception);
    }

    @Test
    public void testMessage() {
        String message = "message";
        ServiceException serviceException = new ServiceException(message);
        Assert.assertEquals(serviceException.getMessage(), message);
    }
}

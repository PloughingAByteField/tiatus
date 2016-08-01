package org.tiatus.api.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 01/08/2016.
 */
public class EnclosingExceptionTest {

    @Test
    public void messageTest() throws Exception {
        EnclosingException ee = new EnclosingException("message");

        Assert.assertNull(ee.getSuppliedException());
        Assert.assertEquals(ee.getMessage(), "message");
    }

    @Test
    public void exceptionTest() throws Exception {
        Exception exception = new Exception();
        EnclosingException ee = new EnclosingException(exception);

        Assert.assertNotNull(ee.getSuppliedException());
        Assert.assertEquals(ee.getSuppliedException(), exception);
    }


    @Test
    public void enclosedExceptionTest() throws Exception {
        Exception exception = new Exception();
        EnclosingException enclosingException = new EnclosingException(exception);
        EnclosingException ee = new EnclosingException(enclosingException);

        Assert.assertNotNull(ee.getSuppliedException());
        Assert.assertEquals(ee.getSuppliedException(), exception);
    }

    @Test
    public void dobleEnclosedExceptionTest() throws Exception {
        Exception exception = new Exception();
        EnclosingException enclosingException = new EnclosingException(exception);
        EnclosingException doubleEnclosingException = new EnclosingException(enclosingException);
        EnclosingException ee = new EnclosingException(doubleEnclosingException);

        Assert.assertNotNull(ee.getSuppliedException());
        Assert.assertEquals(ee.getSuppliedException(), exception);
    }
}

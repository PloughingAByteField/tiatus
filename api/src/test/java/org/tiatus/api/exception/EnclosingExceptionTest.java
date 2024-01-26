package org.tiatus.api.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by johnreynolds on 01/08/2016.
 */
public class EnclosingExceptionTest {

    @Test
    public void messageTest() throws Exception {
        EnclosingException ee = new EnclosingException("message");

        Assertions.assertNull(ee.getSuppliedException());
        Assertions.assertEquals("message", ee.getMessage());
    }

    @Test
    public void exceptionTest() throws Exception {
        Exception exception = new Exception();
        EnclosingException ee = new EnclosingException(exception);

        Assertions.assertNotNull(ee.getSuppliedException());
        Assertions.assertEquals(exception, ee.getSuppliedException());
    }


    @Test
    public void enclosedExceptionTest() throws Exception {
        Exception exception = new Exception();
        EnclosingException enclosingException = new EnclosingException(exception);
        EnclosingException ee = new EnclosingException(enclosingException);

        Assertions.assertNotNull(ee.getSuppliedException());
        Assertions.assertEquals(exception, ee.getSuppliedException());
    }

    @Test
    public void dobleEnclosedExceptionTest() throws Exception {
        Exception exception = new Exception();
        EnclosingException enclosingException = new EnclosingException(exception);
        EnclosingException doubleEnclosingException = new EnclosingException(enclosingException);
        EnclosingException ee = new EnclosingException(doubleEnclosingException);

        Assertions.assertNotNull(ee.getSuppliedException());
        Assertions.assertEquals(exception, ee.getSuppliedException());
    }
}

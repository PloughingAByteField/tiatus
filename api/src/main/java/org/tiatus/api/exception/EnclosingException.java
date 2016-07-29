package org.tiatus.api.exception;

public class EnclosingException extends Exception {
    protected final Exception exception;

    public EnclosingException(Exception exception) {
        super();
        this.exception = exception;
    }

    public EnclosingException(String message) {
        super(message);
        exception = null;
    }

    public Exception getSuppliedException() {
        Exception ex = exception;

        if (exception != null && exception instanceof EnclosingException) {
            EnclosingException ee = (EnclosingException) exception;
            while (ee != null && ee.exception != null) {
                ex = ee.exception;
                if (ee.exception != null && ee.exception instanceof EnclosingException) {
                    ee = (EnclosingException) ee.exception;
                } else {
                    break;
                }
            }
        }

        return ex;
    }

}
package org.tiatus.api.exception;

/**
 * Wrap exceptions so it can be passed cross the application bounds with out other parts needing to know about exception types.
 * Messages can be wrapped instead of an exception.
 * At some point it will be unwrapped.
 */
public class EnclosingException extends Exception {
    protected final Exception exception;

    /**
     * Wrap exception
     * @param exception The exception to be wrapped.
     */
    public EnclosingException(Exception exception) {
        super();
        this.exception = exception;
    }

    /**
     * Wrap message instead of an exception
     * @param message The message to be wrapped.
     */
    public EnclosingException(String message) {
        super(message);
        exception = null;
    }

    /**
     * Return the wrapped exception, walk up the chain to find the original exception.
     * @return Exception
     */
    public Exception getSuppliedException() {
        Exception ex = exception;

        if (exception != null && exception instanceof EnclosingException) {
            EnclosingException ee = (EnclosingException) exception;
            while (ee.exception != null) {
                ex = ee.exception;
                if (ee.exception instanceof EnclosingException) {
                    ee = (EnclosingException) ee.exception;
                } else {
                    break;
                }
            }
        }

        return ex;
    }

}
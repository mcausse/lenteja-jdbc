package org.homs.lentejajdbc.exception;

public class JdbcException extends RuntimeException {

    public JdbcException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JdbcException(final String message) {
        super(message);
    }

    public JdbcException(final Throwable cause) {
        super(cause);
    }
}
package org.homs.lechuga.exception;

import org.homs.lentejajdbc.exception.JdbcException;

public class LechugaException extends JdbcException {

    public LechugaException(final String message) {
        super(message);
    }

    public LechugaException(final String message, Throwable t) {
        super(message, t);
    }
}
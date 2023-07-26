package org.homs.lentejajdbc.exception;

public class EmptyResultException extends UnexpectedResultException {

    public EmptyResultException(final String message) {
        super(message);
    }
}
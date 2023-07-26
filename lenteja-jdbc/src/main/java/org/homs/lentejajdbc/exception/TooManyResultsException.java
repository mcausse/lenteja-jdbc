package org.homs.lentejajdbc.exception;

public class TooManyResultsException extends UnexpectedResultException {

    public TooManyResultsException(final String message) {
        super(message);
    }
}
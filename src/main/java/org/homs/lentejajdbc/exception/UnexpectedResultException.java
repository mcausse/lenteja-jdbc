package org.homs.lentejajdbc.exception;

public class UnexpectedResultException extends JdbcException {

    public UnexpectedResultException(String message) {
        super(message);
    }
}

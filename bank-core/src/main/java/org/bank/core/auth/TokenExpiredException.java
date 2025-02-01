package org.bank.core.auth;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}

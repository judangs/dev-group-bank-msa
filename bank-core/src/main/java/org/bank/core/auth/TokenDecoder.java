package org.bank.core.auth;

public interface TokenDecoder<T> {
    T decode(String token, AuthConstants.TokenType type) throws AuthenticationException;
}
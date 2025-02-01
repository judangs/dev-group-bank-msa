package org.bank.core.auth;

public interface TokenEncoder<T , R> {
    R encode(T contents, AuthConstants.TokenType type) throws IllegalArgumentException;
}


package org.bank.user.core.auth.domain;

public interface TokenEncoder {

    String encode(TokenPayload tokenPayload, String secretKey) throws IllegalArgumentException;
}

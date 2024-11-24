package org.bank.user.core.auth.domain;

public interface TokenDecoder {

    boolean validate(String token);
    TokenPayload decode(String token);

}

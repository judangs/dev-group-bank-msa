package org.bank.gateway.core.fixture;

import org.bank.core.auth.AuthClaims;

public class AuthenticatedFixture {

    private static final AuthClaims VALID_USER = new AuthClaims.ConcreteAuthClaims("user-01", "username", "user@email.com");

    public AuthClaims user() {
        return VALID_USER;
    }
}

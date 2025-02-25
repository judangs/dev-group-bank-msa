package org.bank.pay.fixture;

import org.bank.core.auth.AuthClaims;

public class UserFixture {


    public static AuthClaims authenticated() {
        return new AuthClaims.ConcreteAuthClaims("user-01", "fixture", "user@email.com");
    }

}

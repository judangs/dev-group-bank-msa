package org.bank.gateway.core.auth;

import org.bank.core.auth.AuthClaims;

public interface AuthClaimsEncoder {

    String encode(AuthClaims claims);
}

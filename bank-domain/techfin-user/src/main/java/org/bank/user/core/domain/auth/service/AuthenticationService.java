package org.bank.user.core.domain.auth.service;

import org.bank.core.auth.AuthClaims;
import org.bank.user.core.domain.auth.TokenContents;

public interface AuthenticationService {
    String login(String userid, String password);
    void logout(AuthClaims user, String token);
    String issueAccessToken(TokenContents tokenContents);
}

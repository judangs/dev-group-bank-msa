package org.bank.user.core.domain.auth;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthConstants;
import org.bank.core.common.Role;
import org.bank.user.core.domain.account.Credential;

@Getter
public class TokenClaims extends AuthClaims {
    private Role.UserRole role;

    public TokenClaims(String userId, String username, String email, Role.UserRole role) {
        super(userId, username, email);
        this.role = role;
    }

    public static TokenClaims of(String userId, String username, String email, Role.UserRole role) {
        return new TokenClaims(userId, username, email, role);
    }

    public static TokenClaims of(Credential credential) {
        return new TokenClaims(credential.getUserid(), credential.getUsername(),
                credential.getProfile().getEmail(), credential.getProfile().getRole());
    }

    public static TokenClaims of(Claims claims) {
        String userId = claims.getSubject();
        String username = claims.get(AuthConstants.TokenClaim.USERNAME, String.class);
        String email = claims.get(AuthConstants.TokenClaim.EMAIL, String.class);
        Role.UserRole role = Role.UserRole.valueOf(claims.get(AuthConstants.TokenClaim.ROLE, String.class));

        return TokenClaims.of(userId, username, email, role);
    }
}

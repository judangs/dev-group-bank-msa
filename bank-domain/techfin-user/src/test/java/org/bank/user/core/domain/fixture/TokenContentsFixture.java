package org.bank.user.core.domain.fixture;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.bank.core.auth.AuthConstants;
import org.bank.core.common.Role;
import org.bank.user.core.domain.auth.TokenClaims;
import org.bank.user.core.domain.auth.TokenContents;

import java.util.Date;

public class TokenContentsFixture {

    private static final Date issueAt = new Date();
    private static final Date expiredAt = new Date(issueAt.getTime() + 360000);
    private static final TokenClaims tokenClaims = new TokenClaims("userid", "username", "email", Role.UserRole.INDIVIDUAL);
    private static final TokenContents tokenContents = new TokenContents("subject", "issuer", tokenClaims, issueAt, expiredAt);

    public static TokenContents fixture() {
        return tokenContents;
    }

    public static String expired(String secret) {
        TokenContents expiredTokenContents = new TokenContents("subject", "issuer", tokenClaims, issueAt, new Date(issueAt.getTime() - 36000));
        return Jwts.builder()
                .setSubject(expiredTokenContents.getSubject())
                .claim(AuthConstants.TokenClaim.EMAIL, expiredTokenContents.getClaims().getEmail())
                .claim(AuthConstants.TokenClaim.USERNAME, expiredTokenContents.getClaims().getUsername())
                .claim(AuthConstants.TokenClaim.ROLE, expiredTokenContents.getClaims().getRole())
                .setIssuer("test")
                .setIssuedAt(expiredTokenContents.getIssuedAt())
                .setExpiration(expiredTokenContents.getExpiresAt())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}

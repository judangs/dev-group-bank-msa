package org.bank.user.core.domain.auth;


import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenContents {
    private String subject;
    private String issuer;
    private TokenClaims claims;
    private Date issuedAt;
    private Date expiresAt;

    public void ensureTokenIsValid(String issuer, Date issueAt, Date expireAt) {
        this.issuer = issuer;
        this.issuedAt = issueAt;
        this.expiresAt = expireAt;
    }

    public static TokenContents invalid() {
        return new TokenContents();
    }

    public static TokenContents of(Claims claims) {
        return TokenContents.builder()
                .subject(claims.getSubject())
                .claims(TokenClaims.of(claims))
                .issuer(claims.getIssuer())
                .issuedAt(claims.getIssuedAt())
                .expiresAt(claims.getExpiration())
                .build();
    }
}
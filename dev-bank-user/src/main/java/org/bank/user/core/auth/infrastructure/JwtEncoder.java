package org.bank.user.core.auth.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.bank.common.constants.auth.TokenConstants;
import org.bank.user.core.auth.domain.TokenEncoder;
import org.bank.user.core.auth.domain.TokenPayload;
import org.bank.user.global.property.JwtProperties;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtEncoder implements TokenEncoder {

    private final JwtProperties jwtProperties;


    @Override
    public String encode(TokenPayload tokenPayload, String tokenType) throws IllegalArgumentException {

        Date issueAt = new Date();

        Date expireAt = switch(tokenType) {
            case TokenConstants.ACCESS -> new Date(issueAt.getTime() + jwtProperties.getExpireAccess());
            case TokenConstants.REFRESH -> new Date(issueAt.getTime() + jwtProperties.getExpireRefresh());
            default ->
                    throw new IllegalStateException("invalid Token Type:" + tokenType);
        };
        tokenPayload.setTokenValidityWithDate(jwtProperties.getIssuer(), issueAt, expireAt);

        String secretKey = switch(tokenType) {
            case TokenConstants.ACCESS -> jwtProperties.getAccessKey();
            case TokenConstants.REFRESH -> jwtProperties.getRefreshKey();
            default ->
                    throw new IllegalStateException("invalid Token Type:" + tokenType);
        };


        return Jwts.builder()
                .setSubject(tokenPayload.getSubject())
                .claim(TokenConstants.CLAIM_EMAIL, tokenPayload.getEmail())
                .claim(TokenConstants.CLAIM_USERNAME, tokenPayload.getUsername())
                .claim(TokenConstants.CLAIM_ROLE, tokenPayload.getRoles())
                .setIssuedAt(tokenPayload.getIssuedAt())
                .setExpiration(tokenPayload.getExpiresAt())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

    }
}

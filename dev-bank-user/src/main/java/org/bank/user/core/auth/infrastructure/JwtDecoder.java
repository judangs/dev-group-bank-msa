package org.bank.user.core.auth.infrastructure;

import exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.bank.common.constants.auth.TokenConstants;
import org.bank.user.core.auth.domain.TokenDecoder;
import org.bank.user.core.auth.domain.TokenPayload;
import org.bank.user.global.property.JwtProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtDecoder implements TokenDecoder {

    private final JwtProperties jwtProperties;

    @Override
    public TokenPayload decode(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getRefreshKey().getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        if(!validate(claims.getExpiration())) {
            throw new TokenExpiredException();
        }

        return TokenPayload.builder()
                .subject(claims.getSubject())
                .username(claims.get(TokenConstants.CLAIM_EMAIL, String.class))
                .email(claims.get(TokenConstants.CLAIM_EMAIL, String.class))
                .roles(claims.get(TokenConstants.CLAIM_ROLE, ArrayList.class))
                .build();
    }

    @Override
    public boolean validate(String token) {
         Date expireAt = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getRefreshKey().getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody().getExpiration();

        return Instant.now().isBefore(expireAt.toInstant());
    }

    private boolean validate(Date expireAt) {
        return Instant.now().isBefore(expireAt.toInstant());
    }

}

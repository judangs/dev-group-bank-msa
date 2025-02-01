package org.bank.user.core.domain.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.TokenEncoder;
import org.bank.user.core.domain.auth.TokenContents;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtEncoder implements TokenEncoder<TokenContents, String> {

    private final JwtProperties jwtProperties;

    @Override
    public String encode(TokenContents contents, AuthConstants.TokenType type) throws IllegalArgumentException {

        Date issueAt = new Date();

        Date expireAt = switch(type) {
            case ACCESS -> new Date(issueAt.getTime() + jwtProperties.getAccessExpire());
            case REFRESH -> new Date(issueAt.getTime() + jwtProperties.getRefreshExpire());
            default ->
                    throw new IllegalStateException("토큰 타입이 유효하지 않습니다." + type);
        };
        contents.ensureTokenIsValid(jwtProperties.getIssuer(), issueAt, expireAt);

        String secretKey = switch(type) {
            case ACCESS -> jwtProperties.getAccessSecret();
            case REFRESH -> jwtProperties.getRefreshSecret();
            default ->
                    throw new IllegalStateException("토큰 타입이 유효하지 않습니다." + type);
        };


        return Jwts.builder()
                .setSubject(contents.getSubject())
                .claim(AuthConstants.TokenClaim.EMAIL, contents.getClaims().getEmail())
                .claim(AuthConstants.TokenClaim.USERNAME, contents.getClaims().getUsername())
                .claim(AuthConstants.TokenClaim.ROLE, contents.getClaims().getRole())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(contents.getIssuedAt())
                .setExpiration(contents.getExpiresAt())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

    }
}


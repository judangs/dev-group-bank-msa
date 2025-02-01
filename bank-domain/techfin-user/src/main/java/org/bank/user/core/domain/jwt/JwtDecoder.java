package org.bank.user.core.domain.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.auth.TokenDecoder;
import org.bank.user.core.domain.auth.TokenContents;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtDecoder implements TokenDecoder<TokenContents> {

    private final JwtProperties jwtProperties;

    @Override
    public TokenContents decode(String token, AuthConstants.TokenType type) throws AuthenticationException {

        try {
            Claims claims =  switch (type) {
                case ACCESS -> Jwts.parserBuilder()
                        .setSigningKey(jwtProperties.getAccessSecret().getBytes())
                        .setClock(Date::new)
                        .setAllowedClockSkewSeconds(0)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                case REFRESH -> Jwts.parserBuilder()
                        .setSigningKey(jwtProperties.getRefreshSecret().getBytes())
                        .setClock(Date::new)
                        .setAllowedClockSkewSeconds(0)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

            };

            return TokenContents.of(claims);

        } catch (UnsupportedJwtException | MalformedJwtException e) {
            return TokenContents.invalid();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("토큰이 만료되었습니다. 인증을 다시 수행해주세요.");
        }
    }
}

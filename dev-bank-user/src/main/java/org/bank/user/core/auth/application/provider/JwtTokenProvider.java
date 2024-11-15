package org.bank.user.core.auth.application.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.bank.user.global.provider.KeyProvider;
import org.bank.user.global.http.HeaderAttribute;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {


    private final int ACESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;
    private final int REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    private final String BEARER_PREFIX = "Bearer ";

    public static final String REFRESH = "refresh-token";
    public static final String ACCESS = "access-token";

    public static final String REFRESH_ID = "refresh:";

    private SecretKey secretKey;

    private enum PayloadField {
        USERID,
        ROLE;
    }

    @PostConstruct
    public void init() {

        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createRefreshKeyWithJWT(KeyProvider redisKeyProvider) {
        String redisKey = new StringBuilder()
                .append(REFRESH_ID)
                .append(redisKeyProvider.createKey())
                .toString();
        return redisKey;
    }

    public String generateToken(UserCredential credential, String tokenType) {
        Date now = new Date();

        Date expireDate = switch(tokenType) {
            case ACCESS -> new Date(now.getTime() + ACESS_TOKEN_EXPIRATION_TIME);
            case REFRESH -> new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);
            default ->
                    throw new IllegalStateException("invalid Token Type:" + tokenType);
        };

        return Jwts.builder()
                .setSubject(credential.getUsername())
                .claim(PayloadField.USERID.name(), credential.getUserid())
                .claim(PayloadField.ROLE.name(), credential.getUserType())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (MalformedJwtException | SignatureException error) {
            // 유효하지 않은 JWT
        } catch (ExpiredJwtException error) {
            // 만기된 토큰
        } catch (UnsupportedJwtException error) {
            // 지원하지 않는 토큰
        } catch (IllegalArgumentException error) {
            // 잘못된 JWT 토큰
        }

        return false;
    }

    public String getUseridFromToken(String token) {
        return extractClaims(token).get(PayloadField.USERID.name(), String.class);
    }

    public void addTokenToResponse(HttpServletResponse response, String refresh, String access) {
        addJwtToResponseHeader(response, access);
        addJwtToResponseCookie(response, refresh);
    }

    private void addJwtToResponseHeader(HttpServletResponse response, String token) {
        response.addHeader(HeaderAttribute.AUTHORIZATION,
                BEARER_PREFIX + token);
    }

    private void addJwtToResponseCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(REFRESH, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_TOKEN_EXPIRATION_TIME);

        response.addCookie(cookie);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody();
    }
}


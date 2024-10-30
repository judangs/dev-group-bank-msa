package org.bank.user.application.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.bank.user.common.http.HeaderAttribute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtility {


    private final int ACESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;
    private final int REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    private final String BEARER_PREFIX = "Bearer ";

    public static final String REFRESH = "refresh token";
    public static final String ACCESS = "access token";

    private SecretKey secretKey;

    @Value("${jwt.token.access.name}")
    private String ACCESS_TOKEN_PREFIX;
    @Value("${jwt.token.refresh.name}")
    private String REFRESH_TOKEN_PREFIX;


    private enum PayloadField {
        USERNAME("username"),
        ROLE("role");

        @Getter
        private String field;
        PayloadField(String field) {
            this.field = field;
        }
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(String username, String tokenType) {
        Date now = new Date();

        Date expireDate = switch(tokenType) {
            case ACCESS -> new Date(now.getTime() + ACESS_TOKEN_EXPIRATION_TIME);
            case REFRESH -> new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);
            default ->
                    throw new IllegalStateException("invalid Token Type:" + tokenType);
        };

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .setSigningKey(secretKey).parseClaimsJws(token);
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

    public String getUsernameFromToken(String token) {
        return extractClaims(token).get(PayloadField.USERNAME.getField(), String.class);
    }

    public void addJwtToResponseHeader(HttpServletResponse response, String username) {
        response.addHeader(HeaderAttribute.AUTHORIZATION,
                BEARER_PREFIX + generateToken(username, ACCESS));
    }

    public void addJwtToResponseCookie(HttpServletResponse response, String username) {
        Cookie refreshToken = new Cookie(REFRESH, generateToken(username, REFRESH));
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(true);
        refreshToken.setPath("/");
        refreshToken.setMaxAge(REFRESH_TOKEN_EXPIRATION_TIME);

        response.addCookie(refreshToken);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}


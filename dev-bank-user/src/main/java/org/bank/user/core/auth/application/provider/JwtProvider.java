package org.bank.user.core.auth.application.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.http.HeaderAttribute;
import org.bank.user.global.property.JwtProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public static final String ACCESS = "ACCESS-TOKEN";
    public static final String REFRESH = "REFRESH-TOKEN";

    public static final String REFRESH_ID = "refresh:";


    public String generate(UserCredential credential, String tokenType) {
        Date now = new Date();

        Date expireDate = switch(tokenType) {
            case ACCESS -> new Date(now.getTime() + jwtProperties.getExpireAccess());
            case REFRESH -> new Date(now.getTime() + jwtProperties.getExpireRefresh());
            default ->
                    throw new IllegalStateException("invalid Token Type:" + tokenType);
        };

        return Jwts.builder()
                .setSubject(credential.getUsername())
                .claim(JwtProperties.Claims.USERID.name(), credential.getUserid())
                .claim(JwtProperties.Claims.ROLE.name(), credential.getUserType())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validate(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey())
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
        return extractClaims(token).get(JwtProperties.Claims.USERID.name(), String.class);
    }

    public void addTokenToResponse(HttpServletResponse response, String refresh, String access) {
        addJwtToResponseHeader(response, access);
        addJwtToResponseCookie(response, refresh);
    }

    public Optional<String> getTokenFromRequest(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if(token == null) {
            return Optional.empty();
        }

        return Optional.of(token.split(jwtProperties.getBearer())[1]);
    }

    private void addJwtToResponseHeader(HttpServletResponse response, String token) {
        response.addHeader(HeaderAttribute.AUTHORIZATION,
                jwtProperties.getBearer() + token);
    }

    private void addJwtToResponseCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(jwtProperties.getRefresh(), token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtProperties.getExpireRefresh());

        response.addCookie(cookie);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
}


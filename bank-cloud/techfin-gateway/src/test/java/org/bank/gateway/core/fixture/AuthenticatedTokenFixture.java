package org.bank.gateway.core.fixture;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthConstants;

import java.util.Date;

public class AuthenticatedTokenFixture {

    private final static String INVALID_ISSUER = "not issuer";
    private final static String ISSUER = "application.user";
    private final static String SECRET = "7JWh7IS47Iqk7YKk7J6F64uI64ukLg==";

    private static final AuthClaims VALID_USER = new AuthClaims.ConcreteAuthClaims("userid", "username", "email");

    public static String valid() {
        return Jwts.builder()
                .setSubject(VALID_USER.getUserid())
                .claim(AuthConstants.TokenClaim.USERNAME, VALID_USER.getUsername())
                .claim(AuthConstants.TokenClaim.EMAIL, VALID_USER.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 360000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String expired() {
        return Jwts.builder()
                .setSubject(VALID_USER.getUserid())
                .claim(AuthConstants.TokenClaim.USERNAME, VALID_USER.getUsername())
                .claim(AuthConstants.TokenClaim.EMAIL, VALID_USER.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() - 3600000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String invalid() {
        return "invalid-token-string";
    }

    public static String hacker() {
        return Jwts.builder()
                .setSubject(VALID_USER.getUserid())
                .claim(AuthConstants.TokenClaim.USERNAME, VALID_USER.getUsername())
                .claim(AuthConstants.TokenClaim.EMAIL, VALID_USER.getEmail())
                .setIssuer(INVALID_ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 360000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }


}
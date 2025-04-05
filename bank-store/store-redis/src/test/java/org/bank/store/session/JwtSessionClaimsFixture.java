package org.bank.store.session;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.bank.core.auth.AuthConstants;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.fixture.AccountFixture;

import java.util.Date;

public class JwtSessionClaimsFixture {

    public static final Credential authenticated = AccountFixture.authenticated("fixture");

    public static final Date issueAt = new Date();
    public static final Date expiredAt = new Date(issueAt.getTime() + 3600000);
    public static final String secret = "66as7ZSE66CI7Iuc7YKk7J6F64uI64ukLg==";



    public static String access(String userid, String username, String email) {
        return Jwts.builder()
                .setSubject(userid)
                .claim(AuthConstants.TokenClaim.EMAIL, email)
                .claim(AuthConstants.TokenClaim.USERNAME, username)
                .setIssuer("application.user")
                .setIssuedAt(issueAt)
                .setExpiration(expiredAt)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String access(String userid, String username) {
        return Jwts.builder()
                .setSubject(userid)
                .claim(AuthConstants.TokenClaim.USERNAME, username)
                .setIssuer("application.user")
                .setIssuedAt(issueAt)
                .setExpiration(expiredAt)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String refresh(String userid, String username) {
        return Jwts.builder()
                .setSubject(userid)
                .claim(AuthConstants.TokenClaim.USERNAME, username)
                .setIssuer("application.user")
                .setIssuedAt(issueAt)
                .setExpiration(new Date(issueAt.getTime() + 86400000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String access() {
        return access(authenticated.getUserid(), authenticated.getUsername());
    }

    public static String refresh() {
        return refresh(authenticated.getUserid(), authenticated.getUsername());
    }
}

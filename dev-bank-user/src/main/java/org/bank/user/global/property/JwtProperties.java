package org.bank.user.global.property;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Setter
@Getter
@Component
@PropertySource("classpath:jwt-dev.properties")
@ConfigurationProperties("jwt.secret")
public class JwtProperties {

    @Value("${jwt.secret.bearer}")
    private String bearer;

    @Value("${jwt.secret.access}")
    private String access;

    @Value("${jwt.secret.refresh}")
    private String refresh;

    @Value("${jwt.secret.access-expire-time}")
    private int expireAccess;

    @Value("${jwt.secret.refresh-expire-time}")
    private int expireRefresh;

    @Value("${jwt.secret.access-key}")
    private String accessKey;
    @Value("{jwt.secret.refresh-key}")
    private String refreshKey;

    private SecretKey secretKey;

    public enum Claims {
        USERID, ROLE;
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}

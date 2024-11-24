package org.bank.user.global.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource("classpath:/token/auth-jwt.properties")
@ConfigurationProperties("jwt.secret")
public class JwtProperties {

    @Value("${jwt.secret.access-expire-time}")
    private int expireAccess;

    @Value("${jwt.secret.refresh-expire-time}")
    private int expireRefresh;

    @Value("${jwt.secret.access-key}")
    private String accessKey;

    @Value("{jwt.secret.refresh-key}")
    private String refreshKey;

    @Value("${jwt.secret.issuer-uri}")
    private String issuer;
}

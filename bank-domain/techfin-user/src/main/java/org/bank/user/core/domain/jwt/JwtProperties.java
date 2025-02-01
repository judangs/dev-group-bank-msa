package org.bank.user.core.domain.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String accessSecret;
    private Long accessExpire;
    private String refreshSecret;
    private Long refreshExpire;
    private String issuer;

}

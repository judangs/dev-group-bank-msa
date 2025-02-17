package org.bank.gateway.global.auth;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import java.time.Duration;

@Configuration
@PropertySource("classpath:/auth-jwt.properties")
@EnableConfigurationProperties(JwtProperties.class)
public class JwtDecoderConfiguration {

    @Bean
    public JwtDecoder jwtDecoder(JwtProperties jwtProperties) {

        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getAccessSecret().getBytes());

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();

        OAuth2TokenValidator<Jwt> delegatingValidator =
                new DelegatingOAuth2TokenValidator<>(
                        new JwtTimestampValidator(Duration.ofSeconds(jwtProperties.getExpire())),
                        new JwtIssuerValidator(jwtProperties.getIssuer())
                );

        jwtDecoder.setJwtValidator(delegatingValidator);
        return jwtDecoder;
    }
}
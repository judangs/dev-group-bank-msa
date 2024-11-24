package org.bank.gateway.global.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;

@Configuration
@PropertySource("classpath:/token/auth-jwt.properties")
public class JwtDecoderConfig {
    @Value("${jwt.secret.issuer-uri}")
    private String issuerUri;


    @Value("${jwt.secret.access-key}")
    private String accessKey;

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKey secretKey = Keys.hmacShaKeyFor(accessKey.getBytes());

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        jwtDecoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer(issuerUri)
        );

        return jwtDecoder;
    }
}
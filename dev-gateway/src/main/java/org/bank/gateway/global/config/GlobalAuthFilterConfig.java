package org.bank.gateway.global.config;


import org.bank.gateway.core.auth.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class GlobalAuthFilterConfig {

    @Bean
    @Order(1)
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenValidator jwtTokenValidator,
            UserTokenRefreshService userTokenRefreshService,
            AuthHeaderDecorator authHeaderDecorator
    ) {
        return new JwtAuthenticationFilter(jwtTokenValidator, userTokenRefreshService, authHeaderDecorator);
    }

    @Bean
    @Order(2)
    public JwtClaimsToHeaderFilter jwtClaimsToHeaderFilter(JwtDecoder jwtDecoder, AuthHeaderDecorator authHeaderDecorator) {
        return new JwtClaimsToHeaderFilter(jwtDecoder, authHeaderDecorator);
    }
}

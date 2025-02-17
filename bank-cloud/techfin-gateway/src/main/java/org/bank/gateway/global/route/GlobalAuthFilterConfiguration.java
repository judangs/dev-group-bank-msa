package org.bank.gateway.global.route;


import org.bank.gateway.core.auth.*;
import org.bank.gateway.global.filter.JwtAuthenticationFilter;
import org.bank.gateway.global.filter.JwtClaimsToHeaderFilter;
import org.bank.gateway.global.http.ServerHttpRespStatusMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class GlobalAuthFilterConfiguration {

    @Bean
    @Order(1)
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            ServerHttpRespStatusMapper serverHttpRespStatusMapper,
            JwtTokenSupportManager jwtTokenSupportManager,
            UserTokenRefreshService userTokenRefreshService,
            AuthHeaderResolver authHeaderResolver
    ) {
        return new JwtAuthenticationFilter(serverHttpRespStatusMapper, jwtTokenSupportManager, userTokenRefreshService, authHeaderResolver);
    }

    @Bean
    @Order(2)
    public JwtClaimsToHeaderFilter jwtClaimsToHeaderFilter(
            ServerHttpRespStatusMapper serverHttpRespStatusMapper,
            JwtTokenSupportManager jwtTokenSupportManager,
            AuthHeaderResolver authHeaderResolver) {
        return new JwtClaimsToHeaderFilter(serverHttpRespStatusMapper, jwtTokenSupportManager, authHeaderResolver);
    }
}

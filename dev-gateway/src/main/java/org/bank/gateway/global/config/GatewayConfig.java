package org.bank.gateway.global.config;

import org.bank.gateway.core.auth.JwtAuthenticationFilter;
import org.bank.gateway.core.auth.JwtClaimsToHeaderFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {



    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtClaimsToHeaderFilter jwtClaimsToHeaderFilter
            ) {
        return builder.routes()
                .route("private-service", r -> r
                        .path("/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))
                                .filter(jwtClaimsToHeaderFilter.apply(new JwtClaimsToHeaderFilter.Config()))
                                .rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri("http://127.0.0.1:8081"))
                .route("public-service", r -> r
                        .path("/public/api/user/**")
                        .filters(f -> f
                                .rewritePath("/public/api/(?<segment>.*)", "/${segment}"))
                        .uri("http://127.0.0.1:8081"))
                .build();
    }
}

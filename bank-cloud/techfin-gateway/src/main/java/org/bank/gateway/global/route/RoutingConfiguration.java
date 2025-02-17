package org.bank.gateway.global.route;

import org.bank.core.domain.DomainNames;
import org.bank.gateway.global.filter.JwtAuthenticationFilter;
import org.bank.gateway.global.filter.JwtClaimsToHeaderFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/app-services.properties")
@EnableConfigurationProperties(ServiceServerProperties.class)
public class RoutingConfiguration implements RoutingBeanService {


    @Override
    @Bean
    public RouteLocator userRouteLocator(
            RouteLocatorBuilder builder, ServiceServerProperties serviceServerProperties,
            JwtAuthenticationFilter jwtAuthenticationFilter, JwtClaimsToHeaderFilter jwtClaimsToHeaderFilter
            ) {
        return builder.routes()
                .route("private-user-service", r -> r
                        .path("/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))
                                .filter(jwtClaimsToHeaderFilter.apply(new JwtClaimsToHeaderFilter.Config()))
                                .rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri(serviceServerProperties.url(DomainNames.USER)))
                .route("public-user-service", r -> r
                        .path("/public/api/user/**")
                        .filters(f -> f
                                .rewritePath("/public/api/(?<segment>.*)", "/${segment}"))
                        .uri(serviceServerProperties.url(DomainNames.USER)))
                .build();
    }

    @Override
    @Bean
    public RouteLocator payRouteLocator(
            RouteLocatorBuilder builder, ServiceServerProperties serviceServerProperties,
            JwtAuthenticationFilter jwtAuthenticationFilter, JwtClaimsToHeaderFilter jwtClaimsToHeaderFilter
    ) {
        return builder.routes()
                .route("private-pay-service", r -> r
                        .path("/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))
                                .filter(jwtClaimsToHeaderFilter.apply(new JwtClaimsToHeaderFilter.Config()))
                                .rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri(serviceServerProperties.url(DomainNames.PAY)))
                .route("public-pay-service", r -> r
                        .path("/public/api/user/**")
                        .filters(f -> f
                                .rewritePath("/public/api/(?<segment>.*)", "/${segment}"))
                        .uri(serviceServerProperties.url(DomainNames.PAY)))
                .build();
    }

}

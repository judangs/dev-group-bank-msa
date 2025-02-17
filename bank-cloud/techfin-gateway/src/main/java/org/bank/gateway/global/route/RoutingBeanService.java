package org.bank.gateway.global.route;

import org.bank.gateway.global.filter.JwtAuthenticationFilter;
import org.bank.gateway.global.filter.JwtClaimsToHeaderFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;


public interface RoutingBeanService {
    RouteLocator userRouteLocator(
            RouteLocatorBuilder builder, ServiceServerProperties serviceServerProperties,
            JwtAuthenticationFilter jwtAuthenticationFilter, JwtClaimsToHeaderFilter jwtClaimsToHeaderFilter);

    RouteLocator payRouteLocator(
            RouteLocatorBuilder builder, ServiceServerProperties serviceServerProperties,
            JwtAuthenticationFilter jwtAuthenticationFilter, JwtClaimsToHeaderFilter jwtClaimsToHeaderFilter);
}

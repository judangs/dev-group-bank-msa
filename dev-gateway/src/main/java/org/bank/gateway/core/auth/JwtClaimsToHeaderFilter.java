package org.bank.gateway.core.auth;

import org.bank.common.constants.auth.TokenConstants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public class JwtClaimsToHeaderFilter extends AbstractGatewayFilterFactory<JwtClaimsToHeaderFilter.Config> {

    private final JwtDecoder jwtDecoder;
    private final AuthHeaderDecorator authHeaderDecorator;

    public JwtClaimsToHeaderFilter(JwtDecoder jwtDecoder, AuthHeaderDecorator authHeaderDecorator) {

        this.jwtDecoder = jwtDecoder;
        this.authHeaderDecorator = authHeaderDecorator;
    }

    public static class Config { }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractTokenFromRequest(exchange.getRequest());

            return Mono.fromCallable(() -> jwtDecoder.decode(token))
                    .flatMap(jwt -> continueRequest(exchange, chain, jwt))
                    .onErrorResume(JwtException.class, ex -> unauthorized(exchange));
        };
    }



    private Mono<Void> continueRequest(ServerWebExchange exchange, GatewayFilterChain chain, Jwt jwt) {

        ServerWebExchange newRequest = exchange.mutate()
                .request(authHeaderDecorator.addHeaders(exchange, jwt))
                .build();

        return chain.filter(newRequest);
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return token.substring(TokenConstants.BEARER_PREFIX.length());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
package org.bank.gateway.global.filter;

import org.bank.gateway.core.auth.AuthHeaderResolver;
import org.bank.gateway.core.auth.JwtTokenSupportManager;
import org.bank.gateway.global.http.ServerHttpRespStatusMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public class JwtClaimsToHeaderFilter extends AbstractGatewayFilterFactory<JwtClaimsToHeaderFilter.Config> {

    private final ServerHttpRespStatusMapper serverHttpRespStatusMapper;

    private final JwtTokenSupportManager jwtTokenSupportManager;
    private final AuthHeaderResolver authHeaderResolver;

    public JwtClaimsToHeaderFilter(ServerHttpRespStatusMapper serverHttpRespStatusMapper, JwtTokenSupportManager jwtTokenSupportManager, AuthHeaderResolver authHeaderResolver) {
        this.serverHttpRespStatusMapper = serverHttpRespStatusMapper;
        this.jwtTokenSupportManager = jwtTokenSupportManager;
        this.authHeaderResolver = authHeaderResolver;
    }

    public static class Config { }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = jwtTokenSupportManager.extract(exchange.getRequest());

            return Mono.fromCallable(() -> jwtTokenSupportManager.from(token))
                    .flatMap(jwt -> apply(exchange, chain, jwt))
                    .onErrorResume(JwtException.class, ex -> serverHttpRespStatusMapper.unauthorized(exchange));
        };
    }



    private Mono<Void> apply(ServerWebExchange exchange, GatewayFilterChain chain, Mono<Jwt> jwt) {
        return authHeaderResolver.update(exchange, jwt)
                .flatMap(request -> chain.filter(exchange.mutate().request(request).build()));
    }


    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
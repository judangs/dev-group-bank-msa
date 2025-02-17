package org.bank.gateway.global.filter;

import org.bank.gateway.core.auth.AuthHeaderResolver;
import org.bank.gateway.core.auth.JwtTokenSupportManager;
import org.bank.gateway.core.auth.UserTokenRefreshService;
import org.bank.gateway.global.http.ServerHttpRespStatusMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final ServerHttpRespStatusMapper serverHttpRespStatusMapper;
    private final AuthHeaderResolver authHeaderResolver;

    private final JwtTokenSupportManager jwtTokenSupportManager;
    private final UserTokenRefreshService userTokenRefreshService;


    public static class Config { }

    public JwtAuthenticationFilter(
            ServerHttpRespStatusMapper serverHttpRespStatusMapper,
            JwtTokenSupportManager jwtTokenSupportManager,
            UserTokenRefreshService userTokenRefreshService,
            AuthHeaderResolver authHeaderResolver)
    {

        super(Config.class);

        this.serverHttpRespStatusMapper = serverHttpRespStatusMapper;
        this.jwtTokenSupportManager = jwtTokenSupportManager;
        this.userTokenRefreshService = userTokenRefreshService;
        this.authHeaderResolver = authHeaderResolver;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            try {
                String token = jwtTokenSupportManager.extract(exchange.getRequest());

                return jwtTokenSupportManager.validate(token)
                        .flatMap(valid -> valid
                                ? apply(exchange, chain)
                                : refresh(exchange, chain, token))
                        .onErrorResume(error -> serverHttpRespStatusMapper.unauthorized(exchange));


            } catch (IllegalArgumentException e) {
                return serverHttpRespStatusMapper.unauthorized(exchange);
            }
        };
    }


    private Mono<Void> refresh(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return userTokenRefreshService.refreshForExpired(token)
                .flatMap(newToken -> updateToken(exchange, chain, newToken))
                .onErrorResume(ex -> serverHttpRespStatusMapper.unauthorized(exchange));
    }

    private Mono<Void> updateToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return authHeaderResolver.authorization(exchange, token)
                .flatMap(request -> chain.filter(exchange.mutate().request(request).build()));
    }

    private Mono<Void> apply(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
    }
}
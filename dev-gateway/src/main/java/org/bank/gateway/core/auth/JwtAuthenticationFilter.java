package org.bank.gateway.core.auth;

import org.apache.hc.core5.http.HttpHeaders;
import org.bank.common.constants.auth.TokenConstants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtTokenValidator tokenValidator;
    private final UserTokenRefreshService userTokenRefreshService;
    private final AuthHeaderDecorator authHeaderDecorator;


    public static class Config { }

    public JwtAuthenticationFilter(
            JwtTokenValidator tokenValidator,
            UserTokenRefreshService userTokenRefreshService,
            AuthHeaderDecorator authHeaderDecorator) {

        super(Config.class);
        this.tokenValidator = tokenValidator;
        this.userTokenRefreshService = userTokenRefreshService;
        this.authHeaderDecorator = authHeaderDecorator;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            try {
                String token = tokenValidator.extractTokenFromRequest(exchange.getRequest());

                return tokenValidator.validateToken(token)
                        .flatMap(isValid -> isValid ? continueRequest(exchange, chain)
                                : attemptRefreshToken(exchange, chain, token));


            } catch (IllegalArgumentException e) {
                return unauthorized(exchange);
            }
        };
    }


    private Mono<Void> attemptRefreshToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {

        return userTokenRefreshService.getRefreshTokenForExpiredAcessToken(token)
                .flatMap(newToken -> updateRequestWithNewToken(exchange, chain, newToken))
                .onErrorResume(ex -> handleRefreshTokenFailure(exchange));
    }

    private Mono<Void> handleRefreshTokenFailure(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.empty();
    }

    private Mono<Void> updateRequestWithNewToken(ServerWebExchange exchange, GatewayFilterChain chain, String newToken) {

        ServerWebExchange newRequest = exchange.mutate()
                .request(authHeaderDecorator.addHeaders(exchange, HttpHeaders.AUTHORIZATION, TokenConstants.BEARER_PREFIX + newToken))
                .build();

        return chain.filter(newRequest);
    }

    private Mono<Void> continueRequest(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

}
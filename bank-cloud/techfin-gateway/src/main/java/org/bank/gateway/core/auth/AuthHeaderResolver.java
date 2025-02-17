package org.bank.gateway.core.auth;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class AuthHeaderResolver {

    private final JwtTokenSupportManager jwtTokenSupportManager;
    private final AuthClaimsEncoder authClaimsEncoder;

    public Mono<ServerHttpRequest> update(ServerWebExchange exchange, Jwt jwt) {

        AuthClaims authClaims = new AuthClaims.ConcreteAuthClaims(
                jwt.getSubject(),
                jwt.getClaimAsString(AuthConstants.TokenClaim.USERNAME),
                jwt.getClaimAsString(AuthConstants.TokenClaim.EMAIL));

        ServerWebExchange mutated = exchange.mutate()
                .request(exchange.getRequest().mutate()
                                .headers(headers -> {
                                    headers.remove(HttpHeaders.AUTHORIZATION);
                                    headers.add(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + jwt.getTokenValue());
                                    headers.add(AuthConstants.HeaderField.X_AUTH_CLAIMS, authClaimsEncoder.encode(authClaims));
                                })
                                .build())
                        .build();

        return Mono.just(decorate(mutated, mutated.getRequest().getHeaders()));
    }

    public Mono<ServerHttpRequest> update(ServerWebExchange exchange, Mono<Jwt> monoJwt) {
        return monoJwt.flatMap(jwt -> update(exchange, jwt));
    }

    public Mono<ServerHttpRequest> authorization(ServerWebExchange exchange, String token) {
        return jwtTokenSupportManager.from(token).flatMap(jwt -> update(exchange, jwt));
    }


    private HttpHeaders headers(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders();
    }

    private ServerHttpRequest decorate(ServerWebExchange exchange, HttpHeaders headers) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                return headers;
            }
        };
    }


}

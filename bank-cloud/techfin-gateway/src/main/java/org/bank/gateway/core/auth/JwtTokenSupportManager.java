package org.bank.gateway.core.auth;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.auth.TokenExpiredException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtTokenSupportManager {

    private final JwtDecoder jwtDecoder;
    private final JwtTokenErrorHandler jwtTokenErrorHandler;

    public Mono<Boolean> validate(String token) throws TokenExpiredException {
        return Mono.fromCallable(() -> jwtDecoder.decode(token))
                .map(jwt -> true)
                .onErrorResume(jwtTokenErrorHandler::handler);
    }

    public Mono<Jwt> from(String token) {
        return validate(token)
                .flatMap(valid -> valid
                        ? claims(token)
                        : Mono.error(new AuthenticationException("인증 정보가 올바르지 않습니다."))
                );
    }


    private Mono<Jwt> claims(String token) {
        return Mono.just(jwtDecoder.decode(token));
    }


    public String extract(ServerHttpRequest request) throws IllegalArgumentException {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            throw new IllegalArgumentException();
        }

        return authHeader.substring(AuthConstants.BEARER_PREFIX.length());
    }
}
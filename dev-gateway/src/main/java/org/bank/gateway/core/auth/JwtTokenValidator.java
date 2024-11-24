package org.bank.gateway.core.auth;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.bank.common.constants.auth.TokenConstants;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {
    private final JwtDecoder jwtDecoder;


    public Mono<Boolean> validateToken(String token) {

        return Mono.fromCallable(() -> jwtDecoder.decode(token))
                .map(jwt -> {
                    if (jwt.getExpiresAt().isBefore(Instant.now())) {
                        return false;
                    }
                    return true;
                })
                .onErrorResume(e -> Mono.just(false));
    }

    public String extractTokenFromRequest(ServerHttpRequest request) throws IllegalArgumentException {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith(TokenConstants.BEARER_PREFIX)) {
            throw new IllegalArgumentException();
        }

        return authHeader.substring(TokenConstants.BEARER_PREFIX.length());
    }

}
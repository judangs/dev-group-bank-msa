package org.bank.gateway.core.auth;

import org.bank.core.auth.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenErrorHandler {

    private final String MALFORMED_TOKEN = "Malformed token";
    private final String INVALID_TOKEN = "invalid_token";

    public Mono<Boolean> handler(Throwable error) throws AuthenticationException {
        invalid(error);

        if(timeout(error)) {
            return Mono.just(false);
        }
        return Mono.empty();
    }

    private boolean timeout(Throwable error) {
        if(error instanceof BadJwtException badJwtException) {
            if (badJwtException.getMessage().contains(MALFORMED_TOKEN)) {
                throw new AuthenticationException("토큰의 형식이 잘못되었습니다.");
            }

            return true;
        }

        throw new AuthenticationException("잘못된 토큰 정보입니다.");
    }

    private void invalid(Throwable error) {
        if(error instanceof JwtValidationException) {
            throw new AuthenticationException("토큰의 명세가 잘못되었습니다.");
        }
    }


}

package org.bank.gateway.core.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserTokenRefreshService {

    private final WebClient userServiceClient;

    public Mono<String> getRefreshTokenForExpiredAcessToken(String expiredToken) {

         return userServiceClient.post()
                .uri("/user/auth/expire-token")
                .bodyValue(expiredToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> Mono.error(new AuthenticationException("인증에 실패했습니다.."){}))
                .onStatus(status -> status.is5xxServerError(), response -> Mono.error(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
                .bodyToMono(String.class);
    }
}

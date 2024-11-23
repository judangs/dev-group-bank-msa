package org.bank.gateway.core.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserTokenRefreshService {

    private final WebClient userServiceClient;

    public Mono<String> validRefreshToken(String expiredToken) {
        return userServiceClient.post()
                .uri("/api/token/refresh")
                .bodyValue(expiredToken)
                .retrieve()
                .bodyToMono(String.class);
    }
}

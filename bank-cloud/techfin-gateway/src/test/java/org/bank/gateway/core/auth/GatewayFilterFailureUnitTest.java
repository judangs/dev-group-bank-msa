package org.bank.gateway.core.auth;

import org.bank.core.auth.AuthConstants;
import org.bank.gateway.GatewayApplication;
import org.bank.gateway.core.fixture.AuthenticatedTokenFixture;
import org.bank.gateway.core.type.GatewayUnitTestForMockUserApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        classes = GatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GatewayFilterFailureUnitTest extends GatewayUnitTestForMockUserApplication {


    private WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl(stubUrl())
            .build();

    private int port = 8080;
    private String url = String.format("http://localhost:%d", port);

    @Test
    void 부적절한_토큰을_가진_사용자_요청에_대해_게이트웨이로부터_인증_실패_응답을_받을_수_있습니다() {
        webTestClient.post().uri(url + "/api/user/auth/logout")
                .headers(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + AuthenticatedTokenFixture.invalid());
                })
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void 토큰_발행처가_유효하지_않은_사용자_요청에_대해_게이트웨이로부터_인증_실패_응답을_받을_수_있습니다() {
        webTestClient.post().uri(url + "/api/user/auth/logout")
                .headers(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + AuthenticatedTokenFixture.hacker());
                })
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    void 잘못된_API_요청에_대해_게이트웨이로부터_라우팅_실패_응답을_받을_수_있습니다() {
        webTestClient.post().uri(url + "/api/invalid/auth/logout")
                .headers(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + AuthenticatedTokenFixture.valid());
                })
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

}

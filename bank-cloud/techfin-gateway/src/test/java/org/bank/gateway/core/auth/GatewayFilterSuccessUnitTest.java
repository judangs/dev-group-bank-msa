package org.bank.gateway.core.auth;

import org.bank.core.auth.AuthConstants;
import org.bank.gateway.GatewayApplication;
import org.bank.gateway.core.fixture.AuthenticatedTokenFixture;
import org.bank.gateway.core.stubbing.WebTestJsonRequestMapper;
import org.bank.gateway.core.type.GatewayUnitTestForMockUserApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

@SpringBootTest(
        classes = GatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GatewayFilterSuccessUnitTest extends GatewayUnitTestForMockUserApplication {

    private WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl(stubUrl())
            .build();

    private int port = 8080;
    private String url = String.format("http://localhost:%d", port);


    @Test
    void 사용자_회원가입_요청에_대해_게이트웨이를_거쳐_애플리케이션으로부터_응답을_받을_수_있습니다() throws IOException {
        webTestClient.post().uri(url + "/public/api/user/account/create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(WebTestJsonRequestMapper.body("account-create-request.json"))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void 사용자_로그인_요청에_대해_게이트웨이를_거쳐_애플리케이션으로부터_응답을_받을_수_있습니다() throws IOException {

        webTestClient.post().uri(url + "/public/api/user/auth/login")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(WebTestJsonRequestMapper.body("login-request.json"))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void 사용자_로그아웃_요청에_대해_게이트웨이를_거쳐_애플리케이션으로부터_응답을_받을_수_있습니다() {

        webTestClient.post().uri(url + "/api/user/auth/logout")
                .headers(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + AuthenticatedTokenFixture.valid());
                })
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void 인증기간이_만료된_사용자_요청에_대해_게이트웨이에서_유효한_토큰을_위해_애플리케이션으로부터_토큰_발행을_시도합니다() {
        webTestClient.post().uri(url + "/api/user/auth/logout")
                .headers(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + AuthenticatedTokenFixture.expired());
                })
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

}
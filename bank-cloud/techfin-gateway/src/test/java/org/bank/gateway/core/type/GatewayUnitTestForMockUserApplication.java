package org.bank.gateway.core.type;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.bank.core.auth.AuthConstants;
import org.bank.gateway.core.fixture.AuthenticatedTokenFixture;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GatewayUnitTestForMockUserApplication {

    protected int stubPort = 8081;

    protected WireMockServer userMockServer = new WireMockServer(
            WireMockConfiguration.options()
                    .port(stubPort)
                    .usingFilesUnderClasspath("stubbing/app/user")
    );

    protected GatewayUnitTestForMockUserApplication() {
        스텁을_세팅합니다();
    }


    private void 스텁을_세팅합니다() {
        if(userMockServer.isRunning()) {
            userMockServer.stop();
        }

        userMockServer.start();
        WireMock.configureFor("localhost", 8081);

        사용자_회원가입_요청에_대한_임시_완료_응답을_반환하는_스텁을_생성합니다();
        사용자_로그인_요청에_대한_임시_완료_응답을_반환하는_스텁을_생성합니다();
        사용자_로그아웃_요청에_대한_임시_완료_응답을_반환하는_스텁을_생성합니다();
        만료된_토큰에서_토큰을_재발행하는_임시_완료_응답을_반환하는_스텁을_생성합니다();
    }

    protected String stubUrl() {
        return String.format("http://localhost:%d", stubPort);
    }

    private void 사용자_로그인_요청에_대한_임시_완료_응답을_반환하는_스텁을_생성합니다() {
        userMockServer.stubFor(post(urlEqualTo("/user/auth/login"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("login-response.json")
                )
        );
    }


    private void 사용자_로그아웃_요청에_대한_임시_완료_응답을_반환하는_스텁을_생성합니다() {
        userMockServer.stubFor(post(urlEqualTo("/user/auth/logout"))
                .withHeader(AuthConstants.HeaderField.X_AUTH_CLAIMS, matching(".*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("logout-response.json")
                )
        );
    }

    private void 사용자_회원가입_요청에_대한_임시_완료_응답을_반환하는_스텁을_생성합니다() {
        userMockServer.stubFor(post(urlEqualTo("/user/account/create"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("account-create-response.json")
                )
        );
    }

    private void 만료된_토큰에서_토큰을_재발행하는_임시_완료_응답을_반환하는_스텁을_생성합니다() {
        userMockServer.stubFor(post(urlEqualTo("/user/auth/expire-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(AuthenticatedTokenFixture.valid())
                )
        );
    }
}

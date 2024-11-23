package org.bank.user.core.auth.application.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bank.common.constants.auth.AuthHeaders;
import org.bank.user.core.auth.domain.repository.RefreshTokenRedisRepository;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.dto.credential.LoginRequest;
import org.bank.user.fixture.TestFixtureProvider;
import org.bank.user.global.exception.PermissionException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Set;

;import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("사용자 인증 테스트")
@TestPropertySource(locations = "/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserAuthServiceTest {

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Autowired
    private TestFixtureProvider fixtureProvider;


    private HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
    private UserCredential credential;

    private String authTokenId;

    @BeforeAll
    void setUp() {

        fixtureProvider.createProfileFixture("하루 두끼", "dookie2@github.com");
        credential = fixtureProvider.createCredentialFixture("하루 두끼","dookie", "qpalzm1029!!");
    }

    @Test
    @DisplayName("로그인이 진행된 이후 refresh 토큰은 redis에 저장되어야 한다.")
    @Order(1)
    public void login() {

        LoginRequest request = new LoginRequest("dookie", "qpalzm1029!!");

        userAuthService.login(request, servletResponse);
        Set<String> actual = refreshTokenRedisRepository.findByUser(request.getUserid());

        authTokenId = new ArrayList<>(actual).get(0);
        assertNotEquals(0, actual.size());
    }

    @Test
    @DisplayName("일치하지 않는 패스워드를 입력하면 로그인에 권한과 관련한 예외가 발생해야 한다.")
    public void incorrect() {

        LoginRequest request = new LoginRequest("dookie", "incorrect-password");
        assertThrows(PermissionException.class, () -> userAuthService.login(request, servletResponse));

    }

    @Test
    @DisplayName("로그아웃이 진행된 이후 refresh 토큰은 redis에서 삭제되어야 한다.")
    public void logout() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        String token = authTokenId.split(":")[1];
        String userid = authTokenId.split(":")[2];

        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader(AuthHeaders.USER_ID, userid);

        userAuthService.logout(request);

        assertAll(
                () -> assertTrue(refreshTokenRedisRepository.findById(authTokenId).isEmpty()),
                () -> assertTrue(refreshTokenRedisRepository.findByUser(userid).isEmpty())
        );
    }

    @Test
    @DisplayName("[로그아웃] 요청에 인증 헤더가 없거나 부적절한 경우 예외가 발생해야 한다.")
    public void logoutException() {
        assertThrows(PermissionException.class, () -> userAuthService.logout(servletRequest));
    }

}
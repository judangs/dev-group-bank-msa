package org.bank.user.application.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bank.user.application.service.fixture.TestFixtureProvider;
import org.bank.user.application.util.JwtTokenProvider;
import org.bank.user.domain.credential.UserCredential;
import org.bank.user.domain.credential.repository.RefreshTokenRedisRepository;
import org.bank.user.dto.credential.LoginActionRequest;
import org.bank.user.exception.credential.PermissionException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

;

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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
    private UserCredential credential;

    @BeforeAll
    void setUp() {

        refreshTokenRedisRepository.deleteAll();

        fixtureProvider.createProfileFixture("하루 두끼", "dookie2@github.com");
        credential = fixtureProvider.createCredentialFixture("하루 두끼","dookie", "qpalzm1029!!");
    }

    @Test
    @DisplayName("로그인이 진행된 이후 refresh 토큰은 redis에 저장되어야 한다.")
    @Order(1)
    public void login() {

        LoginActionRequest request = new LoginActionRequest("dookie", "qpalzm1029!!");

        userAuthService.login(request, servletResponse);
        boolean exist = refreshTokenRedisRepository.existsById(
                jwtTokenProvider.createRefreshKeyWithJWT(() -> request.getUserid())
        );

        assertTrue(exist);
    }

    @Test
    @DisplayName("일치하지 않는 패스워드를 입력하면 로그인에 권한과 관련한 예외가 발생해야 한다.")
    public void incorrect() {

        LoginActionRequest request = new LoginActionRequest("dookie", "incorrect-password");
        assertThrows(PermissionException.class, () -> userAuthService.login(request, servletResponse));

    }

    @Test
    @DisplayName("로그아웃이 진행된 이후 refresh 토큰은 redis에서 삭제되어야 한다.")
    public void logout() {

        String refreshTokenKey = jwtTokenProvider.createRefreshKeyWithJWT(() -> credential.getUserid());

        String refresh = refreshTokenRedisRepository.findById(refreshTokenKey).get();

        assertNotNull(refresh);

        Cookie cookie = new Cookie(jwtTokenProvider.REFRESH, refresh);
        when(servletRequest.getCookies()).thenReturn(new Cookie[]{cookie});

        userAuthService.logout(servletRequest);

        boolean exist = refreshTokenRedisRepository.existsById(refreshTokenKey);
        assertFalse(exist);

    }

}
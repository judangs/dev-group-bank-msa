package org.bank.user.core.domain.jwt.service;

import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.AuthenticationException;
import org.bank.user.core.domain.auth.TokenContents;
import org.bank.user.core.domain.fixture.TokenContentsFixture;
import org.bank.user.core.domain.jwt.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtServiceTest.UnitTest.class)
public class JwtServiceTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.user.core.domain.jwt")
    static class UnitTest { }

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtValidator jwtValidator;

    private TokenContents tokenContents;

    @BeforeEach
    public void setUp() {
        tokenContents = TokenContentsFixture.fixture();
    }

    @Test
    void JWT_토큰의_프로퍼티를_조회합니다() {

        assertThat(jwtProperties.getAccessSecret()).isNotNull();
        assertThat(jwtProperties.getRefreshExpire()).isNotNull();
        assertThat(jwtProperties.getIssuer()).isNotNull();
    }

    @Test
    @DisplayName("액세스 JWT 토큰을 생성합니다")
    void 액세스_JWT_토큰을_생성합니다() {

        String access = jwtProvider.generate(tokenContents);
        assertThat(access).isNotNull();
    }

    @Test
    @DisplayName("리프레시 JWT 토큰을 생성합니다")
    void 리프레시_JWT_토큰을_생성합니다() {

        String refresh = jwtProvider.generate(tokenContents, AuthConstants.TokenType.REFRESH);
        assertThat(refresh).isNotNull();
    }

    @Test
    @DisplayName("액세스 JWT 토큰을 검증하고 토큰의 데이터를 가져옵니다")
    void 리프레시_JWT_토큰을_검증하고_토큰의_데이터를_가져옵니다() {

        String refresh = jwtProvider.generate(tokenContents, AuthConstants.TokenType.REFRESH);
        Optional<TokenContents> tokenContents = jwtValidator.retrieveContentsIfNotExpired(refresh);
        assertThat(tokenContents.get()).isNotNull();
    }

    @Test
    @DisplayName("만료된 JWT 토큰을 검증하고 인증 예외를 반환합니다")
    void 만료된_JWT_토큰을_검증하고_인증_예외를_반환합니다() {

        String expiredRefreshToken = TokenContentsFixture.expired(jwtProperties.getRefreshSecret());
        assertThatThrownBy(() -> jwtValidator.retrieveContentsIfNotExpired(expiredRefreshToken))
                .isInstanceOf(AuthenticationException.class);
    }
}
package org.bank.user.core.domain.auth.service;

import org.bank.core.auth.AuthenticationException;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.repository.CredentialRepository;
import org.bank.user.core.domain.auth.repository.SessionTokenRepository;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class SessionManagerTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.user.core.domain.auth",
            "org.bank.user.core.domain.crypto",
            "org.bank.user.core.domain.jwt",
    })
    static class UnitTest { }

    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private CredentialRepository credentialRepository;
    @MockBean
    private SessionTokenRepository tokenRedisRepository;

    private Credential credential;

    @BeforeEach
    public void setUp() {
        String userid = "user-01";
        credential = AccountFixture.authenticated(userid);

        when(credentialRepository.findByUserid(userid)).thenReturn(Optional.ofNullable(credential));
    }

    @Test
    @DisplayName("사용자 로그인을 완료하고 엑세스 토큰을 발급합니다")
    void 사용자_로그인을_완료하고_엑세스_토큰을_발급합니다() {

        String access = authenticationService.login(credential.getUserid(), AccountFixture.password);
        assertThat(access).isNotNull();

    }

    @Test
    @DisplayName("아이디와 일치하는 사용자가 없는 경우 예외를 반환합니다")
    void 아이디와_일치하는_사용자가_없는_경우_예외를_반환합니다() {

        assertThatThrownBy(() -> authenticationService.login("user-02", "password"))
                .isInstanceOf(AuthenticationException.class);

    }

    @Test
    @DisplayName("아이디와 패스워드가 일치하지 않는 경우 예외를 반환합니다")
    void 아이디와_패스워드가_일치하지_않는_경우_예외를_반환합니다() {

        assertThatThrownBy(() -> authenticationService.login(credential.getUserid(), "invalid"))
                .isInstanceOf(AuthenticationException.class);

    }
}
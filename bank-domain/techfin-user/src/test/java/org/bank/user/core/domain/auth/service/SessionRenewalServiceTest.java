package org.bank.user.core.domain.auth.service;

import org.bank.core.auth.AuthConstants;
import org.bank.user.core.domain.account.repository.CredentialRepository;
import org.bank.user.core.domain.auth.TokenContents;
import org.bank.user.core.domain.auth.repository.SessionTokenRepository;
import org.bank.user.core.domain.fixture.TokenContentsFixture;
import org.bank.user.core.domain.jwt.service.JwtProvider;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class SessionRenewalServiceTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.user.core.domain.auth",
            "org.bank.user.core.domain.jwt"
    })
    static class UnitTest { }

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private SessionRenewalService sessionRenewalService;
    @MockBean
    private SessionTokenRepository tokenRedisRepository;
    @MockBean
    private CredentialRepository credentialRepository;


    @Test
    @DisplayName("액세스 토큰을 발급받아 리프레시 토큰의 유효성을 검사하고 액세스 토큰을 재발행합니다")
    void 액세스_토큰을_발급받아_리프레시_토큰의_유효성을_검사하고_액세스_토큰을_재발행합니다() {

        TokenContents tokenContents = TokenContentsFixture.fixture();
        String refresh = jwtProvider.generate(tokenContents, AuthConstants.TokenType.REFRESH);

        when(tokenRedisRepository.findByToken(any())).thenReturn(Optional.ofNullable(refresh));
        String access = sessionRenewalService.validateAndRenewTokenIfNotExpired("");
        assertThat(access).isNotNull();
    }

}
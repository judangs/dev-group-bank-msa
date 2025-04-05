package org.bank.user.core.domain.account.service;

import org.assertj.core.api.Assertions;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.repository.CredentialRepository;
import org.bank.user.core.domain.account.repository.ProfileRepository;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles(profiles = "develop")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CredentialServiceTest.UnitTest.class)
class CredentialServiceTest {

    @Configuration
    @TestPropertySource(locations = "classpath:application.properties")
    @ComponentScan(basePackages = "org.bank.user.core.domain.account")
    static class UnitTest {

    }

    @Configuration
    static class StubPool {
        @MockBean
        private CredentialRepository credentialRepository;
        @MockBean
        private ProfileRepository profileRepository;
    }

    @Autowired
    private CredentialService credentialService;

    @Test
    @DisplayName("새로운 임시 패스워드를 생성합니다")
    void 새로운_임시_패스워드를_생성합니다() {

        Credential credential  = AccountFixture.authenticated("user-01");

        String origin = credential.getPassword();
        String temporal = credentialService.createTemporalPassword(credential);

        Assertions.assertThat(temporal).isNotEqualTo(origin);
    }
}
package org.bank.user.core.domain.crypto;

import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CryptoTest.CryptoUnitTest.class)
class CryptoTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.user.core.domain.crypto")
    static class CryptoUnitTest { }

    @Autowired
    private PasswordProvider passwordProvider;

    @Test
    @DisplayName("유효한 패스워드와 해시를 비교합니다")
    void 유효한_패스워드와_해시를_비교합니다() {

        Credential credential = AccountFixture.authenticated("user-01");
        String password = AccountFixture.password;

        Boolean authenticate = passwordProvider.matches(password, credential.getPassword());
        assertThat(authenticate).isTrue();
    }

    @Test
    @DisplayName("패스워드를 인코딩 합니다.")
    void 패스워드를_인코딩_합니다() {

        Credential credential = AccountFixture.authenticated("user-01");
        passwordProvider.encode(credential);
        assertThat(credential.getPassword()).isNotEqualTo(AccountFixture.password);

    }

    @Test
    @DisplayName("임시 패스워드를 생성합니다.")
    void 임시_패스워드를_생성합니다() {

        String temporal = passwordProvider.generate();
        assertThat(temporal).isNotNull();
    }
}
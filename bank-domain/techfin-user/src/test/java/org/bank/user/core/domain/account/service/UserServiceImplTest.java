package org.bank.user.core.domain.account.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.account.repository.CredentialRepository;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("develop")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserServiceImplTest.UnitTest.class)
class UserServiceImplTest {


    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.user.core.domain.account",
            "org.bank.user.core.domain.crypto"

    })
    static class UnitTest {
    }

    @Autowired
    private AccountManagerService accountManagerService;
    @MockBean(name = "userCredentialRepository")
    private CredentialRepository credentialRepository;
    @MockBean
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        Mockito.reset(credentialRepository);
        when(credentialRepository.findByUserid("userid")).thenReturn(Optional.ofNullable(AccountFixture.credential("userid")));
    }

    @Test
    @DisplayName("회원 프로파일 정보와 계정 정보를 생성합니다")
    void 회원_프로파일_정보와_계정_정보를_생성합니다() {

        Profile profile = AccountFixture.profile("user", "000000-0000000", "user@email.com");
        Credential credential = AccountFixture.credential("user-01");

        ResponseCodeV2 code = accountManagerService.createAccount(profile, credential);
        Assertions.assertThat(code).isEqualTo(ResponseCodeV2.SUCCESS);
    }

    @Test
    @DisplayName("이미 사용하고 있는 계정 정보와 동일한 계정 생성 요청은 실패합니다.")
    void 이미_사용하고_있는_계정_정보와_동일한_계정_생성_요청은_실패합니다() {

//        when(credentialRepository.findByUserid("userid")).thenReturn(Optional.ofNullable(AccountFixture.credential("userid")));

        Profile profile = AccountFixture.profile("user", "000000-0000000", "user@email.com");
        Credential credential = AccountFixture.credential("userid");

        ResponseCodeV2 code = accountManagerService.createAccount(profile, credential);
        Assertions.assertThat(code).isEqualTo(ResponseCodeV2.FAIL);
    }

    @Test
    @DisplayName("사용자가 생성할 수 없는 권한으로 프로파일 정보가 생성되는 것은 금지되어 있습니다")
    void 사용자가_생성할_수_없는_권한으로_프로파일_정보가_생성되는_것은_금지되어_있습니다() {

        Profile profile = AccountFixture.admin("user", "000000-0000000", "user@email.com");
        Credential credential = AccountFixture.credential("user-01");

        ResponseCodeV2 code = accountManagerService.createAccount(profile, credential);
        Assertions.assertThat(code).isEqualTo(ResponseCodeV2.FORBIDDEN);
    }

}
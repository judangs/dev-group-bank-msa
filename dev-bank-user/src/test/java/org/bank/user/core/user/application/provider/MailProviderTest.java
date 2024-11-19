package org.bank.user.core.user.application.provider;

import jakarta.mail.internet.MimeMessage;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingCommandRepository;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingQueryRepository;
import org.bank.user.fixture.TestFixtureProvider;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

@Nested
@DisplayName("메일링 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class MailProviderTest {

    @Autowired
    private MailProvider mailProvider;
    @Autowired
    private TestFixtureProvider testFixtureProvider;
    @Autowired
    private MailVerificationPendingCommandRepository mailCommandRepository;
    @Autowired
    private MailVerificationPendingQueryRepository mailQueryRepository;

    @Mock
    private JavaMailSender mailSender;

    private UserCredential credential;

    @BeforeAll
    public void beforeAll() {
        credential = testFixtureProvider.createCredentialFixture("test-fixture", " fixture", "fixture");
        doNothing().when(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("이메일 생성 시 인증 메일 캐시 저장소에 사용자 정보가 등록되어야 한다.")
    public void sendMailForCreation() {
        mailProvider.sendVerificationAccountMailForCreate(credential, "to-fixture");
        assertFalse(mailCommandRepository.isEmpty());
    }

}
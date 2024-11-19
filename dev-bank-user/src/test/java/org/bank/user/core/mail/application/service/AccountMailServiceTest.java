package org.bank.user.core.mail.application.service;

import jakarta.mail.internet.MimeMessage;
import org.bank.user.core.mail.application.usecase.AccountMailUseCase;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingCommandRepository;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingQueryRepository;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.bank.user.dto.AccountResponse;
import org.bank.user.global.dto.ResponseDto;
import org.bank.user.fixture.TestFixtureProvider;
import org.bank.user.global.mail.CacheType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;

@Nested
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class AccountMailServiceTest {

    @Autowired
    private AccountMailUseCase accountMailService;
    @Autowired
    private MailVerificationPendingQueryRepository mailPendingQueryRepository;
    @Autowired
    private MailVerificationPendingCommandRepository mailPendingCommandRepository;
    @Autowired
    private TestFixtureProvider testFixtureProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @MockBean
    private UserCredentialJpaRepository userCredentialJpaRepository;

    @Mock
    private JavaMailSender mailSender;

    private UserCredential credentialFixture;

    @BeforeAll
    public void beforeAll() {
        lenient().doNothing().when(mailSender).send(any(MimeMessage.class));
        lenient().doReturn(credentialFixture).when(userCredentialJpaRepository).save(any(UserCredential.class));

        credentialFixture = testFixtureProvider.createCredentialFixture("fixture", "fixture-id", "fixture-password");

    }

    @Test
    @DisplayName("[계정 생성] 메일 인증이 완료되면 캐시 저장소에서 계정 정보가 다루어져야 한다")
    public void confirmMailForCreation() {

        String token = UUID.randomUUID().toString();
        mailPendingQueryRepository.save(token, CacheType.CREATE_ACCOUNT, Arrays.asList(credentialFixture));

        ResponseDto actual = accountMailService.confirmAccountEmail(token);
        assertAll(
                () -> assertThrows(NoSuchElementException.class, () -> mailPendingCommandRepository.findById(token)),
                () -> assertEquals("사용자 계정 생성에 성공했습니다.", actual.getMessage())
        );
    }

    @Test
    @DisplayName("[아이디 조회] 메일 인증이 완료되면 캐시 저장소에서 계정 정보가 다루어져야 한다")
    public void confirmMailForFindID() {

        int credentialCount = 5;

        List<UserCredential> credentials = new ArrayList<>();
        for(int idx=0; idx<credentialCount; idx++) {
            credentials.add(UserCredential.builder()
                            .userid("fixture" + idx)
                            .build());
        }

        String token = UUID.randomUUID().toString();
        mailPendingQueryRepository.save(token, CacheType.FIND_ACCOUNT_ID, credentials);
        List<String> actual = ((AccountResponse) accountMailService.confirmAccountEmail(token)).getUserid();

        assertAll(
                () -> assertEquals(credentialCount, actual.size()),
                () -> {
                    for(int idx=0; idx<credentialCount; idx++) {
                        assertEquals(credentials.get(idx).getUserid(), actual.get(idx));
                    }
                }
        );
    }

    @Test
    @DisplayName("[패스워드 찾기] 메일 인증이 완료되면 임시 패스워드가 사용자에게 전송되어야 한다.")
    public void confirmMailForFindPassword() {
        String token = UUID.randomUUID().toString();
        mailPendingQueryRepository.save(token, CacheType.FIND_PASSWORD, Arrays.asList(credentialFixture));

        accountMailService.confirmAccountEmail(token);
        assertFalse(
                passwordEncoder.encode("fixture-password").equals(credentialFixture.getPassword())
        );
    }

    @Test
    @DisplayName("[예외] 부적절한 인증 값이 전달될 경우 인증 실패 예외를 던져야 한다.")
    public void invalidToken() {
        String token = UUID.randomUUID().toString();

        assertThrows(AuthenticationException.class, () -> accountMailService.confirmAccountEmail(token));
        mailPendingQueryRepository.save(token, CacheType.CREATE_ACCOUNT, Arrays.asList(credentialFixture));
    }
}
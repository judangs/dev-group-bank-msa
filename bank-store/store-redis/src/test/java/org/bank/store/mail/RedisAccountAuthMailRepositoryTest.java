package org.bank.store.mail;

import org.bank.core.auth.AuthenticationException;
import org.bank.store.unit.RedisMailStorageUnitTest;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;



@DataRedisTest
@ContextConfiguration(classes = RedisMailStorageUnitTest.class)
class RedisAccountAuthMailRepositoryTest {


    @Autowired
    private RedisAccountAuthMailRepository redisAccountAuthMailRepository;

    private final Credential credential = AccountFixture.authenticated("fixture");

    @Test
    void 메일_인증_시_사용되는_정보를_레디스_저장소에_저장합니다() {
        assertAll(
                () -> assertThatCode(() -> redisAccountAuthMailRepository.save(VerificationReason.CREATE_ACCOUNT, AccountFixture.authenticated("create")))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> redisAccountAuthMailRepository.save(VerificationReason.CHANGE_PASSWORD, AccountFixture.authenticated("change")))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> redisAccountAuthMailRepository.save(VerificationReason.FIND_ACCOUNT_ID, AccountFixture.authenticated("find")))
                        .doesNotThrowAnyException()
        );
    }

    @Test
    void 메일_인증_시_사용되는_정보를_레디스_저장소에서_삭제합니다() {
        assertThatCode(() -> {
            String token = redisAccountAuthMailRepository.save(VerificationReason.CREATE_ACCOUNT, AccountFixture.authenticated("create"));
            redisAccountAuthMailRepository.deleteById(token);
            assertThatCode(() -> redisAccountAuthMailRepository.findById(token))
                    .isInstanceOf(AuthenticationException.class);
        }).doesNotThrowAnyException();
    }

    @Test
    void 인증_메일_토큰으로_메일_발송_이유를_조회합니다() {
        assertThatCode(() ->
            assertThat(redisAccountAuthMailRepository.findVerificationReasonById(
                    redisAccountAuthMailRepository.save(VerificationReason.CREATE_ACCOUNT, credential))
            ).isEqualTo(VerificationReason.CREATE_ACCOUNT))
            .doesNotThrowAnyException();
    }

    @Test
    void 인증_메일_토큰으로_인증된_사용자의_계정_아이디를_조회합니다() {
        assertThatCode(() -> {
            String token = redisAccountAuthMailRepository.save(VerificationReason.FIND_ACCOUNT_ID, credential);
            assertThat(redisAccountAuthMailRepository.findVerifierUserIdById(token))
                    .isNotEmpty()
                    .first()
                    .isEqualTo(credential.getUserid());
        }).doesNotThrowAnyException();
    }


    @Test
    void 인증_메일_토큰으로_메일_인증_정보를_조회합니다() {
        assertThatCode(() -> {
            String token = redisAccountAuthMailRepository.save(VerificationReason.CREATE_ACCOUNT, credential);
            AccountVerificationMail content = redisAccountAuthMailRepository.findById(token);
            assertAll(
                    () -> assertThat(content).isNotNull(),
                    () -> assertThat(content.getReason()).isEqualTo(VerificationReason.CREATE_ACCOUNT),
                    () -> assertThat(content.info().getUsername()).isEqualTo(credential.getUsername()),
                    () -> assertThat(content.info().getUserid()).isEqualTo(credential.getUserid()),
                    () -> assertThat(content.info().getPassword()).isEqualTo(credential.getPassword()),
                    () -> assertThat(content.info().getProfile().getEmail()).isEqualTo(credential.getProfile().getEmail()),
                    () -> assertThat(content.info().getProfile().getResidentNumber()).isEqualTo(credential.getProfile().getResidentNumber()),
                    () -> assertThat(content.info().getCreatedAt()).isEqualTo(credential.getCreatedAt()),
                    () -> assertThat(content.info().getModifiedAt()).isEqualTo(credential.getModifiedAt())
            );
        }).doesNotThrowAnyException();

    }

}
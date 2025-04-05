package org.bank.store.session;

import org.bank.store.unit.RedisSessionStorageUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataRedisTest
@ContextConfiguration(classes = RedisSessionStorageUnitTest.class)
class RedisSessionRepositoryTest  {


    @Autowired
    private RedisSessionTokenRepository redisSessionRepository;

    private final String access = JwtSessionClaimsFixture.access();
    private final String refresh = JwtSessionClaimsFixture.refresh();

    @Test
    void 새션_토큰_정보를_레디스_캐시_저장소에_저장합니다() {
        assertThatCode(() -> redisSessionRepository.save(access, refresh))
                .doesNotThrowAnyException();
    }

    @Test
    void 세션_토큰_정보를_레디스_캐시_저장소에서_조회합니다() {
        assertAll(
                () -> assertThatCode(() -> redisSessionRepository.save(access, refresh))
                        .doesNotThrowAnyException(),
                () -> assertThat(redisSessionRepository.findByToken(access))
                        .isPresent()
                        .hasValueSatisfying(refresh -> assertThat(refresh).isEqualTo(this.refresh))
        );
    }

    @Test
    void 세션_토큰_정보를_레디스_캐시_저장소에서_삭제합니다() {
        assertAll(
                () -> assertThatCode(() -> redisSessionRepository.save(access, refresh))
                        .doesNotThrowAnyException(),
                () -> assertThatCode(() -> redisSessionRepository.deleteByTokenAndUser(access, "fixture"))
                        .doesNotThrowAnyException(),
                () -> assertThat(redisSessionRepository.findByToken(access))
                        .isEmpty()
        );
    }
}

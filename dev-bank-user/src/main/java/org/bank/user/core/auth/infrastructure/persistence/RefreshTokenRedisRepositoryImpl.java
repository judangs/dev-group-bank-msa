package org.bank.user.core.auth.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.auth.domain.TokenPayload;
import org.bank.user.core.auth.domain.repository.RefreshTokenRedisRepository;
import org.bank.user.global.provider.KeyProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepositoryImpl implements RefreshTokenRedisRepository {

    private final StringRedisTemplate stringRedisTemplate;

    private final String REPOSITORY_PREFIX = "refresh:";

    public String createId(KeyProvider keyProvider) {
        return new StringBuilder(REPOSITORY_PREFIX)
                .append(keyProvider.createKey())
                .toString();
    }

    public void save(String id, String token) {

        stringRedisTemplate.opsForValue().set(id, token);
    }

    public void deleteByTokenAndUser(String token, String userid) {

        stringRedisTemplate.delete(String.join(":", token, userid));
        Set<String> keys = findByUser(userid);
        if(!keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

    @Override
    public Optional<String> findByToken(String token) {

        String id = createId(() -> String.join(":", token, "*"));
        Set<String> keys = stringRedisTemplate.keys(id);

        if(keys.isEmpty()) {
            return Optional.empty();
        }
        String refreshToken = stringRedisTemplate.opsForValue().get(keys.iterator().next());
        return Optional.of(refreshToken);
    }

    @Override
    public Optional<String> findByTokenAndUser(String token, TokenPayload payload) {
        String value = stringRedisTemplate.opsForValue().get(
                String.join(":", REPOSITORY_PREFIX, token, payload.getSubject()));

        if(StringUtils.hasText(value)) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    public Optional<String> findById(String id) {

        String token = stringRedisTemplate.opsForValue().get(id);
        if(StringUtils.hasText(token)) {
            return Optional.of(token);
        }

        return Optional.empty();
    }

    // Test용 메서드
    @Override
    public Set<String> findByUser(String userid) {
        return stringRedisTemplate.keys("*" + userid);
    }
}


package org.bank.user.core.auth.infrastructure.persistence;

import lombok.RequiredArgsConstructor;

import org.bank.user.core.auth.domain.repository.RefreshTokenRedisRepository;
import org.bank.user.global.provider.KeyProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public void save(String id, String value) {

        stringRedisTemplate.opsForValue().set(id, value);
    }

    public void delete(String id) {

        stringRedisTemplate.delete(id);
    }

    public void deleteAll() {

        stringRedisTemplate.delete(stringRedisTemplate.keys("*"));
    }

    public Optional<String> findById(String id) {

        return Optional.of(stringRedisTemplate.opsForValue().get(id));
    }

    public boolean existsById(String id) {

        return stringRedisTemplate.hasKey(id);
    }

}


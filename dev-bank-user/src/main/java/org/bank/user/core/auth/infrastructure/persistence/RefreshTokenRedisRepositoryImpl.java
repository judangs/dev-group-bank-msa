package org.bank.user.core.auth.infrastructure.persistence;

import lombok.RequiredArgsConstructor;

import org.bank.user.core.auth.domain.repository.RefreshTokenRedisRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepositoryImpl implements RefreshTokenRedisRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public void save(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void deleteAll() {
        stringRedisTemplate.delete(stringRedisTemplate.keys("*"));
    }

    public Optional<String> findById(String key) {
        return Optional.of(stringRedisTemplate.opsForValue().get(key));
    }

    public boolean existsById(String key) {
        return stringRedisTemplate.hasKey(key);
    }

}


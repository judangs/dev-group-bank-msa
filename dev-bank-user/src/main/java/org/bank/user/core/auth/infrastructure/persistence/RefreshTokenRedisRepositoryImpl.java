package org.bank.user.core.auth.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
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

    public void save(String id, String value) {

        stringRedisTemplate.opsForValue().set(id, value);
    }

    public void delete(String id) {

        stringRedisTemplate.delete(id);
    }

    public void deleteTokenByUser(String token, String userid) {

        stringRedisTemplate.delete(String.join(":", token, userid));
        Set<String> keys = findByUser(userid);
        if(!keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

    public Optional<String> findById(String id) {

        String token = stringRedisTemplate.opsForValue().get(id);
        if(StringUtils.hasText(token)) {
            return Optional.of(token);
        }

        return Optional.empty();
    }

    public Set<String> findByUser(String userid) {
        return stringRedisTemplate.keys("*" + userid);
    }

    public boolean existsById(String id) {

        return stringRedisTemplate.hasKey(id);
    }


}


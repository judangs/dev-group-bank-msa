package org.bank.store.session;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.domain.auth.repository.SessionTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisSessionTokenRepository implements SessionTokenRepository {

    private final RedisSessionTemplateCrudRepository redisSessionTemplateCrudRepository;


    @Override
    public void save(String access, String refresh) {
        redisSessionTemplateCrudRepository.save(new SessionCache(access, refresh));
    }

    @Override
    public Optional<String> findByToken(String access) {
        return redisSessionTemplateCrudRepository.findById(access)
                .map(SessionCache::getRefresh);
    }

    @Override
    public void deleteByTokenAndUser(String access, String userid) {
        redisSessionTemplateCrudRepository.deleteById(access);
    }
}

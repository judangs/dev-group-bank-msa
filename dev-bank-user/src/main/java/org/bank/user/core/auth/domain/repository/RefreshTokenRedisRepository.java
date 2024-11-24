package org.bank.user.core.auth.domain.repository;


import org.bank.user.core.auth.domain.TokenPayload;
import org.bank.user.global.provider.KeyProvider;

import java.util.Optional;
import java.util.Set;

public interface RefreshTokenRedisRepository {

    String createId(KeyProvider keyProvider);
    void save(String id, String token);

    Optional<String> findById(String id);
    Optional<String> findByToken(String token);
    Optional<String> findByTokenAndUser(String token, TokenPayload payload);
    Set<String> findByUser(String userid);

    void deleteByTokenAndUser(String token, String userid);
}

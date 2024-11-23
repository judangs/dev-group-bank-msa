package org.bank.user.core.auth.domain.repository;


import org.bank.user.global.provider.KeyProvider;

import java.util.Optional;
import java.util.Set;

public interface RefreshTokenRedisRepository {

    String createId(KeyProvider keyProvider);

    void save(String id, String value);
    void delete(String id);
    void deleteTokenByUser(String token, String userid);
    Optional<String> findById(String id);
    Set<String> findByUser(String userid);
    boolean existsById(String id);

}

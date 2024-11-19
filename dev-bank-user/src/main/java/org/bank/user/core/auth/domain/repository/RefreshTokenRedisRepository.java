package org.bank.user.core.auth.domain.repository;


import org.bank.user.global.provider.KeyProvider;

import java.util.Optional;

public interface RefreshTokenRedisRepository {

    String createId(KeyProvider keyProvider);

    void save(String id, String value);
    void delete(String id);
    void deleteAll();
    Optional<String> findById(String id);
    boolean existsById(String id);

}

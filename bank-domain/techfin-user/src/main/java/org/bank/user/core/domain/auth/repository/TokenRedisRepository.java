package org.bank.user.core.domain.auth.repository;

import org.bank.user.core.domain.auth.TokenContents;

import java.util.Optional;
import java.util.Set;

public interface TokenRedisRepository {
    void save(String access, String refresh);

    Optional<String> findById(String id);
    Optional<String> findByToken(String token);
    Optional<String> findByTokenAndUser(String token, TokenContents payload);
    Set<String> findByUser(String userid);

    void deleteByTokenAndUser(String token, String userid);
}

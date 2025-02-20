package org.bank.store.mysql.core.user.account;

import org.bank.core.domain.DomainNames;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.bank.user.core.domain.auth.TokenContents;
import org.bank.user.core.domain.auth.repository.TokenRedisRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository(value = "tokenRedisRepository")
@NamedRepositorySource(domain = DomainNames.USER, type = DataSourceType.READWRITE)
public class MockTokenRedisRepository implements TokenRedisRepository {
    @Override
    public void save(String access, String refresh) {

    }

    @Override
    public Optional<String> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<String> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<String> findByTokenAndUser(String token, TokenContents payload) {
        return Optional.empty();
    }

    @Override
    public Set<String> findByUser(String userid) {
        return Set.of();
    }

    @Override
    public void deleteByTokenAndUser(String token, String userid) {

    }
}

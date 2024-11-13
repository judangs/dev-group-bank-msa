package org.bank.user.domain.credential.repository;


import java.util.Optional;

public interface RefreshTokenRedisRepository {

    public void save(String key, String value);
    public void delete(String key);
    public void deleteAll();
    public Optional<String> findById(String key);
    public boolean existsById(String key);

}

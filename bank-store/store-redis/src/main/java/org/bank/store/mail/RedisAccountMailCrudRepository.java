package org.bank.store.mail;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisAccountMailCrudRepository extends CrudRepository<MailCache, String> {
    List<MailCache> findByUserid(String userid);
    void deleteByUserid(String userid);
}

package org.bank.store.session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisSessionTemplateCrudRepository extends CrudRepository<SessionCache, String> {
}

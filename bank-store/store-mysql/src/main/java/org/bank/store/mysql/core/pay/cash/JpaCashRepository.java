package org.bank.store.mysql.core.pay.cash;

import org.bank.pay.core.cash.Cash;
import org.bank.store.mysql.global.infrastructure.JpaBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCashRepository extends JpaBaseRepository<Cash, UUID> {
}

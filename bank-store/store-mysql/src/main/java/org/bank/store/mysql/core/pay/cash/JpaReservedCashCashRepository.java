package org.bank.store.mysql.core.pay.cash;

import org.bank.pay.core.cash.ReservedCharge;
import org.bank.store.mysql.global.infrastructure.JpaBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaReservedCashCashRepository extends JpaBaseRepository<ReservedCharge, UUID> {
}

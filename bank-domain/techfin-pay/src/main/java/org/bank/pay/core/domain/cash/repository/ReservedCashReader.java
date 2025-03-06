package org.bank.pay.core.domain.cash.repository;

import org.bank.pay.core.domain.cash.ReservedCharge;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface ReservedCashReader {
    Page<ReservedCharge> findAllReservedCharges(int page);
    Optional<ReservedCharge> findByScheduledId(UUID scheduledId);
}

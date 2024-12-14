package org.bank.pay.core.cash.repository;

import org.bank.pay.core.cash.ReservedCharge;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservedCashReader {
    List<ReservedCharge> findAllReservedCharges();
    Optional<ReservedCharge> findByScheduledId(UUID scheduledId);
}

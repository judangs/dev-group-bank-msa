package org.bank.pay.core.domain.cash.repository;

import org.bank.pay.core.domain.cash.ReservedCharge;

import java.util.UUID;

public interface ReservedCashStore {
    void save(ReservedCharge reservedCharge);
    void deleteByScheduledId(UUID scheduledId);
}

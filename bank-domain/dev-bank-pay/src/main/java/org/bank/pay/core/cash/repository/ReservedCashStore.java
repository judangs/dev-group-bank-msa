package org.bank.pay.core.cash.repository;

import org.bank.pay.core.cash.ReservedCharge;

import java.util.UUID;

public interface ReservedCashStore {
    void save(ReservedCharge reservedCharge);
    void deleteByScheduledId(UUID scheduledId);
}

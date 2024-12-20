package org.bank.store.mysql.core.pay.cash.infrastructure;


import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.ReservedCharge;
import org.bank.pay.core.cash.repository.ReservedCashStore;
import org.bank.store.mysql.core.pay.cash.JpaReservedCashCashRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservedCashCommandRepository implements ReservedCashStore {

    private final JpaReservedCashCashRepository reservedCashCashRepository;


    @Override
    public void save(ReservedCharge reservedCharge) {
        reservedCashCashRepository.save(reservedCharge);
    }

    @Override
    public void deleteByScheduledId(UUID scheduledId) {
        reservedCashCashRepository.deleteById(scheduledId);
    }
}

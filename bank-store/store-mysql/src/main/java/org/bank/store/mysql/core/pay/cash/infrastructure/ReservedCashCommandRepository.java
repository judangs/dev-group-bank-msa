package org.bank.store.mysql.core.pay.cash.infrastructure;


import lombok.RequiredArgsConstructor;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.cash.ReservedCharge;
import org.bank.pay.core.domain.cash.repository.ReservedCashStore;
import org.bank.store.mysql.core.pay.cash.JpaReservedCashCashRepository;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
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

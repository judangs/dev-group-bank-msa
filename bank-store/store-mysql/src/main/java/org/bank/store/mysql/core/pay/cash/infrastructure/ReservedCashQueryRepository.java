package org.bank.store.mysql.core.pay.cash.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.domain.cash.ReservedCharge;
import org.bank.pay.core.domain.cash.repository.ReservedCashReader;
import org.bank.store.mysql.core.pay.cash.JpaReservedCashCashRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservedCashQueryRepository implements ReservedCashReader {

    private final JpaReservedCashCashRepository jpaReservedCashCashRepository;


    @Override
    public Page<ReservedCharge> findAllReservedCharges(int page) {

        Pageable pageable = PageRequest.of(page, 20);
        return jpaReservedCashCashRepository.findAll(pageable);
    }

    @Override
    public Optional<ReservedCharge> findByScheduledId(UUID scheduledId) {
        return jpaReservedCashCashRepository.findById(scheduledId);
    }
}

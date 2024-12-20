package org.bank.store.mysql.core.pay.cash.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.Cash;
import org.bank.store.mysql.core.pay.cash.JpaCashRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository(value = "cashQueryRepository")
@RequiredArgsConstructor
public class CashQueryRepository {

    private final JpaCashRepository jpaCashRepository;

    public Optional<Cash> findByCash(Cash cash) {
        return jpaCashRepository.findById(cash.getCashId());
    }

}

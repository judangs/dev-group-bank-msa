package org.bank.store.mysql.core.pay.cash.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.Cash;
import org.bank.pay.core.cash.repository.CashStore;
import org.bank.store.mysql.core.pay.cash.JpaCashRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "cashCommandRepository")
@RequiredArgsConstructor
public class CashComandRepository implements CashStore {

    private final JpaCashRepository jpaCashRepository;

    @Override
    public void save(Cash cash) {
        jpaCashRepository.save(cash);
    }
}

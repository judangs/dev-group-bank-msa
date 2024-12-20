package org.bank.store.mongo.core.pay.history.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.history.PayHistory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HistoryCommandRepository {
    private final MongoHistoryRepository mongoHistoryRepository;

    public void save(PayHistory payHistory) {
        mongoHistoryRepository.save(payHistory);
    }

}

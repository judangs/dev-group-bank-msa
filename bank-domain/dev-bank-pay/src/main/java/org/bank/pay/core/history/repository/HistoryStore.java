package org.bank.pay.core.history.repository;

import org.bank.pay.core.history.PayHistory;

public interface HistoryStore {

    void saveHistory(PayHistory history);
}

package org.bank.store.mongo.core.pay.history.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.history.PayHistory;
import org.bank.pay.core.history.PayMethod;
import org.bank.pay.core.history.repository.HistoryReader;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepository implements HistoryReader {

    private final MongoTemplate historyMongoTemplate;

    @Override
    public PayHistory findRecordById(OwnerClaims claims, Long id) {
        return null;
    }

    @Override
    public Page<PayHistory> findAllRecordsByMethodAndBetweenDate(OwnerClaims claims, PayMethod method, Instant from, Instant to, int page) {

        Instant sixMonthsAgo = ZonedDateTime.now()
                .minusMonths(6).toInstant();
        if (from.isBefore(sixMonthsAgo)) {
            to = sixMonthsAgo;
        }

        Pageable pageable = PageRequest.of(page, 10);
        Query query = new Query()
                .addCriteria(Criteria.where("transactionDate").gte(from).lte(to))
                .addCriteria(Criteria.where("userid").is(claims.getUserid()))
                .with(pageable);

        if(method != null) {
            query.addCriteria(Criteria.where("method").is(method));
        }

        List<PayHistory> payHistories = historyMongoTemplate.find(query, PayHistory.class);
        long count = historyMongoTemplate.count(query, PayHistory.class);

        return new PageImpl<>(payHistories, pageable, count);
    }
}

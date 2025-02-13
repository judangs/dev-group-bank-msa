package org.bank.store.mysql.core.persistence.transaction;

import lombok.RequiredArgsConstructor;
import org.bank.core.domain.DomainNames;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TransactionRouter {

    private final Set<PlatformTransactionManager> platformTransactionManagers;
    private final DomainTransactionConverter domainTransactionConverter;

    public PlatformTransactionManager route(DomainNames domain) {

        return platformTransactionManagers.stream()
                .filter(platformTransactionManager -> domainTransactionConverter.convert(platformTransactionManager).equals(domain))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("조건과 일치하는 transactionManager가 빈으로 등록되지 않았습니다."));
    }
}

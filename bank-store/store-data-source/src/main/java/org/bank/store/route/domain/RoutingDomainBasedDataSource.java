package org.bank.store.route.domain;


import jakarta.persistence.TransactionRequiredException;
import org.bank.store.source.DataSourceContextManager;
import org.bank.store.source.NamedHikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;


public class RoutingDomainBasedDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        NamedHikariDataSource namedHikariDataSource = DataSourceContextManager.getDataSourceType();

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if(isReadOnly && !namedHikariDataSource.isReadOnly()) {
            throw new TransactionRequiredException();
        }


        return namedHikariDataSource.getId();
    }
}

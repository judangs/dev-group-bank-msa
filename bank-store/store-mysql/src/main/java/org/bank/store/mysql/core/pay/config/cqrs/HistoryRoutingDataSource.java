package org.bank.store.mysql.core.pay.config.cqrs;

import org.bank.store.mysql.global.config.DataSourceContextManager;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class HistoryRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    public Object determineCurrentLookupKey() {
        NamedDataSource dataSource = DataSourceContextManager.getDataSourceType();

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if(isReadOnly || dataSource.isReadOnlyDataSource()) {
            return DataSourceBeanNames.payHistoryReadOnly;
        }


        return dataSource;
    }
}

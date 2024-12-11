package org.bank.store.mysql.core.pay.config;

import org.bank.store.mysql.global.config.DataSourceContextManager;
import org.bank.store.mysql.core.pay.config.cqrs.HistoryRoutingDataSource;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class PayRoutingDataSource extends AbstractRoutingDataSource {

    private final HistoryRoutingDataSource historyRoutingDataSource;

    public PayRoutingDataSource(HistoryRoutingDataSource historyRoutingDataSource) {
        this.historyRoutingDataSource = historyRoutingDataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {

        NamedDataSource dataSource = DataSourceContextManager.getDataSourceType();
        String dataSourceName = dataSource.getName();

        if(dataSourceName.equals(DataSourceBeanNames.payHistory) || dataSourceName.equals(DataSourceBeanNames.payHistoryReadOnly)){
            return historyRoutingDataSource.determineCurrentLookupKey();
        }

        return dataSource;
    }
}

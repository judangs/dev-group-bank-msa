package org.bank.store.mysql.core.pay.config;

import org.bank.store.mysql.core.pay.config.cqrs.HistoryRoutingDataSource;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
public class PayDomainDataBaseRoutingConfig {

    @DependsOn({DataSourceBeanNames.pay, DataSourceBeanNames.payHistory, DataSourceBeanNames.payHistoryReadOnly})
    @Bean(name = DataSourceBeanNames.route)
    public DataSource routingDataSource(List<NamedDataSource> dataSources) {

        HistoryRoutingDataSource historyRoutingDataSource = new HistoryRoutingDataSource();
        PayRoutingDataSource routingDataSource = new PayRoutingDataSource(historyRoutingDataSource);

        Map<Object, Object> dataSourceMap = dataSources.stream().collect(toMap(NamedDataSource::getName, Function.identity()));

        DataSource defaultDataSource = dataSources.stream()
                .filter(dataSource -> dataSource.getName().equals(DataSourceBeanNames.pay))
                .findFirst().orElse(null);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(Objects.requireNonNull(defaultDataSource));
        return routingDataSource;
    }

    @DependsOn(DataSourceBeanNames.route)
    @Primary
    @Bean
    public DataSource dataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

}

package org.bank.store.mysql.core.pay.config;

import org.bank.store.mysql.core.pay.config.cqrs.HistoryRoutingDataSource;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
public class PayDomainDataBaseRoutingConfig {

//    @DependsOn({DataSourceBeanNames.pay, DataSourceBeanNames.payHistory, DataSourceBeanNames.payHistoryReadOnly})
    @DependsOn({DataSourceBeanNames.pay})
    @Bean(name = DataSourceBeanNames.route)
    public DataSource routingDataSource(Set<NamedDataSource> sources) {

        HistoryRoutingDataSource historyRoutingDataSource = new HistoryRoutingDataSource();
        PayRoutingDataSource routingDataSource = new PayRoutingDataSource(historyRoutingDataSource);

        Map<Object, Object> dataSourceMap = sources.stream()
                .collect(toMap(NamedDataSource::getName, Function.identity()));

        DataSource defaultDataSource = sources.stream()
                .filter(source -> source.getName().equals(DataSourceBeanNames.pay))
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

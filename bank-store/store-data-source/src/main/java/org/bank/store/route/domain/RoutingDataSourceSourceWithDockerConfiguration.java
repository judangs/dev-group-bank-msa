package org.bank.store.route.domain;

import org.bank.store.source.NamedHikariDataSource;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
@EnableAspectJAutoProxy
public class RoutingDataSourceSourceWithDockerConfiguration {

    @Primary
    @DependsOn({"userHikariDataSource", "payHikariDataSource"})
    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(Set<NamedHikariDataSource> namedHikariDataSources) {

        Map<Object, Object> dataSourceMap = namedHikariDataSources.stream()
                .collect(Collectors.toMap(NamedHikariDataSource::getId, NamedHikariDataSource::getDataSource));

        RoutingDomainBasedDataSource routingDomainBasedDataSource = new RoutingDomainBasedDataSource();
        routingDomainBasedDataSource.setTargetDataSources(dataSourceMap);
        return routingDomainBasedDataSource;
    }

}

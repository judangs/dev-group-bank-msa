package org.bank.store.route.domain.pay;

import org.bank.store.source.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.pay-readwrite-docker")
public class PayDataSourceProperties extends DataSourceProperties {
}

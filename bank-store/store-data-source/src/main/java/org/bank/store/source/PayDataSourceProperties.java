package org.bank.store.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.pay-readwrite-docker")
public class PayDataSourceProperties extends DataSourceProperties {
}

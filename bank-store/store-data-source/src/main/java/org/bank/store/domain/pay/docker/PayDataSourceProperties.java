package org.bank.store.domain.pay.docker;

import org.bank.store.source.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.pay-readwrite-docker")
class PayDataSourceProperties extends DataSourceProperties {
}

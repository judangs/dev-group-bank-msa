package org.bank.store.domain.pay.onpremise;

import org.bank.store.source.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.pay-readwrite")
class PayDataSourceProperties extends DataSourceProperties {
}

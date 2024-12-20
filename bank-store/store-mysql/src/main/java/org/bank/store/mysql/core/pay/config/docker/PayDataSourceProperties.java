package org.bank.store.mysql.core.pay.config.docker;

import org.bank.store.mysql.global.config.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "routing-data-source.pay")
public class PayDataSourceProperties extends DataSourceProperties {
}

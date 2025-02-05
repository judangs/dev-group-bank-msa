package org.bank.store.mysql.core.user.config;

import org.bank.store.mysql.global.config.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "data-source.user")
public class UserDataSourceProperties extends DataSourceProperties {
}

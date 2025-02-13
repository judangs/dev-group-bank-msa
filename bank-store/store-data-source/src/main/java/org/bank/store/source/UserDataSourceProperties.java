package org.bank.store.source;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.datasource.user-readwrite-docker")
public class UserDataSourceProperties extends DataSourceProperties {
}

package org.bank.store.route.domain.user;

import org.bank.store.source.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.datasource.user-readwrite-docker")
public class UserDataSourceProperties extends DataSourceProperties {
}

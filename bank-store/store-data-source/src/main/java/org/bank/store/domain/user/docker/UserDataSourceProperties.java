package org.bank.store.domain.user.docker;

import org.bank.store.source.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.datasource.user-readwrite-docker")
class UserDataSourceProperties extends DataSourceProperties {
}

package org.bank.store.domain.user.onpremise;

import org.bank.store.source.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.datasource.user-readwrite")
class UserDataSourceProperties extends DataSourceProperties {
}

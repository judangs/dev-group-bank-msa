package org.bank.store.source;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public interface StoreConnectionBean {
    DataSource dataSource(DataSourceProperties dataSourceProperties);
    NamedHikariDataSource namedHikariDataSource(DataSource dataSource, DataSourceProperties dataSourceProperties);
    LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource userDataSource, JpaProperties jpaProperties);
    PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean userEntityManagerFactory);
}

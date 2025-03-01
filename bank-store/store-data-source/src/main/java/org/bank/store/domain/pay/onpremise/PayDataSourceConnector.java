package org.bank.store.domain.pay.onpremise;

import com.zaxxer.hikari.HikariDataSource;
import org.bank.core.domain.DomainNames;
import org.bank.store.source.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@Profile(value = {"production"})
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.bank.store.mysql.core.pay",
        entityManagerFactoryRef = "payEntityManagerFactory",
        transactionManagerRef = "payTransactionManager"
)
@PropertySource("classpath:pay-app-data-source.properties")
@EnableConfigurationProperties(PayDataSourceProperties.class)
public class PayDataSourceConnector extends JpaConfigurationFactory implements StoreConnectionBean {

    @Bean(name = "payDataSourceProperties")
    public DataSourceProperties payDataSourceProperties() {
        return new PayDataSourceProperties();
    }

    @Override
    @Bean(name = "payDataSource")
    public DataSource dataSource(@Qualifier("payDataSourceProperties") DataSourceProperties dataSourceProperties) {

        HikariDataSource hikariDataSource = new HikariDataSource(dataSourceProperties.getHikari());
        return new LazyConnectionDataSourceProxy(hikariDataSource);
    }

    @Override
    @DependsOn("payDataSource")
    @Bean(name = "payHikariDataSource")
    public NamedHikariDataSource namedHikariDataSource(@Qualifier("payDataSource") DataSource dataSource, @Qualifier("payDataSourceProperties") DataSourceProperties dataSourceProperties) {

        DataSourceProperties.SourceConfig sourceConfig = dataSourceProperties.getSource();

        if(sourceConfig.getType().equals(DataSourceType.READONLY)) {
            return NamedHikariDataSource.asReadOnly(dataSource, sourceConfig.getDomain());
        }

        return NamedHikariDataSource.asReadWrite(dataSource, sourceConfig.getDomain(), sourceConfig.getType());
    }

    @Override
    @Bean(name = "payEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("payDataSource") DataSource dataSource, JpaProperties jpaProperties) {
        String[] entityPackages = {"org.bank.core.domain", "org.bank.pay.core.domain", "org.bank.pay.core.event"};

        return builder.dataSource(dataSource).packages(entityPackages)
                .persistenceUnit(DomainNames.USER.name())
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Override
    @Bean(name = "payTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("payEntityManagerFactory") LocalContainerEntityManagerFactoryBean payEntityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(Objects.requireNonNull(payEntityManagerFactory.getObject()));
        jpaTransactionManager.setPersistenceUnitName(payEntityManagerFactory.getPersistenceUnitName());
        return jpaTransactionManager;
    }
}

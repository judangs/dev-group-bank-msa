package org.bank.store.domain.user.onpremise;

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
        basePackages = "org.bank.store.mysql.core.user",
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager"
)
@PropertySource("classpath:user-app-data-source.properties")
@EnableConfigurationProperties(UserDataSourceProperties.class)
public class UserDataSourceConnector extends JpaConfigurationFactory implements StoreConnectionBean {

    @Bean
    public DataSourceProperties userDataSourceProperties() {
        return new UserDataSourceProperties();
    }

    @Override
    @Bean(name = "userDataSource")
    public DataSource dataSource(@Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {

        HikariDataSource hikariDataSource = new HikariDataSource(dataSourceProperties.getHikari());
        return new LazyConnectionDataSourceProxy(hikariDataSource);
    }

    @Override
    @DependsOn("userDataSource")
    @Bean(name = "userHikariDataSource")
    public NamedHikariDataSource namedHikariDataSource(@Qualifier("userDataSource") DataSource dataSource, @Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {

        DataSourceProperties.SourceConfig sourceConfig = dataSourceProperties.getSource();

        if(sourceConfig.getType().equals(DataSourceType.READONLY)) {
            return NamedHikariDataSource.asReadOnly(dataSource, sourceConfig.getDomain());
        }

        return NamedHikariDataSource.asReadWrite(dataSource, sourceConfig.getDomain(), sourceConfig.getType());
    }


    @Override
    @Bean(name = "userEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("userDataSource") DataSource dataSource, JpaProperties jpaProperties) {
        String[] entityPackages = {"org.bank.core.domain", "org.bank.user.core.domain"};

        return builder.dataSource(dataSource).packages(entityPackages)
                .persistenceUnit(DomainNames.USER.name())
                .properties(jpaProperties.getProperties())
                .build();

    }


    @Override
    @Bean(name = "userTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("userEntityManagerFactory") LocalContainerEntityManagerFactoryBean userEntityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(Objects.requireNonNull(userEntityManagerFactory.getObject()));
        jpaTransactionManager.setPersistenceUnitName(userEntityManagerFactory.getPersistenceUnitName());
        return jpaTransactionManager;
    }
}

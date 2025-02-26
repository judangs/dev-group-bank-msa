package org.bank.store.domain.pay.docker;

import org.bank.core.domain.DomainNames;
import org.bank.store.source.AbstractDockerContainerFacotry;
import org.bank.store.source.DataSourceProperties;
import org.bank.store.source.DataSourceProperties.SourceConfig;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedHikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@Profile(value = {"develop", "test"})
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.bank.store.mysql.core.pay",
        entityManagerFactoryRef = "payEntityManagerFactory",
        transactionManagerRef = "payTransactionManager"
)
@PropertySource("classpath:pay-app-data-source.properties")
@EnableConfigurationProperties(PayDataSourceProperties.class)
public class PayDataSourceContainer extends AbstractDockerContainerFacotry {

    @Bean(name = "payDataSourceProperties")
    public DataSourceProperties payDataSourceProperties() {
        return new PayDataSourceProperties();
    }


    @Override
    @Bean(name = "payDataSource")
    public DataSource dataSource(@Qualifier("payDataSourceProperties") DataSourceProperties dataSourceProperties) {
        container = createContainer(dataSourceProperties, this.MySQLContainerConfigurer());
        return createDataSourceFromContainer(container, dataSourceProperties.getHikari());
    }

    @Override
    @DependsOn("payDataSource")
    @Bean(name = "payHikariDataSource")
    public NamedHikariDataSource namedHikariDataSource(@Qualifier("payDataSource") DataSource payDataSource, @Qualifier("payDataSourceProperties") DataSourceProperties dataSourceProperties) {

        SourceConfig sourceConfig = dataSourceProperties.getSource();

        if(sourceConfig.getType().equals(DataSourceType.READONLY)) {
            return NamedHikariDataSource.asReadOnly(payDataSource, sourceConfig.getDomain());
        }

        return NamedHikariDataSource.asReadWrite(payDataSource, sourceConfig.getDomain(), sourceConfig.getType());
    }

    @Override
    @Bean(name = "payEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("payDataSource") DataSource payDataSource, JpaProperties jpaProperties) {
        String[] entityPackages = {"org.bank.core.domain", "org.bank.pay.core.domain"};
        return createEntityManagerFactory(builder, payDataSource, DomainNames.PAY.name(), jpaProperties, entityPackages);
    }

    @Override
    @Bean(name = "payTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("payEntityManagerFactory") LocalContainerEntityManagerFactoryBean payEntityManagerFactory) {

        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(Objects.requireNonNull(payEntityManagerFactory.getObject()));
        jpaTransactionManager.setPersistenceUnitName(payEntityManagerFactory.getPersistenceUnitName());
        return jpaTransactionManager;
    }

    @Override
    protected MySQLContainerConfigurer MySQLContainerConfigurer() {
        return new MySQLContainerConfigurer() {
            @Override
            public MySQLContainer<?> getContainer() {
                return container;
            }

            @Override
            public MySQLContainer<?> getContainer(boolean readOnly) {
                return container;
            }
        };
    }

}

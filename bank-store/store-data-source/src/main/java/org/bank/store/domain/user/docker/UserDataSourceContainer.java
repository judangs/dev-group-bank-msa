package org.bank.store.domain.user.docker;

import org.bank.core.domain.DomainNames;
import org.bank.store.source.AbstractDockerContainerFacotry;
import org.bank.store.source.DataSourceProperties;
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
        basePackages = "org.bank.store.mysql.core.user",
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager"
)
@PropertySource("classpath:user-app-data-source.properties")
@EnableConfigurationProperties(UserDataSourceProperties.class)
public class UserDataSourceContainer extends AbstractDockerContainerFacotry {

    @Bean(name = "userDataSourceProperties")
    public DataSourceProperties userDataSourceProperties() {
        return new UserDataSourceProperties();
    }

    @Override
    @Bean(name = "userDataSource")
    public DataSource dataSource(@Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {
        container = createContainer(dataSourceProperties, this.MySQLContainerConfigurer());
        return createDataSourceFromContainer(container, dataSourceProperties.getHikari());
    }

    @DependsOn("userDataSource")
    @Bean(name = "userHikariDataSource")
    public NamedHikariDataSource namedHikariDataSource(@Qualifier("userDataSource") DataSource userDataSource, @Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {

        DataSourceProperties.SourceConfig sourceConfig = dataSourceProperties.getSource();

        if(sourceConfig.getType().equals(DataSourceType.READONLY)) {
            return NamedHikariDataSource.asReadOnly(userDataSource, sourceConfig.getDomain());
        }

        return NamedHikariDataSource.asReadWrite(userDataSource, sourceConfig.getDomain(), sourceConfig.getType());
    }

    @Override
    @Bean(name = "userEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("userDataSource") DataSource dataSource, JpaProperties jpaProperties) {
        String[] entityPackages = {"org.bank.core.domain", "org.bank.user.core.domain"};
        return createEntityManagerFactory(builder, dataSource, DomainNames.USER.name(), jpaProperties, entityPackages);
    }

    @Override
    @Bean(name = "userTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("userEntityManagerFactory") LocalContainerEntityManagerFactoryBean userEntityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(Objects.requireNonNull(userEntityManagerFactory.getObject()));
        jpaTransactionManager.setPersistenceUnitName(userEntityManagerFactory.getPersistenceUnitName());
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


package org.bank.store.domain.user.docker;

import org.bank.core.domain.DomainNames;
import org.bank.store.source.UserDataSourceProperties;
import org.bank.store.source.AbstractDockerContainerFacotry;
import org.bank.store.source.DataSourceProperties;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedHikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.bank.store.mysql.core.user",
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager"
)
@PropertySource("classpath:user-app-data-source.properties")
@EnableConfigurationProperties(UserDataSourceProperties.class)
public class UserDataSourceContainer extends AbstractDockerContainerFacotry {

    @Bean(name = "userDataSource")
    public DataSource userDataSource(UserDataSourceProperties userDataSourceProperties) {
        container = createContainer(userDataSourceProperties, this.MySQLContainerConfigurer());
        return createDataSourceFromContainer(container, userDataSourceProperties.getHikari());
    }

    @DependsOn("userDataSource")
    @Bean(name = "userHikariDataSource")
    public NamedHikariDataSource userHikariDataSource(@Qualifier("userDataSource") DataSource userDataSource, UserDataSourceProperties userDataSourceProperties) {

        DataSourceProperties.SourceConfig sourceConfig = userDataSourceProperties.getSource();

        if(sourceConfig.getType().equals(DataSourceType.READONLY)) {
            return NamedHikariDataSource.asReadOnly(userDataSource, sourceConfig.getDomain());
        }

        return NamedHikariDataSource.asReadWrite(userDataSource, sourceConfig.getDomain(), sourceConfig.getType());
    }

    @Override
    @Bean(name = "userEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("userDataSource") DataSource userDataSource, JpaProperties jpaProperties) {
        String[] entityPackages = {"org.bank.core.domain", "org.bank.user.core.domain"};
        return createEntityManagerFactory(builder, userDataSource, DomainNames.USER.name(), jpaProperties, entityPackages);
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


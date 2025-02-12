package org.bank.store.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.Properties;

public abstract class AbstractDockerContainerFacotry {

    protected MySQLContainer<?> container;

    @PreDestroy
    public void destroy() {}

    public interface MySQLContainerConfigurer {
        default MySQLContainer<?> configureContainer(DataSourceProperties property) {
            return new MySQLContainer<>(DockerImageName.parse(property.getDocker().getImage()))
                    .withDatabaseName(property.getSource().getDomain().name())
                    .withUsername(property.getHikari().getUsername())
                    .withPassword(property.getHikari().getPassword())
                    .withReuse(true);
        };
        MySQLContainer<?> getContainer();
        MySQLContainer<?> getContainer(boolean readOnly);
    }

    protected abstract MySQLContainerConfigurer MySQLContainerConfigurer();
    public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource, JpaProperties jpaProperties);
    public abstract PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory);

    @Configuration
    @PropertySource("classpath:jpa-hibernate.properties")
    @EnableConfigurationProperties(JpaProperties.class)
    protected static class JpaManagerConfiguration {
        @Bean
        public JpaProperties jpaProperties() {
            return new JpaProperties();
        }

        @Bean
        public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
            return new EntityManagerFactoryBuilder(new JpaVendorAdapter() {
                @Override
                public PersistenceProvider getPersistenceProvider() {
                    return new HibernatePersistenceProvider();

                }
            }, jpaProperties().getProperties(), null);
        }
    }

    protected LocalContainerEntityManagerFactoryBean createEntityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource, String persistenceUnitName, JpaProperties jpaProperties, String... packages) {
        Properties properties = new Properties();
        properties.putAll(jpaProperties.getProperties());

        return builder.dataSource(dataSource).packages(packages)
                .persistenceUnit(persistenceUnitName)
                .properties(jpaProperties.getProperties())
                .build();
    }

    protected MySQLContainer<?> createContainer(DataSourceProperties property, MySQLContainerConfigurer configurer) {
        MySQLContainer<?> container = configurer.configureContainer(property);

        if(!container.isRunning())
            container.start();
        return container;
    }

    protected DataSource createDataSourceFromContainer(MySQLContainer<?> container, HikariConfig configuration) {
        configuration.setDriverClassName(container.getDriverClassName());
        configuration.setJdbcUrl(container.getJdbcUrl());

        HikariDataSource hikariDataSource = new HikariDataSource(configuration);
        return new LazyConnectionDataSourceProxy(hikariDataSource);
    }
}

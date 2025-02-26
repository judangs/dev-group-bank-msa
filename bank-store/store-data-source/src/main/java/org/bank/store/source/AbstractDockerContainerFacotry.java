package org.bank.store.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

public abstract class AbstractDockerContainerFacotry extends JpaConfigurationFactory implements StoreConnectionBean {

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

    protected LocalContainerEntityManagerFactoryBean createEntityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource, String persistenceUnitName, JpaProperties jpaProperties, String... packages) {
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

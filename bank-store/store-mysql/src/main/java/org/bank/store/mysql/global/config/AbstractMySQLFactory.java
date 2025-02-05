package org.bank.store.mysql.global.config;

import jakarta.annotation.PreDestroy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import static org.bank.store.mysql.global.config.DataSourceProperties.DataSourceProperty;

public abstract class AbstractMySQLFactory {

    protected MySQLContainer<?> container;

    @PreDestroy
    public void destroy() {}

    public interface MySQLContainerConfigurer {
        default MySQLContainer<?> configureContainer(DataSourceProperty property) {
            return new MySQLContainer<>(DockerImageName.parse(property.getDocker().getImage()))
                    .withDatabaseName(property.getDatabase().getName())
                    .withUsername(property.getDatabase().getHikari().getUsername())
                    .withPassword(property.getDatabase().getHikari().getPassword())
                    .withReuse(true);
        };
        MySQLContainer<?> getContainer();
        MySQLContainer<?> getContainer(boolean readOnly);
    }

    protected abstract MySQLContainerConfigurer MySQLContainerConfigurer();

    protected MySQLContainer<?> createContainer(DataSourceProperty property, MySQLContainerConfigurer configurer) {
        MySQLContainer<?> container = configurer.configureContainer(property);

        if(!container.isRunning())
            container.start();
        return container;
    }

    protected DataSource createDataSourceFromContainer(MySQLContainer<?> container) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUsername(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }
}
package org.bank.store.mysql.core.user.config;


import jakarta.persistence.EntityManagerFactory;
import org.bank.core.domain.DomainNames;
import org.bank.store.mysql.global.config.AbstractMySQLFactory;
import org.bank.store.mysql.global.config.DataSourceProperties;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:user-app-data-source-docker.properties")
@EnableConfigurationProperties(UserDataSourceProperties.class)
public class UserDataSourceContainer extends AbstractMySQLFactory {

    @Configuration
    @EnableTransactionManagement
    @EnableJpaRepositories(
            basePackages = "org.bank.store.mysql.core.user",
            entityManagerFactoryRef = "entityManagerFactory",
            transactionManagerRef = "transactionManager"
    )
    @EntityScan(basePackages = {"org.bank.core.domain", "org.bank.user.core.domain"})
    public class JpaConfig {
        @Primary
        @Bean(name = "entityManagerFactory")
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

            Properties properties = new Properties();
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");

            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setPackagesToScan("org.bank.user.core");
            factoryBean.setPersistenceUnitName(DomainNames.USER);
            factoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
            factoryBean.setJpaProperties(properties);
            factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

            return factoryBean;
        }

        @Bean(name = "transactionManager")
        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }
    }

    @Bean
    public DataSource userDataSource(UserDataSourceProperties userDataSourceProperties) {
        DataSourceProperties.DataSourceProperty property = userDataSourceProperties.getDataSourceProperty(DomainNames.USER);
        container = createContainer(property, this.MySQLContainerConfigurer());

        return createDataSourceFromContainer(container);
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

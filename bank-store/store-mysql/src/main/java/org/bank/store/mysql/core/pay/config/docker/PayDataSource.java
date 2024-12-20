package org.bank.store.mysql.core.pay.config.docker;

import jakarta.persistence.EntityManagerFactory;
import org.bank.core.domain.DomainNames;
import org.bank.store.mysql.global.config.AbstractMySQLFactory;
import org.bank.store.mysql.global.config.DataSourceProperties.DataSourceProperty;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
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
@PropertySource("classpath:data-source.properties")
@EnableConfigurationProperties(PayDataSourceProperties.class)
public class PayDataSource extends AbstractMySQLFactory {

    private MySQLContainer<?> payContainer;

    @Configuration
    @EnableTransactionManagement
    @EnableJpaRepositories(
            basePackages = "org.bank.store.mysql.core.pay",
            entityManagerFactoryRef = "entityManagerFactory",
            transactionManagerRef = "transactionManager"
    )
    public static class JapConfig {

        @Primary
        @Bean(name = "entityManagerFactory")
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(
                @Qualifier(DataSourceBeanNames.pay) NamedDataSource dataSource
        ) {

            Properties properties = new Properties();
            properties.put("hibernate.hbm2ddl.auto", "create");
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");

            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource.getDataSource());
            factoryBean.setPackagesToScan("org.bank.pay.core");
            factoryBean.setPersistenceUnitName("pay");
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

    @Primary
    @Bean(name = DataSourceBeanNames.pay)
    public NamedDataSource payGlobalDataSource(PayDataSourceProperties payDataSourceProperties) {

        DataSourceProperty property = payDataSourceProperties.getDataSourceProperty(DomainNames.PAY);
        payContainer = createContainer(property, this.MySQLContainerConfigurer());

        DataSource dataSource = createDataSourceFromContainer(payContainer);
        return NamedDataSource.asWrite(DataSourceBeanNames.pay, DomainNames.PAY, dataSource);
    }



    @Override
    protected MySQLContainerConfigurer MySQLContainerConfigurer() {
        return new MySQLContainerConfigurer() {
            @Override
            public MySQLContainer<?> getContainer() {
                return payContainer;
            }

            @Override
            public MySQLContainer<?> getContainer(boolean readOnly) {
                return payContainer;
            }
        };
    }
}
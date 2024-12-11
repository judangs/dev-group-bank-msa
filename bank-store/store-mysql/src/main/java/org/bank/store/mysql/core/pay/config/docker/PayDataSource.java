package org.bank.store.mysql.core.pay.config.docker;

import org.bank.core.domain.DomainNames;
import org.bank.store.mysql.global.config.AbstractMySQLFactory;
import org.bank.store.mysql.global.config.DataSourceProperties;
import org.bank.store.mysql.global.config.DataSourceProperties.DataSourceProperty;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(PayDataSourceProperties.class)
public class PayDataSource extends AbstractMySQLFactory {

    private MySQLContainer<?> payContainer;

    @Bean(name = DataSourceBeanNames.pay)
    public DataSource createDataSource(DataSourceProperties payDataSourceProperties) {

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
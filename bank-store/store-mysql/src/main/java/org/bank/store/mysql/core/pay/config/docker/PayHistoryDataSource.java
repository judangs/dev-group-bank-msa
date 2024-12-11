package org.bank.store.mysql.core.pay.config.docker;

import org.bank.core.domain.DomainNames;
import org.bank.store.mysql.global.config.AbstractMySQLFactory;
import org.bank.store.mysql.global.config.DataSourceProperties;
import org.bank.store.mysql.global.config.DataSourceProperties.DataSourceProperty;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

@Configuration
public class PayHistoryDataSource extends AbstractMySQLFactory {

    private MySQLContainer<?> payHistoryReaderContainer;
    private MySQLContainer<?> payHistoryWriterContainer;


    @Bean(name = DataSourceBeanNames.payHistory)
    public NamedDataSource payHistoryWriterDataSource(DataSourceProperties payDataSourceProperties) {

        DataSourceProperty property = payDataSourceProperties.getDataSourceProperty("history-write");
        payHistoryWriterContainer = createContainer(property, this.MySQLContainerConfigurer());

        DataSource dataSource =  createDataSourceFromContainer(payHistoryWriterContainer);
        return NamedDataSource.asWrite(DataSourceBeanNames.payHistory, DomainNames.PAY, dataSource);
    }

    @Bean(name = DataSourceBeanNames.payHistoryReadOnly)
    public NamedDataSource payHistoryReaderDataSource(DataSourceProperties payDataSourceProperties) {

        DataSourceProperty property = payDataSourceProperties.getDataSourceProperty("history-read");
        payHistoryReaderContainer = createContainer(property, this.MySQLContainerConfigurer());

        DataSource dataSource = createDataSourceFromContainer(payHistoryReaderContainer);
        return NamedDataSource.asReadOnly(DataSourceBeanNames.payHistoryReadOnly, DomainNames.PAY, dataSource);
    }

    @Override
    protected MySQLContainerConfigurer MySQLContainerConfigurer() {
        return new MySQLContainerConfigurer() {
            @Override
            public MySQLContainer<?> getContainer() {
                return payHistoryWriterContainer;
            }

            @Override
            public MySQLContainer<?> getContainer(boolean readOnly) {
                return payHistoryReaderContainer;
            }
        };
    }
}

package org.bank.store.route.domain.stub;

import org.bank.core.domain.DomainNames;
import org.bank.store.source.DataSourceContextManager;
import org.bank.store.source.NamedHikariDataSource;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestComponent;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@TestComponent
public class PayDomainRepository implements Repository {

    @Autowired @Qualifier("payDataSource")
    private DataSource dataSource;

    @Override
    @NamedRepositorySource(domain = DomainNames.PAY)
    public NamedHikariDataSource routing() {
        return DataSourceContextManager.getDataSourceType();
    }

    @Override
    public Connection connection() throws SQLException {
        return dataSource.getConnection();
    }
}

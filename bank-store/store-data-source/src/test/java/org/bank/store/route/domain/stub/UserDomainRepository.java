package org.bank.store.route.domain.stub;

import org.bank.store.source.DataSourceContextManager;
import org.bank.store.source.NamedHikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@TestComponent
public class UserDomainRepository implements Repository {

    @Autowired
    private DataSource dataSource;

    @Override
    public NamedHikariDataSource routing() {
        return DataSourceContextManager.getDataSourceType();
    }

    @Override
    public Connection connection() throws SQLException {
        return dataSource.getConnection();
    }
}

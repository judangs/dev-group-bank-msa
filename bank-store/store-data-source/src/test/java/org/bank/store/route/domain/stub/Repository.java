package org.bank.store.route.domain.stub;

import org.bank.store.source.NamedHikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public interface Repository {
    NamedHikariDataSource routing();
    public Connection connection() throws SQLException;
}

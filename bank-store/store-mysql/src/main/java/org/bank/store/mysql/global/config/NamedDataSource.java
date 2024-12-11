package org.bank.store.mysql.global.config;

import lombok.Getter;
import lombok.experimental.Delegate;

import javax.sql.DataSource;

@Getter
public class NamedDataSource implements DataSource {
    private final String name;
    private final String domain;
    @Delegate(types = DataSource.class)
    private final DataSource dataSource;

    private boolean isReadOnlyDataSource = false;

    public static NamedDataSource asReadOnly(String name, String domain, DataSource dataSource) {
        return new NamedDataSource(name, domain, dataSource, true);
    }

    public static NamedDataSource asWrite(String name, String domain, DataSource dataSource) {
        return new NamedDataSource(name, domain, dataSource);
    }

    private NamedDataSource(String name, String domain, DataSource dataSource) {
        this.name = name;
        this.domain = domain;
        this.dataSource = dataSource;
    }


    private NamedDataSource(String name, String domain, DataSource dataSource, boolean isReadOnlyDataSource) {
        this.name = name;
        this.domain = domain;
        this.dataSource = dataSource;
        this.isReadOnlyDataSource = isReadOnlyDataSource;
    }
}

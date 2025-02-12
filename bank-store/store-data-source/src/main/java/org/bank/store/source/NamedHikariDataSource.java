package org.bank.store.source;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bank.core.domain.DomainNames;

import javax.sql.DataSource;
import java.util.UUID;

@Getter
public class NamedHikariDataSource {

    private final UUID id;
    private final DomainNames domain;
    private final DataSourceType type;
    private final DataSource dataSource;

    public boolean isReadOnly() {
        return ((HikariDataSource) dataSource).isReadOnly();
    }

    public boolean match(DomainNames domain, DataSourceType type) {
        return this.domain.equals(domain) && this.type.equals(type);
    }

    private NamedHikariDataSource(DataSource dataSource, DomainNames domain, DataSourceType type) {
        this.id = UUID.randomUUID();
        this.domain = domain;
        this.type = type;
        this.dataSource = dataSource;
    }

    private NamedHikariDataSource(DataSource dataSource, DomainNames domain) {
        this.id = UUID.randomUUID();
        this.domain = domain;
        this.type = DataSourceType.READONLY;
        this.dataSource = dataSource;
    }

    public static NamedHikariDataSource asReadOnly(DataSource dataSource, DomainNames domain) {
        return new NamedHikariDataSource(dataSource, domain);
    }

    public static NamedHikariDataSource asReadWrite(DataSource dataSource, DomainNames domain, DataSourceType type) {
        return new NamedHikariDataSource(dataSource, domain, type);
    }

}
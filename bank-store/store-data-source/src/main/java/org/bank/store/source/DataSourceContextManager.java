package org.bank.store.source;

import org.springframework.core.NamedThreadLocal;

public class DataSourceContextManager {

    private static final ThreadLocal<NamedHikariDataSource> contextHolder = new NamedThreadLocal<>("managing dataSource in this thread local");

    public static void setDataSourceType(NamedHikariDataSource dataSource) {
        contextHolder.set(dataSource);
    }

    public static NamedHikariDataSource getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}


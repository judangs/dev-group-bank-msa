package org.bank.store.mysql.global.config;

import org.springframework.core.NamedThreadLocal;

public class DataSourceContextManager {

    private static final ThreadLocal<NamedDataSource> contextHolder = new NamedThreadLocal<>("managing dataSource in this thread local");

    public static void setDataSourceType(NamedDataSource dataSource) {
        contextHolder.set(dataSource);
    }

    public static NamedDataSource getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}

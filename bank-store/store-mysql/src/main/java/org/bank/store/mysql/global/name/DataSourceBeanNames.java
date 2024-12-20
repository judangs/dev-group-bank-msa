package org.bank.store.mysql.global.name;

public class DataSourceBeanNames {

    // 라우팅 dataSource
    public static final String route = "routingDataSource";

    // pay 도메인 dataSource
    public static final String pay = "payGlobalDataSource";
    public static final String payHistory = "payAllHistoryDataSource";
    public static final String payHistoryReadOnly = "payHistoryReadOnlyDataSource";

}

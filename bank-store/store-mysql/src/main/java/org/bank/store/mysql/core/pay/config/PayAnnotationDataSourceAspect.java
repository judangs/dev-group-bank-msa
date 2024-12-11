package org.bank.store.mysql.core.pay.config;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bank.store.mysql.global.config.DataSourceContextManager;
import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.bank.store.mysql.global.config.NamedDataSource;
import org.bank.store.mysql.global.annotation.PayHistoryTransactional;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class PayAnnotationDataSourceAspect {

    private final Set<NamedDataSource> dataSources;

    @Pointcut("@annotation(org.bank.store.mysql.global.annotation.PayHistoryTransactional)")
    private void historyDataSourceMethod() { }

    @Before("historyDataSourceMethod()")
    public Object routeDataSourceContext(ProceedingJoinPoint joinPoint) throws Throwable {

        PayHistoryTransactional payHistoryTransactional = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getAnnotation(PayHistoryTransactional.class);

        try {

            NamedDataSource currentDataSourceContext = null;
            if(payHistoryTransactional.type().equals(PayHistoryTransactional.DataSourceType.READONLY)) {
                currentDataSourceContext = dataSources.stream()
                        .filter(dataSource -> dataSource.getName().equals(DataSourceBeanNames.payHistoryReadOnly))
                        .findFirst().orElse(null);
            }
            else if(payHistoryTransactional.type().equals(PayHistoryTransactional.DataSourceType.WRITE)) {
                currentDataSourceContext = dataSources.stream()
                        .filter(dataSource -> dataSource.getName().equals(DataSourceBeanNames.payHistory))
                        .findFirst().orElse(null);
            }

            DataSourceContextManager.setDataSourceType(Objects.requireNonNull(currentDataSourceContext));

            return joinPoint.proceed();
        } finally {
            DataSourceContextManager.clearDataSourceType();
        }
    }
}

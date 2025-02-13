package org.bank.store.route;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bank.store.source.DataSourceContextManager;
import org.bank.store.source.NamedHikariDataSource;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Aspect
@Component
@RequiredArgsConstructor
public class AnnotationDataSourceAspect {

    private final AnnotationSourceMapper annotationSourceMapper;


    @Pointcut("@annotation(org.bank.store.source.NamedRepositorySource)")
    private void namedRepositorySourceOfMethod() { }

    @Pointcut("@within(org.bank.store.source.NamedRepositorySource)")
    public void namedRepositorySourceOfClass() {}

    @Around("namedRepositorySourceOfClass()")
    public Object routeSourceClassContext(ProceedingJoinPoint joinPoint) throws Throwable {
        NamedRepositorySource namedRepositorySource = joinPoint.getTarget().getClass().getAnnotation(NamedRepositorySource.class);

        try {

            NamedHikariDataSource currentDataSourceContext = annotationSourceMapper.map(namedRepositorySource);
            DataSourceContextManager.setDataSourceType(Objects.requireNonNull(currentDataSourceContext));
            return joinPoint.proceed();

        } finally {
            DataSourceContextManager.clearDataSourceType();
        }
    }

    @Around("namedRepositorySourceOfMethod()")
    public Object routeDataSourceContext(ProceedingJoinPoint joinPoint) throws Throwable {

        NamedRepositorySource namedRepositorySource = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getAnnotation(NamedRepositorySource.class);

        try {

            NamedHikariDataSource currentDataSourceContext = annotationSourceMapper.map(namedRepositorySource);
            DataSourceContextManager.setDataSourceType(Objects.requireNonNull(currentDataSourceContext));
            return joinPoint.proceed();

        } finally {
            DataSourceContextManager.clearDataSourceType();
        }
    }
}

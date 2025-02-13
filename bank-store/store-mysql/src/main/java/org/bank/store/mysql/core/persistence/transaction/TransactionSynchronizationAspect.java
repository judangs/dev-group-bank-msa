package org.bank.store.mysql.core.persistence.transaction;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bank.core.domain.DomainNames;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Aspect
@Order(1)
@EnableAspectJAutoProxy
@Component
@RequiredArgsConstructor
public class TransactionSynchronizationAspect {

    private final TransactionAspectSupport transactionAspectSupport;
    private final TransactionRouter transactionRouter;

    @Pointcut("@within(org.springframework.transaction.annotation.Transactional) || @annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalMethod() { }

    @Pointcut("execution(* org.bank.store.mysql.core.user..*(..)) || execution(* org.bank.user.core.domain.*.service..*(..))")
    public void userTransactionManager() { }

    @Pointcut("execution(* org.bank.store.mysql.core.pay..*(..)) || execution(* org.bank.pay.core.domain..*(..))")
    public void payTransactionManager() { }

    @Around("transactionalMethod() && userTransactionManager()")
    public Object userTransactional(ProceedingJoinPoint joinPoint) throws Throwable {

        transactionAspectSupport.setTransactionManager(transactionRouter.route(DomainNames.USER));
        return joinPoint.proceed();
    }

    @Around("transactionalMethod() && payTransactionManager()")
    public Object payTransactional(ProceedingJoinPoint joinPoint) throws Throwable {

        transactionAspectSupport.setTransactionManager(transactionRouter.route(DomainNames.PAY));
        return joinPoint.proceed();
    }
}


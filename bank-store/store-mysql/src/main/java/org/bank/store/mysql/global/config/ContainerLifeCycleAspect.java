package org.bank.store.mysql.global.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.bank.store.mysql.global.config.AbstractMySQLFactory.MySQLContainerConfigurer;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MySQLContainer;

@Aspect
@Component
public class ContainerLifeCycleAspect {

    @AfterReturning(
            value = "execution(* org.bank.store.mysql.global.config.AbstractMySQLFactory.createContainer(..))",
            returning = "container"
    )
    public void afterCreatingContainer(JoinPoint joinPoint, MySQLContainer<?> container) {
        if (container != null && !container.isRunning()) {
            container.start();
        }
    }


    @Before("execution(* org.bank.store.mysql.global.config.AbstractMySQLFactory.destroy(..))")
    public void stopContainer(JoinPoint joinPoint) {

        Object target = joinPoint.getTarget();
        if (target instanceof MySQLContainerConfigurer) {
            MySQLContainerConfigurer containerConfigurer = (MySQLContainerConfigurer) target;

            MySQLContainer<?> container = containerConfigurer.getContainer();
            MySQLContainer<?> readOnlyContainer = containerConfigurer.getContainer(true);
            if (container != null && container.isRunning())
                container.stop();
            if (readOnlyContainer != null && readOnlyContainer.isRunning())
                container.stop();
        }
    }
}

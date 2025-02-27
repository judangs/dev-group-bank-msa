package org.bank.user.core;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bank.user.global.http.HttpResponseEntityStatusConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ControllerStatusAspect {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;

    @Pointcut("execution(* org.bank.user.core..*Controller.*(..))")
    public void endpoint() {}

    @Around(value = "endpoint()")
    public Object status(ProceedingJoinPoint joinPoint) throws Throwable {

        Object response = joinPoint.proceed();

        if(response instanceof ResponseEntity<?> responseEntity) {
            return httpResponseEntityStatusConverter.convert(responseEntity);
        }
        return response;
    }

}

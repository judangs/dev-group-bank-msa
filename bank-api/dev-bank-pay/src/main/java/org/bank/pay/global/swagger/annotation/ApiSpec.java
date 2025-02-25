package org.bank.pay.global.swagger.annotation;

import org.bank.core.dto.response.ResponseDtoV2;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiSpec {

    String summary() default "Swagger API 명세 요약";
    String description() default "Swagger API 명세 설명";
    Class<?> responseSpec() default ResponseDtoV2.class;

}

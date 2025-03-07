package org.bank.pay.global.swagger.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Parameter(
        name = "cardId",
        in = ParameterIn.PATH,
        description = "카드의 ID",
        required = true
        ,schema = @Schema(type = "UUID"),
        example = "123e4567-e89b-12d3-a456-426614174000")
public @interface SwaggerPayCardPath {
}

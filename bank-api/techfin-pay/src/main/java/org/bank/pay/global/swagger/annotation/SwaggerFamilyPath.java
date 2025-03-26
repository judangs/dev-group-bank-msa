package org.bank.pay.global.swagger.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Parameter(
        name = "familyId",
        in = ParameterIn.PATH,
        description = "패밀리 아이디",
        required = true
        ,schema = @Schema(type = "string", format = "uuid"),
        example = "d4e5f6a7-8b9c-4a3e-b2f1-c2d3e4f5a6b7")
public @interface SwaggerFamilyPath {
}

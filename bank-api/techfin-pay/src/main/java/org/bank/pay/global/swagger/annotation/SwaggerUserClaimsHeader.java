package org.bank.pay.global.swagger.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.bank.core.auth.AuthConstants;
import org.bank.pay.global.swagger.data.AccountPayload;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Parameter(
        name = AuthConstants.HeaderField.X_AUTH_CLAIMS,
        description = "인증된 사용자 정보 헤더",
        required = true,
        in = ParameterIn.HEADER,
        example = AccountPayload.AUTHCLAIMS_HEADER_PAYLOAD
)
public @interface SwaggerUserClaimsHeader {
}

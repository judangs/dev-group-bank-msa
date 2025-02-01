package org.bank.core.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUser {
    String headerName() default AuthConstants.HeaderField.X_AUTH_CLAIMS;
    boolean required() default true;
}


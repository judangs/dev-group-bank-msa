package org.bank.user.core.domain.account.mail.service;

import org.bank.user.core.domain.mail.JavaAuthEmailConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "org.bank.user.core.domain.mail",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = JavaAuthEmailConfig.class
        )
)
public class AuthMailComponentScanner {
}

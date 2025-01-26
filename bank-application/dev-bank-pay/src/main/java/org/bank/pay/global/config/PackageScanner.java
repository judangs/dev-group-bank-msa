package org.bank.pay.global.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages ={
        "org.bank.pay.core",
        "org.bank.store.mysql.core.pay"

})
public class PackageScanner {
}

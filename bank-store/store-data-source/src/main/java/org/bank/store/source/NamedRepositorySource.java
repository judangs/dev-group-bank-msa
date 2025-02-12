package org.bank.store.source;

import org.bank.core.domain.DomainNames;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface NamedRepositorySource {
    DomainNames domain() default DomainNames.NONE;
    DataSourceType type() default DataSourceType.READWRITE;
}

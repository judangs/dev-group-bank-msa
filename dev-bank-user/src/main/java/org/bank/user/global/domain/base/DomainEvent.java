package org.bank.user.global.domain.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public abstract class DomainEvent {

    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";

    public static final String CREATED = "CREATED ";
    public static final String MODIFY = "MODIFY ";
    public static final String DELETE = "DELETE ";
    public static final String UPDATE = "UPDATE ";

    public static final String CREDENTIAL = "CREDENTIAL";
    public static final String PROFILE = "CREDENTIAL";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    public void publishEvent() {
        eventPublisher.publishEvent(this);
    }

}

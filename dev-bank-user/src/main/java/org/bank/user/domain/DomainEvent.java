package org.bank.user.domain;

import lombok.Builder;
import org.bank.user.domain.credential.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public abstract class DomainEvent {

    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    public void publishEvent() {
        eventPublisher.publishEvent(this);
    }

}

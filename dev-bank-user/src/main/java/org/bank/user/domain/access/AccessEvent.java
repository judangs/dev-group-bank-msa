package org.bank.user.domain.access;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.bank.user.domain.DomainEvent;

@Getter
public abstract class AccessEvent extends DomainEvent {

    protected String actionType;
    protected String actionStatus;
    protected String currentIP;


}

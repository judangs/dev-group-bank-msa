package org.bank.user.core.user.domain.access;

import lombok.*;
import org.bank.user.global.domain.base.DomainEvent;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AccessEvent extends DomainEvent {

    protected final String actionType;
    protected final String actionStatus;

    @Setter(AccessLevel.PUBLIC)
    protected String username;

    @Setter(AccessLevel.PUBLIC)
    protected String userid;

    public UserCredentialAccess toDomain() {
        return UserCredentialAccess.builder()
                .userid(userid)
                .username(username)
                .actionType(actionType)
                .actionStatus(actionStatus)
                .build();
    }


}

package org.bank.user.core.user.domain.access;


public final class UserAuthEvent extends AccessEvent {

    public UserAuthEvent(String actionType, String actionStatus) {
        super(actionType, actionStatus);
    }

}

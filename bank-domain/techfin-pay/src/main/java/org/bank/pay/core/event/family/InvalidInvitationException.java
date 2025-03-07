package org.bank.pay.core.event.family;

public class InvalidInvitationException extends RuntimeException {

    public InvalidInvitationException(String message) {
        super(message);
    }

    public InvalidInvitationException(String message, Throwable cause) {
        super(message, cause);
    }
}
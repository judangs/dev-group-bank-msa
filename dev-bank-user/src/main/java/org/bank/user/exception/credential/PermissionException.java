package org.bank.user.exception.credential;

import org.bank.user.exception.WarnException;

public final class PermissionException extends WarnException {


    public PermissionException() {
        super(ExceptionType.INSUFFICIENTPERMISSION_ERROR);
    }

    public PermissionException(String message) {
        super(ExceptionType.INSUFFICIENTPERMISSION_ERROR, message);
    }

}

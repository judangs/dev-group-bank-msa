package org.bank.user.global.exception;

import org.bank.user.global.response.ResponseCode;
import org.bank.user.global.response.ResponseMessage;

public final class PermissionException extends WarnException {


    public PermissionException() {

        super(ResponseCode.FORBIDDEN, ResponseMessage.FORBIDDEN);
    }

    public PermissionException(String message) {

        super(ResponseMessage.FORBIDDEN, message);
    }

}

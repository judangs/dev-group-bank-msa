package org.bank.user.global.exception;

import org.bank.user.global.response.ResponseCode;
import org.bank.user.global.response.ResponseMessage;

public class InvalidArgumentException extends WarnException {

    public InvalidArgumentException() {

        super(ResponseCode.INVALID_REQUEST, ResponseMessage.INVALID_ARUGMENT);
    }

    public InvalidArgumentException(String message) {
        super(ResponseCode.INVALID_REQUEST, message);
    }
}

package org.bank.user.global.exception;

public class WarnException extends CommonException {

    protected WarnException(String code, String message) {
        super(ThrowType.WARN, code, message);
    }

}

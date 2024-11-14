package org.bank.user.exception;

public class CriticalException extends CommonException {

    protected  CriticalException(String code, String message) {
        super(ThrowType.CRITICAL, code, message);
    }


}

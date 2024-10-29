package org.bank.user.exception;

import lombok.Getter;

public class CriticalException extends CommonException {

    @Getter
    public enum ErrorType {
        UNKNOWN_ERROR("식별되지 않은 에러입니다.");

        private String content;

        ErrorType(String content) {
            this.content = content;
        }
    }

    protected CriticalException(ErrorType exceptionType) {
        super(ThrowType.CRITICAL, exceptionType.name(), exceptionType.getContent());
    }

    protected  CriticalException(ErrorType exceptionType, String message) {
        super(ThrowType.CRITICAL, exceptionType.name(), message);
    }


}

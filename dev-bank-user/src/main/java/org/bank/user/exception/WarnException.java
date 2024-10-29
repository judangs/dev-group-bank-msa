package org.bank.user.exception;

import lombok.Getter;

public class WarnException extends CommonException {

    @Getter
    protected enum ExceptionType {

        VALIDATION_ERROR("검증 과정에서 에러가 발생했습니다."),
        INSUFFICIENTPERMISSION_ERROR("작업을 실행하기 위한 사용자 권한이 적절하지 않습니다."),
        LOGICAL_ERROR("비즈니스 로직을 실행하는 과정에러 에러가 발생했습니다.");

        private String content;

        ExceptionType(String content) {
            this.content = content;
        }
    }

    protected WarnException(ExceptionType exceptionType) {
        super(ThrowType.WARN, exceptionType.name(), exceptionType.getContent());
    }

    protected WarnException(ExceptionType exceptionType, String message) {
        super(ThrowType.WARN, exceptionType.name(), message);
    }

}

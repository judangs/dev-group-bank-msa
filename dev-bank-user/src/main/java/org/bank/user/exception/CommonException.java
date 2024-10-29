package org.bank.user.exception;

import lombok.Getter;
import org.bank.user.dto.ExceptionResponse;

import java.time.LocalDateTime;

@Getter
public abstract class CommonException extends RuntimeException {

    protected String type;
    protected LocalDateTime createdAt;
    protected Field detail;

    @Getter
    public final class Field {
        private String status;
        private String message;

        protected Field(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    protected enum ThrowType {
        WARN,
        // 개발자에게 에러 내용이 전달되어야 할 시스템에 영향을 줄 수 있는 메시지
        CRITICAL,
        UNDEFINED;
    }

    public ExceptionResponse toResponse() {
        return ExceptionResponse.builder()
                .type(this.type)
                .createdAt(this.createdAt)
                .detail(this.detail)
                .build();
    }

    protected CommonException(ThrowType type, String status, String message) {
        this.type = type.name();
        detail = new Field(status, message);
        createdAt = LocalDateTime.now();
    }
}

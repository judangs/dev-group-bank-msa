package org.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public abstract class ResponseDto {

    protected String code;
    protected String message;
    protected String responseClass;

    @JsonProperty("completed_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime completedAt;


    @SuppressWarnings("unchecked")
    public static <R extends ResponseDto> ResponseDtoBuilder<R, ?> from(Class<R> actionClass) {
        try {
            return (ResponseDtoBuilder<R, ?>) actionClass.getDeclaredMethod("builder").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

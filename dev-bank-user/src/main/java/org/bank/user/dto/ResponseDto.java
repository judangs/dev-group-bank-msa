package org.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bank.user.global.response.ResponseMessage;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ResponseDto {

    protected String code;
    protected String message;

    @JsonProperty("completed_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime completedAt;


    public static ResponseDto success(String message) {
        return ResponseDto.from(ResponseDto.class)
                .code(ResponseMessage.SUCCESS)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }

    @SuppressWarnings("unchecked")
    public static <R extends ResponseDto> ResponseDtoBuilder<R, ?> from(Class<R> actionClass) {
        try {
            return (ResponseDtoBuilder<R, ?>) actionClass.getDeclaredMethod("builder").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

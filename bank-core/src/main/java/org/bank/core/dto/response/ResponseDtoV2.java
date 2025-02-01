package org.bank.core.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ResponseDtoV2 {

    protected ResponseCodeV2 code;
    protected String message;

    @JsonProperty("completed_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime completedAt;

    public ResponseDtoV2() {
        this.code = ResponseCodeV2.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
        this.completedAt = LocalDateTime.now();
    }

    public ResponseDtoV2(ResponseCodeV2 code, String message, LocalDateTime completedAt) {
        this.code = code;
        this.message = message;
        this.completedAt = completedAt;
    }


    public static ResponseDtoV2 success(String message) {
        return ResponseDtoV2.builder()
                .code(ResponseCodeV2.SUCCESS)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public static ResponseDtoV2 fail(String message) {
        return ResponseDtoV2.builder()
                .code(ResponseCodeV2.FAIL)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public static ResponseDtoV2 forbidden(String message) {
        return ResponseDtoV2.builder()
                .code(ResponseCodeV2.FORBIDDEN)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public static ResponseDtoV2 unauthorized(String message) {
        return ResponseDtoV2.builder()
                .code(ResponseCodeV2.UNAUTHORIZED)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public static ResponseDtoV2 of(ResponseCodeV2 code, String message) {
        return ResponseDtoV2.builder()
                .code(code)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }

}

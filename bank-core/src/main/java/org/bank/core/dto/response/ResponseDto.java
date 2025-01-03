package org.bank.core.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ResponseDto {

    protected String code;
    protected String message;

    @JsonProperty("completed_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime completedAt;

    public ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
        this.completedAt = LocalDateTime.now();
    }

    public ResponseDto(String code, String message, LocalDateTime completedAt) {
        this.code = code;
        this.message = message;
        this.completedAt = completedAt;
    }


    public static ResponseDto success(String message) {
        return ResponseDto.builder()
                .code(ResponseMessage.SUCCESS)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public static ResponseDto fail(String message) {
        return ResponseDto.builder()
                .code(ResponseCode.FAIL)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();
    }
}

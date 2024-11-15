package org.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.bank.user.global.exception.CommonException;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponse {

    private String type;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private CommonException.Field detail;

}

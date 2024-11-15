package org.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.bank.user.global.domain.base.DomainEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public abstract class requestDto {

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createAt;

    abstract public Optional<DomainEntity> toDomain(Class<?> domainClass);
}

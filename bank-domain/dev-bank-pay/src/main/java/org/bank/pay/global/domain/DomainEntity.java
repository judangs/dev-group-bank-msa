package org.bank.pay.global.domain;


import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.bank.core.domain.base.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class DomainEntity extends BaseEntity {

    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime modifiedAt;
}

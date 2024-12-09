package org.bank.pay.global.domain;


import jakarta.persistence.Column;
import lombok.Getter;
import org.bank.core.domain.base.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
public abstract class DomainEntity extends BaseEntity {

    @Column(nullable = false)
    protected boolean isDeleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime modifiedAt;
}

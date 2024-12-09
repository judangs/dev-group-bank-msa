package org.bank.core.domain.base;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {
    protected boolean isDeleted = false;
    protected LocalDateTime createdAt;
    protected LocalDateTime modifiedAt;

    public void deleteEntity() {
        isDeleted = true;
    }
}
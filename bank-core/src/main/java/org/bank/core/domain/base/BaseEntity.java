package org.bank.core.domain.base;

import lombok.Getter;

@Getter
public abstract class BaseEntity {
    protected boolean isDeleted = false;

    public void deleteEntity() {
        isDeleted = true;
    }
}
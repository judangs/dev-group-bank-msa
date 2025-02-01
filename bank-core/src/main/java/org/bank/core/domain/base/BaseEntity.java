package org.bank.core.domain.base;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class BaseEntity {

    @Builder.Default
    protected boolean deleted = false;

    public void deleteEntity() {
        deleted = true;
    }
}
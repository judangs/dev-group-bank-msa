package org.bank.user.global.domain.base;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected boolean isDeleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime modifiedAt;


    // publish할 때 각각의 이벤트 객체에 맞춰 필드를 세팅해주고, 도메인에서 저장해서 넘기는 방식을 사용
    public void publish(DomainEvent event) {
        try {
            Method handler = this.getClass().getDeclaredMethod("handler", this.getClass());
            handler.invoke(null);
        } catch (ReflectiveOperationException e) {
            // 개발자가 인지할 수 있는 Critical 수준의 에러 처리하기.
            throw new RuntimeException(e);
        }
    }

    protected Optional<DomainEvent> includeOtherDomainField(DomainEvent event) {

        return Optional.of(event);
    }

    protected final void handler(DomainEvent domainEvent) {
        includeOtherDomainField(domainEvent)
                .ifPresent((event) -> event.publishEvent());
    }


}

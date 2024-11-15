package org.bank.user.core.user.domain.access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.global.domain.base.DomainEntity;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "access")
public class UserCredentialAccess extends DomainEntity {

    @Column(nullable = false)
    private String userid;
    private String username;
    private String actionType;
    private String actionStatus;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = true)
    private String detail;




}

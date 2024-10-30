package org.bank.user.domain.access;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.domain.DomainEntity;
import org.bank.user.domain.DomainEvent;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "access")
public class UserCredentialAccess extends DomainEntity {

    private String actionType;
    private String actionStatus;
    private String currentIP;

    @Lob
    @Column(columnDefinition = "json")
    private String payload;

}

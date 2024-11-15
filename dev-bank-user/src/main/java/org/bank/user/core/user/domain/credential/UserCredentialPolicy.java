package org.bank.user.core.user.domain.credential;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.global.domain.base.DomainEntity;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "policy")
public class UserCredentialPolicy extends DomainEntity {


    private Boolean emailNotiEnabled;
    private Boolean smsNotiEnabled;
    private LocalDateTime passwordExpirationDate;

    private String lang;





}

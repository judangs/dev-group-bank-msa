package org.bank.user.domain.policy;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userCredentialPolicy")
public class UserCredentialPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean emailNotiEnabled;
    private Boolean smsNotiEnabled;
    private LocalDateTime passwordExpirationDate;

    private String lang;





}

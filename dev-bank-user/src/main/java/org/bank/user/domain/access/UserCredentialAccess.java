package org.bank.user.domain.access;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.domain.credential.UserCredential;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userCredentialAccess")
public class UserCredentialAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currentIP;
    private LocalDateTime lastLoginDate;
    private Integer accessAttempt;

}

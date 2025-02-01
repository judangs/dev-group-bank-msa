package org.bank.user.core.domain.account;

import jakarta.persistence.*;
import lombok.*;
import org.bank.user.global.domain.DomainEntity;

import java.util.UUID;

@Getter
@Builder
@Table(name = "user_credential_tb", uniqueConstraints = @UniqueConstraint(columnNames = {"userid", "password"}))
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Credential extends DomainEntity {

    @Id
    @Column(name = "credential_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Column(nullable = false)
    private String userid;

    @Setter
    private String password;

    @Column(nullable = false)
    private String username;

    @Setter
    @Builder.Default
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile = new Profile();


    public void withdraw() {
        deleteEntity();
    }
}

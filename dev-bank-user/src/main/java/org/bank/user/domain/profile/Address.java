package org.bank.user.domain.profile;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String city;
    private String district;
    private String street;

    private String postCode;

    private String detailed;

}

package org.bank.core.common;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private UUID id;
    private String city;
    private String district;
    private String street;
    private String postCode;
    private String detailed;

}

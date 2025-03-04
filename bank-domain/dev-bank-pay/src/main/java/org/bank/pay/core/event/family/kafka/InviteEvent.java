package org.bank.pay.core.event.family.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;

import java.util.UUID;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonTypeName(value = "INVITE")
public class InviteEvent extends FamilyEvent {

    private AuthClaims to;


    public InviteEvent(UUID familyId, AuthClaims to) {
        super(InviteEvent.class, familyId);
        this.to = to;
    }
}

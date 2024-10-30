package org.bank.user.domain.access;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

// login / logout과 관련된 이벤트
@Setter
public final class UserAuthEvent extends AccessEvent {

    private String userid;
    private String username;

}

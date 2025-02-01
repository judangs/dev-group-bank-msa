package org.bank.core.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum Role {

    CUSTOMER("고객", Arrays.asList(UserRole.INDIVIDUAL, UserRole.CORPORATE, UserRole.GROUP)),
    TELLER("관리자", Arrays.asList(UserRole.ADMIN)),
    DEFAULT("구분 없음", Collections.EMPTY_LIST);

    private final String role;
    private final List<UserRole> userRoleGroup;

    Role(String role, List<UserRole> userRoles) {
        this.role = role;
        this.userRoleGroup = userRoles;
    }

    public static Role of(UserRole type) {
        return Arrays.stream(Role.values())
                .filter(RoleType -> {
                    return RoleType.getUserRoleGroup().stream()
                            .anyMatch(userRole -> userRole.getType().equals(type.getType()));
                })
                .findAny()
                .orElse(DEFAULT);
    }


    @Getter
    public enum UserRole {

        INDIVIDUAL("개인"),
        CORPORATE("기업"),
        GROUP("그룹"),
        ADMIN("관리자"),
        NONE("구분 없음");


        private final String type;

        UserRole(String type) {
            this.type = type;
        }

        public static UserRole of(UserRole arg, String username) {
            return Arrays.stream(UserRole.values())
                    .filter(userRole ->
                            !username.isEmpty() &&  userRole.getType().equals(arg.getType()))
                    .findAny()
                    .orElse(NONE);
        }
    }
}


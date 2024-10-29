package org.bank.user.domain.credential;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum RoleClassification {

    CUSTOMER("고객", Arrays.asList(UserRole.INDIVIDUAL, UserRole.CORPORATE, UserRole.GROUP)),
    TELLER("직원", Arrays.asList(UserRole.CUSTOMERSERVICE, UserRole.ADMINISTRATIVE, UserRole.MANAGER)),
    DEFAULT("구분 없음", Collections.EMPTY_LIST);

    private final String role;
    private final List<UserRole> userRoleGroup;

    RoleClassification(String role, List<UserRole> userRoles) {
        this.role = role;
        this.userRoleGroup = userRoles;
    }

    public static RoleClassification of(UserRole type) {
        return Arrays.stream(RoleClassification.values())
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
        MANAGER("담당자"),
        CUSTOMERSERVICE("고객 관리 담당자"),
        ADMINISTRATIVE("사무 업무 담당자"),
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

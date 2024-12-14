package org.bank.pay.core.familly;

import org.bank.core.cash.Money;

import java.math.BigDecimal;

public class FamilyConstraints {

    // 패밀리 멤버 최대 수 제한
    private static final int MAX_MEMBERS = 4;

    public FamilyConstraints() {
        throw new IllegalArgumentException("static 메서드 호출만 가능합니다.");
    }

    // 패밀리 참여자 수는 최대 수보다 작아야 한다.
    public static void validateParticipantsLimit(Family family) {
        if (family.getParticipants().size() > MAX_MEMBERS) {
            throw new IllegalArgumentException("패밀리 멤버 수는 " + MAX_MEMBERS + "명을 초과할 수 없습니다.");
        }
    }

    // 다뤄지는 멤버가 패밀리에 포함되어 있는지 검증한다.
    public static void validateParticipantContaining(Family family, MemberClaims newLeader) {
        if (!family.getParticipants().contains(newLeader)) {
            throw new IllegalArgumentException("패밀리 멤버이어야 합니다.");
        }
    }


    // 패밀리 캐시 전환 금액이 0보다 커야 한다.
    public static void validateCashRemaining(Family family, Money cashToTransfer) {

        BigDecimal remainingCash = family.getFamillyCredit().getBalance().add(cashToTransfer.getBalance());

        if (cashToTransfer == null || remainingCash.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("전환할 CASH 금액은 0보다 커야 합니다.");
        }
    }
}

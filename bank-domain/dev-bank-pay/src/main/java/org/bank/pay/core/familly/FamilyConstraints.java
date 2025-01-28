package org.bank.pay.core.familly;

public class FamilyConstraints {

    // 패밀리 멤버 최대 수 제한
    private static final int MAX_MEMBERS = 4;

    public FamilyConstraints() {
        throw new IllegalArgumentException("static 메서드 호출만 가능합니다.");
    }

    // 패밀리 참여자 수는 최대 수보다 작아야 한다.
    public static void validateParticipantsLimit(Family family) {
        if (family.getParticipants().size() >= MAX_MEMBERS) {
            throw new IllegalArgumentException("패밀리 멤버 수는 " + MAX_MEMBERS + "명을 초과할 수 없습니다.");
        }
    }

    // 다뤄지는 멤버가 패밀리에 포함되어 있는지 검증한다.
    public static void validateParticipantContaining(Family family, MemberClaims member) {
        if (!family.getParticipants().contains(member)) {
            throw new IllegalArgumentException("패밀리 멤버이어야 합니다.");
        }
    }

    public static void isEligibleForInvitation(Family family, MemberClaims leader) {
        MemberClaims familyLeader = family.getLeader();
        if(!familyLeader.equals(leader)) {
            throw new IllegalStateException("패밀리 대표만 멤버를 초대할 수 있습니다.");
        }
        FamilyConstraints.validateParticipantsLimit(family);
    }
}

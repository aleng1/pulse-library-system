package com.pulse.service.member;

import com.pulse.model.Member;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface MemberService {
    List<Member> getAllMembers();
    Optional<Member> getMemberById(Long id);
    Member getMemberByMemberId(String memberId);
    Member getMemberByEmail(String email);
    Member createMember(Member member);
    Member updateMember(Long id, Member member);
    void deleteMember(Long id);
    Member updateMemberStatus(Long id, String status);
    Member renewMembership(Long id, LocalDateTime expiryDate);
    List<Member> findMembersByMembershipType(String membershipType);
    boolean isMembershipActive(Long id);
} 
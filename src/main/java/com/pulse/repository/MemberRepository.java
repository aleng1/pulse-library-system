package com.pulse.repository;

import com.pulse.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // Custom query methods can be added here
    Member findByMemberId(String memberId);
    Member findByEmail(String email);
} 
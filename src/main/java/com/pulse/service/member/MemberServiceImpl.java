package com.pulse.service.member;

import com.pulse.model.Member;
import com.pulse.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Member createMember(Member member) {
        member.setJoinDate(LocalDateTime.now());
        member.setStatus("ACTIVE");
        // Set default expiry date to 1 year from join date
        member.setExpiryDate(member.getJoinDate().plusYears(1));
        return memberRepository.save(member);
    }

    @Override
    public Member updateMember(Long id, Member memberDetails) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPhoneNumber(memberDetails.getPhoneNumber());
        member.setAddress(memberDetails.getAddress());
        member.setMembershipType(memberDetails.getMembershipType());
        
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public Member updateMemberStatus(Long id, String status) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setStatus(status);
        return memberRepository.save(member);
    }

    @Override
    public Member renewMembership(Long id, LocalDateTime expiryDate) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setExpiryDate(expiryDate);
        member.setStatus("ACTIVE");
        return memberRepository.save(member);
    }

    @Override
    public List<Member> findMembersByMembershipType(String membershipType) {
        return memberRepository.findAll().stream()
            .filter(member -> membershipType.equals(member.getMembershipType()))
            .toList();
    }

    @Override
    public boolean isMembershipActive(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        return "ACTIVE".equals(member.getStatus()) && 
               member.getExpiryDate().isAfter(LocalDateTime.now());
    }
} 
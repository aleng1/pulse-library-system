package com.pulse.controller.member;

import com.pulse.model.Member;
import com.pulse.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/memberId/{memberId}")
    public ResponseEntity<Member> getMemberByMemberId(@PathVariable String memberId) {
        Member member = memberService.getMemberByMemberId(memberId);
        return member != null ? ResponseEntity.ok(member) : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        Member member = memberService.getMemberByEmail(email);
        return member != null ? ResponseEntity.ok(member) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        return ResponseEntity.ok(memberService.createMember(member));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
        return ResponseEntity.ok(memberService.updateMember(id, member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Member> updateMemberStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(memberService.updateMemberStatus(id, status));
    }

    @PatchMapping("/{id}/renew")
    public ResponseEntity<Member> renewMembership(
            @PathVariable Long id,
            @RequestParam LocalDateTime expiryDate) {
        return ResponseEntity.ok(memberService.renewMembership(id, expiryDate));
    }

    @GetMapping("/type/{membershipType}")
    public List<Member> getMembersByMembershipType(@PathVariable String membershipType) {
        return memberService.findMembersByMembershipType(membershipType);
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<Boolean> isMembershipActive(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.isMembershipActive(id));
    }
} 
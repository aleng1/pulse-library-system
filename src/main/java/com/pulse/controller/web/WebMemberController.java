package com.pulse.controller.web;

import com.pulse.model.Member;
import com.pulse.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import com.pulse.exception.ResourceNotFoundException;

@Controller
@RequestMapping("/members")
public class WebMemberController {
    
    private final MemberService memberService;

    @Autowired
    public WebMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members";
    }

    @GetMapping("/new")
    public String showNewMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "member-form";
    }

    @PostMapping("/save")
    public String saveMember(@Valid @ModelAttribute("member") Member member, 
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "member-form";
        }
        memberService.createMember(member);
        redirectAttributes.addFlashAttribute("message", "Member added successfully!");
        return "redirect:/members";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        memberService.getMemberById(id).ifPresent(member -> model.addAttribute("member", member));
        return "member-form";
    }

    @PostMapping("/{id}/update")
    public String updateMember(@PathVariable Long id,
                             @Valid @ModelAttribute("member") Member member,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "member-form";
        }
        memberService.updateMember(id, member);
        redirectAttributes.addFlashAttribute("message", "Member updated successfully!");
        return "redirect:/members";
    }

    @GetMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        memberService.deleteMember(id);
        redirectAttributes.addFlashAttribute("message", "Member deleted successfully!");
        return "redirect:/members";
    }

    @GetMapping("/{id}")
    public String showMemberDetails(@PathVariable Long id, Model model) {
        Member member = memberService.getMemberById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        model.addAttribute("member", member);
        return "member-details";
    }
} 
package com.pulse.controller.web;

import com.pulse.model.Event;
import com.pulse.model.Member;
import com.pulse.service.event.EventService;
import com.pulse.service.member.MemberService;
import com.pulse.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/events")
public class WebEventController {
    
    private final EventService eventService;
    private final MemberService memberService;
    private final RoomService roomService;

    @Autowired
    public WebEventController(EventService eventService, MemberService memberService, RoomService roomService) {
        this.eventService = eventService;
        this.memberService = memberService;
        this.roomService = roomService;
    }

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "events";
    }

    @GetMapping("/new")
    public String showNewEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("availableRooms", roomService.getAllRooms());
        return "event-form";
    }

    @PostMapping("/save")
    public String saveEvent(@Valid @ModelAttribute Event event,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "event-form";
        }
        
        eventService.createEvent(event);
        redirectAttributes.addFlashAttribute("message", "Event created successfully!");
        return "redirect:/events";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        model.addAttribute("event", event);
        model.addAttribute("availableRooms", roomService.getAllRooms());
        return "event-form";
    }

    @PostMapping("/update")
    public String updateEvent(@Valid @ModelAttribute Event event,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "event-form";
        }
        
        eventService.updateEvent(event.getId(), event);
        redirectAttributes.addFlashAttribute("message", "Event updated successfully!");
        return "redirect:/events";
    }

    @GetMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("message", "Event deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events";
    }

    @GetMapping("/{id}/participants")
    public String showParticipants(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        Set<Member> participants = eventService.getEventParticipants(id);
        List<Member> availableMembers = memberService.getAllMembers().stream()
            .filter(member -> !participants.contains(member))
            .filter(member -> "ACTIVE".equals(member.getStatus()))
            .toList();

        model.addAttribute("event", event);
        model.addAttribute("participants", participants);
        model.addAttribute("availableMembers", availableMembers);
        return "event-participants";
    }

    @PostMapping("/{eventId}/participants/add")
    public String addParticipant(@PathVariable Long eventId,
                                @RequestParam Long memberId,
                                RedirectAttributes redirectAttributes) {
        try {
            eventService.addParticipant(eventId, memberId);
            redirectAttributes.addFlashAttribute("message", "Participant added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events/" + eventId + "/participants";
    }

    @PostMapping("/{eventId}/participants/{memberId}/remove")
    public String removeParticipant(@PathVariable Long eventId,
                                   @PathVariable Long memberId,
                                   RedirectAttributes redirectAttributes) {
        try {
            eventService.removeParticipant(eventId, memberId);
            redirectAttributes.addFlashAttribute("message", "Participant removed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events/" + eventId + "/participants";
    }
} 
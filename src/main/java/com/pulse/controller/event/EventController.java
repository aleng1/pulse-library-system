package com.pulse.controller.event;

import com.pulse.model.Event;
import com.pulse.model.Member;
import com.pulse.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/events")
public class EventController {
    
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<Event> getEventsByStatus(@PathVariable String status) {
        return eventService.getEventsByStatus(status);
    }

    @GetMapping("/upcoming")
    public List<Event> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Event> updateEventStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(eventService.updateEventStatus(id, status));
    }

    @PostMapping("/{eventId}/participants/{memberId}")
    public ResponseEntity<Event> addParticipant(
            @PathVariable Long eventId,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(eventService.addParticipant(eventId, memberId));
    }

    @DeleteMapping("/{eventId}/participants/{memberId}")
    public ResponseEntity<Event> removeParticipant(
            @PathVariable Long eventId,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(eventService.removeParticipant(eventId, memberId));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<Set<Member>> getEventParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventParticipants(id));
    }

    @GetMapping("/{id}/full")
    public ResponseEntity<Boolean> isEventFull(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.isEventFull(id));
    }
} 
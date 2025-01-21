package com.pulse.service.event;

import com.pulse.model.Event;
import com.pulse.model.Member;
// import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventService {
    List<Event> getAllEvents();
    Optional<Event> getEventById(Long id);
    List<Event> getEventsByStatus(String status);
    List<Event> getUpcomingEvents();
    Event createEvent(Event event);
    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);
    Event updateEventStatus(Long id, String status);
    Event addParticipant(Long eventId, Long memberId);
    Event removeParticipant(Long eventId, Long memberId);
    Set<Member> getEventParticipants(Long eventId);
    boolean isEventFull(Long eventId);
} 
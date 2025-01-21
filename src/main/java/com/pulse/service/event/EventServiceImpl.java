package com.pulse.service.event;

import com.pulse.model.Event;
import com.pulse.model.Member;
import com.pulse.repository.EventRepository;
import com.pulse.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, MemberRepository memberRepository) {
        this.eventRepository = eventRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> getEventsByStatus(String status) {
        return eventRepository.findByStatus(status);
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByStartDateTimeAfter(LocalDateTime.now());
    }

    @Override
    public Event createEvent(Event event) {
        event.setStatus("UPCOMING");
        event.setParticipants(new HashSet<>());
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        
        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setLocation(eventDetails.getLocation());
        event.setStartDateTime(eventDetails.getStartDateTime());
        event.setEndDateTime(eventDetails.getEndDateTime());
        event.setCapacity(eventDetails.getCapacity());
        event.setOrganizer(eventDetails.getOrganizer());
        event.setContactInfo(eventDetails.getContactInfo());
        
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event updateEventStatus(Long id, String status) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(status);
        return eventRepository.save(event);
    }

    @Override
    public Event addParticipant(Long eventId, Long memberId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        if (isEventFull(eventId)) {
            throw new RuntimeException("Event is already full");
        }

        Set<Member> participants = event.getParticipants();
        if (participants.contains(member)) {
            throw new RuntimeException("Member is already registered for this event");
        }

        participants.add(member);
        return eventRepository.save(event);
    }

    @Override
    public Event removeParticipant(Long eventId, Long memberId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        Set<Member> participants = event.getParticipants();
        if (!participants.contains(member)) {
            throw new RuntimeException("Member is not registered for this event");
        }

        participants.remove(member);
        return eventRepository.save(event);
    }

    @Override
    public Set<Member> getEventParticipants(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        return event.getParticipants();
    }

    @Override
    public boolean isEventFull(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        return event.getParticipants().size() >= event.getCapacity();
    }
} 
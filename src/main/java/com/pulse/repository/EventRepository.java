package com.pulse.repository;

import com.pulse.model.Event;
import com.pulse.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Custom query methods can be added here
    List<Event> findByStatus(String status);
    List<Event> findByStartDateTimeAfter(LocalDateTime date);
    List<Event> findByParticipants(Member member);
} 
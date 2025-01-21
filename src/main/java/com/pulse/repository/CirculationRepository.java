package com.pulse.repository;

import com.pulse.model.Circulation;
import com.pulse.model.Book;
import com.pulse.model.Member;
import com.pulse.model.CirculationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CirculationRepository extends JpaRepository<Circulation, Long> {
    // Custom query methods can be added here
    List<Circulation> findByBook(Book book);
    List<Circulation> findByMember(Member member);
    List<Circulation> findByStatus(CirculationStatus status);
    List<Circulation> findByStatusAndDueDateBefore(CirculationStatus status, LocalDateTime date);
    List<Circulation> findByBorrowDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Circulation> findByBorrowDateBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, CirculationStatus status);
    List<Circulation> findByBookTitleContainingIgnoreCaseOrMemberNameContainingIgnoreCase(String bookTitle, String memberName);
} 
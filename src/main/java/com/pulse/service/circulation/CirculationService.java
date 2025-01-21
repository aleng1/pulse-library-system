package com.pulse.service.circulation;

import com.pulse.model.Circulation;
import com.pulse.model.Book;
import com.pulse.model.Member;
import com.pulse.model.CirculationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CirculationService {
    List<Circulation> getAllCirculations();
    Optional<Circulation> getCirculationById(Long id);
    Circulation borrowBook(Long bookId, Long memberId, LocalDateTime dueDate);
    Circulation returnBook(Long id);
    Circulation renewBook(Long id, LocalDateTime newDueDate);
    List<Circulation> getOverdueCirculations();
    List<Circulation> getCirculationsByBook(Book book);
    List<Circulation> getCirculationsByMember(Member member);
    List<Circulation> getCirculationsByStatus(CirculationStatus status);
    boolean isBookOverdue(Long id);
    void checkAndUpdateOverdueStatus();
    
    // Search and filter methods
    List<Circulation> searchCirculations(String keyword);
    List<Circulation> filterCirculations(LocalDateTime startDate, LocalDateTime endDate, CirculationStatus status);
} 
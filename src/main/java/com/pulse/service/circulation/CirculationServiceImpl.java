package com.pulse.service.circulation;

import com.pulse.model.*;
import com.pulse.repository.CirculationRepository;
import com.pulse.repository.BookRepository;
import com.pulse.repository.MemberRepository;
import com.pulse.repository.FineRepository;
import com.pulse.service.fine.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class CirculationServiceImpl implements CirculationService {
    private final CirculationRepository circulationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final FineRepository fineRepository;
    private final FineService fineService;

    // Fine rate per day in dollars
    private static final BigDecimal FINE_RATE_PER_DAY = new BigDecimal("1.00");

    @Autowired
    public CirculationServiceImpl(CirculationRepository circulationRepository,
                                BookRepository bookRepository,
                                MemberRepository memberRepository,
                                FineRepository fineRepository,
                                FineService fineService) {
        this.circulationRepository = circulationRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.fineRepository = fineRepository;
        this.fineService = fineService;
    }

    @Override
    public List<Circulation> getAllCirculations() {
        return circulationRepository.findAll();
    }

    @Override
    public Optional<Circulation> getCirculationById(Long id) {
        return circulationRepository.findById(id);
    }

    @Override
    @Transactional
    public Circulation borrowBook(Long bookId, Long memberId, LocalDateTime dueDate) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for borrowing");
        }

        Circulation circulation = new Circulation();
        circulation.setBook(book);
        circulation.setMember(member);
        circulation.setBorrowDate(LocalDateTime.now());
        circulation.setDueDate(dueDate);
        circulation.setStatus(CirculationStatus.BORROWED);

        // Update book's available copies and status
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        if (book.getAvailableCopies() == 0) {
            book.setStatus("UNAVAILABLE");
        }
        bookRepository.save(book);

        return circulationRepository.save(circulation);
    }

    @Override
    @Transactional
    public Circulation returnBook(Long id) {
        Circulation circulation = circulationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Circulation record not found"));

        if (circulation.getStatus() == CirculationStatus.RETURNED) {
            throw new IllegalStateException("Book is already returned");
        }

        // Calculate and create fine if book is overdue
        if (isBookOverdue(id)) {
            createFineForOverdueBook(circulation);
        }

        // Update circulation record
        circulation.setReturnDate(LocalDateTime.now());
        circulation.setStatus(CirculationStatus.RETURNED);

        // Update book's available copies and status
        Book book = circulation.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        if (book.getAvailableCopies() > 0) {
            book.setStatus("AVAILABLE");
        }
        bookRepository.save(book);

        return circulationRepository.save(circulation);
    }

    @Override
    @Transactional
    public Circulation renewBook(Long id, LocalDateTime newDueDate) {
        Circulation circulation = circulationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Circulation record not found"));

        if (circulation.getStatus() != CirculationStatus.BORROWED) {
            throw new IllegalStateException("Book cannot be renewed - invalid status");
        }

        if (isBookOverdue(id)) {
            throw new IllegalStateException("Overdue books cannot be renewed");
        }

        circulation.setDueDate(newDueDate);
        circulation.setStatus(CirculationStatus.RENEWED);

        return circulationRepository.save(circulation);
    }

    @Override
    public List<Circulation> getOverdueCirculations() {
        return circulationRepository.findByStatusAndDueDateBefore(
            CirculationStatus.BORROWED,
            LocalDateTime.now()
        );
    }

    @Override
    public List<Circulation> getCirculationsByBook(Book book) {
        return circulationRepository.findByBook(book);
    }

    @Override
    public List<Circulation> getCirculationsByMember(Member member) {
        return circulationRepository.findByMember(member);
    }

    @Override
    public List<Circulation> getCirculationsByStatus(CirculationStatus status) {
        return circulationRepository.findByStatus(status);
    }

    @Override
    public boolean isBookOverdue(Long id) {
        Circulation circulation = circulationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Circulation record not found"));

        return circulation.getStatus() == CirculationStatus.BORROWED &&
               circulation.getDueDate().isBefore(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void checkAndUpdateOverdueStatus() {
        List<Circulation> overdueCirculations = circulationRepository.findByStatusAndDueDateBefore(
            CirculationStatus.BORROWED,
            LocalDateTime.now()
        );

        for (Circulation circulation : overdueCirculations) {
            circulation.setStatus(CirculationStatus.OVERDUE);
            createFineForOverdueBook(circulation);
            circulationRepository.save(circulation);
        }
    }

    @Override
    public List<Circulation> searchCirculations(String keyword) {
        return circulationRepository.findByBookTitleContainingIgnoreCaseOrMemberNameContainingIgnoreCase(
            keyword, keyword
        );
    }

    @Override
    public List<Circulation> filterCirculations(LocalDateTime startDate, LocalDateTime endDate, CirculationStatus status) {
        if (status != null) {
            return circulationRepository.findByBorrowDateBetweenAndStatus(startDate, endDate, status);
        }
        return circulationRepository.findByBorrowDateBetween(startDate, endDate);
    }

    private void createFineForOverdueBook(Circulation circulation) {
        // Check if fine already exists
        if (circulation.getFine() != null) {
            return;
        }

        // Calculate days overdue
        long daysOverdue = ChronoUnit.DAYS.between(
            circulation.getDueDate(),
            circulation.getReturnDate() != null ? circulation.getReturnDate() : LocalDateTime.now()
        );

        if (daysOverdue > 0) {
            BigDecimal fineAmount = FINE_RATE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
            
            Fine fine = new Fine();
            fine.setCirculation(circulation);
            fine.setAmount(fineAmount);
            fine.setStatus(FineStatus.PENDING);
            fine.setIssuedDate(LocalDateTime.now());
            
            fineRepository.save(fine);
            circulation.setFine(fine);
        }
    }
} 
package com.pulse.controller.circulation;

import com.pulse.model.Circulation;
import com.pulse.model.Book;
import com.pulse.model.Member;
import com.pulse.model.CirculationStatus;
import com.pulse.service.circulation.CirculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/circulations")
public class CirculationController {
    
    private final CirculationService circulationService;

    @Autowired
    public CirculationController(CirculationService circulationService) {
        this.circulationService = circulationService;
    }

    @GetMapping
    public List<Circulation> getAllCirculations() {
        return circulationService.getAllCirculations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Circulation> getCirculationById(@PathVariable Long id) {
        return circulationService.getCirculationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/book/{bookId}")
    public List<Circulation> getCirculationsByBook(@PathVariable Book book) {
        return circulationService.getCirculationsByBook(book);
    }

    @GetMapping("/member/{memberId}")
    public List<Circulation> getCirculationsByMember(@PathVariable Member member) {
        return circulationService.getCirculationsByMember(member);
    }

    @GetMapping("/status/{status}")
    public List<Circulation> getCirculationsByStatus(@PathVariable CirculationStatus status) {
        return circulationService.getCirculationsByStatus(status);
    }

    @PostMapping("/borrow")
    public ResponseEntity<Circulation> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate) {
        return ResponseEntity.ok(circulationService.borrowBook(bookId, memberId, dueDate));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Circulation> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(circulationService.returnBook(id));
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<Circulation> renewBook(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDueDate) {
        return ResponseEntity.ok(circulationService.renewBook(id, newDueDate));
    }

    @GetMapping("/overdue")
    public List<Circulation> getOverdueCirculations() {
        return circulationService.getOverdueCirculations();
    }

    @GetMapping("/{id}/overdue")
    public ResponseEntity<Boolean> isBookOverdue(@PathVariable Long id) {
        return ResponseEntity.ok(circulationService.isBookOverdue(id));
    }
} 
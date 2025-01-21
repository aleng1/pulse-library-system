package com.pulse.controller.web;

import com.pulse.model.*;
import com.pulse.service.book.BookService;
import com.pulse.service.member.MemberService;
import com.pulse.service.circulation.CirculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/circulations")
public class WebCirculationController {
    private final CirculationService circulationService;
    private final BookService bookService;
    private final MemberService memberService;

    @Autowired
    public WebCirculationController(CirculationService circulationService,
                                  BookService bookService,
                                  MemberService memberService) {
        this.circulationService = circulationService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @GetMapping
    public String listCirculations(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) CirculationStatus status,
            Model model) {
        
        List<Circulation> circulations;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            circulations = circulationService.searchCirculations(keyword);
        } else if (startDate != null && endDate != null) {
            circulations = circulationService.filterCirculations(startDate, endDate, status);
        } else {
            circulations = circulationService.getAllCirculations();
        }
        
        model.addAttribute("circulations", circulations);
        model.addAttribute("circulationStatuses", CirculationStatus.values());
        return "circulations";
    }

    @GetMapping("/borrow")
    public String showBorrowForm(Model model) {
        List<Book> availableBooks = bookService.getAllBooks().stream()
            .filter(book -> book.getAvailableCopies() > 0)
            .toList();
        List<Member> activeMembers = memberService.getAllMembers().stream()
            .filter(member -> "ACTIVE".equals(member.getStatus()))
            .toList();

        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("activeMembers", activeMembers);
        return "borrow-form";
    }

    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long bookId,
                           @RequestParam Long memberId,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dueDate,
                           @RequestParam(required = false) String notes,
                           RedirectAttributes redirectAttributes) {
        try {
            // Circulation circulation = circulationService.borrowBook(bookId, memberId, dueDate);
            redirectAttributes.addFlashAttribute("message", "Book borrowed successfully");
            return "redirect:/circulations";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/circulations/borrow";
        }
    }

    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Circulation circulation = circulationService.returnBook(id);
            String message = "Book returned successfully";
            if (circulation.getFine() != null) {
                message += ". A fine of RM " + circulation.getFine().getAmount() + " has been issued.";
            }
            redirectAttributes.addFlashAttribute("message", message);
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/circulations";
    }

    @PostMapping("/{id}/renew")
    public String renewBook(@PathVariable Long id,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime newDueDate,
                          RedirectAttributes redirectAttributes) {
        try {
            circulationService.renewBook(id, newDueDate);
            redirectAttributes.addFlashAttribute("message", "Book renewed successfully");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/circulations";
    }
} 
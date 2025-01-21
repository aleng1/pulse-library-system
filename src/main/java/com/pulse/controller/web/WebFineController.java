package com.pulse.controller.web;

import com.pulse.model.Fine;
import com.pulse.model.FineStatus;
import com.pulse.service.fine.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
// import java.util.Optional;

@Controller
@RequestMapping("/fines")
public class WebFineController {

    private final FineService fineService;

    @Autowired
    public WebFineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping("/report")
    public String generateReport(
            @RequestParam(required = false) FineStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<Fine> fines = fineService.getFinesForReport(status, startDate, endDate);

        // Calculate summary statistics
        BigDecimal totalFines = BigDecimal.ZERO;
        BigDecimal collectedFines = BigDecimal.ZERO;
        BigDecimal pendingFines = BigDecimal.ZERO;
        BigDecimal waivedFines = BigDecimal.ZERO;

        for (Fine fine : fines) {
            BigDecimal amount = fine.getAmount();
            totalFines = totalFines.add(amount);

            switch (fine.getStatus()) {
                case PAID -> collectedFines = collectedFines.add(amount);
                case PENDING -> pendingFines = pendingFines.add(amount);
                case WAIVED -> waivedFines = waivedFines.add(amount);
            }
        }

        // Add all necessary data to the model
        model.addAttribute("fines", fines);
        model.addAttribute("totalFines", totalFines);
        model.addAttribute("collectedFines", collectedFines);
        model.addAttribute("pendingFines", pendingFines);
        model.addAttribute("waivedFines", waivedFines);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "fine-report";
    }
} 
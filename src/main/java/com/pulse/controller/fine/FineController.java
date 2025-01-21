package com.pulse.controller.fine;

import com.pulse.model.Fine;
import com.pulse.model.Circulation;
import com.pulse.service.fine.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {
    
    private final FineService fineService;

    @Autowired
    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public List<Fine> getAllFines() {
        return fineService.getAllFines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFineById(@PathVariable Long id) {
        return fineService.getFineById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/circulation/{circulationId}")
    public ResponseEntity<Fine> getFineByCirculation(@PathVariable Circulation circulation) {
        Fine fine = fineService.getFineByCirculation(circulation);
        return fine != null ? ResponseEntity.ok(fine) : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public List<Fine> getFinesByStatus(@PathVariable String status) {
        return fineService.getFinesByStatus(status);
    }

    @PostMapping
    public ResponseEntity<Fine> createFine(@Valid @RequestBody Fine fine) {
        return ResponseEntity.ok(fineService.createFine(fine));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fine> updateFine(@PathVariable Long id, @Valid @RequestBody Fine fine) {
        return ResponseEntity.ok(fineService.updateFine(id, fine));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Fine> payFine(
            @PathVariable Long id,
            @RequestParam String paymentMethod) {
        return ResponseEntity.ok(fineService.payFine(id, paymentMethod));
    }

    @PatchMapping("/{id}/amount")
    public ResponseEntity<Fine> updateFineAmount(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(fineService.updateFineAmount(id, amount));
    }

    @GetMapping("/pending")
    public List<Fine> getPendingFines() {
        return fineService.getPendingFines();
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> calculateTotalFines() {
        return ResponseEntity.ok(fineService.calculateTotalFines());
    }
} 
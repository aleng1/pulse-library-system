package com.pulse.service.fine;

import com.pulse.model.Fine;
import com.pulse.model.Circulation;
import com.pulse.model.FineStatus;
import com.pulse.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FineServiceImpl implements FineService {
    
    private final FineRepository fineRepository;

    @Autowired
    public FineServiceImpl(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    @Override
    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    @Override
    public Optional<Fine> getFineById(Long id) {
        return fineRepository.findById(id);
    }

    @Override
    public Fine getFineByCirculation(Circulation circulation) {
        return fineRepository.findByCirculation(circulation);
    }

    @Override
    public List<Fine> getFinesByStatus(String status) {
        return fineRepository.findByStatus(status);
    }

    @Override
    public Fine createFine(Fine fine) {
        fine.setIssuedDate(LocalDateTime.now());
        fine.setStatus(FineStatus.PENDING);
        return fineRepository.save(fine);
    }

    @Override
    public Fine updateFine(Long id, Fine fineDetails) {
        Fine fine = fineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fine not found"));
        
        fine.setAmount(fineDetails.getAmount());
        fine.setStatus(fineDetails.getStatus());
        fine.setNotes(fineDetails.getNotes());
        
        return fineRepository.save(fine);
    }

    @Override
    public Fine payFine(Long id, String paymentMethod) {
        Fine fine = fineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fine not found"));
        
        if (FineStatus.PAID.equals(fine.getStatus())) {
            throw new RuntimeException("Fine is already paid");
        }
        
        fine.setStatus(FineStatus.PAID);
        fine.setPaidDate(LocalDateTime.now());
        fine.setPaymentMethod(paymentMethod);
        
        return fineRepository.save(fine);
    }

    @Override
    public Fine updateFineAmount(Long id, BigDecimal amount) {
        Fine fine = fineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fine not found"));
        
        if (FineStatus.PAID.equals(fine.getStatus())) {
            throw new RuntimeException("Cannot update amount of paid fine");
        }
        
        fine.setAmount(amount);
        return fineRepository.save(fine);
    }

    @Override
    public List<Fine> getPendingFines() {
        return fineRepository.findByStatus("PENDING");
    }

    @Override
    public BigDecimal calculateTotalFines() {
        return fineRepository.findAll().stream()
            .map(Fine::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Fine> getFinesForReport(FineStatus status, LocalDate startDate, LocalDate endDate) {
        List<Fine> fines = fineRepository.findAll();

        // Apply filters
        return fines.stream()
            .filter(fine -> status == null || fine.getStatus() == status)
            .filter(fine -> startDate == null || 
                !fine.getIssuedDate().toLocalDate().isBefore(startDate))
            .filter(fine -> endDate == null || 
                !fine.getIssuedDate().toLocalDate().isAfter(endDate))
            .collect(Collectors.toList());
    }
} 
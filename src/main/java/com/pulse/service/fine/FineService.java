package com.pulse.service.fine;

import com.pulse.model.Fine;
import com.pulse.model.Circulation;
import com.pulse.model.FineStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FineService {
    List<Fine> getAllFines();
    Optional<Fine> getFineById(Long id);
    Fine getFineByCirculation(Circulation circulation);
    List<Fine> getFinesByStatus(String status);
    Fine createFine(Fine fine);
    Fine updateFine(Long id, Fine fine);
    Fine payFine(Long id, String paymentMethod);
    Fine updateFineAmount(Long id, BigDecimal amount);
    List<Fine> getPendingFines();
    BigDecimal calculateTotalFines();
    List<Fine> getFinesForReport(FineStatus status, LocalDate startDate, LocalDate endDate);
} 
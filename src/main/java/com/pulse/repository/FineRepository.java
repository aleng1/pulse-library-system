package com.pulse.repository;

import com.pulse.model.Fine;
import com.pulse.model.Circulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    // Custom query methods can be added here
    Fine findByCirculation(Circulation circulation);
    List<Fine> findByStatus(String status);
} 
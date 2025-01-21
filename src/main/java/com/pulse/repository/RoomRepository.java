package com.pulse.repository;

import com.pulse.model.Room;
import com.pulse.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus status);
    List<Room> findByCapacityGreaterThanEqual(int capacity);
} 
package com.pulse.repository;

import com.pulse.model.Room;
import com.pulse.model.RoomBooking;
import com.pulse.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    List<RoomBooking> findByRoom(Room room);
    List<RoomBooking> findByStatus(BookingStatus status);
    
    @Query("SELECT rb FROM RoomBooking rb WHERE rb.room = ?1 AND rb.status = 'CONFIRMED' AND " +
           "((rb.startTime BETWEEN ?2 AND ?3) OR (rb.endTime BETWEEN ?2 AND ?3) OR " +
           "(rb.startTime <= ?2 AND rb.endTime >= ?3))")
    List<RoomBooking> findConflictingBookings(Room room, LocalDateTime start, LocalDateTime end);
    
    List<RoomBooking> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
} 
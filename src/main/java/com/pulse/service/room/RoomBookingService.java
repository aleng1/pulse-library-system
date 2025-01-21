package com.pulse.service.room;

import com.pulse.model.Room;
import com.pulse.model.RoomBooking;
import com.pulse.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomBookingService {
    List<RoomBooking> getAllBookings();
    Optional<RoomBooking> getBookingById(Long id);
    List<RoomBooking> getBookingsByRoom(Room room);
    List<RoomBooking> getBookingsByStatus(BookingStatus status);
    List<RoomBooking> getBookingsForPeriod(LocalDateTime start, LocalDateTime end);
    RoomBooking createBooking(RoomBooking booking);
    RoomBooking updateBooking(Long id, RoomBooking booking);
    void deleteBooking(Long id);
    RoomBooking updateBookingStatus(Long id, BookingStatus status);
    boolean isRoomAvailable(Room room, LocalDateTime start, LocalDateTime end);
    List<RoomBooking> getConflictingBookings(Room room, LocalDateTime start, LocalDateTime end);
} 
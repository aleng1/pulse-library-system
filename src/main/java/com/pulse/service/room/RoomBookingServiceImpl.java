package com.pulse.service.room;

import com.pulse.model.Room;
import com.pulse.model.RoomBooking;
import com.pulse.model.BookingStatus;
import com.pulse.model.RoomStatus;
import com.pulse.repository.RoomBookingRepository;
import com.pulse.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomBookingServiceImpl implements RoomBookingService {
    
    private final RoomBookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomBooking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<RoomBooking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<RoomBooking> getBookingsByRoom(Room room) {
        return bookingRepository.findByRoom(room);
    }

    @Override
    public List<RoomBooking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<RoomBooking> getBookingsForPeriod(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByStartTimeBetween(start, end);
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {
        if (!isRoomAvailable(booking.getRoom(), booking.getStartTime(), booking.getEndTime())) {
            throw new RuntimeException("Room is not available for the selected time period");
        }
        
        booking.setStatus(BookingStatus.CONFIRMED);
        RoomBooking savedBooking = bookingRepository.save(booking);
        
        // Update room status
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.BOOKED);
        roomRepository.save(room);
        
        return savedBooking;
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking bookingDetails) {
        RoomBooking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Check if new time slot is available (if changed)
        if (!booking.getStartTime().equals(bookingDetails.getStartTime()) ||
            !booking.getEndTime().equals(bookingDetails.getEndTime())) {
            if (!isRoomAvailable(booking.getRoom(), bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
                throw new RuntimeException("Room is not available for the selected time period");
            }
        }
        
        booking.setStartTime(bookingDetails.getStartTime());
        booking.setEndTime(bookingDetails.getEndTime());
        booking.setPurpose(bookingDetails.getPurpose());
        booking.setBookedBy(bookingDetails.getBookedBy());
        booking.setContactInfo(bookingDetails.getContactInfo());
        booking.setNotes(bookingDetails.getNotes());
        
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        RoomBooking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Update room status if this was the only booking
        Room room = booking.getRoom();
        if (getBookingsByRoom(room).size() == 1) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }
        
        bookingRepository.delete(booking);
    }

    @Override
    public RoomBooking updateBookingStatus(Long id, BookingStatus status) {
        RoomBooking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(status);
        
        // Update room status based on booking status
        Room room = booking.getRoom();
        if (status == BookingStatus.CANCELLED || status == BookingStatus.COMPLETED) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }
        
        return bookingRepository.save(booking);
    }

    @Override
    public boolean isRoomAvailable(Room room, LocalDateTime start, LocalDateTime end) {
        if (room.getStatus() == RoomStatus.MAINTENANCE || 
            room.getStatus() == RoomStatus.UNAVAILABLE) {
            return false;
        }
        
        List<RoomBooking> conflictingBookings = getConflictingBookings(room, start, end);
        return conflictingBookings.isEmpty();
    }

    @Override
    public List<RoomBooking> getConflictingBookings(Room room, LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findConflictingBookings(room, start, end);
    }
} 
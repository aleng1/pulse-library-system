package com.pulse.controller.web;

import com.pulse.model.Room;
import com.pulse.model.RoomBooking;
import com.pulse.model.Event;
// import com.pulse.model.RoomStatus;
// import com.pulse.model.BookingStatus;
import com.pulse.service.room.RoomService;
import com.pulse.service.room.RoomBookingService;
import com.pulse.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
// import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebRoomController {
    
    private final RoomService roomService;
    private final RoomBookingService bookingService;
    private final EventService eventService;

    @Autowired
    public WebRoomController(RoomService roomService, RoomBookingService bookingService, EventService eventService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.eventService = eventService;
    }

    // Room Management
    @GetMapping("/rooms")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms";
    }

    @GetMapping("/rooms/new")
    public String showRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room-form";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(@Valid Room room, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "room-form";
        }
        roomService.createRoom(room);
        redirectAttributes.addFlashAttribute("message", "Room created successfully");
        return "redirect:/rooms";
    }

    @GetMapping("/rooms/{id}/edit")
    public String editRoom(@PathVariable Long id, Model model) {
        roomService.getRoomById(id).ifPresent(room -> model.addAttribute("room", room));
        return "room-form";
    }

    @PostMapping("/rooms/{id}/update")
    public String updateRoom(@PathVariable Long id, @Valid Room room, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "room-form";
        }
        roomService.updateRoom(id, room);
        redirectAttributes.addFlashAttribute("message", "Room updated successfully");
        return "redirect:/rooms";
    }

    @PostMapping("/rooms/{id}/delete")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        roomService.deleteRoom(id);
        redirectAttributes.addFlashAttribute("message", "Room deleted successfully");
        return "redirect:/rooms";
    }

    // Room Booking
    @GetMapping("/bookings/new")
    public String showBookingForm(Model model) {
        model.addAttribute("booking", new RoomBooking());
        model.addAttribute("availableRooms", roomService.getAvailableRooms());
        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents());
        return "room-booking-form";
    }

    @PostMapping("/bookings/save")
    public String saveBooking(@Valid RoomBooking booking, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "room-booking-form";
        }
        try {
            bookingService.createBooking(booking);
            redirectAttributes.addFlashAttribute("message", "Booking created successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/calendar";
    }

    @GetMapping("/bookings/{id}/edit")
    public String editBooking(@PathVariable Long id, Model model) {
        bookingService.getBookingById(id).ifPresent(booking -> {
            model.addAttribute("booking", booking);
            model.addAttribute("availableRooms", roomService.getAllRooms());
            model.addAttribute("upcomingEvents", eventService.getUpcomingEvents());
        });
        return "room-booking-form";
    }

    @PostMapping("/bookings/{id}/update")
    public String updateBooking(@PathVariable Long id, @Valid RoomBooking booking, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "room-booking-form";
        }
        try {
            bookingService.updateBooking(id, booking);
            redirectAttributes.addFlashAttribute("message", "Booking updated successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/calendar";
    }

    @PostMapping("/bookings/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.deleteBooking(id);
        redirectAttributes.addFlashAttribute("message", "Booking deleted successfully");
        return "redirect:/calendar";
    }

    // Calendar View
    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(required = false) Long roomId, Model model) {
        List<Room> rooms = roomService.getAllRooms();
        List<Event> events = eventService.getAllEvents();
        List<RoomBooking> bookings = roomId != null ? 
            bookingService.getBookingsByRoom(roomService.getRoomById(roomId).orElseThrow()) :
            bookingService.getAllBookings();
        
        // Convert bookings to calendar events format
        List<Map<String, Object>> calendarEvents = bookings.stream()
            .map(booking -> {
                Map<String, Object> event = new HashMap<>();
                event.put("id", booking.getId());
                event.put("title", booking.getRoom().getName() + 
                    (booking.getEvent() != null ? " - " + booking.getEvent().getName() : " - " + booking.getPurpose()));
                event.put("start", booking.getStartTime().toString());
                event.put("end", booking.getEndTime().toString());
                event.put("url", "/bookings/" + booking.getId() + "/edit");
                event.put("roomId", booking.getRoom().getId());
                event.put("eventId", booking.getEvent() != null ? booking.getEvent().getId() : null);
                
                // Set color based on booking status
                String color = switch(booking.getStatus()) {
                    case CONFIRMED -> "#28a745"; // Green
                    case PENDING -> "#ffc107";   // Yellow
                    case CANCELLED -> "#dc3545"; // Red
                    case COMPLETED -> "#6c757d"; // Gray
                };
                event.put("backgroundColor", color);
                event.put("borderColor", color);
                
                // Add additional info for tooltip
                event.put("extendedProps", Map.of(
                    "bookedBy", booking.getBookedBy(),
                    "contactInfo", booking.getContactInfo(),
                    "status", booking.getStatus().getDisplayName()
                ));
                
                return event;
            })
            .toList();

        model.addAttribute("rooms", rooms);
        model.addAttribute("events", events);
        model.addAttribute("bookings", calendarEvents);
        
        // If roomId is provided, pre-select the room in the filter
        if (roomId != null) {
            model.addAttribute("selectedRoomId", roomId);
        }
        
        return "calendar";
    }
} 
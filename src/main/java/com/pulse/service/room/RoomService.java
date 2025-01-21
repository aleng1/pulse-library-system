package com.pulse.service.room;

import com.pulse.model.Room;
import com.pulse.model.RoomStatus;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> getAllRooms();
    Optional<Room> getRoomById(Long id);
    List<Room> getAvailableRooms();
    List<Room> getRoomsByCapacity(int minCapacity);
    Room createRoom(Room room);
    Room updateRoom(Long id, Room room);
    void deleteRoom(Long id);
    Room updateRoomStatus(Long id, RoomStatus status);
} 
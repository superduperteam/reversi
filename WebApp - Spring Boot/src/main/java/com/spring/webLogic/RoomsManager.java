package com.spring.webLogic;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomsManager {
    private List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public boolean isRoomWithNameExists(String roomName) {
        for(Room room : rooms) {
            if(room.getRoomName().equals(roomName)) {
                return true;
            }
        }

        return false;
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public Room getRoom(int roomId) {
        for(Room room : rooms) {
            if(room.getId() == roomId) {
                return room;
            }
        }

        return null;
    }
}

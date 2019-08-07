package com.spring.webLogic;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
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

    public Room getRoom(String roomName) {
        for(Room room : rooms) {
            if(room.getRoomName().equals(roomName)) {
                return room;
            }
        }

        return null;
    }
}

package com.spring.webLogic;
import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.Path;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomsManager {
    private List<Room> rooms = new ArrayList<>();

    private RoomsManager(){
        Path path = Paths.get("C:\\Users\\Saar\\Downloads\\ex3-small-regular - Copy.xml");
        try {
            rooms.add(new Room(new GameSettingsReader().readGameSettings(path),"Base", "Unknown"));
        } catch (BoardSizeDoesntMatchNumOfPlayersException | ColumnsNotInRangeException | IslandsOnRegularModeException | NoXMLFileException | PlayersInitPositionsOutOfRangeException | PlayersInitPositionsOverrideEachOtherException | RowsNotInRangeException | PlayerHasNoInitialPositionsException | OutOfRangeNumberOfPlayersException | FileIsNotXML | TooManyInitialPositionsException | ThereAreAtLeastTwoPlayersWithSameID | InvalidNumberOfPlayersException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addRoom(Room room) {
        rooms.add(room);
    }

    public synchronized boolean isRoomWithNameExists(String roomName) {
        for(Room room : rooms) {
            if(room.getRoomName().equals(roomName)) {
                return true;
            }
        }

        return false;
    }

    public synchronized List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public synchronized Room getRoom(int roomId) {
        for(Room room : rooms) {
            if(room.getId() == roomId) {
                return room;
            }
        }

        return null;
    }
}

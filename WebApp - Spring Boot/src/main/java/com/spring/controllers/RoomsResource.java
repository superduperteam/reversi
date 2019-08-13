package com.spring.controllers;

import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
import com.spring.exceptions.*;
import com.spring.webLogic.Room;
import com.spring.webLogic.RoomsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class RoomsResource {
    private final RoomsManager roomsManager;

    @Autowired
    public RoomsResource(RoomsManager roomsManager) {
        this.roomsManager = roomsManager;
    }

    @GetMapping(path = "/rooms")
    public synchronized RoomsManager getRooms() {
        return roomsManager;
    }

    @PostMapping(path = "/createRoom")
    public synchronized ResponseEntity<Object>  createRoom(HttpSession session){
        Room roomToCreate = (Room) session.getAttribute("roomToCreate");

        if(roomToCreate != null){
            roomsManager.addRoom(roomToCreate);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // TODO: 8/8/2019 No content?
        }
    }

    @GetMapping("/rooms/{id}/status")
    public HashMap<String, Integer> getStatus(@PathVariable int id){
        Room roomToGetStatusOn = roomsManager.getRoom(id);

        if(roomToGetStatusOn == null){
            throw new RoomNotFoundException();
        }

        final HashMap<String, Integer> res = new HashMap<String, Integer>() {{
            put("joinedPlayersNum", roomToGetStatusOn.getJoinedPlayersNum());
            put("totalPlayersNum", roomToGetStatusOn.getTotalPlayersNum());
        }};

        return res;
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<String> leaveRoom(@PathVariable int id, HttpSession session){
        Room roomToLeave = roomsManager.getRoom(id);
        String onlinePlayerName = (String) session.getAttribute("playerName");

        if(roomToLeave==null){
            throw new RoomNotFoundException();
        }
        if(onlinePlayerName == null){
            throw new OnlinePlayerNotFoundException();
        }

        roomToLeave.leave(onlinePlayerName);
        return new ResponseEntity<>("Player left successfully",HttpStatus.CREATED);
    }

    @PostMapping("/rooms/{id}")
    public ResponseEntity<String> joinRoom(@PathVariable int id, HttpSession session) {
        Room roomToJoin = roomsManager.getRoom(id);
        String onlinePlayerName = (String) session.getAttribute("playerName");

        if(roomToJoin==null){
            throw new RoomNotFoundException();
        }
        if(onlinePlayerName == null){
            throw new OnlinePlayerNotFoundException();
        }

        if(roomToJoin.join(onlinePlayerName)){
            return new ResponseEntity<>("Player joined room "+ id + " successfully",HttpStatus.CREATED);
        }
        else{
            throw new RoomIsAlreadyFullException();
        }
    }

    @GetMapping("/rooms/{id}/gameStart")
    public GameManager getGame(@PathVariable int id){
        Room room = roomsManager.getRoom(id);
        GameManager gameManager = room.getGameManager();

        synchronized (this){
            if(!gameManager.isGameActive()){
                gameManager.activateGame();
            }
        }

        return gameManager;
    }

    @PostMapping(path = "/xmlFileLoader")
    public ResponseEntity<Room> uploadXMLFile(@RequestParam("xmlFile") MultipartFile xmlFile, HttpSession session) {
        try {
            GameSettingsReader gameSettingsReader = new GameSettingsReader();
            GameManager gameManager = gameSettingsReader.extractGameSettings(xmlFile.getInputStream());

            if (roomsManager.isRoomWithNameExists(gameManager.getGameTitle())) {
                throw new RoomWithTheSameNameAlreadyExistsException();
            } else
                {
                    String playerName = (String)session.getAttribute("playerName");
                    if(playerName == null){
                        playerName = "Unknown";
                    }

                    Room roomToCreate = new Room(gameManager, gameManager.getGameTitle(), playerName);
                    session.setAttribute("roomToCreate", roomToCreate);
                    return new ResponseEntity<>(roomToCreate, HttpStatus.ACCEPTED);
            }
        } catch (PlayersInitPositionsOutOfRangeException | RowsNotInRangeException | ColumnsNotInRangeException |
                ThereAreAtLeastTwoPlayersWithSameID | OutOfRangeNumberOfPlayersException | TooManyInitialPositionsException |
                BoardSizeDoesntMatchNumOfPlayersException | IslandsOnRegularModeException | PlayerHasNoInitialPositionsException |
                InvalidNumberOfPlayersException | PlayersInitPositionsOverrideEachOtherException | IOException e) {
            throw new GameSettingsInvalidException(e.getMessage());
        }
    }
}

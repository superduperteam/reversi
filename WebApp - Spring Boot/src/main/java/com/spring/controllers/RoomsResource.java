package com.spring.controllers;

import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
import com.spring.exceptions.GameSettingsInvalidException;
import com.spring.exceptions.OnlinePlayerNotFoundException;
import com.spring.exceptions.RoomNotFoundException;
import com.spring.exceptions.RoomWithTheSameNameAlreadyExistsException;
import com.spring.webLogic.OnlinePlayer;
import com.spring.webLogic.OnlinePlayersManager;
import com.spring.webLogic.Room;
import com.spring.webLogic.RoomsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

@RestController
public class RoomsResource {
    private final RoomsManager roomsManager;

    @Autowired
    public RoomsResource(RoomsManager roomsManager) {
        this.roomsManager = roomsManager;
    }

    @GetMapping(path = "/rooms")
    public RoomsManager getRooms() {
        return roomsManager;
    }

    @PostMapping(path = "/createRoom")
    public ResponseEntity createRoom(HttpSession session){
        Room roomToCreate = (Room) session.getAttribute("roomToCreate");

        if(roomToCreate != null){
            roomsManager.addRoom(roomToCreate);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // TODO: 8/8/2019 No content?
        }
    }

    @PostMapping("/rooms/{id}")
    public ResponseEntity joinRoom(@PathVariable int id, HttpSession session) {
        Room roomToJoin = roomsManager.getRoom(id);
        String onlinePlayerName = (String) session.getAttribute("playerName");

        if(roomToJoin==null){
            throw new RoomNotFoundException();
        }
        if(onlinePlayerName == null){
            throw new OnlinePlayerNotFoundException();
        }

        roomToJoin.join(onlinePlayerName);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(path = "/xmlFileLoader")
    public ResponseEntity uploadXMLFile(@RequestParam("xmlFile") MultipartFile xmlFile, HttpSession session) {
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

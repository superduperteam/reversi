package com.spring.controllers;

import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
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
    public ResponseEntity<String> createRoom(HttpSession session){
        Room roomToCreate = (Room) session.getAttribute("roomToCreate");

        if(roomToCreate != null){
            roomsManager.addRoom(roomToCreate);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // TODO: 8/8/2019 No content?
        }
    }

    @PostMapping(path = "/xmlFileLoader")
    public ResponseEntity<Room> uploadXMLFile(@RequestParam("xmlFile") MultipartFile xmlFile, HttpSession session) {
        try {
            GameSettingsReader gameSettingsReader = new GameSettingsReader();
            //  System.out.println(parts.getInputStream());
            GameManager gameManager = gameSettingsReader.extractGameSettings(xmlFile.getInputStream());
//            gameDescriptor = xmlLoader.getGameDescriptorFromXml(xmlContent);
//            xmlLoader.validateXml(gameDescriptor, roomsManager);
//

            if (roomsManager.isRoomWithNameExists(gameManager.getGameTitle())) {
//                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            } else
                {
                Room roomToCreate = new Room(gameManager, gameManager.getGameTitle(), "saar");
                session.setAttribute("roomToCreate", roomToCreate);
//                SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
//                sessionHandler.setLastUploadedGameManager(request, gameManager);
//                jsonManager.sendJsonOut(response, room);

               // return new ResponseEntity<>(roomToCreate ,HttpStatus.ACCEPTED);
                    return new ResponseEntity<>(roomToCreate, HttpStatus.ACCEPTED);
            }
        } catch (PlayersInitPositionsOutOfRangeException e) {
            e.printStackTrace();
        } catch (RowsNotInRangeException e) {
            e.printStackTrace();
        } catch (ColumnsNotInRangeException e) {
            e.printStackTrace();
        } catch (ThereAreAtLeastTwoPlayersWithSameID thereAreAtLeastTwoPlayersWithSameID) {
            thereAreAtLeastTwoPlayersWithSameID.printStackTrace();
        } catch (OutOfRangeNumberOfPlayersException e) {
            e.printStackTrace();
        } catch (TooManyInitialPositionsException e) {
            e.printStackTrace();
        } catch (BoardSizeDoesntMatchNumOfPlayersException e) {
            e.printStackTrace();
        } catch (IslandsOnRegularModeException e) {
            e.printStackTrace();
        } catch (PlayerHasNoInitialPositionsException e) {
            e.printStackTrace();
        } catch (InvalidNumberOfPlayersException e) {
            e.printStackTrace();
        } catch (PlayersInitPositionsOverrideEachOtherException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
       return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // TODO: 8/9/2019 remove this
    }
}

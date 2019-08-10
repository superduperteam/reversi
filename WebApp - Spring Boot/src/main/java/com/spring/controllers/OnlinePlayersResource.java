package com.spring.controllers;
import com.spring.exceptions.OnlinePlayerNotFoundException;
import com.spring.webLogic.OnlinePlayer;
import com.spring.webLogic.OnlinePlayersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class OnlinePlayersResource {
    private final OnlinePlayersManager onlinePlayersManager;

    @Autowired
    public OnlinePlayersResource(OnlinePlayersManager onlinePlayersManager) {
        this.onlinePlayersManager = onlinePlayersManager;
    }

    @PostMapping(path = "/players")
    public ResponseEntity helloWorld(String playerName, HttpSession session){
        if(onlinePlayersManager.isPlayerExists(playerName)){
            return new ResponseEntity<>(playerName +" already exists", HttpStatus.CONFLICT);
        }

        onlinePlayersManager.addPlayer(playerName);
        session.setAttribute("playerName", playerName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/players/playerName")
    public ResponseEntity retrievePlayerName(HttpSession session){
        String playerName = (String) session.getAttribute("playerName");

        if(onlinePlayersManager.isPlayerExists(playerName)){
            return new ResponseEntity<>(playerName, HttpStatus.ACCEPTED);
        }
        else{
            throw new OnlinePlayerNotFoundException();
        }
    }

    @DeleteMapping(path = "/players/playerName")
    public ResponseEntity deletePlayerName(HttpSession session){
        String playerName = (String) session.getAttribute("playerName");

        if(onlinePlayersManager.isPlayerExists(playerName)){
            onlinePlayersManager.removePlayer(playerName);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            throw new OnlinePlayerNotFoundException();
        }
    }

    @GetMapping(path = "/players")
    public OnlinePlayersManager retrieveAllUsers(){
        return onlinePlayersManager;
    }

//    @DeleteMapping("/signout")
//    public void deleteUser(int id) {
//        User user = service.deleteById(id);
//
//        if(user==null)
//            throw new UserNotFoundException("id-"+ id);
//    }
}

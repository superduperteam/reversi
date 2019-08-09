package com.spring.controllers;
import com.spring.webLogic.OnlinePlayer;
import com.spring.webLogic.OnlinePlayersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OnlinePlayersResource {
    private final OnlinePlayersManager onlinePlayersManager;

    @Autowired
    public OnlinePlayersResource(OnlinePlayersManager onlinePlayersManager) {
        this.onlinePlayersManager = onlinePlayersManager;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> helloWorld(String playerName){
        if(onlinePlayersManager.isPlayerExists(playerName)){
            return new ResponseEntity<>(playerName +" already exists", HttpStatus.CONFLICT);
        }

        onlinePlayersManager.addPlayer(playerName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/whosOnline")
    public OnlinePlayersManager retrieveAllUsers(){
        return onlinePlayersManager;
    }

    // TODO: 8/7/2019 add delete - signout
//    @DeleteMapping("/signout")
//    public void deleteUser(int id) {
//        User user = service.deleteById(id);
//
//        if(user==null)
//            throw new UserNotFoundException("id-"+ id);
//    }
}

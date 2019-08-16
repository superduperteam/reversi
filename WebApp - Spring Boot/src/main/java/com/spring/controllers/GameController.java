package com.spring.controllers;

import GameEngine.GameManager;
import GameEngine.GameManager.eMoveStatus;
import GameEngine.Player;
import GameEngine.Point;
import com.spring.exceptions.OnlinePlayerNotFoundException;
import com.spring.webLogic.Move;
import com.spring.webLogic.OnlinePlayersManager;
import com.spring.webLogic.RoomsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GameController {
    private final RoomsManager roomsManager;
    private final OnlinePlayersManager onlinePlayersManager;

    @Autowired
    public GameController(RoomsManager roomsManager, OnlinePlayersManager onlinePlayersManager) {
        this.roomsManager = roomsManager;
        this.onlinePlayersManager = onlinePlayersManager;
    }

    @MessageMapping("/{id}/game/getInitialGameDetails")
    @SendTo("/topic/rooms/{id}/game")
    public GameManager getInitialGameDetails(@DestinationVariable int id){
        return roomsManager.getRoom(id).getGameManager();
    }

    @MessageMapping("/{id}/game/getWhoseTurn")
    @SendTo("/topic/rooms/{id}/game")
    public Player getWhoseTurn(@DestinationVariable int id){
        return roomsManager.getRoom(id).getGameManager().getActivePlayer();
    }

    @MessageMapping("/{id}/game/getUpdatedBoard")
    @SendTo("/topic/rooms/{id}/game")
    public GameManager getUpdatedBoard(@DestinationVariable int id){
        return roomsManager.getRoom(id).getGameManager();
    }

    @MessageMapping("/{id}/game/executeMove")
    @SendTo("/topic/rooms/{id}/game")
    public eMoveStatus executeMove(@DestinationVariable int id, Move move){
        GameManager gameManager= roomsManager.getRoom(id).getGameManager();
        Player activePlayer = gameManager.getActivePlayer();
        String onlinePlayerName = move.getPlayerName();

        if(!onlinePlayersManager.isPlayerExists(onlinePlayerName)){
            throw new OnlinePlayerNotFoundException();
        }

        if(move.getPlayerName().equals(activePlayer.getName())){
            return activePlayer.makeMove(new Point(move.getDestinationRow(), move.getDestinationCol()), gameManager.getBoard());
        }
        else{
            return eMoveStatus.NOT_YOUR_TURN;
        }
    }
}

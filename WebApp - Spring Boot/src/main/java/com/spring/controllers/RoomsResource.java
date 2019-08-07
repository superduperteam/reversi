package com.spring.controllers;

import com.spring.webLogic.RoomsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomsResource {
    private final RoomsManager roomsManager;

    @Autowired
    public RoomsResource(RoomsManager roomsManager) {
        this.roomsManager = roomsManager;
    }

    @GetMapping(path = "/rooms")
    public RoomsManager getRooms(){
        return roomsManager;
    }
}

package com.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomIsAlreadyFullException extends RuntimeException {
    @Override
    public String toString() {
        return "Room is already full!";
    }
}

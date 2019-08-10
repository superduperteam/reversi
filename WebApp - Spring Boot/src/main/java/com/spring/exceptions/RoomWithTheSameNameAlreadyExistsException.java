package com.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoomWithTheSameNameAlreadyExistsException extends RuntimeException {
    @Override
    public String toString() {
        return "A room with the same name already exists!";
    }
}

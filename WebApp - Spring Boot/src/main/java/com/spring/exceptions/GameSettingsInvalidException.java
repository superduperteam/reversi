package com.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class GameSettingsInvalidException extends RuntimeException {
    private String message;

    public GameSettingsInvalidException(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

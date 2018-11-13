package Exceptions;

import java.io.IOException;

public class PlayerHasNoInitialPositionsException extends Exception
{
    private final String errMessage = "A player has no initial positions";

    @Override
    public String toString() {
        return errMessage;
    }
}

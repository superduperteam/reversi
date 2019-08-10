package Exceptions;

import java.io.IOException;

public class PlayerHasNoInitialPositionsException extends Exception
{

    @Override
    public String toString() {
        return "A player has no initial positions";
    }
}

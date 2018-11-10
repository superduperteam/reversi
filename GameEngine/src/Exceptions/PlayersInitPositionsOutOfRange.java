package Exceptions;

public class PlayersInitPositionsOutOfRange extends Exception  {
    private final String errMessage = "Players initial positions are out of range";

    @Override
    public String toString() {
        return errMessage;
    }
}

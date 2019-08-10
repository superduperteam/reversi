package Exceptions;

public class PlayersInitPositionsOutOfRangeException extends Exception  {

    @Override
    public String toString() {
        return "Players initial positions are out of range";
    }
}

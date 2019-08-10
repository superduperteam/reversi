package Exceptions;

public class BoardSizeDoesntMatchNumOfPlayersException extends Exception {
    @Override
    public String toString() {
        return "Board size doesn't match number of players";
    }
}

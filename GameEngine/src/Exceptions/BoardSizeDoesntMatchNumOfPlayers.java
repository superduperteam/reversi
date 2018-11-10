package Exceptions;

public class BoardSizeDoesntMatchNumOfPlayers extends Exception {
    private final String errMessage = "Board size doesn't match number of players";

    @Override
    public String toString() {
        return errMessage;
    }
}

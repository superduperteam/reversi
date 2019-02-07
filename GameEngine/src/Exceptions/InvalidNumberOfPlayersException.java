package Exceptions;

public class InvalidNumberOfPlayersException extends Exception {
    private final String errMessage = "Invalid total Number of players. Should be between 2 and 4.";

    @Override
    public String toString() {
        return errMessage;
    }
}
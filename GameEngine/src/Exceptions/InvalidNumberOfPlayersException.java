package Exceptions;

public class InvalidNumberOfPlayersException extends Exception {
    @Override
    public String toString() {
        return "Invalid total Number of players. Should be between 2 and 4.";
    }
}
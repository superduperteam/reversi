package Exceptions;

public class TooManyInitialPositionsException extends Exception{
    private final String errMessage = "There are more initial positions for players than players!";

    @Override
    public String toString() {
        return errMessage;
    }
}

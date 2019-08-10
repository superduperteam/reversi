package Exceptions;

public class TooManyInitialPositionsException extends Exception{

    @Override
    public String toString() {
        return "There are more initial positions for players than players!";
    }
}

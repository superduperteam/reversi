package Exceptions;

public class IslandsOnRegularModeException extends Exception{
    private final String errMessage = "Islands are not allowed in regular mode";

    @Override
    public String toString() {
        return errMessage;
    }
}

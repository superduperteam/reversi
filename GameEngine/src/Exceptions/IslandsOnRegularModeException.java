package Exceptions;

public class IslandsOnRegularModeException extends Exception{
    @Override
    public String toString() {
        return "Islands are not allowed in regular mode";
    }
}

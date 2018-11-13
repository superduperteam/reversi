package Exceptions;

public class NoXMLFileException extends Exception  {
    private final String errMessage = "The given XML file doesn't exist";

    @Override
    public String toString() {
        return errMessage;
    }
}

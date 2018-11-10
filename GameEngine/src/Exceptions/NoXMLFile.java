package Exceptions;

public class NoXMLFile extends Exception  {
    private final String errMessage = "The given XML file doesn't exist";

    @Override
    public String toString() {
        return errMessage;
    }
}

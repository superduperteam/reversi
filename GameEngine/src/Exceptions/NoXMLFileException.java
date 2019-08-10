package Exceptions;

public class NoXMLFileException extends Exception  {
    @Override
    public String toString() {
        return "The given XML file doesn't exist";
    }
}

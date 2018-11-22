package Exceptions;

public class FileIsNotXML extends Exception{
    private final String errMessage = "The file is not an XML";

    @Override
    public String toString() {
        return errMessage;
    }
}

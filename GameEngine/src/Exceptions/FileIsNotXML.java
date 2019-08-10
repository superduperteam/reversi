package Exceptions;

public class FileIsNotXML extends Exception{
    @Override
    public String toString() {
        return "The file is not an XML";
    }
}

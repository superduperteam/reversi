package Exceptions;

public class RowsNotInRangeException extends Exception  {
    private final String errMessage = "Rows not in range";

    @Override
    public String toString() {
        return errMessage;
    }
}

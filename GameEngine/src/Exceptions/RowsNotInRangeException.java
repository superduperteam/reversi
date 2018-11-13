package Exceptions;

public class RowsNotInRangeException extends Exception  {
    private final String errMessage = "Rows not in range [4,50]";

    @Override
    public String toString() {
        return errMessage;
    }
}

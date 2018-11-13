package Exceptions;

public class ColumnsNotInRangeException extends Exception  {
    private final String errMessage = "Columns not in range";

    @Override
    public String toString() {
        return errMessage;
    }
}

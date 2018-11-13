package Exceptions;

public class ColumnsNotInRangeException extends Exception  {
    private final String errMessage = "Columns not in range [4,30]";

    @Override
    public String toString() {
        return errMessage;
    }
}

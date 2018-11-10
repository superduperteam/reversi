package Exceptions;

public class ColumnsNotInRange extends Exception  {
    private final String errMessage = "Columns not in range";

    @Override
    public String toString() {
        return errMessage;
    }
}

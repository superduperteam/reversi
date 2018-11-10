package Exceptions;

public class RowsNotInRange extends Exception  {
    private final String errMessage = "Rows not in range";

    @Override
    public String toString() {
        return errMessage;
    }
}

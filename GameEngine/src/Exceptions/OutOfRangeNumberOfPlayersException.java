package Exceptions;

public class OutOfRangeNumberOfPlayersException extends Exception {
    int numberOfExpectedPlayersMin;
    int numberOfExpectedPlayersMax;

    public OutOfRangeNumberOfPlayersException(int numberOfExpectedPlayersMin, int numberOfExpectedPlayersMax)
    {
        this.numberOfExpectedPlayersMin = numberOfExpectedPlayersMin;
        this.numberOfExpectedPlayersMax = numberOfExpectedPlayersMax;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("There should be ");
        stringBuilder.append(String.valueOf(numberOfExpectedPlayersMin));
        stringBuilder.append(" to ");
        stringBuilder.append(String.valueOf(numberOfExpectedPlayersMax));
        stringBuilder.append(" players!");

        return stringBuilder.toString();
    }
}

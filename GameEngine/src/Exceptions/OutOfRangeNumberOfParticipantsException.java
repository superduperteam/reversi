package Exceptions;

public class OutOfRangeNumberOfParticipantsException extends Exception {
    int numberOfExpectedParticipants;

    public OutOfRangeNumberOfParticipantsException(int numberOfExpectedParticipants)
    {
        this.numberOfExpectedParticipants = numberOfExpectedParticipants;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("There should be initial positions for ");
        stringBuilder.append(String.valueOf(numberOfExpectedParticipants));
        stringBuilder.append(" participants");

        return stringBuilder.toString();
    }
}

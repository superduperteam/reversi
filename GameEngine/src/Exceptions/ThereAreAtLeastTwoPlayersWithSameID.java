package Exceptions;

public class ThereAreAtLeastTwoPlayersWithSameID extends Exception{
    private final String errMessage = "There are at least two players with same ID";

    @Override
    public String toString() {
        return errMessage;
    }
}

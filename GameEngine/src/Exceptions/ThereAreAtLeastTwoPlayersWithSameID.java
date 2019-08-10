package Exceptions;

public class ThereAreAtLeastTwoPlayersWithSameID extends Exception{

    @Override
    public String toString() {
        return "There are at least two players with same ID";
    }
}

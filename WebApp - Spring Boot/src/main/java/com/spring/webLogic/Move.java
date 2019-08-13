package com.spring.webLogic;

public class Move {
    private String playerName;
    private int destinationCol;
    private int destinationRow;

    public Move(String playerName, int destinationCol, int destinationRow) {
        this.playerName = playerName;
        this.destinationCol = destinationCol;
        this.destinationRow = destinationRow;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getDestinationCol() {
        return destinationCol;
    }

    public int getDestinationRow() {
        return destinationRow;
    }
}

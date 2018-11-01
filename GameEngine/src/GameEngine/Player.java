package GameEngine;

import java.util.List;

public class Player {
    private Statistics statistics;
    private int countOfRemainingDiscs;
    private String name;
    private boolean isHuman;
    private eDiscType discType;

    public void SetDiscType(eDiscType discType)
    {
        this.discType = discType;
    }

    public eDiscType GetDiscType()
    {
        return discType;
    }

    public Player(String name, boolean isHuman, eDiscType discType)
    {
        this.name = name;
        this.isHuman = isHuman;
        this.discType = discType;
    }

    private class Statistics {
        private int countOfPlayedTurns;
        private double averageOfFlips;
        private int score;
    }

    private List<Point> getListOfAllPossibleMoves() {return null;}

    public void MakeMove(Point targetInsertionPoint, Board board) {}

    public Point GetRandomMove(List<Point> allPossibleMoves) {return null;}
}
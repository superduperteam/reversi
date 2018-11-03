package GameEngine;

import java.util.List;

public class Player {
    private Statistics statistics;
    private int countOfRemainingDiscs;
    private String name;
    private boolean isHuman;
    private eDiscType discType;
    private  int id;

    public void SetDiscType(eDiscType discType)
    {
        this.discType = discType;
    }

    public eDiscType GetDiscType()
    {
        return discType;
    }

    public Player(String name, boolean isHuman, eDiscType discType, int id)
    {
        this.name = name;
        this.isHuman = isHuman;
        this.discType = discType;
        this.id = id;
    }

    private class Statistics {
        private int countOfPlayedTurns;
        private double averageOfFlips;
        private int score;
    }

    public boolean IsHuman()
    {
        return isHuman;
    }

    private List<Point> getListOfAllPossibleMoves() {return null;}

    public boolean MakeMove(Point targetInsertionPoint, Board board)
    {
       boolean isAbleToDoTheMove;

       isAbleToDoTheMove = board.IsMoveLegal(targetInsertionPoint, discType);

       if(isAbleToDoTheMove)
       {
           board.UpdateBoard(targetInsertionPoint, discType);
       }

       return isAbleToDoTheMove;
    }

    public Point GetRandomMove(List<Point> allPossibleMoves) {return null;}
}
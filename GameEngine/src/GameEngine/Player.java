package GameEngine;

import java.util.List;
import java.util.Random;

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

    public class Statistics {
        private int countOfPlayedTurns;
        private double averageOfFlips;
        private int score;

        public void resetScore() {
            score = 0;
        }

        public void incScore() {
            ++score;
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public boolean IsHuman()
    {
        return isHuman;
    }

    public Point GetRandomMove(Board board)
    {
        List<Point> allPossibleMoves = board.GetListOfAllPossibleMoves(this);

        return pickRandomMoveFromList(allPossibleMoves);
    }

    private Point pickRandomMoveFromList(List<Point> allPossibleMoves)
    {
        Random random = new Random();
        int moveIndex = random.nextInt(allPossibleMoves.size());

        return allPossibleMoves.get(moveIndex);
    }


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
}
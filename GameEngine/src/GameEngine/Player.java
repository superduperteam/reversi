package GameEngine;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class Player implements  Cloneable{
    private Statistics statistics;
    private int countOfRemainingDiscs;
    private String name;
    private boolean isHuman;
    private eDiscType discType;
    private BigInteger id;

    // probably not needed
//    public void SetDiscType(eDiscType discType)
//    {
//        this.discType = discType;
//    }

    public eDiscType GetDiscType()
    {
        return discType;
    }

    public Player(String name, boolean isHuman, eDiscType discType, BigInteger id)
    {
        this.name = name;
        this.isHuman = isHuman;
        this.discType = discType;
        this.id = id;
        statistics = new Statistics();
    }

    public Player(Player toCopy) {
        this.statistics = new Statistics(toCopy.getStatistics());
        this.countOfRemainingDiscs = toCopy.getCountOfRemainingDiscs();
        this.name = new String(toCopy.getName());
        this.isHuman = toCopy.getIsHuman();
        this.discType = toCopy.discType; //note (from ido): im assuming that playaer doesn't change his disk type.
                                        // if he does, a different logic should be implemented
        this.id = new BigInteger(toCopy.getId().toString());
    }

//    public Player(jaxb.schema.generated.Player player, eDiscType discType) {
//        this.name = player.getName();
//        this.isHuman = player.getType().equals("Human");
//        this.discType = discType;
//        this.id = player.getId();
//        statistics = new Statistics();
//    }

    public class Statistics {
        private int countOfPlayedTurns;
        private double averageOfFlips;
        private int score;

        public Statistics() {
            countOfPlayedTurns = 0;
            averageOfFlips = 0;
            score = 0;
        }

        public Statistics(Statistics toCopy) {
            this.countOfPlayedTurns = toCopy.countOfPlayedTurns;
            this.averageOfFlips = toCopy.averageOfFlips;
            this.score = toCopy.score;
        }

        public void resetScore() {
            score = 0;
        }

        public void incScore() {
            ++score;
        }
    }

    public BigInteger getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public eDiscType getDiscType() {
        return discType;
    }

    public int getCountOfRemainingDiscs() {
        return countOfRemainingDiscs;
    }

    public boolean getIsHuman(){
        return isHuman;
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

    public String GetName()
    {
        return name;
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
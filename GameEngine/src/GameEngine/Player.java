package GameEngine;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Player implements Serializable {
    private Statistics statistics;
    private int countOfRemainingDiscs;
    private String name;
    private boolean isHuman;
    private eDiscType discType;
    private BigInteger id;

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

    public eDiscType GetDiscType()
    {
        return discType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }

//    public Player(jaxb.schema.generated.Player player, eDiscType discType) {
//        this.name = player.getName();
//        this.isHuman = player.getType().equals("Human");
//        this.discType = discType;
//        this.id = player.getId();
//        statistics = new Statistics();
//    }

    public class Statistics implements  Serializable{
        private int countOfPlayedTurns;
        private double totalNumOfFlips;
        private int score;

        public Statistics() {
            countOfPlayedTurns = 0;
            totalNumOfFlips = 0;
            score = 0;
        }

        public Statistics(Statistics toCopy) {
            this.countOfPlayedTurns = toCopy.countOfPlayedTurns;
            this.totalNumOfFlips = toCopy.totalNumOfFlips;
            this.score = toCopy.score;
        }

        public void resetStatistics()
        {
            resetScore();
            countOfPlayedTurns = 0;
            totalNumOfFlips = 0;
        }

        public void resetScore() {
            score = 0;
        }

        public void incScore() {
            ++score;
        }

        public int getCountOfPlayedTurns() {
            return countOfPlayedTurns;
        }

        public double getAverageOfFlips() {
            double avgNumOfFlips = 0;

            if(countOfPlayedTurns > 0){
                avgNumOfFlips = totalNumOfFlips / countOfPlayedTurns;
            }
            return  avgNumOfFlips;
        }

        public int getScore() {
            return score;
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

    public boolean isHuman()
    {
        return isHuman;
    }

    public Point getRandomMove(Board board)
    {
        List<Point> allPossibleMoves = board.getListOfAllPossibleMoves(this);

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

    public int getScore()
    {
        return statistics.score;
    }

    public GameManager.eMoveStatus makeMove(Point targetInsertionPoint, Board board)
    {
        GameManager.eMoveStatus isAbleToDoTheMove;

        isAbleToDoTheMove = board.isMoveLegal(targetInsertionPoint, discType);

        if(isAbleToDoTheMove == GameManager.eMoveStatus.OK)
        {
            statistics.totalNumOfFlips += board.updateBoard(targetInsertionPoint, discType);
            statistics.countOfPlayedTurns++;
        }

        return isAbleToDoTheMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
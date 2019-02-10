package GameEngine;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Player implements Serializable {

    //private SimpleStringProperty nameUI;

    private Statistics statistics;
    private String name;
    private boolean isHuman;
    private eDiscType discType;
    private BigInteger id;

    public Player(jaxb.schema.generated.Player player, eDiscType discType) {
        this(player.getName(), player.getType().equals("Human"), discType, player.getId());
    }

    public Player(String name, boolean isHuman, eDiscType discType, BigInteger id)
    {
        this.name = name;
        this.isHuman = isHuman;
        this.discType = discType;
        this.id = id;
        statistics = new Statistics();
    }

    public Player(String name, boolean isHuman){
        this.name=name;
        this.isHuman = isHuman;
        statistics = new Statistics();
    }

    public Player(Player toCopy) {
        this.statistics = new Statistics(toCopy.getStatistics());
        this.name = new String(toCopy.getName());
        this.isHuman = toCopy.getIsHuman();
        this.discType = toCopy.discType; //note (from ido): im assuming that playaer doesn't change his disk type.
                                        // if he does, a different logic should be implemented
        //this.id = new BigInteger(toCopy.getId().toString());


        //nameUI = new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }

    public BigInteger getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void quitGame(GameManager gameManager)
    {
        gameManager.retirePlayerFromGame(this);
    }

    public eDiscType getDiscType() { return discType;}

    public void setDiscType(eDiscType discType) { this.discType = discType; }

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

    public int getTurnsPlayed()
    {
        return statistics.countOfPlayedTurns;
    }

    public double getAverageOfFlips() { return statistics.getAverageOfFlips(); }

    public String getColor() {return discType.toString();}

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
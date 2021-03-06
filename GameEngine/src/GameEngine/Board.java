package GameEngine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jaxb.schema.generated.Game;
import jaxb.schema.generated.Participant;

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable
{
    private int height;
    private int width;
    private GameManager.eGameMode gameMode;
    @JsonProperty
    private CellBoard[][] gameboard;

    public Board(jaxb.schema.generated.Board board, HashMap<Participant, List<Point>> initialDiscPointsOfPlayers, GameManager.eGameMode gameMode)
    {
        this(board.getRows(), board.getColumns(), initialDiscPointsOfPlayers, gameMode);
    }





    public Board(int height, int width, HashMap<Participant, List<Point>> initialDiscPointsOfPlayers, GameManager.eGameMode gameMode)
    {
        this.height = height;
        this.width = width;
        this.gameboard = new CellBoard[height][width];
       // this.initialDiscPointsOfPlayers = initialDiscPointsOfPlayers;
        initializeBoard(initialDiscPointsOfPlayers);
        this.gameMode = gameMode;
    }


    public Board(Board toCopy){
        height = toCopy.height;
        width = toCopy.width;
        this.gameboard = new CellBoard[height][width];
        gameMode = toCopy.gameMode; //note(ido): i assume game mode won't change during the game.
                                    // if it can change , I need to change the logic here.

        for(int row = 0; row < height; ++row){
            for(int col = 0; col < width; ++col){
                //  if(toCopy.board[row][col] != null) {
                // this.board[row][col] = new Disc(toCopy.board[row][col]);
                this.gameboard[row][col] = new CellBoard(toCopy.gameboard[row][col]);
            }
        }
    }

    // Returns the number of flipped discs that were flipped due to the given move.
    public int updateBoard(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        //board[targetInsertionPoint.getRow()][targetInsertionPoint.getCol()] = new Disc(discTypeToBeInserted);
        gameboard[targetInsertionPoint.getRow()][targetInsertionPoint.getCol()].setDisc(new Disc(discTypeToBeInserted));
        return flipEnemyDiscs(targetInsertionPoint, discTypeToBeInserted, false);
    }

    public GameManager.eMoveStatus isMoveLegal(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        if(isCellPointInRange(targetInsertionPoint))
        {
            if(isCellEmpty(targetInsertionPoint))
            {
                if(gameMode == GameManager.eGameMode.Regular)
                {
                    if(isThereDiscAdjacent(targetInsertionPoint))
                    {
                        //return canFlipEnemyDiscs(targetInsertionPoint, discTypeToBeInserted);
                        return GameManager.eMoveStatus.OK;
                    }
                    else return GameManager.eMoveStatus.ILLEGAL_ISLAND;
                }
                else return GameManager.eMoveStatus.OK; // Assuming you can insert to any empty point in the board in islands mode.
            }
            else return GameManager.eMoveStatus.CELL_IS_ALREADY_TAKEN; // There's a disc in this point.
        }
        else return GameManager.eMoveStatus.POINT_IS_NOT_IN_RANGE_OF_BOARD;    // Point is not in board.
    }

    private boolean isCellEmpty(Point point)
    {
        if(gameboard[point.getRow()][point.getCol()].getDisc() == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isThereDiscAdjacent(Point point)
    {
        int row = point.getRow(), col = point.getCol();
        Disc adjacentDisc;
        List<Direction> allDirections = generateListOfAllDirection();
        List<Point> allPossibleAdjacentCellPoints = new ArrayList<>(8);
        List<Point> allPossibleAdjacentCellPointsInBoardRange = new ArrayList<>(8);

        // We want to get a list of all adjacent points.
        for(Direction direction : allDirections)
        {
            allPossibleAdjacentCellPoints.add(new Point(row + direction.getDirectionY(), col + direction.getDirectionX()));
        }

        // We want to remove any point that is not in range of the board.
        for(Point cellPoint : allPossibleAdjacentCellPoints)
        {
            if(isCellPointInRange(cellPoint))
            {
                allPossibleAdjacentCellPointsInBoardRange.add(cellPoint);
            }
        }

        for(Point adjacentCellPoint : allPossibleAdjacentCellPointsInBoardRange)
        {
            adjacentDisc = gameboard[adjacentCellPoint.getRow()][adjacentCellPoint.getCol()].getDisc();

            if(adjacentDisc != null)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isCellPointInRange(Point cellPoint)
    {
        int row, col;

        row = cellPoint.getRow();
        col = cellPoint.getCol();

        return isCellPointInRange(row, col);
    }

    private boolean isCellPointInRange(int row, int col)
    {
        if(row >= 0 && row < height  && col >= 0 && col < width )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<Point> getListOfAllPossibleMoves(Player playerToSearchFor)
    {
        int row, col;
        List<Point> listOfAllPossibleMoves = new ArrayList<>();
        Point insertionCellPoint;

        for(row = 0; row < height; row++)
        {
            for (col = 0; col < width; col++)
            {
                insertionCellPoint = new Point(row, col);

                if (isMoveLegal(insertionCellPoint, playerToSearchFor.getDiscType()) == GameManager.eMoveStatus.OK)
                {
                    listOfAllPossibleMoves.add(insertionCellPoint);
                }
            }
        }

        return listOfAllPossibleMoves;
    }

    private boolean canFlipEnemyDiscs(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        List<Direction> allDirections = generateListOfAllDirection();

        for(Direction direction : allDirections)
        {
            if(canFlipEnemyDiscsInDirection(targetInsertionPoint, direction, discTypeToBeInserted))
            {
                return true;
            }
        }

        return false;
    }

    // regular
    private boolean canFlipEnemyDiscsInDirection(Point targetInsertionPoint, Direction direction, eDiscType discTypeToBeInserted)
    {
        int rowDelta = direction.getDirectionY(), colDelta = direction.getDirectionX();
        int countOfSequenceFlippableDiscs = 0;
        boolean isFriendlyDiscFoundYet = false;
        int row = targetInsertionPoint.getRow() + rowDelta, col = targetInsertionPoint.getCol() + colDelta;
        Disc currentDisc;

        while(!isFriendlyDiscFoundYet)
        {
            if(isCellPointInRange(row,col))
            {
                currentDisc = gameboard[row][col].getDisc();
                if (currentDisc != null) {
                    if (currentDisc.getType() != discTypeToBeInserted) {
                        countOfSequenceFlippableDiscs++;
                    } else if (countOfSequenceFlippableDiscs == 0) {
                        return false;
                    } else // currentDisc.GetType() == discTypeToBeInserted && countOfSequenceFlippableDiscs != 0
                    { // it means there was a sequence but this disc is friendly
                        isFriendlyDiscFoundYet = true;
                    }
                } else // currentDisc is null, which means there is no sequence of foes that ends with friendly disc.
                {
                    return false;
                }

                row += rowDelta;
                col += colDelta;
            }
            else return false;
        }

        return true;
    }

    public int checkFlipPotential(Point targetInsertionPoint, eDiscType discTypeToBeInserted){
        return flipEnemyDiscs(targetInsertionPoint, discTypeToBeInserted, true);
    }

    private int flipEnemyDiscs(Point targetInsertionPoint, eDiscType discTypeToBeInserted,
                               boolean isOnlyCheckingFlipPotential)
    {
        // Assuming you can flip whosoever discs but yours.
        int countOfFlippedDiscs = 0;
        List<Direction> allDirections = generateListOfAllDirection();

        for(Direction direction : allDirections)
        {
            countOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, direction,
                    discTypeToBeInserted, isOnlyCheckingFlipPotential);
        }

        return countOfFlippedDiscs;
    }

    private int flipEnemyDiscsInDirection(Point targetInsertionPoint, Direction direction,
                                          eDiscType discTypeToBeInserted, boolean isOnlyCheckingFlipPotential)
    {
        int rowDelta = direction.getDirectionY(), colDelta = direction.getDirectionX();
        int row = targetInsertionPoint.getRow() + rowDelta, col = targetInsertionPoint.getCol() + colDelta;
        int countOfFlippedDiscs = 0;
        Disc currentDisc;

        if(isCellPointInRange(row, col))
        {
            currentDisc = gameboard[row][col].getDisc();

            if (canFlipEnemyDiscsInDirection(targetInsertionPoint, direction, discTypeToBeInserted))
            {
                while (currentDisc.getType() != discTypeToBeInserted)
                {
                    if(!isOnlyCheckingFlipPotential) {
                        currentDisc.setType(discTypeToBeInserted);

                    }
                    row += rowDelta;
                    col += colDelta;
                    currentDisc = gameboard[row][col].getDisc();
                    countOfFlippedDiscs++;
                }
            }
        }
        return countOfFlippedDiscs;
    }

    public CellBoard get(int row, int column){
        return gameboard[row][column];
    }

    public Disc getDisc(int row, int column)
    {
        return gameboard[row][column].getDisc();
    }

    public GameManager.eGameMode getGameMode() {
        return gameMode;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private void initializeBoard(HashMap<Participant, List<Point>> initialDiscsPointsOfPlayers)
    {
        int discTypeIndex = 0;
        eDiscType[] discTypes = eDiscType.values();
        createCellBoards();
        List<Point> currentPlayerInitialDiscs;
        Set<Participant> playersSet = initialDiscsPointsOfPlayers.keySet();

        for(Participant participant : playersSet)
        {
            currentPlayerInitialDiscs = initialDiscsPointsOfPlayers.get(participant);

            for(Point point : currentPlayerInitialDiscs)
            {
                gameboard[point.getRow()][point.getCol()].setDisc(new Disc(discTypes[discTypeIndex]));
            }
            discTypeIndex++;
        }
    }

    public void createCellBoards()
    {
        Disc disc = null;

        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                gameboard[row][col] = new CellBoard(disc);
            }
        }
    }

    private List<Direction> generateListOfAllDirection()
    {
        List<Direction> listOfAllDirections = new ArrayList<>(8);

        listOfAllDirections.add(new Direction(-1,  +0));
        listOfAllDirections.add(new Direction(-1,  +1));
        listOfAllDirections.add(new Direction(+0,  +1));
        listOfAllDirections.add(new Direction( +1,  +1));
        listOfAllDirections.add(new Direction( +1,  +0));
        listOfAllDirections.add(new Direction( +1,  -1));
        listOfAllDirections.add(new Direction( +0,  -1));
        listOfAllDirections.add(new Direction( -1,  -1));

        return listOfAllDirections;
    }

    public boolean areThereAnyMovesForPlayers(List<Player> playersList){
        boolean areThereAnyMovesForPlayers = false;
        List<Point> singlePlayerPossibleMoves;

        for(Player player: playersList){
            singlePlayerPossibleMoves  = getListOfAllPossibleMoves(player);

            if(singlePlayerPossibleMoves.size() > 0) {
                areThereAnyMovesForPlayers = true;
                break;
            }
        }

        return areThereAnyMovesForPlayers;
    }

    private class Direction
    {
        Point directionPoint;

        public Direction(int directionY, int directionX)
        {
            directionPoint = new Point(directionY, directionX);
        }

        private int getDirectionY()
        {
            return directionPoint.getRow();
        }

        private int getDirectionX()
        {
            return directionPoint.getCol();
        }
    }
}
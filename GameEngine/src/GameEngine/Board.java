package GameEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import jaxb.schema.generated.*;

public class Board
{
    private int height;
    private int width;
    private GameManager.eGameMode gameMode;
    private Disc board[][];

//    public Board(int height, int width, LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers, GameManager.eGameMode gameMode)
//    {
//        board = new Disc[height][width];
//        initializeBoard(intialDiscsPointsOfPlayers);
//        this.height = height;
//        this.width = width;
//        this.gameMode = gameMode;
//    }

    public Board(jaxb.schema.generated.Board board, LinkedHashMap<Player, List<Point>> intialDiscsPointsOfPlayers, GameManager.eGameMode gameMode)
    {
        this.height = board.getRows();
        this.width = board.getColumns();
        this.board = new Disc[height][width];
        initializeBoard(intialDiscsPointsOfPlayers);
        this.gameMode = gameMode;
    }

    // Returns the number of flipped discs that were flipped because of the given move.
    public int UpdateBoard(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        board[targetInsertionPoint.GetRow()][targetInsertionPoint.GetCol()] = new Disc(discTypeToBeInserted);
        return flipEnemyDiscs(targetInsertionPoint, discTypeToBeInserted);
    }

    public boolean IsMoveLegal(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        if(IsCellPointInRange(targetInsertionPoint))
        {
            if(isCellEmpty(targetInsertionPoint))
            {
                if(gameMode == GameManager.eGameMode.Regular)
                {
                    if(isThereFoeDiscAdjacent(targetInsertionPoint, discTypeToBeInserted))
                    {
                        return canFlipEnemyDiscs(targetInsertionPoint, discTypeToBeInserted);
                    }
                    else return false;
                }
                else return true; // Assuming you can insert to any empty point in the board in islands mode.
            }
            else return false; // There's a disc in this point.
        }
        else return false;    // Point is not in board.
    }

    private boolean isCellEmpty(Point point)
    {
        if(board[point.GetRow()][point.GetCol()] == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isThereFoeDiscAdjacent(Point point, eDiscType discTypeToBeInserted)
    {
        int row = point.GetRow(), col = point.GetCol();
        Disc adjacentDisc;
        List<Point> allDirections = generateListOfAllDirection();
        List<Point> allPossibleAdjacentCellPoints = new ArrayList<Point>(8);
        List<Point> allPossibleAdjacentCellPointsInBoardRange = new ArrayList<>(8);

//        allPossibleAdjacentCellPoints.add(new Point(row -1, col +0));
//        allPossibleAdjacentCellPoints.add(new Point(row -1, col +1));
//        allPossibleAdjacentCellPoints.add(new Point(row +0, col +1));
//        allPossibleAdjacentCellPoints.add(new Point(row +1, col +1));
//        allPossibleAdjacentCellPoints.add(new Point(row +1, col +0));
//        allPossibleAdjacentCellPoints.add(new Point(row +1, col -1));
//        allPossibleAdjacentCellPoints.add(new Point(row +0, col -1));
//        allPossibleAdjacentCellPoints.add(new Point(row -1, col -1));

        // We want to get a list of all adjacent points.
        for(Point direction : allDirections)
        {
            allPossibleAdjacentCellPoints.add(new Point(row + direction.GetRow(), col + direction.GetCol()));
        }

        // We want to remove any point that is not in range of the board.
        for(Point cellPoint : allPossibleAdjacentCellPoints)
        {
            if(IsCellPointInRange(cellPoint))
            {
                allPossibleAdjacentCellPointsInBoardRange.add(cellPoint);
            }
        }

        for(Point adjacentCellPoint : allPossibleAdjacentCellPointsInBoardRange)
        {
            adjacentDisc = board[adjacentCellPoint.GetRow()][adjacentCellPoint.GetCol()];

            if(adjacentDisc != null)
            {
                if(adjacentDisc.GetType() != discTypeToBeInserted)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean IsCellPointInRange(Point cellPoint)
    {
        int row, col;

        row = cellPoint.GetRow();
        col = cellPoint.GetCol();

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

    public List<Point> GetListOfAllPossibleMoves(Player playerToSearchFor)
    {
        int row, col;
        List<Point> listOfAllPossibleMoves = new ArrayList<>();
        Point insertionCellPoint;

        for(row = 0; row < height; row++)
        {
            for (col = 0; col < width; col++)
            {
                insertionCellPoint = new Point(row, col);

                if (IsMoveLegal(insertionCellPoint, playerToSearchFor.GetDiscType()))
                {
                    listOfAllPossibleMoves.add(insertionCellPoint);
                }
            }
        }

        return listOfAllPossibleMoves;
    }

    private boolean canFlipEnemyDiscs(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        List<Point> allDirections = generateListOfAllDirection();

        for(Point direction : allDirections)
        {
            if(canFlipEnemyDiscsInDirection(targetInsertionPoint, direction.GetRow(), direction.GetCol(), discTypeToBeInserted))
            {
                return true;
            }
        }

//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, -1, +0, discTypeToBeInserted); // UP
//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, -1, +1, discTypeToBeInserted); // UP-RIGHT
//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +0, +1, discTypeToBeInserted); // RIGHT
//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +1, +1, discTypeToBeInserted); // DOWN-RIGHT
//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +1, +0, discTypeToBeInserted); // DOWN
//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +1, -1, discTypeToBeInserted); // DOWN-LEFT
//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +0, -1, discTypeToBeInserted); // LEFT
//        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, -1, -1, discTypeToBeInserted); // UP-LEFT

        return false;
    }

    // regular
    private boolean canFlipEnemyDiscsInDirection(Point targetInsertionPoint, int rowDelta, int colDelta, eDiscType discTypeToBeInserted)
    {
        int countOfSequenceFlippableDiscs = 0;
        boolean keepChecking = true;
        int row = targetInsertionPoint.GetRow() + rowDelta, col = targetInsertionPoint.GetCol() + colDelta;
        Disc currentDisc;

        while(keepChecking)
        {
            if(isCellPointInRange(row,col))
            {
                currentDisc = board[row][col];
                if (currentDisc != null) {
                    if (currentDisc.GetType() != discTypeToBeInserted) {
                        countOfSequenceFlippableDiscs++;
                    } else if (countOfSequenceFlippableDiscs == 0) {
                        return false;
                    } else // currentDisc.GetType() == discTypeToBeInserted && countOfSequenceFlippableDiscs != 0
                    { // it means there was a sequence but this disc is friendly
                        keepChecking = false;
                    }
                } else // currentDisc is null, which means there is no sequence of foes that ends with friendly disc.
                {
                    return false;
                }

                row += rowDelta;
                col += colDelta;
            }
        }

        return true;
    }

    private int flipEnemyDiscs(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        // Assuming you can flip whosoever discs but yours.
        int countOfFlippedDiscs = 0;
        List<Point> allDirections = generateListOfAllDirection();

//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +1, +0, discTypeToBeInserted); // UP
//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +1, +1, discTypeToBeInserted); // UP-RIGHT
//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +0, +1, discTypeToBeInserted); // RIGHT
//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, -1, +1, discTypeToBeInserted); // DOWN-RIGHT
//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, -1, +0, discTypeToBeInserted); // DOWN
//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, -1, -1, discTypeToBeInserted); // DOWN-LEFT
//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +0, -1, discTypeToBeInserted); // LEFT
//        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +1, -1, discTypeToBeInserted); // UP-LEFT

        for(Point direction : allDirections)
        {
            countOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, direction.GetRow(), direction.GetCol(), discTypeToBeInserted);
        }

        return countOfFlippedDiscs;
    }

    private int flipEnemyDiscsInDirection(Point targetInsertionPoint, int rowDelta, int colDelta, eDiscType discTypeToBeInserted)
    {
        int row = targetInsertionPoint.GetRow() + rowDelta, col = targetInsertionPoint.GetCol() + colDelta;
        int countOfFlippedDiscs = 0;
        Disc currentDisc = board[row][col];

        if(canFlipEnemyDiscsInDirection(targetInsertionPoint, rowDelta, colDelta, discTypeToBeInserted))
        {
            while(currentDisc.GetType() != discTypeToBeInserted)
            {
                currentDisc.SetType(discTypeToBeInserted);
                countOfFlippedDiscs++;

                row += rowDelta;
                col += colDelta;
                currentDisc = board[row][col];
            }
        }

        return countOfFlippedDiscs;
    }

//    private void InitializeBoard(LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers)
//    {
//        nullifyBoardCells();
//        ArrayList<Point> currentPlayerIntialDiscs;
//        Set<Player> playersSet = intialDiscsPointsOfPlayers.keySet();
//
//        for(Player player : playersSet)
//        {
//            currentPlayerIntialDiscs = intialDiscsPointsOfPlayers.get(player);
//
//            for(Point point : currentPlayerIntialDiscs)
//            {
//                board[point.GetRow()][point.GetCol()] = new Disc(player.GetDiscType());
//            }
//        }
//    }

    public Disc Get(int row, int col)
    {
        // if not in range?
        return board[row][col];
    }

    public int GetHeight() {
        return height;
    }

    public int GetWidth() {
        return width;
    }


    private void initializeBoard(LinkedHashMap<Player, List<Point>> initialDiscsPointsOfPlayers)
    {
        nullifyBoardCells();
        List<Point> currentPlayerInitialDiscs;
        Set<Player> playersSet = initialDiscsPointsOfPlayers.keySet();

        for(Player player : playersSet)
        {
            currentPlayerInitialDiscs = initialDiscsPointsOfPlayers.get(player);

            for(Point point : currentPlayerInitialDiscs)
            {
                board[point.GetRow()][point.GetCol()] = new Disc(player.GetDiscType());
            }
        }
    }

    public void nullifyBoardCells()
    {
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                board[row][col] = null;
            }
        }
    }

    private List<Point> generateListOfAllDirection()
    {
        List<Point> listOfAllDirections = new ArrayList<>(8);

        listOfAllDirections.add(new Point(-1,  +0));
        listOfAllDirections.add(new Point(-1,  +1));
        listOfAllDirections.add(new Point(+0,  +1));
        listOfAllDirections.add(new Point( +1,  +1));
        listOfAllDirections.add(new Point( +1,  +0));
        listOfAllDirections.add(new Point( +1,  -1));
        listOfAllDirections.add(new Point( +0,  -1));
        listOfAllDirections.add(new Point( -1,  -1));

        return listOfAllDirections;
    }

    public boolean areThereAnyMovesForPlayers(List<Player> playersList){
        boolean AreThereAnyMovesForPlayers = true;
        List<Point> singlePlayerPossibleMoves;

        for(Player player: playersList){
            singlePlayerPossibleMoves  = GetListOfAllPossibleMoves(player);

            if(singlePlayerPossibleMoves.size() > 0) {
                AreThereAnyMovesForPlayers = false;
                break;
            }
        }

        return AreThereAnyMovesForPlayers;
    }

//    private class Direction
//    {
//
//    }
}
package GameEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Board
{
    private int height;
    private int width;
    private GameManager.eGameMode gameMode;
    public Disc board[][];

    public Board(int height, int width, LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers, GameManager.eGameMode gameMode)
    {
        board = new Disc[height][width];
        InitializeBoard(intialDiscsPointsOfPlayers);
        this.height = height;
        this.width = width;
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
        if(isCellPointInRange(targetInsertionPoint))
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
                else return true; // Asumming you can insert to any empty point in the board in islands mode.
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
        List<Point> allPossibleAdjacentCellPoints = new ArrayList<Point>(8);
        List<Point> allPossibleAdjacentCellPointsInBoardRange = new ArrayList<>();

        allPossibleAdjacentCellPoints.add(new Point(row -1, col +0));
        allPossibleAdjacentCellPoints.add(new Point(row -1, col +1));
        allPossibleAdjacentCellPoints.add(new Point(row +0, col +1));
        allPossibleAdjacentCellPoints.add(new Point(row +1, col +1));
        allPossibleAdjacentCellPoints.add(new Point(row +1, col +0));
        allPossibleAdjacentCellPoints.add(new Point(row +1, col -1));
        allPossibleAdjacentCellPoints.add(new Point(row +0, col -1));
        allPossibleAdjacentCellPoints.add(new Point(row -1, col -1));

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

    private boolean isCellPointInRange(Point cellPoint)
    {
        int row, col;

        row = cellPoint.GetRow();
        col = cellPoint.GetCol();

        return isCellPointInRange(row, col);
    }

    private boolean isCellPointInRange(int row, int col)
    {
        if(row >= 0 && row <= height && col >= 0 && col <= width )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean canFlipEnemyDiscs(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        boolean canFlip = false;

        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +0, -1, discTypeToBeInserted); // UP
        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +1, -1, discTypeToBeInserted); // UP-RIGHT
        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +1, +0, discTypeToBeInserted); // RIGHT
        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +1, +1, discTypeToBeInserted); // DOWN-RIGHT
        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, +0, +1, discTypeToBeInserted); // DOWN
        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, -1, +1, discTypeToBeInserted); // DOWN-LEFT
        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, -1, +0, discTypeToBeInserted); // LEFT
        canFlip = canFlip || canFlipEnemyDiscsInDirection(targetInsertionPoint, -1, -1, discTypeToBeInserted); // UP-LEFT

        return canFlip;
    }

    // regular
    private boolean canFlipEnemyDiscsInDirection(Point targetInsertionPoint, int deltaX, int deltaY, eDiscType discTypeToBeInserted)
    {
        int countOfSequenceFlippableDiscs = 0;
        boolean keepChecking = true;
        int row = targetInsertionPoint.GetRow() + deltaY, col = targetInsertionPoint.GetCol() + deltaX;
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


                row += deltaY;
                col += deltaX;
            }
        }

        return true;
    }

    private int flipEnemyDiscs(Point targetInsertionPoint, eDiscType discTypeToBeInserted)
    {
        // Assuming you can flip whosoever discs but yours.
        int CountOfFlippedDiscs = 0;

        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +0, +1, discTypeToBeInserted); // UP
        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +1, +1, discTypeToBeInserted); // UP-RIGHT
        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +1, +0, discTypeToBeInserted); // RIGHT
        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +1, -1, discTypeToBeInserted); // DOWN-RIGHT
        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, +0, -1, discTypeToBeInserted); // DOWN
        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, -1, -1, discTypeToBeInserted); // DOWN-LEFT
        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, -1, +0, discTypeToBeInserted); // LEFT
        CountOfFlippedDiscs += flipEnemyDiscsInDirection(targetInsertionPoint, -1, +1, discTypeToBeInserted); // UP-LEFT

        return CountOfFlippedDiscs;
    }

    private int flipEnemyDiscsInDirection(Point targetInsertionPoint, int deltaX, int deltaY, eDiscType discTypeToBeInserted)
    {
        int row = targetInsertionPoint.GetRow(), col = targetInsertionPoint.GetCol(), countOfFlippedDiscs = 0;
        Disc currentDisc = board[row + deltaY][col + deltaX];

        if(canFlipEnemyDiscsInDirection(targetInsertionPoint, deltaX, deltaY, discTypeToBeInserted))
        {
            while(currentDisc != null)
            {
                currentDisc.SetType(discTypeToBeInserted);
                countOfFlippedDiscs++;

                row += deltaY;
                col += deltaX;
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

    private void InitializeBoard(LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers)
    {
        nullifyBoardCells();
        ArrayList<Point> currentPlayerIntialDiscs;
        Set<Player> playersSet = intialDiscsPointsOfPlayers.keySet();

        for(Player player : playersSet)
        {
            currentPlayerIntialDiscs = intialDiscsPointsOfPlayers.get(player);

            for(Point point : currentPlayerIntialDiscs)
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
}

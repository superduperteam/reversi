package GameEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class Board
{
    private int height;
    private int width;
    private Disc board[][];

    public Board(int height, int width, LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers)
    {
        board = new Disc[height][width];
        InitializeBoard(intialDiscsPointsOfPlayers);
    }

    // Returns the number of flipped discs that were flipped because of the given move.
    public int UpdateBoard(Point targetInsertionPoint){return 0;}

    public boolean IsMoveLegal(Point targetInsertionPoint) {return true;}

    private int flipEnemyDiscs(Point targetInsertionPoint) {return 0;}

    private int flipEnemyDiscsInDirection(Point targetInsertionPoint, int deltaX, int deltaY) {return 0;}

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
//                board[point.GetX()][point.GetY()] = new Disc(player.GetDiscType());
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
                board[point.GetX()][point.GetY()] = new Disc(player.GetDiscType());
            }
        }
    }

    private void nullifyBoardCells()
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

package GameEngine;
//import GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class GameUI
{
    // ## remember to switch back
    private static int colIntialNumber = 0;
    private static int boardCellSize = 5;
    private static int rowIntialNumber = 0;
    private static char space = ' ';
    private static int height = 10;
    private static int width = 10;
    private static char rowSeparator = '=';
    private static char colSeparator = '|';

    public void Start()
    {


        // Test
        // Will be deleted after XML addition
        List<Player> playersList = new ArrayList<>(2);
        playersList.add(new Player("Ido", true, eDiscType.BLACK, 0));
        playersList.add(new Player("Saar", true, eDiscType.WHITE, 1));

        GameManager gameManager = new GameManager(GameManager.eGameMode.Regular, playersList);
        LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers = new  LinkedHashMap<Player, ArrayList<Point>>();
        ArrayList<Point> IdoPoints = new ArrayList<>();
        IdoPoints.add(new Point(4,4));
        IdoPoints.add(new Point(5,5));

        ArrayList<Point> SaarPoints = new ArrayList<>();
        SaarPoints.add(new Point(4,5));
        SaarPoints.add(new Point(5,4));

        intialDiscsPointsOfPlayers.put(playersList.get(0), IdoPoints);
        intialDiscsPointsOfPlayers.put(playersList.get(1), SaarPoints);
        Board board = new Board(height, width, intialDiscsPointsOfPlayers, GameManager.eGameMode.Regular);
        //


        board.nullifyBoardCells();
        board.board[7][5] = new Disc(eDiscType.BLACK);
        board.board[7][5] = new Disc(eDiscType.BLACK);
        board.board[7][1] = new Disc(eDiscType.BLACK);

        board.board[4][4] = new Disc(eDiscType.WHITE);
        board.board[4][5] = new Disc(eDiscType.WHITE);
        board.board[5][3] = new Disc(eDiscType.WHITE);
        board.board[5][5] = new Disc(eDiscType.WHITE);
        board.board[6][2] = new Disc(eDiscType.WHITE);
        board.board[6][5] = new Disc(eDiscType.WHITE);

        board.board[2][4] = new Disc(eDiscType.WHITE);

        printGameState(board);

        Player activePlayer = gameManager.GetActivePlayer();

        activePlayer.MakeMove(new Point(5, 3), board);
        printGameState(board);
    }

    private void printDiscTypeToScreen(eDiscType discType)
    {
        System.out.print(discType.toString());
    }

    private void printGameState(Board board)
    {
        printColsLetters(board);
        printRowsSeparators(board);
        printInnerBoard(board);
    }

    private void printColsLetters(Board board)
    {
        int colLetter = colIntialNumber;

        printSpaces(boardCellSize);
        for (int i = 0; i < width; i++)
        {
            System.out.print(colLetter);

            printSpaces(boardCellSize - 1);
            if(colLetter <= 9) // 1-9 numbers need one digit.
            {
                System.out.print(space);
            }

            colLetter++;
        }

        System.out.println();
    }

    private void printRowsSeparators(Board board)
    {
        System.out.print(space);
        System.out.print(space);
        System.out.print(rowSeparator);
        for (int i = 0; i < width; i++)
        {
            printRowSeparators(boardCellSize);
            System.out.print(rowSeparator);
        }

        System.out.println();
    }

    private void printInnerBoard(Board board)
    {
        int rowLetter = rowIntialNumber;
        Disc discInCell;

        for (int i = 0; i < height; i++)
        {
            if(rowLetter <= 9) // 1-9 numbers need one digit.
            {
                System.out.print(space);
            }
            System.out.print(rowLetter);
            System.out.print(colSeparator);

            for (int j = 0; j < width; j++)
            {
                discInCell = board.Get(i,j);

                if (discInCell == null)
                {
                    printSpaces(boardCellSize);
                }
                else
                {
                    System.out.print(space);
                    System.out.print(space);
                    printDiscTypeToScreen(discInCell.GetType());
                    System.out.print(space);
                    System.out.print(space);
                }

                System.out.print(colSeparator);
            }

            rowLetter++;
            System.out.println();
            printRowsSeparators(board);
        }
    }

    private void printSpaces(int numOfSpaces)
    {
        for (int i = 0; i < numOfSpaces; i++)
        {
            System.out.print(space);
        }
    }

    private void printRowSeparators(int numOfSeparators)
    {
        for (int i = 0; i < numOfSeparators; i++)
        {
            System.out.print(rowSeparator);
        }
    }
}

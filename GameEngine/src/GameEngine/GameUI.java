package GameEngine;
//import GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.lang.String;

public class GameUI
{
    private static int colIntialNumber = 1;
    private static int rowIntialNumber = 1;
    private static int boardCellSize = 5;
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

        LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers = new  LinkedHashMap<Player, ArrayList<Point>>();
        ArrayList<Point> IdoPoints = new ArrayList<>();
        IdoPoints.add(new Point(4, 4));
        IdoPoints.add(new Point(5, 5));

        ArrayList<Point> SaarPoints = new ArrayList<>();
        SaarPoints.add(new Point(5, 4));
        SaarPoints.add(new Point(4, 5));

        intialDiscsPointsOfPlayers.put(playersList.get(0), IdoPoints);
        intialDiscsPointsOfPlayers.put(playersList.get(1), SaarPoints);
        Board board = new Board(height, width, intialDiscsPointsOfPlayers, GameManager.eGameMode.Regular);

        GameManager gameManager = new GameManager(GameManager.eGameMode.Regular, playersList, board);


//
//
//
//
//        board.nullifyBoardCells();
//        board.board[7][5] = new Disc(eDiscType.BLACK);
//        board.board[7][5] = new Disc(eDiscType.BLACK);
//        board.board[7][1] = new Disc(eDiscType.BLACK);
//
//        board.board[4][4] = new Disc(eDiscType.WHITE);
//        board.board[4][5] = new Disc(eDiscType.WHITE);
//        board.board[5][3] = new Disc(eDiscType.WHITE);
//        board.board[5][5] = new Disc(eDiscType.WHITE);
//        board.board[6][2] = new Disc(eDiscType.WHITE);
//        board.board[6][5] = new Disc(eDiscType.WHITE);
//
//        board.board[2][4] = new Disc(eDiscType.WHITE);
//
//        printGameState(board);
//
//        Player activePlayer = gameManager.GetActivePlayer();
//
//        activePlayer.MakeMove(new Point(3, 5), board);
//        printGameState(board);
          gameLoop(gameManager);
    }
// TODO IsGameover(), UpdateGameScore(), getMoveFromHuman(), GetRandomMove() ,List<Point> GetAllPossibleMoves()
    private void gameLoop(GameManager gameManager)
    {
        boolean isMoveLegalInserted;
        Player activePlayer;
        Point targetInsertionPoint;
        Board board = gameManager.GetBoard();

        while(true) // call gameManager.IsGameover
        {
            printGameState(board);
            activePlayer = gameManager.GetActivePlayer();

            do {
                targetInsertionPoint = getMoveFromPlayer(activePlayer, board);
                isMoveLegalInserted = activePlayer.MakeMove(targetInsertionPoint, board);

                if(!isMoveLegalInserted)
                {
                    printIllegalMoveInserted();
                }
            }
            while(!isMoveLegalInserted);

            gameManager.ChangeTurn();
        }
    }

    private void printIllegalMoveInserted()
    {
        System.out.println("Illegal move was inserted. Please try again.");
    }

    private void printWhoseTurn(Player activePlayer)
    {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(activePlayer.GetName());
        strBuilder.append("'s turn ('");
        strBuilder.append(activePlayer.GetDiscType().toString());
        strBuilder.append("')!");
        System.out.println(strBuilder.toString());
    }

    private Point getMoveFromPlayer(Player activePlayer, Board board)
    {
        Point targetInsertionPoint;

        if(activePlayer.IsHuman())
        {
            printWhoseTurn(activePlayer);
            targetInsertionPoint = getMoveFromHuman(board);
        }
        else
        {
            targetInsertionPoint = activePlayer.GetRandomMove(board);
        }

        return targetInsertionPoint;
    }

    private Point getMoveFromHuman(Board gameBoard)
    {
        Point nextMoveOfUser = null;
        int row, col;
        StringBuilder strBuilder = new StringBuilder();
        boolean isMoveSyntactic = false; // isMoveLegal means it's in board range and it's syntactic
        Scanner reader = new Scanner(System.in);  // Reading from System.in

        while(!isMoveSyntactic) {
            System.out.println("Please enter your next move: row,column");
            strBuilder.append("For example: ");
            strBuilder.append("\"4,5\"");
            strBuilder.append(" (without the quotation marks)");
            System.out.println(strBuilder.toString());
            String userInputStr = reader.nextLine();

            if (isStringOnlyDigitsAndSeperator(userInputStr)) {
                if (userInputStr.contains(",")) {
                    String[] coordinates = userInputStr.split(",");

                    if (coordinates[0].length() != 0 && coordinates[1].length() != 0) {
                        if (isStringOnlyDigits(coordinates[0]) && (isStringOnlyDigits(coordinates[1]))) {
                            row = Integer.parseInt(coordinates[0]);
                            col = Integer.parseInt(coordinates[1]);
                            nextMoveOfUser = new Point(row - rowIntialNumber, col - colIntialNumber);

                            if (gameBoard.IsCellPointInRange(nextMoveOfUser)) {
                                isMoveSyntactic = true;
                            } else {
                                System.out.println("Please enter a point that is in the range of the game board. Try again");
                                System.out.println();
                            }
                        } else {
                            System.out.println("Your row/column doesn't consist of only digits. Please Try again.");
                            System.out.println();
                        }
                    } else {
                        System.out.println("Your row/column doesn't consist of any chars. Please Try again.");
                        System.out.println();
                    }
                } else {
                    System.out.println("Your input doesn't contain ','. Please Try again.");
                    System.out.println();
                }
            } else {
                System.out.println("Your input doesn't contain only numbers and ','. Please Try again.");
                System.out.println();
            }
        }

        return nextMoveOfUser;
    }

    private boolean isStringOnlyDigits(String str)
    {
        for(int i = 0; i< str.length(); i++)
        {
            if(str.charAt(i) < '0' || str.charAt(i) > '9')
            {
                return false;
            }
        }

        return true;
    }

    private boolean isStringOnlyDigitsAndSeperator(String str)
    {
        for(int i = 0; i< str.length(); i++)
        {
            if((str.charAt(i) < '0' || str.charAt(i) > '9') && (str.charAt(i) != ','))
            {
                return false;
            }
        }

        return true;
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

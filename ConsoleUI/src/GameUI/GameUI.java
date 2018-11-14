package GameUI;

import Exceptions.*;
import GameEngine.*;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.lang.String;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameUI
{
    private static int colInitialNumber = 1;
    private static int rowInitialNumber = 1;
    private static int boardCellSize = 5;
    private static char space = ' ';
    private static char rowSeparator = '=';
    private static char colSeparator = '|';
    private GameManager gameManager;
    private final String FILE_NAME = "saved_game_data.dat";

    public void start()
    {
        final int LOAD_XML = 1;
        final int START_GAME = 2;
        final int SHOW_GAME_DESCRIPTION = 3;
        final int LOAD_PREVIOUSLY_SAVED_GAME = 4;
        //GameManager gameManager;
        // Test
        // Will be deleted after XML addition
//        List<Player> playersList = new ArrayList<>(2);
//        playersList.add(new Player("Ido", true, eDiscType.BLACK, new BigInteger("0")));
//        playersList.add(new Player("Saar", true, eDiscType.WHITE, new BigInteger("1")));
//
//        LinkedHashMap<Player, ArrayList<Point>> intialDiscsPointsOfPlayers = new  LinkedHashMap<Player, ArrayList<Point>>();
//        ArrayList<Point> IdoPoints = new ArrayList<>();
//        IdoPoints.add(new Point(4, 4));
//        IdoPoints.add(new Point(5, 5));
//
//        ArrayList<Point> SaarPoints = new ArrayList<>();
//        SaarPoints.add(new Point(5, 4));
//        SaarPoints.add(new Point(4, 5));
//
//        intialDiscsPointsOfPlayers.put(playersList.get(0), IdoPoints);
//        intialDiscsPointsOfPlayers.put(playersList.get(1), SaarPoints);
//        Board board = new Board(height, width, intialDiscsPointsOfPlayers, GameManager.eGameMode.Regular);
//
//        GameManager gameManager = new GameManager(GameManager.eGameMode.Regular, playersList, board);
        int menuInput;
        boolean isGameLoaded = false;
        boolean doesUserWantToPlay = true;
        boolean didLoadPreviouslyPlayedGame = false;
        List<Player> playersList = generateInitialPlayersList();

        List<String> menuOptions= new ArrayList<>
                (Arrays.asList(String.valueOf(LOAD_XML), String.valueOf(START_GAME), String.valueOf(SHOW_GAME_DESCRIPTION), String.valueOf(LOAD_PREVIOUSLY_SAVED_GAME)));

        while(isGameLoaded == false || doesUserWantToPlay)
        {
            printStartMenu();
            menuInput = getMenuInput(menuOptions);

            if(menuInput == LOAD_XML)
            {
                isGameLoaded = loadXML(playersList);
            }
            else if(menuInput == LOAD_PREVIOUSLY_SAVED_GAME)
            {
                gameManager = loadGameFromFile();

                if(gameManager != null){
                    isGameLoaded = true;
                    didLoadPreviouslyPlayedGame = true;
                }
                else{
                    isGameLoaded = false;
                    System.out.println("Failed to load previously saved game. Please try again");
                }
            }
            else if(menuInput == START_GAME)
            {
                if(isGameLoaded)
                { // You must to have gameManager here
//                playersList = getPlayersDetailsFromUser(playersList);
                    if(!didLoadPreviouslyPlayedGame) {
                        getPlayersDetailsFromUser(gameManager.getPlayersList());
                    }
                    doesUserWantToPlay = gameLoop(gameManager);
                    gameManager.resetGame();
                }
                else System.out.println("Game isn't loaded yet.");
            }
            else if(menuInput == SHOW_GAME_DESCRIPTION)
            {
                if(isGameLoaded)
                {
                    printGameState(gameManager, false);
                }
                else System.out.println("Game isn't loaded yet.");
            }

            if(isGameLoaded && menuInput != START_GAME) {System.out.println("Game was loaded successfully!\n"); }
        }

//        playersList = getPlayersDetailsFromUser(playersList);
//        gameLoop(gameManager);

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
    }


    private boolean loadXML(List<Player> playersList)
    {
        GameSettingsReader gameSettingsReader = new GameSettingsReader();
        boolean isGameLoaded = true;
        System.out.println("Please enter a XML path:");
        Path filePath = getXMLPathFromUser();

        try {
            gameManager = gameSettingsReader.readGameSettings(playersList, filePath);
        } catch (NoXMLFileException noXMLFile) {
            //noXMLFile.printStackTrace();
            System.out.println("Error: " + noXMLFile);
            isGameLoaded = false;
        } catch (PlayerHasNoInitialPositionsException playerHasNoInitialPositionsException) {
            System.out.println("Error: " + playerHasNoInitialPositionsException);
            isGameLoaded = false;
        }
        catch (PlayersInitPositionsOverrideEachOtherException playersInitPositionsOverrideEachOtherException) {
            //playersInitPositionsOverrideEachOtherException.printStackTrace();
            System.out.println("Error: " + playersInitPositionsOverrideEachOtherException);
            isGameLoaded = false;
        } catch (BoardSizeDoesntMatchNumOfPlayersException boardSizeDoesntMatchNumOfPlayersException) {
            //boardSizeDoesntMatchNumOfPlayersException.printStackTrace();
            System.out.println("Error: " + boardSizeDoesntMatchNumOfPlayersException);
            isGameLoaded = false;
        } catch (PlayersInitPositionsOutOfRangeException playersInitPositionsOutOfRangeException) {
            //playersInitPositionsOutOfRangeException.printStackTrace();
            System.out.println("Error: " + playersInitPositionsOutOfRangeException);
            isGameLoaded = false;
        } catch (RowsNotInRangeException rowsNotInRangeException) {
            //rowsNotInRangeException.printStackTrace();
            System.out.println("Error: " + rowsNotInRangeException);
            isGameLoaded = false;
        } catch (ColumnsNotInRangeException columnsNotInRangeException) {
            //columnsNotInRangeException.printStackTrace();
            System.out.println("Error: " + columnsNotInRangeException);
            isGameLoaded = false;
        } catch (IslandsOnRegularModeException islandsOnRegularModeException) {
            //islandsOnRegularModeException.printStackTrace();
            System.out.println("Error: " + islandsOnRegularModeException);
            isGameLoaded = false;
        }
        //TODO check if xml file is application legal

        return isGameLoaded;
    }

    // returns true if user want to play again.
    private boolean gameLoop(GameManager gameManager)
    {
        // int i = 1; // Option 5 (History) check
        List<String> menuOptions = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
        int menuInput;
        boolean didUserAskToEndGame = false;
        boolean doesUserWantToPlayAgain;

        // call this only after all info about players is gathered. ##
        gameManager.activateGame();

        while(!gameManager.isGameOver() && !didUserAskToEndGame)
        {
            printGameMenu();
            menuInput = getMenuInput(menuOptions);

            didUserAskToEndGame = executeMenuCommand(menuInput);
        }

        if(!didUserAskToEndGame)
        {
            printWinnerDeclarationMessage();
            doesUserWantToPlayAgain =  AskUserToPlayAgain();
        }
        else doesUserWantToPlayAgain = false;

        return doesUserWantToPlayAgain;
    }

    private void printWinnerDeclarationMessage()
    {
        Player winnerPlayer = gameManager.getWinner();
        System.out.print("The winner is: ");
        System.out.println(winnerPlayer.getName());
        System.out.print("Score: ");
        System.out.println(winnerPlayer.getStatistics().getScore());

        // Print for other players. (just one in this exercise)
        for(Player player : gameManager.getPlayersList())
        {
            if(!player.equals(winnerPlayer))
            {
                System.out.println(player.getName());
                System.out.print("Score: ");
                System.out.println(player.getStatistics().getScore());
            }
        }
    }

    private boolean AskUserToPlayAgain()
    {
        boolean hasAnswered = false;
        boolean doesUserWantToPlayAgain = false;
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while(!hasAnswered)
        {
            System.out.println("Do you want to play again? y/n");
            userInput = scanner.nextLine();

            if(userInput.toLowerCase().equals("y"))
            {
                hasAnswered = true;
                doesUserWantToPlayAgain = true;
            }
            else if(userInput.toLowerCase().equals("n"))
            {
                hasAnswered = true;
                doesUserWantToPlayAgain = false;
            }
            else
            {
                System.out.println("Please answer accordingly. Please try again.");
            }
        }

        return doesUserWantToPlayAgain;
    }

    private int getMenuInput(List<String> numbersList) {
        Scanner scanner = new Scanner(System.in);
        String input;
        int result = 0;
        boolean isInputValid = false;


        do{
            input = scanner.nextLine();
            if(numbersList.contains(input)) {
                result = Integer.parseInt(input);
                isInputValid = true;
            }

            else{
                System.out.print("Please enter a valid input (the options are: ");
                System.out.print(numbersList);
                System.out.println(")");
            }
        }while(!isInputValid);

        return result;
    }

    private void printStartMenu() {
        StringBuilder startMenuOptions = new StringBuilder();

        startMenuOptions.append("Please enter one of the options below (enter the desired option number)\n");
        startMenuOptions.append("1.Load new XML file\n");
        startMenuOptions.append("2.Start game\n");
        startMenuOptions.append("3.Show game description\n");
        startMenuOptions.append("4.Load previous saved game\n");

        System.out.println(startMenuOptions.toString());
    }

    private Path getXMLPathFromUser()
    {
        Scanner reader = new Scanner(System.in);
        String filePathString;

        filePathString = reader.nextLine();
        Path filePath = Paths.get(filePathString);

        return filePath;
    }

    private void printHistoryOfBoardStates(GameManager gameManager)
    {
        List<Board> boardStatesList = gameManager.getHistoryOfBoardStates();

        System.out.println();
        System.out.println("History of board states: ");
        for (Board board : boardStatesList)
        {
            printBoardState(board);
        }
        System.out.println();
    }

    private void printGameMenu() {
        StringBuilder gameMenuOptions = new StringBuilder();

        gameMenuOptions.append("Please enter one of the options below (enter the desired option number)\n");
        gameMenuOptions.append("1.Show game description\n");
        gameMenuOptions.append("2.Make a turn\n");
        gameMenuOptions.append("3.Show game history\n");
        gameMenuOptions.append("4.Exit\n");
        gameMenuOptions.append("5.Undo last move\n");
        gameMenuOptions.append("6.Save game\n");

        System.out.println(gameMenuOptions.toString());
    }

    private void printGameMode(GameManager gameManager)
    {
        System.out.print("Game mode: ");
        System.out.println(gameManager.getGameMode());
    }

    private void printIsGameActive(boolean isGameActive)
    {
        if(isGameActive)
        {
            System.out.println("Game is active.");
        }
        else System.out.println("Game isn't active.");
    }

    private void printPlayersStatistics(List<Player> playersList)
    {
        System.out.println();
        System.out.println("Players Statistics:");
        playersList.forEach(player ->
        {
            System.out.println();
            System.out.print(player.GetName());
            System.out.println(":");
            System.out.print("Turns played: ");
            System.out.println(player.getStatistics().getCountOfPlayedTurns());
            System.out.print("Average of flips: ");
            System.out.println(player.getStatistics().getAverageOfFlips());
            System.out.print("Score: ");
            System.out.println(player.getStatistics().getScore());
        });
    }

    private void printPlayersAndTheirDiscType(List<Player> playersList) {
        playersList.forEach(player ->
        {
            System.out.print(player.getName());
            System.out.print(": '");
            System.out.print(player.GetDiscType());
            System.out.println("'");
        });
    }

    private void printGameState(GameManager gameManager, boolean isGameActive)
    {
        System.out.println("Game Description:");
        // System.out.println(getStringOfInitialDiscPointsOfPlayers(gameManager.getBoard().getInitialDiscPositionOfPlayers()));
        printBoardState(gameManager.getInitialBoard()); // or printBoardState(gameManager.getBoard())?
        printPlayersAndTheirDiscType(gameManager.getPlayersList());
        printGameMode(gameManager);
        printIsGameActive(isGameActive);

        if(!gameManager.isGameOver() && isGameActive)
        {
            System.out.print("Active Player: ");
            System.out.println(gameManager.getActivePlayer().getName());
            printPlayersStatistics(gameManager.getPlayersList());
        }
        System.out.println();
    }

//    public String getStringOfInitialDiscPointsOfPlayers(HashMap<Player, List<Point>> initialDiscPointsOfPlayers)
//    {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append("Initial positions for players: \n");
//
//        initialDiscPointsOfPlayers.forEach((player, discPointsList) ->
//                {
//                    stringBuilder.append(player.getName());
//                    stringBuilder.append(" ('"+player.GetDiscType()+"')");
//                    stringBuilder.append(":");
//                    for(Point discPoint : discPointsList)
//                    {
//                        stringBuilder.append(" " + discPoint.toString() + " ");
//                    }
//                    stringBuilder.append("\n");
//                }
//        );
//
//        return stringBuilder.toString();
//    }

    private boolean executeMenuCommand(int commandToExecute) {
        final int PRINT_GAME_STATE = 1;
        final int MAKE_MOVE = 2;
        final int SHOW_HISTORY = 3;
        final int EXIT = 4;
        final int UNDO = 5;
        final int SAVE_GAME = 6;
        Board board = gameManager.getBoard();
        boolean didUserAskToEndGame = false;


        switch (commandToExecute){
            case PRINT_GAME_STATE:
                //TODO implement (command 3 in reversi.doc) (saar)
                printGameState(gameManager, true);
                break;
            case MAKE_MOVE:
                playNextMove(gameManager);
                break;
            case SHOW_HISTORY:
                printHistoryOfBoardStates(gameManager);
                break;
            case EXIT:
                didUserAskToEndGame = true;
                break;
            case UNDO:
                gameManager.undo();
                System.out.println("After undo the game state is:");
                printBoardState(gameManager.getBoard());
                printWhoseTurn(gameManager.getActivePlayer());
                break;
            case SAVE_GAME:
                saveGameToFile(gameManager);
                System.out.println("Game saved to file");
                break;
        }

        return  didUserAskToEndGame;
    }

    private GameManager loadGameFromFile(){
        GameManager loadedGameManager = null;

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {
            // we know that we read array list of Persons
            loadedGameManager =
                    (GameManager) in.readObject();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error while trying to load file. Please try again.");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("Error while trying to load file (Class Not Found). Please try again.");
        }

        return loadedGameManager;
    }

    private void saveGameToFile(GameManager gameManager) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {
            out.writeObject(gameManager);
            out.flush();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error while trying to save file. Please try again.");
        }
    }

    private void playNextMove(GameManager gameManager)
    {
        Board board = gameManager.getBoard();
        Player activePlayer = gameManager.getActivePlayer();
        Point targetInsertionPoint;
        GameManager.eMoveStatus moveStatus;

        if(gameManager.getActivePlayer().isHuman()) {
            printBoardState(board);
            printWhoseTurn(gameManager.getActivePlayer());
        }

        do {
            targetInsertionPoint = getMoveFromPlayer(activePlayer, board);
            moveStatus = activePlayer.makeMove(targetInsertionPoint, board);
            printToUserIfIllegalMoveWasInserted(moveStatus);
        }
        while(moveStatus != GameManager.eMoveStatus.OK);

        gameManager.changeTurn();
        printBoardState(board);
    }

    private boolean isPlayerHumanUserAnswer(String playerNumber)
    {
        boolean isPlayerHuman = true;
        boolean hasUserAnswered = false;
        Scanner reader = new Scanner(System.in);
        String isPlayerHumanUserStr;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Is ");
        stringBuilder.append(playerNumber);
        stringBuilder.append(" player human? y/n");

        do {
            System.out.println(stringBuilder.toString());
            isPlayerHumanUserStr = reader.nextLine();

            if (isPlayerHumanUserStr.toLowerCase().equals("y")) {
                isPlayerHuman = true;
                hasUserAnswered = true;
            } else if (isPlayerHumanUserStr.toLowerCase().equals("n")) {
                isPlayerHuman = false;
                hasUserAnswered = true;
            }
            if(!hasUserAnswered)
            {
                System.out.println("Please answer accordingly. Please try again.");
            }
        } while (!hasUserAnswered);

        return isPlayerHuman; // not really needed
    }

    private void printToUserIfIllegalMoveWasInserted(GameManager.eMoveStatus moveStatus)
    {
        StringBuilder illegalMoveMessageToUser = new StringBuilder();

        if(moveStatus != GameManager.eMoveStatus.OK)
        {
            illegalMoveMessageToUser.append(moveStatus.toString());
            illegalMoveMessageToUser.append(" Please try again.");
            System.out.println(illegalMoveMessageToUser.toString());
        }
    }

    private void printWhoseTurn(Player activePlayer)
    {
        StringBuilder strBuilder = new StringBuilder();

        if(activePlayer.getIsHuman()) {

            strBuilder.append(activePlayer.GetName());
            strBuilder.append("'s turn ('");
            strBuilder.append(activePlayer.GetDiscType().toString());
            strBuilder.append("')!");
            System.out.println(strBuilder.toString());
        }
    }

    private Point getMoveFromPlayer(Player activePlayer, Board board)
    {
        Point targetInsertionPoint;

        if(activePlayer.isHuman())
        {
            //printWhoseTurn(activePlayer);
            targetInsertionPoint = getMoveFromHuman();
        }
        else
        {
            targetInsertionPoint = activePlayer.getRandomMove(board);
        }

        return targetInsertionPoint;
    }

    private Point getMoveFromHuman() {
        Point nextMoveOfUser = null;
        int row, col;

        boolean isMoveSyntactic = false; // isMoveLegal means it's in board range and it's syntactic
        Scanner reader = new Scanner(System.in);

        while (!isMoveSyntactic) {
            System.out.println("Please enter your next move: row,column");
            printExampleOfInsertionFormatForUser();
            String userInputStr = reader.nextLine();


            if (isStringOnlyDigitsAndSeperator(userInputStr)) {
                if (userInputStr.contains(",")) {
                    String[] coordinates = userInputStr.split(",");
                    if (coordinates[0].length() != 0 && coordinates[1].length() != 0) {
                        if (isStringOnlyDigits(coordinates[0]) && (isStringOnlyDigits(coordinates[1]))) {
                            row = Integer.parseInt(coordinates[0]);
                            col = Integer.parseInt(coordinates[1]);
                            nextMoveOfUser = new Point(row - rowInitialNumber, col - colInitialNumber);

//                                if (gameBoard.isCellPointInRange(nextMoveOfUser)) {
                            isMoveSyntactic = true;
//                                } else {
//                                    System.out.println("Please enter a point that is in the range of the game board. Try again");
//                                    System.out.println();
//                                }
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


    private void printExampleOfInsertionFormatForUser()
    {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("For example: ");
        strBuilder.append("\"4,5\"");
        strBuilder.append(" (without the quotation marks)\n");
        System.out.println(strBuilder.toString());
    }

    private void getPlayersDetailsFromUser(List<Player> playersList)
    {
        boolean isFirstPlayerHuman, isSecondPlayerHuman;
        boolean areBothAIs;

        do {
            isFirstPlayerHuman = isPlayerHumanUserAnswer("first");


            isSecondPlayerHuman = isPlayerHumanUserAnswer("second");
            if(!isFirstPlayerHuman && !isSecondPlayerHuman)
            {
                System.out.println("Both players can't be AIs. Please try again.");
                areBothAIs = true;
            }

            areBothAIs = !isFirstPlayerHuman && !isSecondPlayerHuman;
        }while(areBothAIs);

        // Now we have all the data we need
        playersList.get(0).setIsHuman(isFirstPlayerHuman);
        playersList.get(1).setIsHuman(isSecondPlayerHuman);
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

    private void printBoardState(Board board)
    {
        printColsLetters(board);
        printRowsSeparators(board);
        printInnerBoard(board);
    }

    private void printColsLetters(Board board)
    {
        int colLetter = colInitialNumber;

        printSpaces(boardCellSize);
        for (int i = 0; i < board.getWidth(); i++)
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
        for (int i = 0; i < board.getWidth(); i++)
        {
            printRowSeparators(boardCellSize);
            System.out.print(rowSeparator);
        }

        System.out.println();
    }

    private void printInnerBoard(Board board)
    {
        int rowLetter = rowInitialNumber;
        Disc discInCell;

        for (int i = 0; i < board.getHeight(); i++)
        {
            if(rowLetter <= 9) // 1-9 numbers need one digit.
            {
                System.out.print(space);
            }
            System.out.print(rowLetter);
            System.out.print(colSeparator);

            for (int j = 0; j < board.getWidth(); j++)
            {
                discInCell = board.get(i,j);

                if (discInCell == null)
                {
                    printSpaces(boardCellSize);
                }
                else
                {
                    System.out.print(space);
                    System.out.print(space);
                    printDiscTypeToScreen(discInCell.getType());
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

    private List<Player> generateInitialPlayersList()
    {
        List<Player> playersList = new ArrayList<>(2);
        playersList.add(new Player("Player 1", true,eDiscType.BLACK, new BigInteger("1")));
        playersList.add(new Player("Player 2", true,eDiscType.WHITE, new BigInteger("2")));

        return playersList;
    }
}
package GameEngine;

import Exceptions.*;
import jaxb.schema.generated.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.io.InputStream;
import java.lang.String;


public class GameSettingsReader {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";
    private final byte MAX_ROWS = 50; // rows in [4, 50]
    private final byte MIN_ROWS = 4;
    private final byte MAX_COLS = 30; // cols in [4, 30]
    private final byte MIN_COLS = 4;
    private final int MIN_NUMBER_OF_PLAYERS = 2;
    private final int MAX_NUMBER_OF_PLAYERS = 2;
    private int expectedNumberOfParticipants = 2; // ## to be removed in exercise 2

    // TODO: Check path, check XML


    public GameManager readGameSettings(List<Player> playersList, Path xmlFilePath) throws BoardSizeDoesntMatchNumOfPlayersException,
            ColumnsNotInRangeException, IslandsOnRegularModeException, NoXMLFileException, PlayersInitPositionsOutOfRangeException, PlayersInitPositionsOverrideEachOtherException,
            RowsNotInRangeException, PlayerHasNoInitialPositionsException, OutOfRangeNumberOfParticipantsException {
//        Scanner reader = new Scanner(System.in);
//        String filePathString;
//
//        filePathString = reader.nextLine();
//        Path filePath = Paths.get(filePathString);
        // check path

        // ## REMEMBER TO CHANGE .XML !!
        File xmlFile = new File(xmlFilePath.toString());//xmlFilePath.toString());

//        if(xmlFile != null) {
//            throw new Exceptions.NoXMLFileException();
//        }
        if(!xmlFilePath.toString().toLowerCase().endsWith(".xml"))
        {
            throw new NoXMLFileException();
        }
        else {
            try
            {
                InputStream inputStream = new FileInputStream(xmlFile);
                return extractGameSettings(inputStream, playersList);
            } catch (IOException e)
            {
                throw new NoXMLFileException();
            }
        }
    }

    private void checkNumberOfParticipants(GameDescriptor gameDescriptor, int expectedNumberOfParticipants) throws OutOfRangeNumberOfParticipantsException
    {
        if(gameDescriptor.getGame().getInitialPositions().getParticipant().size()!= expectedNumberOfParticipants)
        {
            throw new OutOfRangeNumberOfParticipantsException(expectedNumberOfParticipants);
        }
    }

    private void areNumberOfRowsInRange(GameDescriptor gamedDescriptor) throws RowsNotInRangeException
    {
        byte xmlRows = gamedDescriptor.getGame().getBoard().getRows();

        if(xmlRows < MIN_ROWS || xmlRows > MAX_ROWS)
            throw new RowsNotInRangeException();
    }

    private GameManager getGameDetails(GameDescriptor gameDescriptor, List<GameEngine.Player> playersList)
    {
        GameManager.eGameMode gameMode;
        GameEngine.Board board;

        board = createBoardFromGameDetails(gameDescriptor, playersList);
        gameMode = getEGameMode(gameDescriptor);
        GameManager gameManager = new GameManager(gameMode, playersList, board);

        return gameManager;
    }

    private GameEngine.Board createBoardFromGameDetails(GameDescriptor gameDescriptor,  List<GameEngine.Player> playersList) {
        GameManager.eGameMode gameMode;
        int playerIndex = 0;
        List<Point> currInitialPointsList;
        List<Position> currPlayerIntialPositions;
        List<Participant> participantsList = gameDescriptor.getGame().getInitialPositions().getParticipant();
        HashMap<GameEngine.Player, List<Point>> initialDiscsPointsOfPlayers = new  LinkedHashMap<>();

        for(Participant participant : participantsList)
        {
            currPlayerIntialPositions = participant.getPosition();
            currInitialPointsList = new ArrayList<>();

            for(Position position : currPlayerIntialPositions)
            {
                currInitialPointsList.add(new Point(position.getRow() - 1, position.getColumn() - 1));
            }

            initialDiscsPointsOfPlayers.put(playersList.get(playerIndex), currInitialPointsList);
            playerIndex++;
        }

        gameMode = getEGameMode(gameDescriptor);
        GameEngine.Board board = new GameEngine.Board(gameDescriptor.getGame().getBoard(), initialDiscsPointsOfPlayers, gameMode);

        return board;
    }

//    private List<Position> findIntialPositionsForPlayer(int playerIndex ,List<Participant> participantsList)
//    {
//        for(Participant participant : participantsList)
//        {
//            if(participant.getNumber() == playerIndex)
//            {
//                return participant.getPosition();
//            }
//        }
//
//        return null; // ## throw execption
//    }

    private GameManager extractGameSettings(InputStream xmlStream, List<GameEngine.Player> playersList) throws RowsNotInRangeException,
            ColumnsNotInRangeException, IslandsOnRegularModeException, PlayersInitPositionsOverrideEachOtherException,
            BoardSizeDoesntMatchNumOfPlayersException, PlayersInitPositionsOutOfRangeException, PlayerHasNoInitialPositionsException, OutOfRangeNumberOfParticipantsException
    {
        try{
            GameDescriptor gamedDescriptor = deserializeFrom(xmlStream);
            areNumberOfRowsInRange(gamedDescriptor);
            areNumberOfColsInRange(gamedDescriptor);
            checkNumberOfParticipants(gamedDescriptor, expectedNumberOfParticipants);
            doEachPlayerHasAtLeastOneInitialPoint(gamedDescriptor);
            doesBoardSizeMatchNumOfPlayers(gamedDescriptor);
            areIntialPositionsInRange(gamedDescriptor);
            doIntialPositionsOverrideEachOther(gamedDescriptor);
            areThereIslandsOnRegularMode(gamedDescriptor, playersList);
            return getGameDetails(gamedDescriptor, playersList);
        } catch (JAXBException e)
        {
            e.printStackTrace();
            return null;
        }
    }

        private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }

    private void doIntialPositionsOverrideEachOther(GameDescriptor gameDescriptor) throws PlayersInitPositionsOverrideEachOtherException
    {
        HashSet<Point> positionsSet = new HashSet<>();
        List<Participant> participantsList = gameDescriptor.getGame().getInitialPositions().getParticipant();
        List<Position> currPlayerInitialPositions;
        Point currCellPoint;

        for(Participant participant : participantsList)
        {
            currPlayerInitialPositions = participant.getPosition();

            for(Position position : currPlayerInitialPositions)
            {
                currCellPoint = new Point(position.getRow(),position.getColumn());

                if(!positionsSet.contains(currCellPoint))
                {
                    positionsSet.add(currCellPoint);
                }
                else
                {
                    throw new PlayersInitPositionsOverrideEachOtherException();
                }
            }
        }
    }

    private void doEachPlayerHasAtLeastOneInitialPoint(GameDescriptor gameDescriptor) throws PlayerHasNoInitialPositionsException
    {
        HashSet<Position> positionsSet = new HashSet<>();
        List<Participant> participantsList = gameDescriptor.getGame().getInitialPositions().getParticipant();
        List<Position> currPlayerInitialPositions;

        for(Participant participant : participantsList)
        {
            currPlayerInitialPositions = participant.getPosition();

            if(currPlayerInitialPositions.size() == 0)
            {
                throw new PlayerHasNoInitialPositionsException();
            }
        }
    }

    private void areThereIslandsOnRegularMode(GameDescriptor gameDescriptor, List<GameEngine.Player> playersList) throws IslandsOnRegularModeException {
        GameEngine.Board board;

        board = createBoardFromGameDetails(gameDescriptor, playersList);
        for(int row = 0; row < board.getHeight(); ++row){
            for(int col = 0; col < board.getWidth(); ++col){
                if(board.get(row,col) != null){
                    if(!board.isThereDiscAdjacent(new Point(row,col))){
                        throw new IslandsOnRegularModeException();
                    }
                }
            }
        }
    }

    private void areNumberOfColsInRange(GameDescriptor gamedDescriptor) throws ColumnsNotInRangeException
    {
        byte xmlCols = gamedDescriptor.getGame().getBoard().getColumns();

        if(xmlCols < MIN_COLS || xmlCols > MAX_COLS)
            throw new ColumnsNotInRangeException();
    }

    private void areIntialPositionsInRange(GameDescriptor gameDescriptor) throws PlayersInitPositionsOutOfRangeException
    {
        int maxRowInBoard = (int)gameDescriptor.getGame().getBoard().getRows();
        int maxColInBoard = (int)gameDescriptor.getGame().getBoard().getColumns();
        List<Participant> participantsList = gameDescriptor.getGame().getInitialPositions().getParticipant();
        List<Position> currPlayerInitialPositions;

        for(Participant participant : participantsList)
        {
            currPlayerInitialPositions = participant.getPosition();

            for(Position position : currPlayerInitialPositions)
            {
                if(position.getRow() > maxRowInBoard || position.getRow() < 1)
                {
                    throw new PlayersInitPositionsOutOfRangeException();
                }
                if(position.getColumn() > maxColInBoard || position.getColumn() < 1)
                {
                    throw new PlayersInitPositionsOutOfRangeException();
                }
            }
        }
    }

    private void doesBoardSizeMatchNumOfPlayers(GameDescriptor gameDescriptor) throws BoardSizeDoesntMatchNumOfPlayersException {
        int boardSize, numOfEmptyCells, numOfInitPositions = 0;
        int numOfPlayers = 2;

        //numOfPlayers = gameDescriptor.getPlayers().getPlayer().size();  THIS WILL BE IN EX 02
        boardSize = gameDescriptor.getGame().getBoard().getColumns() * gameDescriptor.getGame().getBoard().getRows();

        for(Participant participant : gameDescriptor.getGame().getInitialPositions().getParticipant()) {
            for(Position position : participant.getPosition()) {
                ++numOfInitPositions;
            }
        }

        numOfEmptyCells = boardSize - numOfInitPositions;
        if(numOfEmptyCells % numOfPlayers != 0) {
            throw new BoardSizeDoesntMatchNumOfPlayersException();
        }
    }

    private GameManager.eGameMode getEGameMode(GameDescriptor gameDescriptor)
    {
        GameManager.eGameMode gameMode = GameManager.eGameMode.Regular;

        if(gameDescriptor.getGame().getVariant().equals(GameManager.eGameMode.Islands.toString()))
        {
            gameMode = GameManager.eGameMode.Islands;
        }

        return gameMode;
    }
}


//    public static void main(String[] args) {
//        InputStream inputStream = SchemaBasedJAXBMain.class.getResourceAsStream("/resources/world.xml");
//        try {
//            Countries countries = deserializeFrom(inputStream);
//            System.out.println("name of first country is: " + countries.getCountry().get(0).getName());
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static Countries deserializeFrom(InputStream in) throws JAXBException {
//        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
//        Unmarshaller u = jc.createUnmarshaller();
//        return (Countries) u.unmarshal(in);
//    }


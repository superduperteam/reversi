package GameEngine;

import Exceptions.*;
import jaxb.schema.generated.Board;
import jaxb.schema.generated.Players;
import jaxb.schema.generated.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    // TODO: Check path, check XML
    
    public GameManager readGameSettings(List<Player> playersList, Path xmlFilePath) throws BoardSizeDoesntMatchNumOfPlayers,
            ColumnsNotInRange, IslandsOnRegularMode, NoXMLFile, PlayersInitPositionsOutOfRange,PlayersInitPositionsOverrideEachOther,
            RowsNotInRange{
//        Scanner reader = new Scanner(System.in);
//        String filePathString;
//
//        filePathString = reader.nextLine();
//        Path filePath = Paths.get(filePathString);
        // check path

        // ## REMEMBER TO CHANGE .XML !!
        File file = new File(xmlFilePath.toString());
        GameManager gameManager;

        if(!Files.exists(Paths.get(xmlFilePath.toAbsolutePath().toString()))) {
            throw new Exceptions.NoXMLFile();
        }
        else if(!xmlFilePath.toString().toLowerCase().endsWith(".xml") )
        {
            throw new Exceptions.NoXMLFile();
        }
        else {
            InputStream inputSteam = GameSettingsReader.class.getResourceAsStream(xmlFilePath.toString());
            return extractGameSettings(inputSteam, playersList);
        }
    }

    private void areNumberOfRowsInRange(GameDescriptor gamedDescriptor) throws RowsNotInRange
    {
        byte xmlRows = gamedDescriptor.getGame().getBoard().getRows();

        if(xmlRows < MIN_ROWS || xmlRows > MAX_ROWS)
            throw new RowsNotInRange();
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
        LinkedHashMap<GameEngine.Player, List<Point>> initialDiscsPointsOfPlayers = new  LinkedHashMap<>();

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

    private GameManager extractGameSettings(InputStream xmlStream, List<GameEngine.Player> playersList) throws RowsNotInRange,
            ColumnsNotInRange, IslandsOnRegularMode, PlayersInitPositionsOverrideEachOther,
            BoardSizeDoesntMatchNumOfPlayers, PlayersInitPositionsOutOfRange
    {
        try{
            GameDescriptor gamedDescriptor = deserializeFrom(xmlStream);
            areNumberOfRowsInRange(gamedDescriptor);
            areNumberOfColsInRange(gamedDescriptor);
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

    private void doIntialPositionsOverrideEachOther(GameDescriptor gameDescriptor) throws PlayersInitPositionsOverrideEachOther
    {
        HashSet<Position> positionsSet = new HashSet<>();
        List<Participant> participantsList = gameDescriptor.getGame().getInitialPositions().getParticipant();
        List<Position> currPlayerInitialPositions;

        for(Participant participant : participantsList)
        {
            currPlayerInitialPositions = participant.getPosition();

            for(Position position : currPlayerInitialPositions)
            {
                if(!positionsSet.contains(position))
                {
                    positionsSet.add(position);
                }
                else
                {
                    throw new PlayersInitPositionsOverrideEachOther();
                }
            }
        }
    }

    private void areThereIslandsOnRegularMode(GameDescriptor gameDescriptor, List<GameEngine.Player> playersList) throws  IslandsOnRegularMode{
        GameEngine.Board board;

        board = createBoardFromGameDetails(gameDescriptor, playersList);
        for(int row = 0; row < board.getHeight(); ++row){
            for(int col = 0; col < board.getWidth(); ++col){
                if(board.get(row,col) != null){
                    if(!board.isThereDiscAdjacent(new Point(row,col))){
                        throw new IslandsOnRegularMode();
                    }
                }
            }
        }
    }

    private void areNumberOfColsInRange(GameDescriptor gamedDescriptor) throws ColumnsNotInRange
    {
        byte xmlCols = gamedDescriptor.getGame().getBoard().getColumns();

        if(xmlCols < MIN_COLS || xmlCols > MAX_COLS)
            throw new ColumnsNotInRange();
    }

    private void areIntialPositionsInRange(GameDescriptor gameDescriptor) throws PlayersInitPositionsOutOfRange
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
                    throw new PlayersInitPositionsOutOfRange();
                }
                if(position.getColumn() > maxColInBoard || position.getColumn() < 1)
                {
                    throw new PlayersInitPositionsOutOfRange();
                }
            }
        }
    }

    private void doesBoardSizeMatchNumOfPlayers(GameDescriptor gameDescriptor) throws BoardSizeDoesntMatchNumOfPlayers {
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
            throw new BoardSizeDoesntMatchNumOfPlayers();
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


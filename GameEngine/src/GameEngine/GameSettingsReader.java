package GameEngine;

import Exceptions.*;
import jaxb.schema.generated.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;
import java.io.InputStream;
import java.lang.String;


public class GameSettingsReader {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";
    private static final byte MAX_ROWS = 50; // rows in [4, 50]
    private static final byte MIN_ROWS = 4;
    private static final byte MAX_COLS = 30; // cols in [4, 30]
    private static final byte MIN_COLS = 4;
    private static final int MIN_NUMBER_OF_PLAYERS = 2;
    private static final int MAX_NUMBER_OF_PLAYERS = 4;
    private int numberOfPlayers;

    public GameManager readGameSettings(Path xmlFilePath) throws BoardSizeDoesntMatchNumOfPlayersException,
            ColumnsNotInRangeException, IslandsOnRegularModeException, NoXMLFileException, PlayersInitPositionsOutOfRangeException, PlayersInitPositionsOverrideEachOtherException,
            RowsNotInRangeException, PlayerHasNoInitialPositionsException, OutOfRangeNumberOfPlayersException, FileIsNotXML, TooManyInitialPositionsException,
            ThereAreAtLeastTwoPlayersWithSameID, InvalidNumberOfPlayersException {

        // ## REMEMBER TO CHANGE .XML !!
        File xmlFile = new File(xmlFilePath.toString());//xmlFilePath.toString());

        if(!xmlFilePath.toString().toLowerCase().endsWith(".xml"))
        {
            throw new FileIsNotXML();
        }
        else {
            try
            {
                InputStream inputStream = new FileInputStream(xmlFile);
                return extractGameSettings(inputStream);
            } catch (IOException e)
            {
                throw new NoXMLFileException();
            }
        }
    }

    private void checkNumberOfPlayers(GameDescriptor gameDescriptor) throws OutOfRangeNumberOfPlayersException
    {
        numberOfPlayers = gameDescriptor.getPlayers().getPlayer().size();

        if(numberOfPlayers < MIN_NUMBER_OF_PLAYERS || numberOfPlayers > MAX_NUMBER_OF_PLAYERS) {
            throw new OutOfRangeNumberOfPlayersException(MIN_NUMBER_OF_PLAYERS, MAX_NUMBER_OF_PLAYERS);
        }
    }

    private void areNumberOfRowsInRange(GameDescriptor gamedDescriptor) throws RowsNotInRangeException
    {
        byte xmlRows = gamedDescriptor.getGame().getBoard().getRows();

        if(xmlRows < MIN_ROWS || xmlRows > MAX_ROWS)
            throw new RowsNotInRangeException();
    }

    private GameManager getGameDetails(GameDescriptor gameDescriptor)
    {
        GameManager.eGameMode gameMode;
        GameEngine.Board board;
        String gameTitle;
        //List<GameEngine.Player> playersList = createPlayersListFromGameDetails(gameDescriptor);

        board = createBoardFromGameDetails(gameDescriptor);
        gameMode = getEGameMode(gameDescriptor);
        gameTitle = gameDescriptor.getDynamicPlayers().getGameTitle();

        return new GameManager(gameMode, board, numberOfPlayers, gameTitle);
    }

    public static byte getMAX_ROWS() {
        return MAX_ROWS;
    }

    public static byte getMIN_ROWS() {
        return MIN_ROWS;
    }

    public static byte getMAX_COLS() {
        return MAX_COLS;
    }

    public static byte getMIN_COLS() {
        return MIN_COLS;
    }

//    private List<GameEngine.Player> createPlayersListFromGameDetails(GameDescriptor gameDescriptor) {
//        eDiscType[] eDiscTypes = eDiscType.values();
//        List<GameEngine.Player> playersList = new ArrayList<>();
//        List<jaxb.schema.generated.Player> xmlPlayersList = gameDescriptor.getPlayers().getPlayer();
//
//        int discTypeIndex = 0;
//        for(jaxb.schema.generated.Player xmlPlayer : xmlPlayersList) {
//            playersList.add(new Player(xmlPlayer, eDiscTypes[discTypeIndex++]));
//        }
//
//        return playersList;
//    }

    private GameEngine.Board createBoardFromGameDetails(GameDescriptor gameDescriptor) {
        GameManager.eGameMode gameMode;
        int playerIndex = 0;
        List<Point> currInitialPointsList;
        List<Position> currPlayerIntialPositions;
        List<Participant> participantsList = gameDescriptor.getGame().getInitialPositions().getParticipant();
        HashMap<Participant, List<Point>> initialDiscsPointsOfPlayers = new  LinkedHashMap<>();

        for(Participant participant : participantsList)
        {
            currPlayerIntialPositions = participant.getPosition();
            currInitialPointsList = new ArrayList<>();

            for(Position position : currPlayerIntialPositions)
            {
                currInitialPointsList.add(new Point(position.getRow() - 1, position.getColumn() - 1));
            }

            initialDiscsPointsOfPlayers.put(participantsList.get(playerIndex), currInitialPointsList);
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

    public GameManager extractGameSettings(InputStream xmlStream) throws RowsNotInRangeException,
            ColumnsNotInRangeException, IslandsOnRegularModeException, PlayersInitPositionsOverrideEachOtherException,
            BoardSizeDoesntMatchNumOfPlayersException, PlayersInitPositionsOutOfRangeException, PlayerHasNoInitialPositionsException,
            OutOfRangeNumberOfPlayersException, TooManyInitialPositionsException, ThereAreAtLeastTwoPlayersWithSameID,
            InvalidNumberOfPlayersException
    {
        try{
            GameDescriptor gamedDescriptor = deserializeFrom(xmlStream);
            //arePlayersIDsUnique(gamedDescriptor);
            areNumberOfRowsInRange(gamedDescriptor);
            areNumberOfColsInRange(gamedDescriptor);
            //checkNumberOfPlayers(gamedDescriptor);
            areThereValidNumberOfPlayers(gamedDescriptor);
            doEachPlayerHasAtLeastOneInitialPoint(gamedDescriptor);
            doesBoardSizeMatchNumOfPlayers(gamedDescriptor);
            areIntialPositionsInRange(gamedDescriptor);
            doInitialPositionsOverrideEachOther(gamedDescriptor);
            areThereIslandsOnRegularMode(gamedDescriptor);
            return getGameDetails(gamedDescriptor);
        } catch (JAXBException e)
        {
            e.printStackTrace();
            return null;
        }
    }

//    private void arePlayersIDsUnique(GameDescriptor gamedDescriptor) throws ThereAreAtLeastTwoPlayersWithSameID{
//        List<jaxb.schema.generated.Player> xmlPlayersList = gamedDescriptor.getPlayers().getPlayer();
//        HashSet<BigInteger> idsSet = new HashSet<>();
//        BigInteger currID;
//
//        for(jaxb.schema.generated.Player xmlPlayer : xmlPlayersList){
//            currID = new BigInteger(xmlPlayer.getId().toString());
//
//            if(!idsSet.contains(currID)) {
//                idsSet.add(currID);
//            }
//            else{
//                throw new ThereAreAtLeastTwoPlayersWithSameID();
//            }
//        }
//    }

    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }

    private void doInitialPositionsOverrideEachOther(GameDescriptor gameDescriptor) throws PlayersInitPositionsOverrideEachOtherException {
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

    private void doEachPlayerHasAtLeastOneInitialPoint(GameDescriptor gameDescriptor) throws PlayerHasNoInitialPositionsException, TooManyInitialPositionsException
    {
        //List<jaxb.schema.generated.Player> playersList = gameDescriptor.getPlayers().getPlayer();
        List<Participant> participantsList = gameDescriptor.getGame().getInitialPositions().getParticipant();
        List<Position> currPlayerInitialPositions;

        //if(playersList.size() == participantsList.size())
        //{
            for(Participant participant : participantsList)
            {
                currPlayerInitialPositions = participant.getPosition();

                if(currPlayerInitialPositions.size() == 0)
                {
                    throw new PlayerHasNoInitialPositionsException();
                }
            }
        //}
//        else if(playersList.size() > participantsList.size()){ // it means there is a player that doesn't have initial positions
//            throw new PlayerHasNoInitialPositionsException();
//        }
//        else {
//            throw new TooManyInitialPositionsException();
//        }
    }

    private void areThereIslandsOnRegularMode(GameDescriptor gameDescriptor) throws IslandsOnRegularModeException {
        GameEngine.Board board;
        //List<GameEngine.Player> playersList= createPlayersListFromGameDetails(gameDescriptor);
        board = createBoardFromGameDetails(gameDescriptor);

        if(getEGameMode(gameDescriptor) == GameManager.eGameMode.Regular) {
            for (int row = 0; row < board.getHeight(); ++row) {
                for (int col = 0; col < board.getWidth(); ++col) {
                    if (board.getDisc(row, col) != null) {
                        if (!board.isThereDiscAdjacent(new Point(row, col))) {
                            throw new IslandsOnRegularModeException();
                        }
                    }
                }
            }
        }
    }

    public void areThereValidNumberOfPlayers (GameDescriptor gameDescriptor) throws InvalidNumberOfPlayersException {
        numberOfPlayers = gameDescriptor.getDynamicPlayers().getTotalPlayers();

        if(numberOfPlayers < 2 || numberOfPlayers > 4)
            throw new InvalidNumberOfPlayersException();
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

    private void doesBoardSizeMatchNumOfPlayers(GameDescriptor gameDescriptor) throws BoardSizeDoesntMatchNumOfPlayersException, OutOfRangeNumberOfPlayersException {
        int boardSize, numOfEmptyCells, numOfInitPositions = 0;


        //numOfPlayers = gameDescriptor.getPlayers().getPlayer().size();  THIS WILL BE IN EX 02
        boardSize = gameDescriptor.getGame().getBoard().getColumns() * gameDescriptor.getGame().getBoard().getRows();

        for(Participant participant : gameDescriptor.getGame().getInitialPositions().getParticipant()) {
            for(Position position : participant.getPosition()) {
                ++numOfInitPositions;
            }
        }

        numOfEmptyCells = boardSize - numOfInitPositions;
        if(numOfEmptyCells % numberOfPlayers != 0) {
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


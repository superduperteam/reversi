package GameEngine;

import jaxb.schema.generated.Board;
import jaxb.schema.generated.Players;
import jaxb.schema.generated.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.lang.String;


public class GameSettingsReader {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    // TODO: Check path, check XML
    
    public GameManager readGameSettings(List<Player> playersList, Path xmlFilePath) {
//        Scanner reader = new Scanner(System.in);
//        String filePathString;
//
//        filePathString = reader.nextLine();
//        Path filePath = Paths.get(filePathString);
        // check path

        // ## REMEMBER TO CHANGE .XML !!
        InputStream inputSteam = GameSettingsReader.class.getResourceAsStream("/resources/master.xml");
        return extractGameSettings(inputSteam, playersList);
    }

    private GameManager getGameDetails(GameDescriptor gamedDescriptor, List<GameEngine.Player> playersList)
    {
        int playerIndex = 0;
        GameManager.eGameMode gameMode;
        List<Point> currInitialPointsList;
        List<Position> currPlayerIntialPositions;
        LinkedHashMap<GameEngine.Player, List<Point>> initialDiscsPointsOfPlayers = new  LinkedHashMap<>();
        List<Participant> participantsList = gamedDescriptor.getGame().getInitialPositions().getParticipant();

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
    //        try {
//            Countries countries = deserializeFrom(inputStream);
//            System.out.println("name of first country is: " + countries.getCountry().get(0).getName());
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }

//    private GameManager getGameDetails(GameDescriptor gamedDescriptor)
//    {
//        int playerIndex = 0;
//
//        Players players = gamedDescriptor.getPlayers();
//        List<Player> playersDescList = players.getPlayer();
//        List<Participant> participantsList = gamedDescriptor.getGame().getInitialPositions().getParticipant();
//        List<GameEngine.Player> playersList = new ArrayList<>();
//        List<Point> currInitialPointsList;
//        List<Position> currPlayerIntialPositions;
//        eDiscType discTypes[] = eDiscType.values();
//        LinkedHashMap<GameEngine.Player, List<Point>> intialDiscsPointsOfPlayers = new  LinkedHashMap<>();
//        GameManager.eGameMode gameMode;
//
//
//        for(Participant participant : participantsList)
//        {
//            playersList.add(new GameEngine.Player(playerDesc, discTypes[playerIndex]));
//            currInitialPointsList = new ArrayList<>();
//
//            for(Position position : currPlayerIntialPositions)
//            {
//                currInitialPointsList.add(new Point(position.getRow(), position.getColumn()));
//            }
//
//        }
//
//
////        for(Player playerDesc : playersDescList)
////        {
////            playersList.add(new GameEngine.Player(playerDesc, discTypes[playerIndex]));
////
////            currPlayerIntialPositions = findIntialPositionsForPlayer(playerIndex, participantsList);
////            currInitialPointsList = new ArrayList<>();
////
////            for(Position position : currPlayerIntialPositions)
////            {
////                currInitialPointsList.add(new Point(position.getRow(), position.getColumn()));
////            }
////
////            intialDiscsPointsOfPlayers.put(playersList.get(playerIndex), currInitialPointsList);
////            playerIndex++;
//       }

//        for(Player playerDesc : playersDescList)
//        {
//            playersList.add(new GameEngine.Player(playerDesc, discTypes[playerIndex]));
//            //Participant participant = gamedDescriptor.getGame().getInitialPositions().getParticipant().get(playerIndex);
//            //currPlayerIntialPositions = participant.getPosition();
//
//            currIntialPointsList = new ArrayList<>();
//
//            for(Position position : currPlayerIntialPositions)
//            {
//                currIntialPointsList.add(new Point(position.getRow(),position.getColumn()));
//            }
//
//            intialDiscsPointsOfPlayers.put(playersList.get(playerIndex), currIntialPointsList);
//
//            playerIndex++;
//        }

        gameMode = getEGameMode(gamedDescriptor);
        GameEngine.Board board = new GameEngine.Board(gamedDescriptor.getGame().getBoard(), initialDiscsPointsOfPlayers, gameMode);
        GameManager gameManager = new GameManager(gameMode, playersList, board);

        return gameManager;
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

    private GameManager extractGameSettings(InputStream xmlStream, List<GameEngine.Player> playersList)
    {
        try{
            GameDescriptor gamedDescriptor = deserializeFrom(xmlStream);
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


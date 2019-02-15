package game.webLogic;

//import game.logic.GameManager;
//import game.webLogic.xml.generated.GameDescriptor;

import GameEngine.GameManager;
import GameEngine.Player;
import GameEngine.Point;

import java.util.*;

public class Room {

    private String roomName;
    private String uploaderName;
    private String variant;
    private int boardRows;
    private int boardCols;
    public int turnsPlayed = 0;
    private int totalPlayers;
    private int joinedPlayersNum = 0;
    private HashMap<Player, Boolean> playersUpdatedBoardMap;
    private HashMap<Player, Boolean> playersMovedToNextTurnMap;
    private boolean isActivePlayerMadeHisMove = false;
    private boolean isGameActive = false;
    private GameManager gameManager = null;

    public Room(String roomName, String uploaderName, String variant, int boardRows, int boardCols, int totalPlayers) {
        this.roomName = roomName;
        this.uploaderName = uploaderName;
        this.variant = variant;
        this.boardRows = boardRows;
        this.boardCols = boardCols;
        this.totalPlayers = totalPlayers;
    }

    public Room(GameManager gameManager, String roomName,  String uploaderName){
        this.roomName = roomName;
        this.uploaderName = uploaderName;
        this.variant = gameManager.getGameMode().toString();
        this.boardRows = gameManager.getBoard().getHeight();
        this.boardCols = gameManager.getBoard().getWidth();
        this.gameManager = gameManager;
        this.totalPlayers = gameManager.getTotalNumOfPlayers();
    }

    public String getRoomName() {
        return roomName;
    }

   // public boolean getIsGameActive() {
//        return isGameActive;
//    }

    public void setIsGameActive(boolean isGameActive) {
        this.isGameActive = isGameActive;

        if(isGameActive){
            List<Player> playerList = gameManager.getPlayersList();

            playersUpdatedBoardMap = new HashMap<>();
            playersMovedToNextTurnMap = new HashMap<>();

            for(Player player : playerList){
                playersUpdatedBoardMap.put(player, false);
                playersMovedToNextTurnMap.put(player, false);
            }
        }
    }

    public void setIsActivePlayerMadeHisMove(){
        isActivePlayerMadeHisMove = true;
    }

    public boolean isActivePlayerMadeHisMove(){ // new
        return isActivePlayerMadeHisMove;
    }

    public void clearIsActivePlayerMadeHisMove(){
        isActivePlayerMadeHisMove = false;
    }

    private void clearPlayersBooleanMap(HashMap<Player, Boolean> playerBooleanHashMap){
        List<Player> playerList = gameManager.getPlayersList();

        playerBooleanHashMap.clear();

        for (Player player : playerList) {
            playerBooleanHashMap.put(player, false);
        }
    }

    public void clearUpdatedBoardPlayers() {
        clearPlayersBooleanMap(playersUpdatedBoardMap);
    }


    public void clearPlayerMovedToNextTurn(){
        clearPlayersBooleanMap(playersMovedToNextTurnMap);
    }

    public void markPlayerAsMovedToNextTurn(String playerName){
        Player playerToUpdate = gameManager.getPlayerByName(playerName);
        markPlayerInHashMap(playersMovedToNextTurnMap ,playerToUpdate);

        System.out.println("## debug: #PlayersMovedToNextTurn value is - " + getCountOfPlayerWhoFinished(playersMovedToNextTurnMap));
        System.out.println("## debug: total players in PlayersMovedToNextTurn  - " + playersMovedToNextTurnMap.keySet().size());
    }

    public void markPlayerAsUpdatedBoard(String playerName){
        Player playerToUpdate = gameManager.getPlayerByName(playerName);
        if(playerToUpdate == null){
            System.out.println("hello");
        }
        markPlayerInHashMap(playersUpdatedBoardMap ,playerToUpdate);

        System.out.println("## debug: #PlayersUpdatedBoard value is - " + getCountOfPlayerWhoFinished(playersUpdatedBoardMap));
        System.out.println("## debug: total players in PlayersUpdatedBoard  - " + playersUpdatedBoardMap.keySet().size());
    }

    private void markPlayerInHashMap(HashMap<Player, Boolean> playerBooleanHashMap, Player playerToUpdate){
        if(playerBooleanHashMap.containsKey(playerToUpdate)){
            playerBooleanHashMap.put(playerToUpdate, true); // ##
        }
    }

    private int getCountOfPlayerWhoFinished(HashMap<Player, Boolean> playerBooleanHashMap){
        Set<Player> playersInGameSet = playerBooleanHashMap.keySet();
        int countOfPlayersWithTrueValue = 0;

        for(Player player : playersInGameSet){
            if(playerBooleanHashMap.containsKey(player)){
                if(playerBooleanHashMap.get(player)){
                    countOfPlayersWithTrueValue++;
                }
            }
        }

        return countOfPlayersWithTrueValue;
    }

    public boolean isTotalPlayersUpdatedBoard() { // new
        if(totalPlayers == getCountOfPlayerWhoFinished(playersUpdatedBoardMap)) {
            return true;
        }

        return false;
    }

    public boolean isTotalPlayersMovedToNextTurn() {
        if( getCountOfPlayerWhoFinished(playersMovedToNextTurnMap) == totalPlayers) { // maybe gamemanager.getPlayerList().size()?
            return true;
        }

        return false;
    }

    public void increaseJoinedPlayersNumByOne() {
        ++joinedPlayersNum;
    }

    public void decreaseJoinedPlayersNumByOne() {
        --joinedPlayersNum;
    }

    public boolean isTotalPlayersJoined() {
        if(totalPlayers == joinedPlayersNum) {
            return true;
        }

        return false;
    }

    public int getJoinedPlayersNum() {
        return joinedPlayersNum;
    }

    public int getTotalPlayersNum() {
        return totalPlayers;
    }

    public GameManager getGameManager() { return gameManager; }
}

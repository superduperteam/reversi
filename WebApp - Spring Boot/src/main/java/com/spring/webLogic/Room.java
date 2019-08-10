package com.spring.webLogic;

//import game.logic.GameManager;
//import game.webLogic.xml.generated.GameDescriptor;


import GameEngine.GameManager;
import GameEngine.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {
    private static AtomicInteger nextId = new AtomicInteger(0);
    @JsonProperty()
    private int id;
    @JsonProperty
    private String roomName;
    @JsonProperty
    private String uploaderName;
    private int turnsPlayed = 0;
    private int totalPlayersNum;
    private int joinedPlayersNum = 0;
    private int numberOfMovesMade = 0;
    private HashMap<Player, Integer> playersUpdatedBoardMap;
    private HashMap<Player, Integer> playersMovedToNextTurnMap;
    private boolean isActivePlayerMadeHisMove = false;
    private boolean isActivePlayerQuit = false;
    private boolean isGameActive = false;
    private GameManager gameManager;
    private ChatManager chatManager;

    public Room(GameManager gameManager, String roomName,  String uploaderName){
        this.roomName = roomName;
        this.uploaderName = uploaderName;
        this.gameManager = gameManager;
        this.totalPlayersNum = gameManager.getTotalNumOfPlayers();
        id = nextId.getAndUpdate(x-> x+1);
    }

    public int getId(){
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setIsGameActive(boolean isGameActive) {
        this.isGameActive = isGameActive;

        if(isGameActive){
            List<Player> playerList = gameManager.getPlayersList();
            playersUpdatedBoardMap = new HashMap<>();
            playersMovedToNextTurnMap = new HashMap<>();

            for(Player player : playerList){
                playersUpdatedBoardMap.put(player, 0);
                playersMovedToNextTurnMap.put(player, 0);
            }

            chatManager = new ChatManager();
        }
    }

    public boolean join(String playerName){
        boolean isSucceeded = (joinedPlayersNum<totalPlayersNum) && (playerName != null);

        if(isSucceeded){
            gameManager.addToPlayersList(new Player(playerName, false));
        }

        return isSucceeded;
    }

    @JsonIgnore
    public void markActivePlayerMadeHisMove(){
        numberOfMovesMade++;
    }

//    public boolean isActivePlayerMadeHisMove(){ // new
//        return isActivePlayerMadeHisMove;
//    }

//    public void clearIsActivePlayerMadeHisMove(){
//        isActivePlayerMadeHisMove = false;
//    }

//    private void clearPlayersBooleanMap(HashMap<Player, Boolean> playerBooleanHashMap){
//        List<Player> playerList = gameManager.getPlayersList();
//
//        playerBooleanHashMap.clear();
//
//        for (Player player : playerList) {
//            playerBooleanHashMap.put(player, false);
//        }
//    }
//
//    public void clearUpdatedBoardPlayers() {
//        clearPlayersBooleanMap(playersUpdatedBoardMap);
//    }
//
//
//    public void clearPlayerMovedToNextTurn(){
//        clearPlayersBooleanMap(playersMovedToNextTurnMap);
//    }

    public boolean isPlayerBoardSynced(String playerName){
        Player playerToCheck = gameManager.getPlayerByName(playerName);
        if(playerToCheck == null){
            System.out.println("hello");
        }

       if(playersUpdatedBoardMap.containsKey(playerToCheck)){
           if(playersUpdatedBoardMap.get(playerToCheck) + 1 == numberOfMovesMade){
               return true;
           }
           else return false;
       }{
           System.out.println("hello - player isn't here!!^%&%^&^&^&^");
           return true;
        }
    }

    public void markPlayerAsMovedToNextTurn(String playerName){
        Player playerToUpdate = gameManager.getPlayerByName(playerName);
        markPlayerInHashMap(playersMovedToNextTurnMap ,playerToUpdate);

//        System.out.println("## debug: #Players MOVED TO NEXT TURN is - " + getCountOfPlayerForCriteria(playersMovedToNextTurnMap, numberOfMovesMade) + "numberOfMovesMade = " + numberOfMovesMade);
//        System.out.println("## debug: total players in PlayersMovedToNextTurn map is - " + playersMovedToNextTurnMap.keySet().size());
        System.out.println("playersMovedToNextTurnMap CONTENT:");
        System.out.println(playersMovedToNextTurnMap);
        System.out.println("numberOfMovesMade = " + numberOfMovesMade);
        System.out.println();
    }

    public void markPlayerAsUpdatedBoard(String playerName){
        Player playerToUpdate = gameManager.getPlayerByName(playerName);
        if(playerToUpdate == null){
            System.out.println("hello");
        }
        markPlayerInHashMap(playersUpdatedBoardMap ,playerToUpdate);

//        System.out.println("## debug: #PlayersUpdatedBoard value is - " + getCountOfPlayerForCriteria(playersUpdatedBoardMap, numberOfMovesMade) + "numberOfMovesMade = " + numberOfMovesMade);
//        System.out.println("## debug: total players in PlayersUpdatedBoard  - " + playersUpdatedBoardMap.keySet().size());
        System.out.println("playersUpdatedBoardMap CONTENT:");
        System.out.println(playersUpdatedBoardMap);
        System.out.println("numberOfMovesMade = " + numberOfMovesMade);
        System.out.println();
    }

    private void markPlayerInHashMap(HashMap<Player, Integer> playerIntegerHashMap, Player playerToUpdate){
        if(playerIntegerHashMap.containsKey(playerToUpdate)){
            playerIntegerHashMap.put(playerToUpdate, playerIntegerHashMap.get(playerToUpdate)+1); // ##
        }
    }

    private int getCountOfPlayerForCriteria(HashMap<Player, Integer> playerIntegerHashMap, Integer criteria){
        Set<Player> playersInGameSet = playerIntegerHashMap.keySet();
        int countOfPlayersWithTrueValue = 0;

        for(Player player : playersInGameSet){
            if(playerIntegerHashMap.containsKey(player)){
                if(playerIntegerHashMap.get(player).equals(criteria)){
                    countOfPlayersWithTrueValue++;
                }
            }
        }

        return countOfPlayersWithTrueValue;
    }

    @JsonIgnore
    public boolean isTotalPlayersUpdatedBoard() { // new
        if(getCountOfPlayerForCriteria(playersUpdatedBoardMap, numberOfMovesMade) == joinedPlayersNum) {
            return true;
        }

        return false;
    }

    @JsonIgnore
    public boolean isTotalPlayersMovedToNextTurn() {
        if( getCountOfPlayerForCriteria(playersMovedToNextTurnMap, numberOfMovesMade) == joinedPlayersNum) { // maybe gamemanager.getPlayerList().size()?
            return true;
        }

        return false;
    }

    @JsonIgnore
    public boolean isActivePlayerMadeHisMove() {
        return maxBoardUpdateValue() + 1 == numberOfMovesMade;
    }

    private int maxBoardUpdateValue(){
        Set<Player> playerSet = playersUpdatedBoardMap.keySet();
        int maxVal = 0;

        for (Player player : playerSet){
            if(playersUpdatedBoardMap.get(player) > maxVal){
                maxVal = playersUpdatedBoardMap.get(player);
            }
        }

        return maxVal;
    }

    @JsonIgnore
    public boolean isEveryOneInSameTurn(){
        List<Player> playersList = new ArrayList<>(playersMovedToNextTurnMap.keySet());
        int turnsPlayedValuePrev = playersMovedToNextTurnMap.get(playersList.get(0));
        int turnsPlayedValue;

        for(int i = 1; i< playersList.size(); i++){
            turnsPlayedValue = playersMovedToNextTurnMap.get(playersList.get(i));

            if(turnsPlayedValue != turnsPlayedValuePrev){
                return false;
            }

            turnsPlayedValuePrev = turnsPlayedValue;
        }

        return true;
    }

    public void increaseJoinedPlayersNumByOne() {
        ++joinedPlayersNum;
    }

    public synchronized void decreaseJoinedPlayersNumByOne() {
        --joinedPlayersNum;
    }

    public boolean isTotalPlayersJoined() {
        if(totalPlayersNum == joinedPlayersNum) {
            return true;
        }

        return false;
    }

    public int getJoinedPlayersNum() {
        return joinedPlayersNum;
    }

    public int getTotalPlayersNum() {
        return totalPlayersNum;
    }

    public GameManager getGameManager() { return gameManager; }

    public synchronized boolean isTotalPlayerLeft()
    {
        return joinedPlayersNum == 0;
    }

    public synchronized void resetRoom() {
//        joinedPlayersNum = 0;
        isGameActive = false;
        gameManager.resetGame();
        playersUpdatedBoardMap.clear();;
        playersMovedToNextTurnMap.clear();
        turnsPlayed = 0;
        numberOfMovesMade = 0;
        isActivePlayerQuit = false;
    }

    public void clearJoinedPlayersNum(){
        isGameActive = false;
    }

    public boolean isActivePlayerQuit() {
        return isActivePlayerQuit;
    }

    public void markIsActivePlayerQuit() {
        ++numberOfMovesMade;
        isActivePlayerQuit = true;
    }

    public void clearMarkOfIsActivePlayerQuit() {
        isActivePlayerQuit = false;
    }

    public void removePlayerFromRecords(Player playerToRemove) {
        --joinedPlayersNum;

        if(playersUpdatedBoardMap.containsKey(playerToRemove)){
            playersUpdatedBoardMap.remove(playerToRemove);
        }
        if(playersMovedToNextTurnMap.containsKey(playerToRemove)){
            playersMovedToNextTurnMap.remove(playerToRemove);
        }
    }

//    public ChatManager getChatManager(ServletContext servletContext) {
//        return chatManager;
//    }
//
//    public int getIntParameter(HttpServletRequest request, String name) {
//        String value = request.getParameter(name);
//        if (value != null) {
//            try {
//                return Integer.parseInt(value);
//            } catch (NumberFormatException numberFormatException) {
//            }
//        }
//        return Constants.INT_PARAMETER_ERROR;
//    }

}

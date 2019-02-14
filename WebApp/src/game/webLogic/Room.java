package game.webLogic;

//import game.logic.GameManager;
//import game.webLogic.xml.generated.GameDescriptor;

import GameEngine.GameManager;
import GameEngine.Point;

import java.util.List;

public class Room {

    private String roomName;
    private String uploaderName;
    private String variant;
    private int boardRows;
    private int boardCols;
    private int totalPlayers;
    private int joinedPlayersNum = 0;
    private int updatedBoardPlayersNum = 0;
    private int numOfPlayerMovedToNextTurn = 0;
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
        this.totalPlayers = gameManager.getPlayersList().size();
        this.gameManager = gameManager;
        this.totalPlayers = gameManager.getTotalNumOfPlayers();
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean getIsGameActive() {
        return isGameActive;
    }

    public void setIsGameActive(boolean isGameActive) {
        this.isGameActive = isGameActive;
    }

    public void increaseJoinedPlayersNumByOne() {
        ++joinedPlayersNum;
    }

    public void setIsActivePlayerMadeHisMove(){
        isActivePlayerMadeHisMove = true;
    }

    public void clearIsActivePlayerMadeHisMove(){
        isActivePlayerMadeHisMove = false;
    }

    public void clearUpdatedBoardPlayersNum(){
        updatedBoardPlayersNum = 0;
    }

    public void increaseNumOfPlayerMovedToNextTurn(){
        ++numOfPlayerMovedToNextTurn;
    }

    public void clearNumOfPlayerMovedToNextTurn(){
        numOfPlayerMovedToNextTurn = 0;
    }

    public boolean isActivePlayerUpdatedBoard(){ // new
        return isActivePlayerMadeHisMove;
    }

    public boolean isTotalPlayersUpdatedBoard() { // new
        if(totalPlayers == updatedBoardPlayersNum) {
            return true;
        }

        return false;
    }

    public void increaseTotalPlayerUpdatedBoardByOne(){
        ++updatedBoardPlayersNum;
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

    public boolean isTotalPlayersMovedToNextTurn() {
        if(numOfPlayerMovedToNextTurn == totalPlayers) {
            return true;
        }

        return false;
    }
}

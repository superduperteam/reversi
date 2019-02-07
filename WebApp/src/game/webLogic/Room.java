package game.webLogic;

//import game.logic.GameManager;
//import game.webLogic.xml.generated.GameDescriptor;

import GameEngine.GameManager;

public class Room {

    private String roomName;
    private String uploaderName;
    private String variant;
    private int boardRows;
    private int boardCols;
    private int totalPlayers;
    private int joinedPlayersNum = 0;
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

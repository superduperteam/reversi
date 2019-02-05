package game.webLogic;

//import game.logic.GameManager;
//import game.webLogic.xml.generated.GameDescriptor;

public class Room {

    private String roomName;
    private String uploaderName;
    private String variant;
    private int boardRows;
    private int boardCols;
    private int target;
    private int totalPlayers;
    private int joinedPlayersNum = 0;
    //private GameManager gameManager = null;
    private boolean isGameActive = false;

    public Room(String roomName, String uploaderName, String variant, int boardRows, int boardCols, int target, int totalPlayers) {
        this.roomName = roomName;
        this.uploaderName = uploaderName;
        this.variant = variant;
        this.boardRows = boardRows;
        this.boardCols = boardCols;
        this.target = target;
        this.totalPlayers = totalPlayers;
    }

//    public Room(GameDescriptor gameDescriptor) {
//        roomName = gameDescriptor.getDynamicPlayers().getGameTitle();
//        variant = gameDescriptor.getGame().getVariant();
//        boardRows = gameDescriptor.getGame().getBoard().getRows();
//        boardCols = gameDescriptor.getGame().getBoard().getColumns();
//        target = gameDescriptor.getGame().getTarget();
//        totalPlayers = gameDescriptor.getDynamicPlayers().getTotalPlayers();
//    }

    public void createGameManager() {
//        if(gameManager == null) {
//            gameManager = new GameManager(boardRows, boardCols, variant, target);
//        }
    }

//    public GameManager getGameManager() {
//        return gameManager;
//    }

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
}

package game.json;

public class RoomJoinedPlayersJson {

    private boolean isTotalPlayersJoinedTheRoom;
    private int joinedPlayersNum;
    private int totalPlayersNum;

    public RoomJoinedPlayersJson(boolean isTotalPlayersJoinedTheRoom, int joinedPlayersNum, int totalPlayersNum) {
        this.isTotalPlayersJoinedTheRoom = isTotalPlayersJoinedTheRoom;
        this.joinedPlayersNum = joinedPlayersNum;
        this.totalPlayersNum = totalPlayersNum;
    }
}

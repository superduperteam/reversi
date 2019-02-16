package game.handlers;

import GameEngine.GameManager;
import game.webLogic.Room;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHandler {

    private final String PLAYER_NAME_ATT = "playerName";
    private final String IS_COMPUTER_ATT = "isComputer";
    private final String JOINED_ROOM_ATT = "joinedRoom";
    private final String LAST_UPLOADED_GAME_MANAGER_ATT = "uploadedGameManager";

    public void createSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.setAttribute(PLAYER_NAME_ATT, null);
        session.setAttribute(IS_COMPUTER_ATT, false);
        session.setAttribute(JOINED_ROOM_ATT, null);
    }

    public String getPlayerName(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return (String) session.getAttribute(PLAYER_NAME_ATT);
    }

    public GameManager getLastUploadedGameManager(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return (GameManager) session.getAttribute(LAST_UPLOADED_GAME_MANAGER_ATT);
    }

    public void setPlayerName(HttpServletRequest request, String playerName) {
        HttpSession session = request.getSession();

        session.setAttribute(PLAYER_NAME_ATT, playerName);
    }

    public boolean getIsPlayerComputer(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return (boolean) session.getAttribute(IS_COMPUTER_ATT);
    }

    public void setIsPlayerComputer(HttpServletRequest request, boolean isComputer) {
        HttpSession session = request.getSession();

        session.setAttribute(IS_COMPUTER_ATT, isComputer);
    }

    public Room getJoinedRoom(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return (Room) session.getAttribute(JOINED_ROOM_ATT);
    }

    public void setJoinedRoom(HttpServletRequest request, Room joinedRoom) {
        HttpSession session = request.getSession();

        session.setAttribute(JOINED_ROOM_ATT, joinedRoom);
    }

    public void setLastUploadedGameManager(HttpServletRequest request, GameManager gameManager) {
        HttpSession session = request.getSession();

        session.setAttribute(LAST_UPLOADED_GAME_MANAGER_ATT, gameManager);
    }
}

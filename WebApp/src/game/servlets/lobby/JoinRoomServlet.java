package game.servlets.lobby;

import GameEngine.GameManager;
import GameEngine.Player;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.webLogic.Room;
import game.webLogic.RoomsManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JoinRoomServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        String roomName = request.getParameter("roomName");
        RoomsManager roomsManager = servletContextHandler.getRoomsManager(getServletContext());
        Room joinedRoom = roomsManager.getRoom(roomName);
//        if(joinedRoom.getGameManager().isGameActive()){
//            joinedRoom.resetRoom();
//        }
        GameManager gameManager = joinedRoom.getGameManager();
        String playerName = sessionHandler.getPlayerName(request);
        boolean isPlayerComputer = sessionHandler.getIsPlayerComputer(request);

        sessionHandler.setJoinedRoom(request, joinedRoom);
        gameManager.addToPlayersList(new Player(playerName, !isPlayerComputer));
        joinedRoom.increaseJoinedPlayersNumByOne();


//        if(joinedRoom.isTotalPlayersJoined()) {
//            gameManager.activateGame();
//            joinedRoom.setIsGameActive(true);
//        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Servlet that joins player to the room";
    }
}

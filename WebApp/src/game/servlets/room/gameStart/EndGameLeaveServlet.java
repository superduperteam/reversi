package game.servlets.room.gameStart;

import GameEngine.GameManager;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.webLogic.Room;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EndGameLeaveServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        GameManager gameManager = joinedRoom.getGameManager();

        joinedRoom.decreaseJoinedPlayersNumByOne();
        sessionHandler.setJoinedRoom(request, null);

        gameManager.retirePlayerFromGame(gameManager.getActivePlayer());

//        if(gameManager.getAreAllPlayersEndedUpdateGameOver()) {
//            joinedRoom.setIsGameActive(false);
//            gameManager.initializeGameManager();
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
        return "Servlet that initialize the game for next time playing (end game of the room)";
    }
}

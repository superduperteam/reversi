package game.servlets.room.gameStart;

import GameEngine.GameManager;
import GameEngine.Player;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.webLogic.Room;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlayerQuitServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        GameManager gameManager = joinedRoom.getGameManager();

        Player quitter = gameManager.getPlayerByName(request.getParameter("myName"));
        quitter.quitGame(gameManager);
        //gameManager.retirePlayerFromGame(quitter);
        joinedRoom.removePlayerFromRecords(quitter);
        joinedRoom.markIsActivePlayerQuit(); // saar: Without this, other player will not know that the user quit.
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
        return "Servlet that responsible for player quitting from the game";
    }
}

package game.servlets.lobby;

import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.webLogic.OnlinePlayersManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        String playerName = sessionHandler.getPlayerName(request);
        OnlinePlayersManager onlinePlayersManager = servletContextHandler.getPlayersManager(getServletContext());

        onlinePlayersManager.removePlayer(playerName);
        sessionHandler.setPlayerName(request, null);
        sessionHandler.setIsPlayerComputer(request, false);
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
        return "Servlet that removes a player from the system (logout)";
    }
}

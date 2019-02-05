package game.servlets.lobby;

import game.handlers.ServletContextHandler;
import game.webLogic.OnlinePlayersManager;
import game.handlers.JsonManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OnlinePlayersServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        OnlinePlayersManager onlinePlayersManager = servletContextHandler.getPlayersManager(getServletContext());
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());

        jsonManager.sendJsonOut(response, onlinePlayersManager);
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
        return "Servlet that gets all online players in the system";
    }
}

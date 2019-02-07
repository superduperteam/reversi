package game.servlets.signup;

import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.json.MessageJson;
import game.handlers.JsonManager;
import game.webLogic.OnlinePlayersManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignupServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        OnlinePlayersManager onlinePlayersManager = servletContextHandler.getPlayersManager(getServletContext());
        String playerName = request.getParameter("playerName");
        String isComputer = request.getParameter("isComputer");

        if(playerName == null) {
            if(sessionHandler.getPlyerName(request) != null) {
                response.sendRedirect("pages/lobby.html");
            }
            else {
                response.sendRedirect("pages/signup.html");
            }
        }
        else {
            playerName = playerName.trim();
            MessageJson messageJson;
            JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());

            if (!playerName.isEmpty())
            {
                if (!onlinePlayersManager.isPlayerExists(playerName)) {
                    onlinePlayersManager.addPlayer(playerName);
                    sessionHandler.setPlayerName(request, playerName);
                    sessionHandler.setIsPlayerComputer(request, isComputer.equals("true"));

                    messageJson = new MessageJson(true, playerName + " has signed up successfully");
                }
                else {
                    messageJson = new MessageJson(false, playerName + " is already exists");
                }
            }
            else
            {
                messageJson = new MessageJson(false, playerName + "player name is empty");
            }

            jsonManager.sendJsonOut(response, messageJson);
        }
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
        return "Servlet that displays the signup.html file and signs up players";
    }
}
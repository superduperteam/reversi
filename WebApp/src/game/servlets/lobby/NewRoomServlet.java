package game.servlets.lobby;

import GameEngine.GameManager;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.json.MessageJson;
import game.handlers.JsonManager;
import game.webLogic.Room;
import game.webLogic.RoomsManager;

import javax.servlet.http.*;
import java.io.IOException;

public class NewRoomServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        RoomsManager roomsManager = servletContextHandler.getRoomsManager(getServletContext());
        String roomName = request.getParameter("roomName");
        String uploaderName = sessionHandler.getPlayerName(request);
        String variant = request.getParameter("variant");
        int boardRows = Integer.parseInt(request.getParameter("boardRows"));
        int boardCols = Integer.parseInt(request.getParameter("boardCols"));
        int totalPlayers = Integer.parseInt(request.getParameter("totalPlayers"));

        GameManager gameManager = sessionHandler.getLastUploadedGameManager(request);
        Room room = new Room(gameManager, gameManager.getGameTitle(), sessionHandler.getPlayerName(request));
        roomsManager.addRoom(room);

        MessageJson messageJson = new MessageJson(true, "The room " + roomName + " was created successfully");
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
        jsonManager.sendJsonOut(response, messageJson);
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
        return "Servlet that creates and adds a new room to the list of available rooms";
    }
}

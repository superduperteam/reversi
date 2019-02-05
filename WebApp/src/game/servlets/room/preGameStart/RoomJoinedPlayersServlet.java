package game.servlets.room.preGameStart;

import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.json.RoomJoinedPlayersJson;
import game.handlers.JsonManager;
import game.webLogic.Room;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoomJoinedPlayersServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());

        boolean isTotalPlayersJoinedTheRoom = joinedRoom.isTotalPlayersJoined();
        int joinedPlayers = joinedRoom.getJoinedPlayersNum();
        int totalPlayers = joinedRoom.getTotalPlayersNum();

        RoomJoinedPlayersJson roomJoinedPlayersJson = new RoomJoinedPlayersJson(isTotalPlayersJoinedTheRoom, joinedPlayers, totalPlayers);
        jsonManager.sendJsonOut(response, roomJoinedPlayersJson);
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
        return "Servlet that checks if the total number of players of the room, joined the room, to start the game in the room";
    }
}

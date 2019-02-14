package game.servlets.room.gameStart;

import GameEngine.GameManager;
import GameEngine.Player;
import game.handlers.JsonManager;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.webLogic.Room;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardUpdaterServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setContentType("application/json");
//
//        ServletContextHandler servletContextHandler = new ServletContextHandler();
//        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
//        Room joinedRoom = sessionHandler.getJoinedRoom(request);
//        GameManager gameManager = joinedRoom.getGameManager();
//        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
//        Player currentPlayer = gameManager.getActivePlayer();
//        //LastMoveChangesJson lastMoveChangesJson = new LastMoveChangesJson(gameManager.getLastChangedCells(), currentPlayer.getIsPlayerQuit());
//
//        jsonManager.sendJsonOut(response, gameManager.getBoard());



        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        GameManager gameManager = joinedRoom.getGameManager();
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
        Player senderPlayer = joinedRoom.getGameManager().getPlayerByName(sessionHandler.getPlayerName(request)); // new line
        System.out.println("## debug: this player wants to get an updated board - " + senderPlayer.getName());

       // boolean isActivePlayerPlayedHisTurn = Boolean.parseBoolean(request.getParameter("activePlayer"));


//        if(isActivePlayerPlayedHisTurn){ // moved it to ExecuteMoveServlet
//            joinedRoom.setIsActivePlayerMadeHisMove();
//        }

        // note: active player will always get board upon request, passive players will get board only if active player already played his turn.
        if(joinedRoom.isActivePlayerUpdatedBoard()){
            joinedRoom.increaseTotalPlayerUpdatedBoardByOne();

            System.out.println("## debug: this player got updated board - " + senderPlayer.getName());

            jsonManager.sendJsonOut(response, gameManager); // passive players can get board now.
        }
        else{
            jsonManager.sendJsonOut(response, false); // not yet.
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
        return "Servlet that gets last move changes (discs changes and if player quit the game)";
    }
}

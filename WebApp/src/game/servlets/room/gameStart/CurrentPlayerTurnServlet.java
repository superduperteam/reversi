package game.servlets.room.gameStart;

import GameEngine.GameManager;
import game.handlers.JsonManager;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.webLogic.Room;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CurrentPlayerTurnServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        GameManager gameManager = joinedRoom.getGameManager();
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
        String senderName = request.getParameter("myName");

//        synchronized (gameManager.mtx){
//            joinedRoom.markPlayerAsMovedToNextTurn(senderName);
//            if(joinedRoom.isTotalPlayersMovedToNextTurn()){
//                joinedRoom.turnsPlayed++;
//                System.out.printf("@@ turns played = " + joinedRoom.turnsPlayed);
//                joinedRoom.clearUpdatedBoardPlayers();
//                joinedRoom.clearIsActivePlayerMadeHisMove();
//                joinedRoom.clearPlayerMovedToNextTurn();
//            }

         //   jsonManager.sendJsonOut(response, gameManager.getActivePlayer());
  //      }
        if (joinedRoom.isEveryOneInSameTurn()) { // if active player didn't make his move - go on and execute the new move.
            jsonManager.sendJsonOut(response, gameManager);
        }
        else{
            jsonManager.sendJsonOut(response, false);
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
        return "Servlet that gets current player";
    }
}

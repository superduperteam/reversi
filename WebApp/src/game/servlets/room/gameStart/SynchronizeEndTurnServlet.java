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

public class SynchronizeEndTurnServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        boolean responseAnswer;

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
        String senderName = request.getParameter("myName");
        System.out.println("## debug: want to go to next turn - " + senderName);


        responseAnswer = joinedRoom.isTotalPlayersUpdatedBoard();
        if(responseAnswer){
            Player senderPlayer = joinedRoom.getGameManager().getPlayerByName(sessionHandler.getPlayerName(request)); // new line
            System.out.println("## debug: all players updated board - " + senderName);

            joinedRoom.increaseNumOfPlayerMovedToNextTurn();
            if(joinedRoom.isTotalPlayersMovedToNextTurn()){
                joinedRoom.clearUpdatedBoardPlayersNum();
                joinedRoom.clearIsActivePlayerMadeHisMove();
                joinedRoom.clearNumOfPlayerMovedToNextTurn();
            }
        }

        jsonManager.sendJsonOut(response, responseAnswer);
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
        return "Servlet that synchronizes all players to be after a complete turn of the game";
    }
}

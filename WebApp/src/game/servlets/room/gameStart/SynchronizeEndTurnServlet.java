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

        if(joinedRoom != null){
//            joinedRoom.markPlayerAsUpdatedBoard(senderName); // if user asked this. this means he got board.

            System.out.println("## debug: this player got updated board - " + senderName);

            responseAnswer = joinedRoom.isTotalPlayersUpdatedBoard();

            if(responseAnswer){
                // Saar: I think this line might create bugs(it's probably get the name from cookie)
//           Player senderPlayer = joinedRoom.getGameManager().getPlayerByName(sessionHandler.getPlayerName(request));
                System.out.println("## debug: all players updated board - " + senderName);

                joinedRoom.markPlayerAsMovedToNextTurn(senderName);
                if(joinedRoom.isTotalPlayersMovedToNextTurn()){
//                    System.out.println("@@ turns played = " + joinedRoom.turnsPlayed++);
//                    joinedRoom.clearUpdatedBoardPlayers();
//                    joinedRoom.clearIsActivePlayerMadeHisMove();
//                    joinedRoom.clearPlayerMovedToNextTurn();
                    GameManager gameManager = joinedRoom.getGameManager();

                    if(joinedRoom.isActivePlayerQuit())
                    {
                        joinedRoom.clearMarkOfIsActivePlayerQuit(); // if he retired, the turn already changed..
                    }
                    else{
                        gameManager.changeTurn();
                        gameManager.updateGameScore();
                        gameManager.calcFlipPotential();
                    }


                }
            }

            jsonManager.sendJsonOut(response, responseAnswer);
        }
        else System.out.println("@@ debug warning, GO TO SynchronizeEndTurnServlet, joinedRoom was NULL");
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

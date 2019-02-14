package game.servlets.room.gameStart;

import GameEngine.GameManager;
import GameEngine.GameManager.eMoveStatus;
import GameEngine.Player;
import GameEngine.Point;
import GameEngine.eDiscType;
import game.handlers.JsonManager;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.json.MessageJson;
import game.webLogic.Room;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExecuteMoveServlet extends HttpServlet {

    //private static final Object Player = ;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        GameManager gameManager = joinedRoom.getGameManager();
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());

        if(gameManager.getActivePlayer().isHuman()) {
            int destinationCol = Integer.parseInt(request.getParameter("destinationCol"));
            int destinationRow = Integer.parseInt(request.getParameter("destinationRow"));
            //boolean isPopoutMove = Boolean.parseBoolean(request.getParameter("isPopoutMove"));
            //eDiscType discType = eDiscType.valueOf(request.getParameter("discType"));

            // Saar: I think this method is just for updating, we need a method that will check the move and update the player.
            //gameManager.getBoard().updateBoard(new GameEngine.Point(destinationRow, destinationCol),discType);

            Player senderPlayer = gameManager.getPlayerByName(sessionHandler.getPlayerName(request)); // new line

            if(senderPlayer.getName().equals(gameManager.getActivePlayer().getName())){ // is this his turn?
                eMoveStatus moveStatus = senderPlayer.makeMove(new Point(destinationRow, destinationCol), gameManager.getBoard()); // is move legal?
                if(moveStatus == eMoveStatus.OK){
                    gameManager.changeTurn();


                    joinedRoom.setIsActivePlayerMadeHisMove(); // new here


                    jsonManager.sendJsonOut(response, true);
                }
                else{

                    jsonManager.sendJsonOut(response, moveStatus.toString());
                }
            }
        }
        else {
            Thread.sleep(1000);
            //gameManager.getBoard()(-1); computer move goes here.
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            processRequest(req, resp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            processRequest(req, resp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet that executes the current player move";
    }
}

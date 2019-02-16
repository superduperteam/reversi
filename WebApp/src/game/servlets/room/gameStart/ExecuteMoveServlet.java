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

        synchronized (gameManager.mtx) { // Saar: I think this is necessary. Every request the server gets, tomcat creates a new thread, but they all work on the same
            // object - gameManager. I can't synchronized on gameManager object because it's a local reference, so I created a mtx object so every
            // thread will have this same reference.
            String senderName = request.getParameter("myName");
            Player player = gameManager.getPlayerByName(senderName);

            if(!player.isHuman()){
                Thread.sleep(1000);
            }

            if (joinedRoom.isEveryOneInSameTurn() && !joinedRoom.isActivePlayerMadeHisMove())
            { // if active player didn't make his move - go on and execute the new move.
                if (player.getName().equals(gameManager.getActivePlayer().getName())) { // is this his turn?

                    if (gameManager.getActivePlayer().isHuman()) {
                        int destinationCol = Integer.parseInt(request.getParameter("destinationCol"));
                        int destinationRow = Integer.parseInt(request.getParameter("destinationRow"));
                        //boolean isPopoutMove = Boolean.parseBoolean(request.getParameter("isPopoutMove"));
                        //eDiscType discType = eDiscType.valueOf(request.getParameter("discType"));

                        // Saar: This method is just for updating, we need a method that will check the move and update the player.
                        //gameManager.getBoard().updateBoard(new GameEngine.Point(destinationRow, destinationCol),discType);

                        //Player senderPlayer = gameManager.getPlayerByName(sessionHandler.getPlayerName(request)); // problematic


                        eMoveStatus moveStatus = player.makeMove(new Point(destinationRow, destinationCol), gameManager.getBoard()); // is move legal?
                        if (moveStatus == eMoveStatus.OK) {
                           // gameManager.changeTurn();
                            gameManager.updateGameScore();
                            gameManager.calcFlipPotential();

                            joinedRoom.markActivePlayerMadeHisMove();
                            System.out.println("%% " + senderName + " made move: " + new Point(destinationRow, destinationCol));

                            jsonManager.sendJsonOut(response, true);
                        }
                        else
                            {
                            jsonManager.sendJsonOut(response, moveStatus.toString());
                            }
                    }
                    else {

                        eMoveStatus moveStatus = player.makeMove(player.getRandomMove(gameManager.getBoard()), gameManager.getBoard());

                        //if(moveStatus == eMoveStatus.OK){ // a random move should always be valid, but just to be on the safer side..
                        // gameManager.changeTurn();
                        gameManager.updateGameScore();
                        gameManager.calcFlipPotential();

                        joinedRoom.markActivePlayerMadeHisMove();
                        System.out.println("** (AI) " + senderName + " made move: ");
                        jsonManager.sendJsonOut(response, true);
                        // }
                        //  else System.out.println("** error made a move");
                    }
                }
                else{
                    jsonManager.sendJsonOut(response, false);
                    }
                }
            else
                { // else - a move was already made by an active player - this can only mean that: not all of the users got a positive response to the request of synchronizeEndTurn.
                // This case means - There's someone trailing behind. Try again later. We return nothing.
                // NOTE: we cannot return 'false' as an answer because the user will not click the cell again.
                    jsonManager.sendJsonOut(response, "again");
            }
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

package game.servlets.room.gameStart;

import GameEngine.GameManager;
import GameEngine.eDiscType;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
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

        if(gameManager.getActivePlayer().isHuman()) {
            int destinationCol = Integer.parseInt(request.getParameter("destinationCol"));
            int destinationRow = Integer.parseInt(request.getParameter("destinationRow"));
            //boolean isPopoutMove = Boolean.parseBoolean(request.getParameter("isPopoutMove"));
            eDiscType discType = eDiscType.valueOf(request.getParameter("discType"));
            gameManager.getBoard().updateBoard(new GameEngine.Point(destinationRow, destinationCol),discType);
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

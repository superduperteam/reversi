package game.servlets.room.gameStart;

import GameEngine.GameManager;
import GameEngine.Player;
import game.handlers.JsonManager;
import game.handlers.ServletContextHandler;
import game.handlers.SessionHandler;
import game.json.GameOverJson;
import game.webLogic.Room;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameOverServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        SessionHandler sessionHandler = servletContextHandler.getSessionHandler(getServletContext());
        Room joinedRoom = sessionHandler.getJoinedRoom(request);
        GameManager gameManager = joinedRoom.getGameManager();
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
        List<String> winners = new ArrayList<>();
        for(Player winner : gameManager.getHighestScoringPlayers())
            winners.add(winner.getName());
        GameOverJson gameOverJson = new GameOverJson(gameManager.isGameOver() || joinedRoom.getJoinedPlayersNum() == 1
                ,winners.size() > 1 ,winners);
//        System.out.print("joined players ");
//        System.out.println(joinedRoom.getJoinedPlayersNum());

        if(gameManager.isGameOver()) {
            Thread.sleep(500);
        }

        jsonManager.sendJsonOut(response, gameOverJson);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            processRequest(req, resp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet that checks if game is over";
    }
}

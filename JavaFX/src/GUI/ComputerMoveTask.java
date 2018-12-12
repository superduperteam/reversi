package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ComputerMoveTask extends Task<Boolean> {

    private GameManager gameManager;
    private AppController appController;
    public boolean isDone;

    public ComputerMoveTask(GameManager gameManager, AppController appController){
        this.gameManager = gameManager;
        this.appController = appController;
    }

//    @Override
//    protected Boolean call() throws Exception {
//        Player activePlayer = gameManager.getActivePlayer();
//        if(!activePlayer.isHuman()){
//            activePlayer.makeMove(gameManager.getActivePlayer().getRandomMove(gameManager.getBoard()), gameManager.getBoard());
//            Thread.sleep(500);
//            gameManager.changeTurn();
//            Platform.runLater(new Informer(gameManager, appController));
//        }
//        return true;
//    }

        @Override
        protected Boolean call() throws Exception {
        gameManager.getActivePlayer().makeMove(gameManager.getActivePlayer().getRandomMove(gameManager.getBoard()), gameManager.getBoard());
        Thread.sleep(500);
        gameManager.changeTurn();
        Platform.runLater(new Informer(gameManager, appController));

        return true;
    }
}


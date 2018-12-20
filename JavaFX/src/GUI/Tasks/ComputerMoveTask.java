package GUI.Tasks;

import GUI.Controllers.AppController;
import GameEngine.GameManager;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ComputerMoveTask extends Task<Boolean> {

    private GameManager gameManager;
    private AppController appController;
    private static final int SLEEP_TIME = 1200;

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
//            Platform.runLater(new UpdateComputerMoveTask(gameManager, appController));
//        }
//        return true;
//    }

        @Override
        protected Boolean call() throws Exception {
        if(appController.isGameInProgressProperty().get() && gameManager.isGameActiveProperty().get() && !gameManager.isGameOver()){
            appController.setIsComputerMoveInProgress(true);
            gameManager.getActivePlayer().makeMove(gameManager.getActivePlayer().getRandomMove(gameManager.getBoard()), gameManager.getBoard());
            Thread.sleep(SLEEP_TIME);
            //gameManager.changeTurn();
            Platform.runLater(new UpdateComputerMoveTask(gameManager, appController));
        }

        return true;
    }
}


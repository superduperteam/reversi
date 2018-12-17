package GUI;

import GameEngine.GameManager;
import javafx.application.Platform;

public class Informer implements Runnable {
    private AppController appController;
    private GameManager gameManager;

    public Informer(GameManager gameManager, AppController appController){
        this.appController = appController;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if(appController.isGameInProgressProperty().get()){
            appController.setIsComputerMoveInProgress(false);
            gameManager.changeTurn();
            appController.updateGUI();
            if (!gameManager.getActivePlayer().isHuman() && gameManager.isGameActiveProperty().get()&& !gameManager.isGameOver()) {
                Thread thread = new Thread(new ComputerMoveTask(gameManager, appController));
                thread.start();
            }
            else if(gameManager.isGameOver()){
                appController.onGameOver();
            }
        }
    }
}

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
        appController.updateGUI();
        if (!gameManager.getActivePlayer().isHuman()) {
            Thread thread = new Thread(new ComputerMoveTask(gameManager, appController));
            thread.start();
        }
    }
}

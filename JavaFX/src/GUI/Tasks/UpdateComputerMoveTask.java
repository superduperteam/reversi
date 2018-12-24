package GUI.Tasks;

import GUI.Controllers.AppController;
import GUI.Tasks.ComputerMoveTask;
import GameEngine.GameManager;

public class UpdateComputerMoveTask implements Runnable {
    private AppController appController;
    private GameManager gameManager;

    public UpdateComputerMoveTask(GameManager gameManager, AppController appController){
        this.appController = appController;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        synchronized (gameManager) {
            if (appController.isShowBoardProperty().get()) {
                appController.setIsComputerMoveInProgress(false);
                gameManager.changeTurn();
                appController.updateGUI();
                if (!gameManager.getActivePlayer().isHuman() && gameManager.isGameActiveProperty().get() && !gameManager.isGameOver()) {
                    Thread thread = new Thread(new ComputerMoveTask(gameManager, appController));
                    thread.start();
                } else if (gameManager.isGameOver()) {
                    appController.onGameOver();
                }
            }
        }
    }
}

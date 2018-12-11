package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import GameEngine.Point;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import javax.swing.plaf.PopupMenuUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class AppController {

    private GameManager gameManager;

    private BoardController boardController;
    private boolean isTutorialMode = false;
    @FXML private StatsController statsComponentController;
    @FXML private CheckBox tutorialMode;
    @FXML private Button undoLastMoveButton;

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        lateInitialize();
    }

    @FXML
    public void initialize() {
        if (statsComponentController != null) {
            statsComponentController.setMainController(this);
        }

        tutorialMode.setOnMouseClicked((event) -> {
            if(tutorialMode.isSelected()){
                isTutorialMode = true;
            }
            else{
                isTutorialMode = false;
            }

            boardController.updateGIUDiscs(isTutorialMode);
        });
    }

    // call this after gameManager is given.
    private void lateInitialize(){
        undoLastMoveButton.setOnMouseClicked(event -> { undoLastMove(); });
        undoLastMoveButton.disableProperty().bind(Bindings.not(gameManager.canUndoProperty()));
    }

    private void updateGUI(){
        boardController.updateGIUDiscs(isTutorialMode);
        statsComponentController.refreshTable();
    }

    private void undoLastMove() {
        gameManager.undo();
        updateGUI();

        if(!gameManager.getActivePlayer().isHuman()){
            simulateComputerTurns();
        }
    }

    public void initTable() {
        statsComponentController.setPlayers(gameManager.getPlayersList());
    }

    public void playTurn(Point clickedCellBoardPoint) {
        Player activePlayer = gameManager.getActivePlayer();
        GameManager.eMoveStatus moveStatus;

        moveStatus = activePlayer.makeMove(clickedCellBoardPoint, gameManager.getBoard());

        if (moveStatus == GameManager.eMoveStatus.OK) {
            updateEndTurn();
        }

        if(!gameManager.getActivePlayer().isHuman()){
            simulateComputerTurns();
        }

        if (gameManager.isGameOver()) {
            onGameOver();
        }

//                Task<Boolean> AITurnThread = new Task<Boolean>() {
//                    @Override
//                    protected Boolean call() {
//                        try { Thread.sleep(1000); } catch (InterruptedException ex) {}
//                        gameManager.getActivePlayer().makeMove(gameManager.getActivePlayer().getRandomMove(gameManager.getBoard()), gameManager.getBoard());
//                        Platform.runLater(() ->  updateEndTurn());
//                        return true;
//                    }
//                };
//                AITurnThread.getOnSucceeded();
//            AITurnThread.run();
//            }

//                Thread thread = new Thread(() -> {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                    }
//                    Platform.runLater(() -> {
//                        System.out.println("start");
//                        gameManager.getActivePlayer().makeMove(gameManager.getActivePlayer().getRandomMove(gameManager.getBoard()), gameManager.getBoard());
//                        updateEndTurn();
//                    });
//                });
//
//
//                //            while (!gameManager.getActivePlayer().isHuman()) {}
//                thread.start();

    }

    private void simulateComputerTurns() {
        while (!gameManager.getActivePlayer().isHuman() && !gameManager.isGameOver()) {
            gameManager.getActivePlayer().makeMove(gameManager.getActivePlayer().getRandomMove(gameManager.getBoard()),
                    gameManager.getBoard());
            updateEndTurn();
        }
    }

//    public void playTurnsForComputers(){
//
//    }

    public void updateEndTurn(){
        gameManager.changeTurn();
        updateGUI();
    }

    public void onGameOver(){
        StringBuilder winMessageBuilder = new StringBuilder();

        undoLastMoveButton.setDisable(true);

        winMessageBuilder.append("Game Over.\n");

        if(gameManager.getHighestScoringPlayers().size() == 1) {
            winMessageBuilder.append(gameManager.getHighestScoringPlayers().get(0).getName())
                    .append(" is the winner!");
        } else {
            winMessageBuilder.append("It's a tie!");
        }
        PopupFactory.showPopup(winMessageBuilder.toString());
    }
}

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import javax.swing.plaf.PopupMenuUI;
import java.util.Optional;
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
    @FXML private Label taskMessageLabel;
    @FXML private Button loadFileButton;
    @FXML private Button startGameButton;

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

        loadFileButton.setOnMouseClicked((event) -> {
            LoadFileTask loadFileTask = new LoadFileTask();
            bindTaskToUIComponents(loadFileTask, ()->{});
            new Thread(loadFileTask).start();
        });

        startGameButton.setOnMouseClicked((event)-> gameManager.activateGame());
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        taskMessageLabel.textProperty().bind(aTask.messageProperty());

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        taskMessageLabel.textProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

    // call this after gameManager is given.
    private void lateInitialize(){
        undoLastMoveButton.setOnMouseClicked(event -> { undoLastMove(); });
        undoLastMoveButton.disableProperty().bind(Bindings.not(gameManager.canUndoProperty()));
    }

    public void updateGUI(){
        boolean showHintsForPlayer = isTutorialMode && gameManager.getActivePlayer().isHuman();

        boardController.updateGIUDiscs(showHintsForPlayer);
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

        if(gameManager.getActivePlayer().isHuman()){
            moveStatus = activePlayer.makeMove(clickedCellBoardPoint, gameManager.getBoard());

            if (moveStatus == GameManager.eMoveStatus.OK) {
                updateEndTurn();
            }

            if(!gameManager.getActivePlayer().isHuman()){
                simulateComputerTurns();
            }
        }

        if (gameManager.isGameOver()) {
            onGameOver();
        }
    }

    private void simulateComputerTurns() {
            Thread thread = new Thread(new ComputerMoveTask(gameManager, this));
            thread.start();
    }

    public void updateEndTurn(){
        gameManager.changeTurn();
        updateGUI();
    }

    public void onGameOver(){
        System.out.println("Test");
        StringBuilder winMessageBuilder = new StringBuilder();

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

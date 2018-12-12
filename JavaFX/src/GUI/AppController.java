package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import GameEngine.GameManager.TurnHistory.Turn;
import GameEngine.Point;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.plaf.PopupMenuUI;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class AppController {

    private GameManager gameManager;

    private BoardGUI boardGUI;
    private Stage primaryStage;
    private boolean isInReplayMode;
    private ListIterator<GameManager.TurnHistory.Turn> replayTurnIterator;
    private BoardController boardController;
    private boolean isTutorialMode = false;
    private BorderPane boardParent;
    @FXML private StatsController statsComponentController;
    @FXML private CheckBox tutorialMode;
    @FXML private Button undoLastMoveButton;
    @FXML private Button replayModeButton;
    @FXML private Button replayModePrevButton;
    @FXML private Button replayModeNextButton;
    @FXML private Label taskMessageLabel;
    @FXML private Button loadFileButton;
    @FXML private Button startGameButton;

    private SimpleBooleanProperty didLoadXmlFile;
    private SimpleBooleanProperty didStartGame;

//    public void setBoardController(BoardController boardController) {
//        this.boardController = boardController;
//    }
//
//    public GameManager getGameManager() {
//        return gameManager;
//    }
//
//    public void setGameManager(GameManager gameManager) {
//        this.gameManager = gameManager;
//        lateInitialize();
//    }
//
//    @FXML
//    public void initialize() {
//        if (statsComponentController != null) {
//            statsComponentController.setMainController(this);
//        }
//
//        tutorialMode.setOnMouseClicked((event) -> {
//            if(tutorialMode.isSelected()){
//                isTutorialMode = true;
//            }
//            else{
//                isTutorialMode = false;
//            }
//
//            boardController.updateGIUDiscs(gameManager.getBoard(), isTutorialMode);
//        });
//
////        loadFileButton.setOnMouseClicked((event) -> {
////            LoadFileTask loadFileTask = new LoadFileTask();
////            bindTaskToUIComponents(loadFileTask, ()->{});
////            new Thread(loadFileTask).start();
////        });
////
////        startGameButton.setOnMouseClicked((event)-> gameManager.activateGame());
//    }
//
////    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
////        // task message
////        taskMessageLabel.textProperty().bind(aTask.messageProperty());
////
////        // task cleanup upon finish
////        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
////            onTaskFinished(Optional.ofNullable(onFinish));
////        });
////    }
////
////    public void onTaskFinished(Optional<Runnable> onFinish) {
////        taskMessageLabel.textProperty().unbind();
////        onFinish.ifPresent(Runnable::run);
////    }
//
//    // call this after gameManager is given.
//    private void lateInitialize(){
//        undoLastMoveButton.setOnMouseClicked(event -> { undoLastMove(); });
//        undoLastMoveButton.disableProperty().bind(Bindings.not(gameManager.canUndoProperty()));
//    }
//
//    public void updateGUI(){
//        boolean showHintsForPlayer = isTutorialMode && gameManager.getActivePlayer().isHuman();
//
//        boardController.updateGIUDiscs(gameManager.getBoard(), showHintsForPlayer);
//        statsComponentController.refreshTable(gameManager.getPlayersList(), gameManager.getActivePlayer());
//    }
//
//    private void undoLastMove() {
//        gameManager.undo();
//        updateGUI();
//
//        if(!gameManager.getActivePlayer().isHuman()){
//            simulateComputerTurns();
//        }
//    }
//
//    public void initTable() {
//        statsComponentController.setPlayers(gameManager.getPlayersList(), gameManager.getActivePlayer());
//    }
//
//    public void playTurn(Point clickedCellBoardPoint) {
//        Player activePlayer = gameManager.getActivePlayer();
//        GameManager.eMoveStatus moveStatus;
//
//        if(gameManager.getActivePlayer().isHuman()){
//            moveStatus = activePlayer.makeMove(clickedCellBoardPoint, gameManager.getBoard());
//
//            if (moveStatus == GameManager.eMoveStatus.OK) {
//                updateEndTurn();
//            }
//
//            if(!gameManager.getActivePlayer().isHuman()){
//                simulateComputerTurns();
//            }
//        }
//
//        if (gameManager.isGameOver()) {
//            onGameOver();
//        }
//    }
//
//    private void simulateComputerTurns() {
//            Thread thread = new Thread(new ComputerMoveTask(gameManager, this));
//            thread.start();
//    }
//
//    public void updateEndTurn(){
//        gameManager.changeTurn();
//        updateGUI();
//    }
//
//    public void onGameOver(){
//        System.out.println("Test");
//        StringBuilder winMessageBuilder = new StringBuilder();
//
//        winMessageBuilder.append("Game Over.\n");
//
//        if(gameManager.getHighestScoringPlayers().size() == 1) {
//            winMessageBuilder.append(gameManager.getHighestScoringPlayers().get(0).getName())
//                    .append(" is the winner!");
//        } else {
//            winMessageBuilder.append("It's a tie!");
//        }
//        PopupFactory.showPopup(winMessageBuilder.toString());
//    }
//
//    public boolean isInReplayMode() {
//        return isInReplayMode;
//    }

    public void setBoardGUI(BoardGUI _boardGUI){
        boardGUI = _boardGUI;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setPrimaryStage(Stage _primaryStage){
        primaryStage = _primaryStage;
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
        didLoadXmlFile = new SimpleBooleanProperty(false);
        didStartGame = new SimpleBooleanProperty(false);
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

            boardController.updateGIUDiscs(gameManager.getBoard(), isTutorialMode);
        });

        loadFileButton.setOnMouseClicked((event) -> onLoadFileClick());
        startGameButton.setOnMouseClicked((event)-> onStartGameClick());
        startGameButton.disableProperty().bind(didLoadXmlFile.not());
        loadFileButton.disableProperty().bind(didStartGame);
    }

    public void onStartGameClick(){
        gameManager.activateGame();
        initTable();
        didStartGame.set(true);
        boardGUI.setIsGameActive(true);
    }

    public void onLoadFileClick(){
        LoadFileTask loadFileTask = new LoadFileTask(primaryStage);
        bindTaskToUIComponents(loadFileTask, ()->{});
        new Thread(loadFileTask).run();

        gameManager = loadFileTask.getGameManager();
        if(gameManager != null) {
            boardGUI = new BoardGUI(gameManager.getBoard(), this);
            boardParent.setCenter(boardGUI);
            boardParent.setAlignment(boardGUI, javafx.geometry.Pos.TOP_CENTER);
            didLoadXmlFile.set(true);
        }
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


        // call this after gameManager is set.
    private void lateInitialize(){
        undoLastMoveButton.setOnMouseClicked(event -> { undoLastMove(); });
        undoLastMoveButton.disableProperty().bind(Bindings.not(gameManager.canUndoProperty()));

        replayModeButton.setOnMouseClicked(event -> {
            isInReplayMode = true;
            showReplayMode();
        });
        replayModeButton.setDisable(true);

        replayModePrevButton.setOnMouseClicked(event -> { showPrevTurn(); });
        replayModePrevButton.setDisable(true);

        replayModeNextButton.setOnMouseClicked(event -> { showNextTurn(); });
        replayModeNextButton.setDisable(true);
        isInReplayMode = false;
    }

    private void showReplayMode() {
        replayModeButton.setDisable(true);
        replayModePrevButton.setDisable(false);

        List<Turn> turnsList = gameManager.getHistoryOfTurns();
        replayTurnIterator = turnsList.listIterator(turnsList.size() - 1);
        showTurnInGIU(replayTurnIterator.next());
        replayTurnIterator.previous();
    }

    private void showPrevTurn() {
        if(replayTurnIterator.hasPrevious()){
            Turn currTurnToShow = replayTurnIterator.previous();
            showTurnInGIU(currTurnToShow);
            replayModeNextButton.setDisable(false);

            updateReplayModePrevNextButtons();
        }
    }

    private void showNextTurn() {
        if(replayTurnIterator.hasNext()){
            if(replayTurnIterator.hasNext()){
                replayTurnIterator.next();
                Turn currTurnToShow = replayTurnIterator.next();
                replayTurnIterator.previous();
                showTurnInGIU(currTurnToShow);
                replayModePrevButton.setDisable(false);
            }


            updateReplayModePrevNextButtons();
        }
    }

    private void updateReplayModePrevNextButtons(){
        replayModePrevButton.setDisable(!replayTurnIterator.hasPrevious());
        if(replayTurnIterator.hasNext()){
            replayTurnIterator.next();
            replayModeNextButton.setDisable(!replayTurnIterator.hasNext());
            replayTurnIterator.previous();
        }
        else{
            replayModeNextButton.setDisable(!replayTurnIterator.hasNext());
        }
    }




    private void showTurnInGIU(Turn turnToShow){
        List<Player> turnPlayerList = turnToShow.getPlayersList();

        boardController.updateGIUDiscs(turnToShow.getBoard(), false);
        statsComponentController.refreshTable(turnPlayerList,  turnToShow.getActivePlayer());
    }


    public void updateGUI(){
        boolean showHintsForPlayer = isTutorialMode && gameManager.getActivePlayer().isHuman();

        boardController.updateGIUDiscs(gameManager.getBoard(), showHintsForPlayer);
        statsComponentController.refreshTable(gameManager.getPlayersList(), gameManager.getActivePlayer());
    }

    private void undoLastMove() {
        gameManager.undo();
        updateGUI();

        if(!gameManager.getActivePlayer().isHuman()){
            simulateComputerTurns();
        }
    }

    public void initTable() {
        statsComponentController.setPlayers(gameManager.getPlayersList(), gameManager.getActivePlayer());
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
        StringBuilder winMessageBuilder = new StringBuilder();

        winMessageBuilder.append("Game Over.\n");

        if(gameManager.getHighestScoringPlayers().size() == 1) {
            winMessageBuilder.append(gameManager.getHighestScoringPlayers().get(0).getName())
                    .append(" is the winner!");
        } else {
            winMessageBuilder.append("It's a tie!");
        }
        PopupFactory.showPopup(winMessageBuilder.toString());

        replayModeButton.setDisable(false);
    }

    public boolean isInReplayMode() {
        return isInReplayMode;
    }

    public void setBoardParent(BorderPane _borderPane) {
        boardParent = _borderPane;
    }
}

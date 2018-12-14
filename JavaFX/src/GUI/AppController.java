package GUI;

import GameEngine.GameManager;
import GameEngine.GameManager.TurnHistory.Turn;
import GameEngine.Player;
import GameEngine.Point;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class AppController {

    private GameManager gameManager;

    // it's better if appController wouldn't have this member. (boardController should be his referent)
    //private BoardGUI boardGUI;
    private Stage primaryStage;
    private ListIterator<GameManager.TurnHistory.Turn> replayTurnIterator;
    private BoardController boardController;
    private boolean isTutorialMode = false;
    private BorderPane boardParent;
    @FXML private StatsController statsComponentController;
    @FXML private CheckBox tutorialModeCheckBox;
    @FXML private Button undoLastMoveButton;
    @FXML private Button replayModeButton;
    @FXML private Button replayModePrevButton;
    @FXML private Button replayModeNextButton;
    @FXML private Button stopReplayButton;
    @FXML private Label taskMessageLabel;
    @FXML private Button loadFileButton;
    @FXML private Button startGameButton;
    @FXML private Button stopGameButton;

    private BooleanProperty didLoadXmlFile;
    private BooleanProperty didStartGame;
    private SimpleBooleanProperty isGameInReplayMode;
    private SimpleBooleanProperty isComputerMoveInProgress;

    private void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        if(gameManager != null) {
            lateInitialize();
        }
    }

    @FXML
    public void initialize() {
        isComputerMoveInProgress = new SimpleBooleanProperty(false);
        isGameInReplayMode = new SimpleBooleanProperty(false);
        didLoadXmlFile = new SimpleBooleanProperty(false);
        didStartGame = new SimpleBooleanProperty(false);
        if (statsComponentController != null) {
            statsComponentController.setMainController(this);
        }

        loadFileButton.setOnMouseClicked((event) -> onLoadFileClick());
        startGameButton.setOnMouseClicked((event)-> onStartGameClick());
        stopGameButton.setOnMouseClicked(event ->  OnStopGameClick());
        // startGameButton.disableProperty().bind(didLoadXmlFile.not()); // not good
        startGameButton.disableProperty().bind(Bindings.or(didLoadXmlFile.not(), isGameInReplayMode));
        stopGameButton.setDisable(true);
        //loadFileButton.disableProperty().bind(didStartGame); // not good
        loadFileButton.disableProperty().bind(Bindings.or(didStartGame, isGameInReplayMode));

        undoLastMoveButton.setDisable(true);
        tutorialModeCheckBox.setDisable(true);
        replayModeButton.setDisable(true);
        replayModePrevButton.setDisable(true);
        replayModeNextButton.setDisable(true);
        stopReplayButton.setDisable(true);
    }

    private void onStartGameClick(){
        if(gameManager.isGameOver()){
            resetGame();
        }
        if(isGameInReplayMode.get()){
            stopReplayMode();
            replayModeButton.setDisable(true);
            stopReplayButton.setDisable(true);
        }

        gameManager.activateGame();
        initTable();
        didStartGame.set(true);
        //boardGUI.setIsGameActive(true);
    }

    private void resetGame(){
        gameManager.resetGame();
        updateGUI();
        //boardGUI.setIsGameActive(false);
    }

    private void onLoadFileClick(){
        if(isGameInReplayMode.get()){
            stopReplayMode();
            replayModeButton.setDisable(true);
            stopReplayButton.setDisable(true);
        }

        LoadFileTask loadFileTask = new LoadFileTask(primaryStage);
        bindTaskToUIComponents(loadFileTask, ()->{});
        new Thread(loadFileTask).run();

        setGameManager(loadFileTask.getGameManager());
        if(gameManager != null) {
//            startGameButton.visibleProperty().bind(gameManager.isGameActiveProperty().not());
            startGameButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty(), isGameInReplayMode));
            stopGameButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty().not(), isGameInReplayMode));
            BoardGUI boardGUI = new BoardGUI(gameManager.getBoard(), this);
            boardParent.setCenter(boardGUI);
            boardParent.setAlignment(boardGUI, javafx.geometry.Pos.TOP_CENTER);
            didLoadXmlFile.set(true);
        }
    }

    private void OnStopGameClick() {
        resetGame();
    }

    private void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        taskMessageLabel.textProperty().bind(aTask.messageProperty());

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    private void onTaskFinished(Optional<Runnable> onFinish) {
        taskMessageLabel.textProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }


    public SimpleBooleanProperty isGameInReplayModeProperty() {
        return isGameInReplayMode;
    }

    public SimpleBooleanProperty isComputerMoveInProgressProperty() {
        return isComputerMoveInProgress;
    }

    public void setIsComputerMoveInProgress(boolean isComputerMoveInProgress) {
        this.isComputerMoveInProgress.set(isComputerMoveInProgress);
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

    // call this after gameManager is set.
    private void lateInitialize(){
        undoLastMoveButton.setOnMouseClicked(event -> { undoLastMove(); });
//        undoLastMoveButton.disableProperty().bind(Bindings.not(gameManager.canUndoProperty())); // not good
        undoLastMoveButton.disableProperty().bind(Bindings.or(Bindings.or(gameManager.canUndoProperty().not(),
                gameManager.isGameActiveProperty().not()), isComputerMoveInProgress));

        replayModeButton.setOnMouseClicked(event -> {
            isGameInReplayMode.setValue(true);
            showReplayMode();
        });
        replayModeButton.setDisable(true);

        stopReplayButton.setOnMouseClicked(event -> {
            isGameInReplayMode.setValue(false);
            stopReplayMode();
        });

        replayModePrevButton.setOnMouseClicked(event -> { showPrevTurn(); });
        replayModePrevButton.setDisable(true);

        replayModeNextButton.setOnMouseClicked(event -> { showNextTurn(); });
        replayModeNextButton.setDisable(true);

        tutorialModeCheckBox.disableProperty().bind(gameManager.isGameActiveProperty().not());
        tutorialModeCheckBox.setOnMouseClicked((event) -> {
            isTutorialMode = tutorialModeCheckBox.isSelected();
            boardController.updateGIUDiscs(gameManager.getBoard(), isTutorialMode);
        });
    }

    private void stopReplayMode(){
        replayModePrevButton.setDisable(true);
        replayModeNextButton.setDisable(true);
        replayModeButton.setDisable(false);
        stopReplayButton.setDisable(true);

        updateGUI();
    }

    private void showReplayMode() {
        replayModeButton.setDisable(true);
        replayModePrevButton.setDisable(false);
        stopReplayButton.setDisable(false);

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

    private void initTable() {
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
        gameManager.setIsGameActive(false);
        didLoadXmlFile.set(false);
        didStartGame.set(false);
    }

    public boolean isInReplayMode() {
        return isGameInReplayMode.getValue();
    }

    public void setBoardParent(BorderPane _borderPane) {
        boardParent = _borderPane;
    }
}



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
    @FXML private Button endGameButton;
    @FXML private Button playerRetireButton;
    @FXML private Label gameModeLabel;
    @FXML private Label hintContentLabel;

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
        endGameButton.setOnMouseClicked(event ->  OnStopGameClick());
        // startGameButton.disableProperty().bind(didLoadXmlFile.not()); // not good
        startGameButton.disableProperty().bind(Bindings.or(didLoadXmlFile.not(), isGameInReplayMode));
        endGameButton.setDisable(true);
        //loadFileButton.disableProperty().bind(didStartGame); // not good
        loadFileButton.disableProperty().bind(Bindings.or(didStartGame, isGameInReplayMode));

        undoLastMoveButton.setDisable(true);
        playerRetireButton.setDisable(true);
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
            updateGameModeLabel();
//            startGameButton.visibleProperty().bind(gameManager.isGameActiveProperty().not());
            startGameButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty(), isGameInReplayMode));
            endGameButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty().not(), isGameInReplayMode));
            BoardGUI boardGUI = new BoardGUI(gameManager.getBoard(), this);
            boardParent.setCenter(boardGUI);
            boardParent.setAlignment(boardGUI, javafx.geometry.Pos.TOP_CENTER);
            didLoadXmlFile.set(true);
            initTable();
        }
    }

    private void updateGameModeLabel(){
        if(gameManager.getGameMode().equals(GameManager.eGameMode.Islands)){
            gameModeLabel.setText("Mode: Islands");
        }
        else{
            gameModeLabel.setText("Mode: Regular");
        }
    }


    private void OnStopGameClick() {
        resetGame();

        StringBuilder winMessageBuilder = new StringBuilder();

        winMessageBuilder.append("Game has ended!\n");
        PopupFactory.showPopup(winMessageBuilder.toString());
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
        undoLastMoveButton.setOnMouseClicked(event -> { onUndoClick(); });
//        undoLastMoveButton.disableProperty().bind(Bindings.not(gameManager.canUndoProperty())); // not good
        undoLastMoveButton.disableProperty().bind(Bindings.or(Bindings.or(gameManager.canUndoProperty().not(),
                gameManager.isGameActiveProperty().not()), isComputerMoveInProgress));

        playerRetireButton.setOnMouseClicked(event -> {onPlayerRetireClick();});
        playerRetireButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty().not(), isComputerMoveInProgress));

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

        tutorialModeCheckBox.disableProperty().bind(Bindings.and(gameManager.isGameActiveProperty().not(), isGameInReplayMode.not()));
        tutorialModeCheckBox.setOnMouseClicked((event) -> {
            isTutorialMode = tutorialModeCheckBox.isSelected();

            if(isGameInReplayMode.get()){
                boardController.updateGIUDiscs(replayTurnIterator.next().getBoard(), isTutorialMode);
                replayTurnIterator.previous();
            }
            else{
                boardController.updateGIUDiscs(gameManager.getBoard(), isTutorialMode);
            }
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

    private void onPlayerRetireClick(){
        if(gameManager.getActivePlayer().isHuman()){
            gameManager.getActivePlayer().quitGame(gameManager);
            updateGUI();

            if(gameManager.isGameOver()){
                onGameOver();
            }
            else{
                if(!gameManager.getActivePlayer().isHuman()){
                    simulateComputerTurns();
                }
            }

        }
    }

    private void showTurnInGIU(Turn turnToShow){
        List<Player> turnPlayerList = turnToShow.getPlayersList();

        boardController.updateGIUDiscs(turnToShow.getBoard(), isTutorialMode);
        statsComponentController.refreshTable(turnPlayerList,  turnToShow.getActivePlayer());
    }


    public void updateGUI(){
        boolean showPotentialFlipsForPlayer = isTutorialMode && gameManager.getActivePlayer().isHuman();

        boardController.updateGIUDiscs(gameManager.getBoard(), showPotentialFlipsForPlayer);
        statsComponentController.refreshTable(gameManager.getPlayersList(), gameManager.getActivePlayer());
    }

    private void onUndoClick() {
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
        GameManager.eMoveStatus moveStatus = null;

        if(gameManager.getActivePlayer().isHuman()){
            moveStatus = activePlayer.makeMove(clickedCellBoardPoint, gameManager.getBoard());
            updateHintContentLabel(moveStatus, true);

            if (moveStatus == GameManager.eMoveStatus.OK) {
                updateEndTurn();
            }

            if(!gameManager.getActivePlayer().isHuman()){
                hintContentLabel.setText("");
                simulateComputerTurns();
            }
        }
        else{
            updateHintContentLabel(moveStatus, false);
        }

        if (gameManager.isGameOver()) {
            onGameOver();
        }
    }

    private void updateHintContentLabel(GameManager.eMoveStatus moveAttemptStatus, boolean isUserTurn){
        StringBuilder stringBuilder = new StringBuilder();

        if(isUserTurn){
            if(moveAttemptStatus != GameManager.eMoveStatus.OK){
                stringBuilder.append("Hint:\n");
                stringBuilder.append(moveAttemptStatus.toString());
            }
        }
        else{
            stringBuilder.append("Hint:\n");
            stringBuilder.append("Please wait for your turn");
        }

        hintContentLabel.setText(stringBuilder.toString());
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



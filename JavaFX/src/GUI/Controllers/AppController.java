package GUI.Controllers;

import GUI.BoardGUI;
import GUI.Tasks.ComputerMoveTask;
import GUI.Tasks.LoadFileTask;
import GUI.PopupFactory;
import GameEngine.GameManager;
import GameEngine.GameManager.TurnHistory.Turn;
import GameEngine.Player;
import GameEngine.Point;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Turn turnToShowWhenDoneWithReplay;
    @FXML private StatsController statsComponentController;
    @FXML private CheckBox tutorialModeCheckBox;
    @FXML private CheckBox animationsCheckBox;
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
    @FXML private ComboBox skinComboBox;
    @FXML private ProgressBar loadProgressBar;

    public BooleanProperty getDidStartGameProperty() {
        return didStartGame;
    }

    public BooleanProperty isShowBoardProperty() {
        return isShowBoard;
    }

    private BooleanProperty isShowBoard;
    private BooleanProperty didLoadXmlFile;
    private BooleanProperty didStartGame;
    private SimpleBooleanProperty isGameInReplayMode;
    private SimpleBooleanProperty isComputerMoveInProgress;
    private SimpleBooleanProperty isScaleAnimationsInPlay;

    private void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        if(gameManager != null) {
            lateInitialize();
        }
    }

    @FXML
    public void initialize() {
        loadProgressBar.setProgress(0);

        isScaleAnimationsInPlay = new SimpleBooleanProperty(false); // new line

        isComputerMoveInProgress = new SimpleBooleanProperty(false);
        isGameInReplayMode = new SimpleBooleanProperty(false);
        didLoadXmlFile = new SimpleBooleanProperty(false);
        didStartGame = new SimpleBooleanProperty(false);
        if (statsComponentController != null) {
            statsComponentController.setMainController(this);
        }
        isShowBoard = new SimpleBooleanProperty(false);

        loadFileButton.setOnMouseClicked((event) -> {
            onLoadFileClick();
        });
        startGameButton.setOnMouseClicked((event)-> onStartGameClick());
        endGameButton.setOnMouseClicked(event ->  onStopGameClick());
        // startGameButton.disableProperty().bind(didLoadXmlFile.not()); // not good
        //startGameButton.disableProperty().bind(Bindings.and(Bindings.or(didLoadXmlFile.not(), isGameInReplayMode),gameManager.isGameActiveProperty()));
        endGameButton.setDisable(true);
        startGameButton.setDisable(true);
        //loadFileButton.disableProperty().bind(didStartGame); // not good
        //loadFileButton.disableProperty().bind(Bindings.or(didStartGame, isGameInReplayMode)); //not good: what happens when game stops?
        loadFileButton.setDisable(false);
        skinComboBox.getItems().addAll("Default Skin", "Normal Skin", "Beautiful Skin");
        skinComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue.equals("Default Skin")) {
                primaryStage.getScene().getStylesheets().remove("/resources/BeautifulSkin.css");
                primaryStage.getScene().getStylesheets().remove("/resources/NormalSkin.css");
                primaryStage.getScene().getStylesheets().add("/resources/caspian.css");
            }
            else if(newValue.equals("Normal Skin")){
                primaryStage.getScene().getStylesheets().remove("/resources/BeautifulSkin.css");
                primaryStage.getScene().getStylesheets().remove("/resources/caspian.css");
                primaryStage.getScene().getStylesheets().add("/resources/NormalSkin.css");
            }
            else{
                primaryStage.getScene().getStylesheets().remove("/resources/NormalSkin.css");
                primaryStage.getScene().getStylesheets().remove("/resources/caspian.css");
                primaryStage.getScene().getStylesheets().add("/resources/BeautifulSkin.css");
            }

        });

        disableButtonsAndCheckBoxesOPreStart();
    }

    public boolean isScaleAnimationsInPlay() {
        return isScaleAnimationsInPlay.get();
    }

    public SimpleBooleanProperty isScaleAnimationsInPlayProperty() {
        return isScaleAnimationsInPlay;
    }

    public void setIsScaleAnimationsInPlay(boolean isScaleAnimationsInPlay) {
        this.isScaleAnimationsInPlay.set(isScaleAnimationsInPlay);
    }

    public Button getReplayModeButton() {
        return replayModeButton;
    }

    private void disableButtonsAndCheckBoxesOPreStart(){
        undoLastMoveButton.setDisable(true);
        playerRetireButton.setDisable(true);
        tutorialModeCheckBox.setDisable(true);
        animationsCheckBox.setDisable(true);
        replayModeButton.setDisable(true);
        replayModePrevButton.setDisable(true);
        replayModeNextButton.setDisable(true);
        stopReplayButton.setDisable(true);
    }

    private void onStartGameClick(){
//        if(gameManager.isGameOver()){
//            resetGame();
//        }
        isComputerMoveInProgress.setValue(false); // new line
        boardController.stopScaleAnimationsIfItIsInPlay();

        loadProgressBar.setProgress(0);
        taskMessageLabel.setText("");
        loadFileButton.setDisable(true); // new here
        resetGame();

        if(isGameInReplayMode.get()){
            stopReplayMode();
            replayModeButton.setDisable(true);
            stopReplayButton.setDisable(true);
        }

        gameManager.activateGame();
        didStartGame.set(true);
        replayModeButton.setDisable(true);
        isShowBoard.setValue(true);
        updateGUI();
        //boardGUI.setIsGameActive(true);
    }

    private void resetGame(){
        gameManager.resetGame();
        boardController.updateGIUDiscs(gameManager.getBoard(), isTutorialMode, false);
        statsComponentController.refreshTable(gameManager.getPlayersList(), gameManager.getActivePlayer());
        //boardGUI.setIsGameActive(false);
        isShowBoard.setValue(false);
        hintContentLabel.setText("");
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

        if(loadFileTask.getGameManager() != null) {
            setGameManager(loadFileTask.getGameManager());
            updateGameModeLabel();

            endGameButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty().not(), isGameInReplayMode));
            BoardGUI boardGUI = new BoardGUI(gameManager.getBoard(), this);
            boardParent.setCenter(boardGUI);
            boardParent.setAlignment(boardGUI, javafx.geometry.Pos.TOP_CENTER);
            didLoadXmlFile.set(true);
            isShowBoard.setValue(false);
            initTable();
        }

//        setGameManager(loadFileTask.getGameManager());
//        if(gameManager != null) {
//            updateGameModeLabel();
////            startGameButton.visibleProperty().bind(gameManager.isGameActiveProperty().not());
//            //startGameButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty(), isGameInReplayMode));
//            endGameButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty().not(), isGameInReplayMode));
//            BoardGUI boardGUI = new BoardGUI(gameManager.getBoard(), this);
//            boardParent.setCenter(boardGUI);
//            boardParent.setAlignment(boardGUI, javafx.geometry.Pos.TOP_CENTER);
//            didLoadXmlFile.set(true);
//            isShowBoard.setValue(false);
//            initTable();
//
    }

    private void updateGameModeLabel(){
        if(gameManager.getGameMode().equals(GameManager.eGameMode.Islands)){
            gameModeLabel.setText("Mode: Islands");
        }
        else{
            gameModeLabel.setText("Mode: Regular");
        }
    }


    private void onStopGameClick() {
        loadFileButton.setDisable(false); // new here

        replayModeButton.setDisable(false);
        gameManager.setIsGameActive(false);
        isShowBoard.setValue(false);

        List<Turn> turnsList = gameManager.getHistoryOfTurns();
        turnToShowWhenDoneWithReplay = turnsList.get(0);
        showTurnInGIU(turnToShowWhenDoneWithReplay, false);


        StringBuilder winMessageBuilder = new StringBuilder();

        winMessageBuilder.append("Game has ended!\n");
        PopupFactory.showPopup(winMessageBuilder.toString());
    }

    private void bindTaskToUIComponents(LoadFileTask aTask, Runnable onFinish) {
        // task message
        taskMessageLabel.textProperty().bind(aTask.messageProperty());

        loadProgressBar.progressProperty().bind(aTask.progressProperty());

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish), aTask);
        });
    }

    private void onTaskFinished(Optional<Runnable> onFinish, LoadFileTask aTask) {
        taskMessageLabel.textProperty().unbind();
        loadProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
        if(!aTask.IsChooseFile()){
            loadProgressBar.setProgress(0);
        }
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
       // startGameButton.disableProperty().bind(Bindings.or(Bindings.or(didLoadXmlFile.not(), isGameInReplayMode),gameManager.isGameActiveProperty()));
        startGameButton.disableProperty().bind(
                Bindings.or(Bindings.or(Bindings.or(didLoadXmlFile.not(), isGameInReplayMode),gameManager.isGameActiveProperty()), isScaleAnimationsInPlayProperty()));

        undoLastMoveButton.setOnMouseClicked(event -> { onUndoClick(); });
//        undoLastMoveButton.disableProperty().bind(Bindings.not(gameManager.canUndoProperty())); // not good
        undoLastMoveButton.disableProperty().bind(Bindings.or(Bindings.or(gameManager.canUndoProperty().not(),
                gameManager.isGameActiveProperty().not()), isComputerMoveInProgress));

        playerRetireButton.setOnMouseClicked(event -> {onPlayerRetireClick();});
        playerRetireButton.disableProperty().bind(Bindings.or(gameManager.isGameActiveProperty().not(), isComputerMoveInProgress));

        replayModeButton.setOnMouseClicked(event -> {
            boardController.stopScaleAnimationsIfItIsInPlay();
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

//            if(isGameInReplayMode.get()){
//                boardController.updateGIUDiscs(replayTurnIterator.next().getBoard(),
//                        isTutorialMode && gameManager.getActivePlayer().isHuman(), animationsCheckBox.isSelected());
//                replayTurnIterator.previous();
//            }
            if(isGameInReplayMode.get()){
                boardController.updateGIUDiscs(replayTurnIterator.next().getBoard(),
                        isTutorialMode, animationsCheckBox.isSelected());
                replayTurnIterator.previous();
            }
            else{
                boardController.updateGIUDiscs(gameManager.getBoard(), isTutorialMode, animationsCheckBox.isSelected());
            }
        });

        animationsCheckBox.disableProperty().bind(Bindings.and(gameManager.isGameActiveProperty().not(), isGameInReplayMode.not()));
//        animationsCheckBox.setOnMouseClicked(event -> {
//            animationsCheckBox.isSelected();
//        });
    }

    private void stopReplayMode(){
        loadFileButton.setDisable(false); // new here

        replayModePrevButton.setDisable(true);
        replayModeNextButton.setDisable(true);
        replayModeButton.setDisable(false);
        stopReplayButton.setDisable(true);

        if(!gameManager.isGameOver()){
            isShowBoard.setValue(false); // new line
        }

        showTurnInGIU(turnToShowWhenDoneWithReplay, false);
    }

    private void showReplayMode() {
        loadFileButton.setDisable(true); // new here
        replayModeButton.setDisable(true);
        stopReplayButton.setDisable(false);
        //replayModePrevButton.setDisable(false);
        isShowBoard.setValue(true); // new line

        List<Turn> turnsList = gameManager.getHistoryOfTurns();
        replayTurnIterator = turnsList.listIterator(turnsList.size() - 1);
        showTurnInGIU(replayTurnIterator.next(), false);
        replayTurnIterator.previous();

        replayModePrevButton.setDisable(!replayTurnIterator.hasPrevious());
    }

    private void showPrevTurn() {
        if(replayTurnIterator.hasPrevious()){
            Turn currTurnToShow = replayTurnIterator.previous();
            showTurnInGIU(currTurnToShow, false);
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
                showTurnInGIU(currTurnToShow,animationsCheckBox.isSelected());
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

    private void showTurnInGIU(Turn turnToShow, boolean showAnimations){
        List<Player> turnPlayerList = turnToShow.getPlayersList();

        boardController.updateGIUDiscs(turnToShow.getBoard(), isTutorialMode, showAnimations);
        statsComponentController.refreshTable(turnPlayerList,  turnToShow.getActivePlayer());
    }


    public void updateGUI(){
//        boolean showPotentialFlipsForPlayer = isTutorialMode && gameManager.getActivePlayer().isHuman();
        boolean showPotentialFlipsForPlayer = isTutorialMode;

        boardController.updateGIUDiscs(gameManager.getBoard(), showPotentialFlipsForPlayer, animationsCheckBox.isSelected());
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

        if(isShowBoard.get()&&gameManager.isGameActiveProperty().get()){
            if(gameManager.getActivePlayer().isHuman() && !isComputerMoveInProgress.get()){
                moveStatus = activePlayer.makeMove(clickedCellBoardPoint, gameManager.getBoard());
                updateHintContentLabel(moveStatus, true, true);

                if (moveStatus == GameManager.eMoveStatus.OK) {
                    updateEndTurn();
                }

                if (gameManager.isGameOver()) {
                    onGameOver();
                    //isShowBoard.setValue(false); new line
//                hintContentLabel.setText(""); // new line
                }else{
                    if(!gameManager.getActivePlayer().isHuman()){
                        hintContentLabel.setText("");
                        simulateComputerTurns();
                    }
                }
            }
            else{
                updateHintContentLabel(moveStatus, false, true);
            }

//            if (gameManager.isGameOver()) {
//                onGameOver();
//                //isShowBoard.setValue(false); new line
////                hintContentLabel.setText(""); // new line
//            }
        }
        else if(!gameManager.isGameActiveProperty().get()){
            updateHintContentLabel(moveStatus, false, false);
        }

    }

    private void updateHintContentLabel(GameManager.eMoveStatus moveAttemptStatus, boolean isUserTurn, boolean isGameActive) {
        StringBuilder stringBuilder = new StringBuilder();

        if (isGameActive) {
            if (isUserTurn) {
                if (moveAttemptStatus != GameManager.eMoveStatus.OK) {
                    stringBuilder.append("Hint:\n");
                    stringBuilder.append(moveAttemptStatus.toString());
                }
            } else {
                stringBuilder.append("Hint:\n");
                stringBuilder.append("Please wait for your turn");
            }
        }
        else{
            stringBuilder.append("Hint:\n");
            stringBuilder.append("Game is no longer active.. Start a new game");
        }

        hintContentLabel.setText(stringBuilder.toString());
    }



    private void simulateComputerTurns() {
        Thread thread = new Thread(new ComputerMoveTask(gameManager, this));
        thread.start();
//        try{thread.join();} catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void updateEndTurn(){
        gameManager.changeTurn();
        updateGUI();
    }

    public void onGameOver(){
        hintContentLabel.setText(""); // new line
        loadFileButton.setDisable(false);
        Player winner = null;

        if(gameManager.getHighestScoringPlayers().size() == 1) {
            winner = gameManager.getHighestScoringPlayers().get(0);

            if(animationsCheckBox.isSelected()){
                boardController.highlightDiscsOfType(gameManager.getBoard(), winner.getDiscType());
            }
        }

        showEndOfGamePopupMessage(winner);
        replayModeButton.setDisable(false);
        gameManager.setIsGameActive(false);
        //didLoadXmlFile.set(false);
        //didStartGame.set(false);

        turnToShowWhenDoneWithReplay = gameManager.getHistoryOfTurns().get(gameManager.getHistoryOfTurns().size() - 1);
    }

    private void showEndOfGamePopupMessage(Player winner){
        StringBuilder winMessageBuilder = new StringBuilder();

        winMessageBuilder.append("Game Over.\n");

        if(winner != null) {
            winMessageBuilder.append(winner.getName())
                    .append(" is the winner!");
        }
        else {
            winMessageBuilder.append("It's a tie!");
        }
        PopupFactory.showPopup(winMessageBuilder.toString());
    }


    public boolean isInReplayMode() {
        return isGameInReplayMode.getValue();
    }

    public void setBoardParent(BorderPane _borderPane) {
        boardParent = _borderPane;
    }
}



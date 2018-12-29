package GUI.Controllers;

import GUI.BoardGUI;
import GameEngine.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class BoardController {

    private AppController appController;
    private BoardGUI boardComponent;
    private HashMap<eDiscType, Color> discTypeToDiscColorMap;
//    private Point cellBoardButtonClickedPoint;

//    private SimpleBooleanProperty isGameActive;

//    public void setIsGameActive(boolean _isGameActive){
//        isGameActive.set(_isGameActive);
//    }

    //@FXML

    public void stopScaleAnimationsIfItIsInPlay(){
        boardComponent.stopScaleAnimations();
    }

    public void disableReplayMode(boolean newValue){
        appController.getReplayModeButton().setDisable(true);
    }

    public boolean isScaleAnimationsInPlay() {
        return appController.isScaleAnimationsInPlayProperty().get();
    }

    public SimpleBooleanProperty isScaleAnimationsInPlayProperty() {
        return appController.isScaleAnimationsInPlayProperty();
    }

    public void setIsScaleAnimationsInPlay(boolean isScaleAnimationsInPlay) {
        appController.isScaleAnimationsInPlayProperty().set(isScaleAnimationsInPlay);
    }

    public BoardController(AppController parentController, BoardGUI boardGUI){
        boardComponent = boardGUI;
        appController = parentController;
        mapDiscTypesToColors();
//        isGameActive = appController.getGameManager().isGameActiveProperty();
    }

    public Color discTypeToColor(eDiscType discType){
        return discTypeToDiscColorMap.get(discType);
    }

    public void CellBoardButtonClicked(Point cellBoardButtonClickedPoint) {
//        this.cellBoardButtonClickedPoint = cellBoardButtonClickedPoint;

        appController.playTurn(cellBoardButtonClickedPoint);
    }

    public void mapDiscTypesToColors()
    {
        discTypeToDiscColorMap = new HashMap<>();

        discTypeToDiscColorMap.put(eDiscType.BLACK, Color.BLACK);
        discTypeToDiscColorMap.put(eDiscType.WHITE, Color.WHITE);
        discTypeToDiscColorMap.put(eDiscType.BLUE, Color.BLUE);
        discTypeToDiscColorMap.put(eDiscType.GREEN, Color.GREEN);
        discTypeToDiscColorMap.put(eDiscType.RED, Color.RED);
    }

    public void updateGIUDiscs(Board gameBoard, boolean isTutorialMode, boolean isAnimationsEnabled)
    {
        boardComponent.updateBoard(gameBoard, isTutorialMode, isAnimationsEnabled);
    }

    public void highlightDiscsOfType(Board gameboard, eDiscType discType){
        boardComponent.highlightDiscs(gameboard, discType);
    }

    public AppController getAppController() {
        return appController;
    }
}

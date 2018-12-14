package GUI;

import GameEngine.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
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

    public void updateGIUDiscs(Board gameBoard, boolean isTutorialMode)
    {
        boardComponent.updateBoard(gameBoard, isTutorialMode);
    }
}

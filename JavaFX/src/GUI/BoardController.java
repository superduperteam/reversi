package GUI;

import GameEngine.*;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class BoardController {

    private AppController mainController;
    private BoardGUI boardComponent;
    private HashMap<eDiscType, Color> discTypeToDiscColorMap;
    private Point cellBoardButtonClickedPoint;

    //@FXML

    public BoardController(AppController parentController, BoardGUI boardGUI){
        boardComponent = boardGUI;
        mainController = parentController;
        mapDiscTypesToColors();
    }

    public Color discTypeToColor(eDiscType discType){
        return discTypeToDiscColorMap.get(discType);
    }

    public void CellBoardButtonClicked(Point cellBoardButtonClickedPoint) {
        this.cellBoardButtonClickedPoint = cellBoardButtonClickedPoint;

        mainController.playTurn(cellBoardButtonClickedPoint);
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

    public void updateGIUDiscs(boolean isTutorialMode)
    {
        boardComponent.updateBoard(mainController.getGameManager().getBoard(), isTutorialMode);
    }
}

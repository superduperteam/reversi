package GUI;

import GameEngine.eDiscType;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.HashMap;

public class BoardController {

    private AppController mainController;
    private BoardGUI boardComponent;
    private HashMap<eDiscType, Color> discTypeToDiscColorMap;

    @FXML

    public void setBoardGUI(BoardGUI boardGUI){
        boardComponent = boardGUI;
    }

    public BoardController(AppController parentController){
        mainController = parentController;
        mapDiscTypesToColors();
    }

    public Color discTypeToColor(eDiscType discType){
        return discTypeToDiscColorMap.get(discType);
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

//    @FXML
//    void OnMouseClicked(MouseEvent event) {
//        event.getButton()
//    }

}

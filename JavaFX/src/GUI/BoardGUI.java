package GUI;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class BoardGUI extends ScrollPane {

    private final GridPane gridPane;
    private final ColumnConstraints columnConstraints;
    private final RowConstraints rowConstraints;
    private Button[][] buttons;


    public BoardGUI(int rowsCount, int columnsCount) {

        rowsCount = 8;
        columnsCount = 8;

        gridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        buttons = new Button[rowsCount][columnsCount];

        setFitToHeight(true);
        setFitToWidth(true);
        setMaxHeight(600); // used to be USE_PREF_
        setMaxWidth(600); // used to be USE_PREF_

        gridPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        gridPane.setGridLinesVisible(true);
        gridPane.setPrefHeight(USE_COMPUTED_SIZE);
        gridPane.setPrefWidth(USE_COMPUTED_SIZE);
        gridPane.setStyle("-fx-background-color: WHITE;");

        columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints.setMinWidth(10.0);
        columnConstraints.setPrefWidth(30.0);

        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        for(int i=0; i<rowsCount; i++){
            for(int j=0; j< columnsCount; j++){
                buttons[i][j] = new Button();

                GridPane.setHalignment(buttons[i][j], javafx.geometry.HPos.CENTER);
                if(j!= 0) {GridPane.setColumnIndex(buttons[i][j], j);}
                if(i!=0){ GridPane.setRowIndex(buttons[i][j], i);}

                buttons[i][j].setMnemonicParsing(false);
                buttons[i][j].setPrefHeight(100.0);
                buttons[i][j].setPrefWidth(100.0);
                buttons[i][j].setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            }
        }


        for(int i=0; i<rowsCount; i++){
            for(int j=0; j< columnsCount; j++){
                gridPane.getChildren().add(buttons[i][j]);
            }
        }

        for(int i=0; i< rowsCount; i++){
            gridPane.getRowConstraints().add(rowConstraints);
        }
        for(int j=0; j< columnsCount; j++){
            gridPane.getColumnConstraints().add(columnConstraints);
        }
        setContent(gridPane);

        gridPane.autosize();
    }
}


package GUI;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BoardGUI extends ScrollPane {

    private final GridPane gridPane;
    private final ColumnConstraints columnConstraints;
    private final RowConstraints rowConstraints;
    private Button[][] buttons;
    private final int rowsCount;
    private final int columnsCount;


    public BoardGUI(int rowsCount, int columnsCount) {
        gridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        buttons = new Button[rowsCount][columnsCount];
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;

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
                buttons[i][j].setPrefHeight(5000.0);
                buttons[i][j].setPrefWidth(5000.0);
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


        for(int i=0; i<rowsCount; i++){
            for(int j=0; j< columnsCount; j++){
//                ImageView imageView = new ImageView();
//
//                imageView.setNodeOrientation(javafx.geometry.NodeOrientation.INHERIT);
//                imageView.setImage(new Image(getClass().getResource("/resources/black-disc.png").toExternalForm()));
//                buttons[i][j].setGraphic(imageView);
//
//                imageView.fitHeightProperty().bind(buttons[i][j].heightProperty());
//                imageView.fitWidthProperty().bind(buttons[i][j].widthProperty());

                Circle circle = new Circle(50, 50, 40);
                buttons[i][j].setGraphic(circle);
                buttons[i][j].setContentDisplay(ContentDisplay.CENTER);

                circle.radiusProperty().bind(buttons[i][j].heightProperty().divide(4));


//                Image image = new Image("/resources/black-disc.png", buttons[i][j].getWidth(), buttons[i][j].getHeight(), false, true, true);
//                BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(buttons[i][j].getWidth(), buttons[i][j].getHeight(), true, true, true, false));
//
//                Background backGround = new Background(bImage);
//                buttons[i][j].setBackground(backGround);



//                BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("/resources/black-disc.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
//                Background background = new Background(backgroundImage);

//                buttons[i][j].setBackground(background);


            }
        }


        setContent(gridPane);

        gridPane.autosize();

    }
}


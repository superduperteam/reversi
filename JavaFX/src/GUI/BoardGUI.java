package GUI;

import GUI.Controllers.AppController;
import GUI.Controllers.BoardController;
import GameEngine.*;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class BoardGUI extends ScrollPane {
    private BoardController boardController;
    private final GridPane gridPane;
    private final ColumnConstraints columnConstraints;
    private final RowConstraints rowConstraints;
    private CellBoardButton[][] cellBoardButtons;
    private int rowsCount;
    private int columnsCount;
    private List<ScaleTransition> scaleTransitionList;
    private final ScrollPane boardScrollPane;
    public BoardGUI(Board gameBoard, AppController appController) {
        scaleTransitionList = new ArrayList<>();
        boardScrollPane = new ScrollPane();
        //isGameActive = appController.getGameManager().isGameActiveProperty();
        boardController = new BoardController(appController, this);
        appController.setBoardController(boardController);
        gridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        this.rowsCount = gameBoard.getHeight();
        this.columnsCount = gameBoard.getWidth();
        cellBoardButtons = new CellBoardButton[rowsCount][columnsCount];
        Disc currDisc;
        CellBoard currCellBoard;
        CellBoardButton currCellBoardButton;


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//        setFitToHeight(true);
//        setFitToWidth(true);
        setMaxHeight(Math.min(Math.max(600,85*rowsCount),Math.min(screenBounds.getHeight()*0.9,screenBounds.getWidth()*0.65))); // used to be USE_PREF_ -> then used to be 600
        setMaxWidth(Math.min(Math.max(600,85*rowsCount),Math.min(screenBounds.getHeight()*0.9,screenBounds.getWidth()*0.65))); // used to be USE_PREF_ -> then used to be 600

        gridPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        gridPane.setGridLinesVisible(true);
        gridPane.setPrefHeight(USE_COMPUTED_SIZE);
        gridPane.setPrefWidth(USE_COMPUTED_SIZE);
        gridPane.setStyle("-fx-background-color: WHITE;");

        //gridPane.setPadding(new Insets(15,15,15,15));

        columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints.setMinWidth(20.0);
        columnConstraints.setPrefWidth(30.0);

        rowConstraints.setMinHeight(20.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES);


        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                cellBoardButtons[i][j] = new CellBoardButton(i, j);
                currCellBoardButton = cellBoardButtons[i][j];
                //currCellBoardButton.disableProperty().bind(Bindings.and(isGameActive.not(), appController.isGameInReplayModeProperty().not()));
//                currCellBoardButton.disableProperty().bind(Bindings.and(appController.getGameManager().isGameActiveProperty().not(),
//                        appController.isGameInReplayModeProperty().not()));
                currCellBoardButton.disableProperty().bind(appController.isShowBoardProperty().not());
                //                currCellBoardButton.disableProperty().bind(Bindings.or(appController.isShowBoardProperty().not(), appController.getGameManager().isGameActiveProperty().not()));
//                currCellBoardButton.disableProperty().bind(isGameActive.not());
//                 cellBoardButtons[i][j].setDisable();
                GridPane.setHalignment(currCellBoardButton, javafx.geometry.HPos.CENTER);
                if (j != 0) {
                    GridPane.setColumnIndex(currCellBoardButton, j);
                }
                if (i != 0) {
                    GridPane.setRowIndex(currCellBoardButton, i);
                }

                currCellBoardButton.setFont(Font.font("System Bold"));
                currCellBoardButton.setMnemonicParsing(false);
                currCellBoardButton.setPrefHeight(5000.0);
                currCellBoardButton.setPrefWidth(5000.0);
                currCellBoardButton.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

                currCellBoardButton.setOnMouseClicked((event) -> {
                    informCellBoardButtonsClicked((CellBoardButton) event.getSource());
                });
            }

            gridPane.setMinSize(300,300); //new line
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                gridPane.getChildren().add(cellBoardButtons[i][j]);
            }
        }

        for (int i = 0; i < rowsCount; i++) {
            gridPane.getRowConstraints().add(rowConstraints);
        }
        for (int j = 0; j < columnsCount; j++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {

                currDisc = gameBoard.getDisc(i, j);
                currCellBoardButton = cellBoardButtons[i][j];
                currCellBoard = gameBoard.get(i, j);

                // boardController.discTypeToColor(currDisc.getType())

                if (currDisc != null) {
                    Circle circle = new Circle(50, 50, 40, boardController.discTypeToColor(currDisc.getType()));
                    currCellBoardButton.setGraphic(circle);
                    currCellBoardButton.setContentDisplay(ContentDisplay.CENTER);

                    circle.radiusProperty().bind(Bindings.min(currCellBoardButton.heightProperty().divide(4), currCellBoardButton.widthProperty().divide(4)));
                }
            }
        }
        //boardScrollPane.setContent(gridPane);
        //gridPane.setPrefHeight(1000);
        //gridPane.setPrefWidth(1000);
        gridPane.setPrefHeight(Math.min(Math.max(600,85*rowsCount),Math.min(screenBounds.getHeight()*0.9,screenBounds.getWidth()*0.65)) - 3); // used to be USE_PREF_ -> then used to be 600
        gridPane.setPrefWidth(Math.min(Math.max(600,85*rowsCount),Math.min(screenBounds.getHeight()*0.9,screenBounds.getWidth()*0.65)) - 3);
        setContent(gridPane);
        gridPane.autosize();
    }

    public void initBoardButtons(Board gameBoard){
        Disc currDisc;
        CellBoard currCellBoard;
        CellBoardButton currCellBoardButton;

        this.rowsCount = gameBoard.getHeight();
        this.columnsCount = gameBoard.getWidth();
        cellBoardButtons = new CellBoardButton[rowsCount][columnsCount];

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                cellBoardButtons[i][j] = new CellBoardButton(i, j);
                currCellBoardButton = cellBoardButtons[i][j];

                GridPane.setHalignment(currCellBoardButton, javafx.geometry.HPos.CENTER);
                if (j != 0) {
                    GridPane.setColumnIndex(currCellBoardButton, j);
                }
                if (i != 0) {
                    GridPane.setRowIndex(currCellBoardButton, i);
                }

                currCellBoardButton.setMnemonicParsing(false);
                currCellBoardButton.setMinSize(30,30); //new line
                currCellBoardButton.setPrefHeight(5000.0);
                currCellBoardButton.setPrefWidth(5000.0);
                currCellBoardButton.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

                currCellBoardButton.setOnMouseClicked((event) -> {
                    informCellBoardButtonsClicked((CellBoardButton) event.getSource());
                });

                //currCellBoardButton.prefHeightProperty().bind(currCellBoardButton.widthProperty()); // new line
                gridPane.setFillHeight(currCellBoardButton, true);
                gridPane.setFillWidth(currCellBoardButton, true);
            }
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                gridPane.getChildren().add(cellBoardButtons[i][j]);
            }
        }

        for (int i = 0; i < rowsCount; i++) {
            gridPane.getRowConstraints().add(rowConstraints);
        }
        for (int j = 0; j < columnsCount; j++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                currDisc = gameBoard.getDisc(i, j);
                currCellBoardButton = cellBoardButtons[i][j];
                currCellBoard = gameBoard.get(i, j);

                // boardController.discTypeToColor(currDisc.getType())

                if (currDisc != null) {
                    Circle circle = new Circle(50, 50, 40, boardController.discTypeToColor(currDisc.getType()));
                    currCellBoardButton.setGraphic(circle);
                    currCellBoardButton.setContentDisplay(ContentDisplay.CENTER);

                    circle.radiusProperty().bind(Bindings.min(currCellBoardButton.heightProperty().divide(4), currCellBoardButton.widthProperty().divide(4)));
                }
            }
        }
        setContent(gridPane);
        gridPane.autosize();
    }

    public void informCellBoardButtonsClicked(CellBoardButton clickedButton){
        boardController.CellBoardButtonClicked(new Point(clickedButton.getRow(), clickedButton.getColumn()));
    }

    public void highlightDiscs(Board gameBoard, eDiscType discType){
        Disc currDisc;
        CellBoardButton currButton;
        Circle currGUIDisc;

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                currButton = cellBoardButtons[i][j];
                currDisc = gameBoard.getDisc(i, j);
                currGUIDisc = (Circle)currButton.getGraphic();

                if(currDisc != null && currGUIDisc != null){
                    if(currDisc.getType() == discType){
//                        currGUIDisc.setEffect(new Glow(1.2));
                        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), currGUIDisc);
                        scaleTransition.setFromX(1.7);
                        scaleTransition.setToX(1);
                        scaleTransition.setFromY(1.7);
                        scaleTransition.setToY(1);
                        scaleTransition.setAutoReverse(true);
                        scaleTransition.setCycleCount(3);
//                        boardController.isScaleAnimationsInPlayProperty().setValue(true);
//                        boardController.disableReplayMode(true);
                        scaleTransitionList.add(scaleTransition);
                        scaleTransition.play();
                        scaleTransition.setOnFinished(event -> {
//                            boardController.disableReplayMode(false);
//                            boardController.isScaleAnimationsInPlayProperty().setValue(false);
                            if(scaleTransitionList.contains(event.getSource()))
                                scaleTransitionList.remove(event.getSource());
                        });
                    }
                }
            }
        }
    }



    public void updateBoard(Board gameBoard, boolean isTutorialMode, boolean isAnimationsEnabled) {
        boolean isFirstAnimation = true;
        Disc currDisc;
        CellBoard currCellBoard;
        CellBoardButton currButton;
        Circle currGUIDisc;
        Color oldColor, newColor;
        boolean prevButtonState = boardController.getAppController().getReplayModePrevButton().isDisabled();
        boolean nextButtonState = boardController.getAppController().getReplayModeNextButton().isDisabled();

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                currDisc = gameBoard.getDisc(i, j);
                currButton = cellBoardButtons[i][j];
                currCellBoard = gameBoard.get(i, j);

                if(boardController.getAppController().isShowBoardProperty().get()){
                    currButton.setOpacity(1);
                }
                else {
                    currButton.setOpacity(0.4);
                }

                // boardController.discTypeToColor(currDisc.getType())

                if (currDisc != null) {
                    if (currButton.getGraphic() == null) {
                        currGUIDisc = new Circle(50, 50, 40, boardController.discTypeToColor(currDisc.getType()));
                        currButton.setGraphic(currGUIDisc);
                        currButton.setContentDisplay(ContentDisplay.CENTER);
                        currGUIDisc.radiusProperty().bind(Bindings.min(currButton.heightProperty().divide(4), currButton.widthProperty().divide(4)));
                    }
                    else { // the circle already there, just changing it's color..
                        newColor = boardController.discTypeToColor(currDisc.getType());
                        currGUIDisc = (Circle) currButton.getGraphic();

                        if(isAnimationsEnabled) {
                            oldColor = (Color) currGUIDisc.getFill();
                            FillTransition ft = new FillTransition(Duration.millis(550), currGUIDisc, oldColor, newColor);
//                            ft.play();

                            if (boardController.getAppController().isInReplayMode()) {
                                boardController.getAppController().getReplayModePrevButton().setDisable(true);
                                boardController.getAppController().getReplayModeNextButton().setDisable(true);

                                if (isFirstAnimation) {
                                    ft.setOnFinished(event -> {
                                        boardController.getAppController().getReplayModePrevButton().setDisable(prevButtonState);
                                        boardController.getAppController().getReplayModeNextButton().setDisable(nextButtonState);
                                    });
                                }

                                isFirstAnimation = false;
                            }

                            ft.play();
                        }
                        else{
                            currGUIDisc.setFill(newColor);
                        }
                    }
                }
                else{
                    if(currButton.getGraphic() != null){ // need to remove the circle disc.
                        currButton.setGraphic(null);
                    }
                }

                if(isTutorialMode){
                    if(currCellBoard.getCountOfFlipsPotential() != 0){
                        currButton.setText(String.valueOf(currCellBoard.getCountOfFlipsPotential()));
                    }
                    else{
                        currButton.setText("");
                    }
                }
                else{
                    currButton.setText("");
                }
            }
        }
    }

    public void setRowsCount(int rows) {
        rowsCount = rows;
    }


    public void setColumnsCount(int columns) {
        columnsCount = columns;
    }

    public void setCellBoardButtons(CellBoardButton[][] _cellBoardButtons) {
        cellBoardButtons = _cellBoardButtons;
    }

    public void stopScaleAnimations() {
        for (ScaleTransition scaleTransition : scaleTransitionList) {
            scaleTransition.stop();
        }

        scaleTransitionList.clear();

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if(cellBoardButtons[i][j].getGraphic() != null) {
                    cellBoardButtons[i][j].getGraphic().setScaleX(1);
                    cellBoardButtons[i][j].getGraphic().setScaleY(1);
                }
            }
        }
    }
}
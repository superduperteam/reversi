package GUI;

import javafx.scene.control.Button;

public class CellBoardButton extends Button {
    private int row;
    private int column;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public  CellBoardButton(int row, int column){
        this.row = row;
        this.column = column;
    }
}

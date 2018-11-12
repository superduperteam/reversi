package GameEngine;

public class Point
{
    private int col;
    private int row;

    public int getCol()
    {
       return col;
    }

    public int getRow()
    {
        return row;
    }

    public Point(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return new String(String.valueOf(row + 1) +","+String.valueOf(col + 1));
    }
}

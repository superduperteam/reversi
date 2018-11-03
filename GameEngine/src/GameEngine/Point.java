package GameEngine;

public class Point
{
    private int col;
    private int row;

    public int GetCol()
    {
       return col;
    }

    public int GetRow()
    {
        return row;
    }

    public Point(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
}

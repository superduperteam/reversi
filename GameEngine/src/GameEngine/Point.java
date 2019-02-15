package GameEngine;

import java.io.Serializable;
import java.util.Objects;

public class Point implements Serializable
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return col == point.col &&
                row == point.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public String toString() {
        return new String(String.valueOf(row) +","+String.valueOf(col));
    }
}

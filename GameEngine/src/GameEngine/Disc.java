package GameEngine;

public class Disc
{
    private eDiscType type;

    public Disc(eDiscType type)
    {
        this.type = type;
    }

    public eDiscType GetType()
    {
        return type;
    }
}

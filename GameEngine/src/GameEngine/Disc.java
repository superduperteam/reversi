package GameEngine;

public class Disc
{
    private eDiscType type;

    public Disc(eDiscType type)
    {
        this.type = type;
    }

    public Disc(Disc toCopy){
        type = toCopy.type; //note(ido): i assume game mode won't change during the game.
                            // if it can change , I need to change the logic here.
    }

    public eDiscType GetType()
    {
        return type;
    }

    public void SetType(eDiscType newDiscTypeToSet)
    {
        this.type = newDiscTypeToSet;
    }
}

package GameEngine;

import java.io.Serializable;

public class Disc implements Serializable
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

    public eDiscType getType()
    {
        return type;
    }

    public void setType(eDiscType newDiscTypeToSet)
    {
        this.type = newDiscTypeToSet;
    }
}

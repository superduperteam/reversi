package GameEngine;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disc disc = (Disc) o;
        return type == disc.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
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

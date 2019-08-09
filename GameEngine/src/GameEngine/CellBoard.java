package GameEngine;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CellBoard {
    private int flipPotential = 0;
    @JsonProperty
    private Disc disc;
    private int countOfFlipsPotential;

    public int getCountOfFlipsPotential(){
        return countOfFlipsPotential;
    }

    public void setCountOfFlipsPotential(int countOfFlipsPotential){
        this.countOfFlipsPotential = countOfFlipsPotential;
    }

    public int getFlipPotential() {
        return flipPotential;
    }

    public void setFlipPotential(int flipPotential) {
        this.flipPotential = flipPotential;
    }

    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc discToBeSet) {
        this.disc = discToBeSet;
    }

    public CellBoard(CellBoard toCopy){
        if(toCopy.getDisc()!= null){
            this.disc = new Disc(toCopy.getDisc()); // ##
        }
        else{
            this.disc = null;
        }

        this.countOfFlipsPotential = toCopy.getCountOfFlipsPotential();
    }

    public CellBoard(Disc disc){
        this.disc = disc;
    }
}
package GameEngine;

public class CellBoard {
    private int flipPotential = 0;
    private Disc disc;

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
    }

    public CellBoard(Disc disc){
        this.disc = disc;
    }
}

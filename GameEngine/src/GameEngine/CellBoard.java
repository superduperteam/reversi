package GameEngine;

public class CellBoard {
    private Disc disc;


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
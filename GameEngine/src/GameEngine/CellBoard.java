package GameEngine;

public class CellBoard {
    private Disc disc;
    private int countOfFlipsPotential;

    public int getCountOfFlipsPotential(){
        return countOfFlipsPotential;
    }

    public void setCountOfFlipsPotential(int countOfFlipsPotential){
        this.countOfFlipsPotential = countOfFlipsPotential;
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
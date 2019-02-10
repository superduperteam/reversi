package game.json;

//import game.logic.variants.Variant;

import java.util.List;

public class GameOverJson {

    private boolean isGameOver;
    private boolean isTie;
    private List<String> winnersNames;

    public GameOverJson(boolean isGameOver,boolean isTie ,List<String> winnersNames) {
        this.isGameOver = isGameOver;
        this.isTie = isTie;
        this.winnersNames = winnersNames;
    }
}

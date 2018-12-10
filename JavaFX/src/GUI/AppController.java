package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import GameEngine.Point;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.plaf.PopupMenuUI;

public class AppController {

    private GameManager gameManager;

    private BoardController boardController;
    private boolean isTutorialMode = false;
    @FXML private StatsController statsComponentController;
    @FXML private CheckBox tutorialMode;

    public void func(){

    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @FXML
    public void initialize() {
        if (statsComponentController != null) {
            statsComponentController.setMainController(this);
        }

        tutorialMode.setOnMouseClicked((event) -> {
            if(tutorialMode.isSelected()){
                isTutorialMode = true;
            }
            else{
                isTutorialMode = false;
            }

            boardController.updateGIUDiscs(isTutorialMode);
        });
    }

    public void initTable() {
        statsComponentController.setPlayers(gameManager.getPlayersList());
    }

    public void playTurn(Point clickedCellBoardPoint){
        Player activePlayer = gameManager.getActivePlayer();
        GameManager.eMoveStatus moveStatus;

        moveStatus = activePlayer.makeMove(clickedCellBoardPoint, gameManager.getBoard());

        if(moveStatus == GameManager.eMoveStatus.OK){
            boardController.updateGIUDiscs(isTutorialMode);
            statsComponentController.refreshTable();
            gameManager.changeTurn();
        }

        if(gameManager.isGameOver()){
            onGameOver();
        }
    }

    public void onGameOver(){
        StringBuilder winMessageBuilder = new StringBuilder();

        winMessageBuilder.append("Game Over.\n");

        if(gameManager.getHighestScoringPlayers().size() == 1) {
            winMessageBuilder.append(gameManager.getHighestScoringPlayers().get(0).getName())
                    .append(" is the winner!");
        } else {
            winMessageBuilder.append("It's a tie!");
        }
        PopupFactory.showPopup(winMessageBuilder.toString());
    }
}

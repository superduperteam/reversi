package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import GameEngine.Point;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppController {
    private GameManager gameManager;
    private BoardController boardController;
    @FXML private StatsController statsComponentController;

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
    }

    public void initTable() {
        statsComponentController.setPlayers(gameManager.getPlayersList());
    }

    public void playTurn(Point clickedCellBoardPoint){
        Player activePlayer = gameManager.getActivePlayer();
        GameManager.eMoveStatus moveStatus;

        moveStatus = activePlayer.makeMove(clickedCellBoardPoint, gameManager.getBoard());

        if(moveStatus == GameManager.eMoveStatus.OK){
            boardController.updateGIUDiscs();
            statsComponentController.refreshTable();
            gameManager.changeTurn();
            //statsComponentController. need to highlight next player
        }
    }
}

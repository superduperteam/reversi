package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import GameEngine.Point;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class AppController {
    private GameManager gameManager;
    private BoardController boardController;
    private Stage primaryStage;
    @FXML private StatsController statsComponentController;

    public void setPrimaryStage(Stage _primaryStage) {primaryStage = _primaryStage;}
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

        String winnerPlayerName;
        if(gameManager.isGameOver()){
            if(gameManager.getHighestScoringPlayers().size() == 1) {
                winnerPlayerName = gameManager.getHighestScoringPlayers().get(0).getName();
            } else {//it's a tie.
                winnerPlayerName = "";
            }
            onGameOver(winnerPlayerName);
        }
    }

    public void onGameOver(String winnerPlayerNAme){
        StringBuilder winMessageBuilder = new StringBuilder();

        winMessageBuilder.append("Game Over!\n");
        if(!winnerPlayerNAme.equals("")) {
            winMessageBuilder.append(winnerPlayerNAme)
                    .append(" Is the winner!");
        } else {
            winMessageBuilder.append("It's a tie!");
        }

        PopupFactory.showPopup(winMessageBuilder.toString());
    }
}

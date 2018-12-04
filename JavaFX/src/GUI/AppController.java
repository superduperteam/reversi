package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppController {
    GameManager gameManager;
    @FXML private StatsController statsComponentController;


    @FXML
    public void initialize() {
        if (statsComponentController != null) {
            statsComponentController.setMainController(this);
        }
    }

    public void initTable() {
        statsComponentController.setPlayers(gameManager.getPlayersList());
    }
}

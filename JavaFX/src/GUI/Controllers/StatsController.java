package GUI.Controllers;

import GameEngine.GameManager;
import GameEngine.Player;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class StatsController {
    private AppController appController;

    @FXML private TableView<Player> tableView;
    @FXML private TableColumn<Player,String> turnColumn;
    @FXML private TableColumn<Player,String> colorColumn;
    @FXML private TableColumn<Player,String> isHumanColumn;
    @FXML private TableColumn<Player,String> nameColumn;
    @FXML private TableColumn<Player,String> idColumn;
    @FXML private TableColumn<Player,Integer> scoreColumn;
    @FXML private TableColumn<Player,Integer> turnsPlayedColumn;
    @FXML private TableColumn<Player,String> averageOfFlipsColumn;

    @FXML public void initialize() {
        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(30));

    }

    public void setMainController(AppController mainController) {
        this.appController = mainController;
    }

    public void setPlayers(List<Player> playersList, Player activePlayer){
        turnColumn.setCellValueFactory(data -> {
            GameManager gameManager = appController.getGameManager();
            if(data.getValue().equals(activePlayer)&&(!gameManager.isGameOver()||appController.isInReplayMode())){
                return new ReadOnlyStringWrapper("->");
            }
            else{
                return new ReadOnlyStringWrapper("");
            }
        });

        isHumanColumn.setCellValueFactory(data -> {
            if(data.getValue().isHuman()){
                return new ReadOnlyStringWrapper("Human");
            }
            else{
                return new ReadOnlyStringWrapper("Computer");
            }
        });

        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("score"));
        turnsPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("turnsPlayed"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        //averageOfFlipsColumn.setCellValueFactory(new PropertyValueFactory<>("averageOfFlips"));

        averageOfFlipsColumn.setCellValueFactory(data -> {
            double num = data.getValue().getAverageOfFlips()*100;
            int roundedNum = (int)num;
            num = (double) roundedNum;
            num/=100;
            return new ReadOnlyStringWrapper(String.valueOf(num));
        });

        tableView.getItems().setAll(playersList);
        tableView.autosize();

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(27));
    }

    public void refreshTable(List<Player> playersList, Player activePlayer){
        setPlayers(playersList, activePlayer);
        tableView.refresh();
    }
}

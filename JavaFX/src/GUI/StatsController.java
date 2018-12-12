package GUI;

import GameEngine.GameManager;
import GameEngine.Player;
import GameEngine.eDiscType;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.math.BigInteger;
import java.net.URL;
import java.util.*;

public class StatsController {
    private AppController appController;
    private HashMap<Player, TableRow<Player>> playerStatsToRowNumber = new HashMap();

    @FXML private TableView<Player> tableView;
    @FXML private TableColumn<Player,String> turnColumn;
    @FXML private TableColumn<Player,String> colorColumn;
    @FXML private TableColumn<Player,String> nameColumn;
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

    public void setPlayers(List<Player> playersList){
        turnColumn.setCellValueFactory(data -> {
            GameManager gameManager = appController.getGameManager();
            if(data.getValue().equals(gameManager.getActivePlayer())&&!gameManager.isGameOver()){
                return new ReadOnlyStringWrapper("->");
            }
            else{
                return new ReadOnlyStringWrapper("");
            }
        });

        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("score"));
        turnsPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("turnsPlayed"));
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
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(26));
    }

    public void refreshTable(){
        setPlayers(appController.getGameManager().getPlayersList());
        tableView.refresh();
    }
}

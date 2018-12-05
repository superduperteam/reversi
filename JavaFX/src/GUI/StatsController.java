package GUI;

import GameEngine.Player;
import GameEngine.eDiscType;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class StatsController {
    private AppController mainController;

    @FXML private TableView<Player> tableView;

    @FXML TableColumn<Player,String> colorColumn;
    @FXML TableColumn<Player,String> nameColumn;
    @FXML TableColumn<Player,Integer> scoreColumn;
    @FXML TableColumn<Player,Integer> turnsPlayedColumn;
    @FXML TableColumn<Player,Integer> averageOfFlipsColumn;
    @FXML

    public void initialize() {
//        List<GameEngine.Player> playersList = new ArrayList<>();
//        playersList.add(new Player("Saar", true, eDiscType.WHITE, new BigInteger("1")));
//        ObservableList<Player> playersData = FXCollections.observableArrayList(playersList);

        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("score"));
        turnsPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("turnsPlayed"));
        averageOfFlipsColumn.setCellValueFactory(new PropertyValueFactory<>("averageOfFlips"));

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(30));

        //tableView.getColumns().addAll(colorColumn, nameColumn, scoreColumn, turnsPlayedColumn, averageOfFlipsColumn);

        //table.setItems(playersData);
//        tableView.getItems().setAll(playersList);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setPlayers(List<Player> playersList){
        tableView.getItems().setAll(playersList);
        tableView.autosize();

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(26));
    }
}

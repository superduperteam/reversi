package GUI;

import GameEngine.GameManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

//import static examples.advance.nested.subcomponents.app.CommonResourcesPaths.APP_FXML_INCLUDE_RESOURCE;
//import static examples.advance.nested.subcomponents.app.CommonResourcesPaths.APP_FXML_LIGHT_RESOURCE;

public class Main extends Application {
    GameManager gameManager;

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BoardGUI boardGUI;
        //getGameDetails(); // extract GameManager from xml
        //tableView = createStatsComponent(gameManager.getPlayersList());



        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("app.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        BorderPane borderPane = (BorderPane) root.getContent();
        AppController appController = fxmlLoader.getController();
        appController.setBoardParent(borderPane);
        appController.setPrimaryStage(primaryStage);
        ////appController.setGameManager(gameManager);
        //appController.initTable();
        //root.setTop(tableView);
        //boardGUI = new BoardGUI(null,appController);

        ////borderPane.setCenter(boardGUI);
        ////BorderPane.setAlignment(boardGUI, javafx.geometry.Pos.TOP_CENTER);

//        BorderPane.setAlignment(boardComponent, javafx.geometry.Pos.TOP_CENTER);
//        borderPane.setCenter(boardComponent);
//        setContent(borderPane);


//                 <center>
//              <fx:include fx:id="boardComponent" minHeight="-Infinity" minWidth="100.0" source="board.fxml" BorderPane.alignment="TOP_CENTER" />
//         </center>
        ////appController.initTable();
        Scene scene = new Scene(root, 1090, 850);
        scene.getStylesheets().addAll("/resources/caspian.css");
        //scene.getStylesheets().set(2, "resources/caspian.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    private TableView<Player> createStatsComponent(List<Player> playersList){
//        TableView<Player> table = new TableView<>();
//        ArrayList<Player> arrayList = (ArrayList<Player>) playersList;
//        ObservableList<Player> playersData = FXCollections.observableArrayList(arrayList);
//        table.setEditable(true);
//
//        TableColumn<Player,String> color = new TableColumn<>("Color");
//        TableColumn<Player,String> name = new TableColumn<>("Name");
//        TableColumn<Player,Integer> score = new TableColumn<>("Score"); // ##
//        TableColumn<Player,Integer> turnsPlayed = new TableColumn<>("Turns Played");
//        TableColumn<Player,Integer> averageOfFlips = new TableColumn<>("Average Of Flips");
//
//        color.setCellValueFactory(new PropertyValueFactory<>("color"));
//        name.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
//        score.setCellValueFactory(new PropertyValueFactory<Player, Integer>("score"));
//        turnsPlayed.setCellValueFactory(new PropertyValueFactory<>("turnsPlayed"));
//        averageOfFlips.setCellValueFactory(new PropertyValueFactory<>("averageOfFlips"));
//
//       table.getColumns().addAll(color, name, score, turnsPlayed, averageOfFlips);
//
//        table.setItems(playersData);
//        table.prefHeight(358);
//        table.prefWidth(456);
//
//        return table;
//    }
}

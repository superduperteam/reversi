package GUI;

import GUI.Controllers.AppController;
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
        URL url = getClass().getResource("FXML/app.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        //root.setMinSize(500,400);
        //fdssd
        BorderPane borderPane = (BorderPane) root.getContent();
        //borderPane.setMinSize(500,500);

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

}

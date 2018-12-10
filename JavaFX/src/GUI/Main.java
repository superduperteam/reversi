package GUI;

import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
import GameEngine.Player;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        getGameDetails(); // extract GameManager from xml
        //tableView = createStatsComponent(gameManager.getPlayersList());



        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("app.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        BorderPane borderPane = (BorderPane) root.getContent();
        AppController appController = fxmlLoader.getController();
        appController.gameManager = this.gameManager;
        //appController.initTable();
        //root.setTop(tableView);
        appController.initTable();
        boardGUI = new BoardGUI(gameManager.getBoard(),appController);

        borderPane.setCenter(boardGUI);
        BorderPane.setAlignment(boardGUI, javafx.geometry.Pos.TOP_CENTER);

//        BorderPane.setAlignment(boardComponent, javafx.geometry.Pos.TOP_CENTER);
//        borderPane.setCenter(boardComponent);
//        setContent(borderPane);


//                 <center>
//              <fx:include fx:id="boardComponent" minHeight="-Infinity" minWidth="100.0" source="board.fxml" BorderPane.alignment="TOP_CENTER" />
//         </center>

        Scene scene = new Scene(root, 1050, 800);
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

    private void getGameDetails()
    {
        while(!loadXML());
        gameManager.activateGame();
    }

    private boolean loadXML()
    {
        GameManager currGameManager;
        GameSettingsReader gameSettingsReader = new GameSettingsReader();
        boolean isGameLoaded = true;
        System.out.println("Please enter a XML path (it should end with \".xml\")");
        //Path filePath = getFilePathFromUser();
        Path filePath = Paths.get("C:\\Users\\Saar\\IdeaProjects\\reversi\\GameEngine\\src\\resources\\master.xml");

        if(filePath != null) {
            try {
                currGameManager = gameSettingsReader.readGameSettings(filePath);
                if(currGameManager != null)
                {
                    gameManager = currGameManager;
                }
            } catch (FileIsNotXML fileIsNotXML){
                System.out.println("Error: " + fileIsNotXML);
                isGameLoaded = false;
            } catch (NoXMLFileException noXMLFile) {
                //noXMLFile.printStackTrace();
                System.out.println("Error: " + noXMLFile);
                isGameLoaded = false;
            } catch (OutOfRangeNumberOfPlayersException playersInitPositionsOutOfRangeException) {
                System.out.println("Error: " + playersInitPositionsOutOfRangeException);
                isGameLoaded = false;
            } catch (TooManyInitialPositionsException tooManyInitialPositionsException) {
                System.out.println("Error: " + tooManyInitialPositionsException);
                isGameLoaded = false;
            } catch (ThereAreAtLeastTwoPlayersWithSameID thereAreAtLeastTwoPlayersWithSameID) {
                System.out.println("Error: " + thereAreAtLeastTwoPlayersWithSameID);
                isGameLoaded = false;
            }
            catch (PlayerHasNoInitialPositionsException playerHasNoInitialPositionsException) {
                System.out.println("Error: " + playerHasNoInitialPositionsException);
                isGameLoaded = false;
            } catch (PlayersInitPositionsOverrideEachOtherException playersInitPositionsOverrideEachOtherException) {
                //playersInitPositionsOverrideEachOtherException.printStackTrace();
                System.out.println("Error: " + playersInitPositionsOverrideEachOtherException);
                isGameLoaded = false;
            } catch (BoardSizeDoesntMatchNumOfPlayersException boardSizeDoesntMatchNumOfPlayersException) {
                //boardSizeDoesntMatchNumOfPlayersException.printStackTrace();
                System.out.println("Error: " + boardSizeDoesntMatchNumOfPlayersException);
                isGameLoaded = false;
            } catch (PlayersInitPositionsOutOfRangeException playersInitPositionsOutOfRangeException) {
                //playersInitPositionsOutOfRangeException.printStackTrace();
                System.out.println("Error: " + playersInitPositionsOutOfRangeException);
                isGameLoaded = false;
            } catch (RowsNotInRangeException rowsNotInRangeException) {
                //rowsNotInRangeException.printStackTrace();
                System.out.println("Error: " + rowsNotInRangeException);
                isGameLoaded = false;
            } catch (ColumnsNotInRangeException columnsNotInRangeException) {
                //columnsNotInRangeException.printStackTrace();
                System.out.println("Error: " + columnsNotInRangeException);
                isGameLoaded = false;
            } catch (IslandsOnRegularModeException islandsOnRegularModeException) {
                //islandsOnRegularModeException.printStackTrace();
                System.out.println("Error: " + islandsOnRegularModeException);
                isGameLoaded = false;
            }
        }
        else {
            isGameLoaded  = false;
        }
        return isGameLoaded;
    }

    private Path getFilePathFromUser()
    {
        Scanner reader = new Scanner(System.in);
        String filePathString;
        Path filePath;

        filePathString = reader.nextLine();

        try {
            filePath = Paths.get(filePathString);
        }
        catch (InvalidPathException e) {
            System.out.println("Error- illegal path entered. Please try again.");
            return null;
        }
        return filePath;
    }
}

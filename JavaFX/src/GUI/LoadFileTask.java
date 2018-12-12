package GUI;

import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class LoadFileTask extends Task<Boolean> {

    private String fileName;
    private Consumer<Runnable> onCancel;
    private Stage primaryStage;
    private GameManager gameManager;

    public GameManager getGameManager() {
        return gameManager;
    }

    public LoadFileTask(Stage _primaryStage){
        this.primaryStage = _primaryStage;
    }

    @Override
    protected Boolean call() {
        FileChooser fileChooser = new FileChooser();
        boolean didLoadSuccessfully;

        fileChooser.setTitle("Select XML game file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Documents", "*.XML"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return Boolean.FALSE;
        }

        updateMessage("Successfully fetched file");
        String absolutePath = selectedFile.getAbsolutePath();
        updateMessage("Loading XML file...");
        didLoadSuccessfully = loadXML(absolutePath);
        if(didLoadSuccessfully){
            updateMessage("Successfully loaded XML file!");
        }

        return Boolean.TRUE;
    }

    public boolean loadXML(String filePath){
        GameManager currGameManager;
        GameSettingsReader gameSettingsReader = new GameSettingsReader();
        boolean isGameLoaded = true;
        //System.out.println("Please enter a XML path (it should end with \".xml\")");
        //Path filePath = getFilePathFromUser();
        //Path filePath = Paths.get("C:\\Users\\Ido\\IdeaProjects\\reversi\\GameEngine\\src\\resources\\master.xml");

        if(filePath != null) {
            try {
                currGameManager = gameSettingsReader.readGameSettings(Paths.get(filePath));
                if(currGameManager != null)
                {
                    gameManager = currGameManager;
                }
            } catch (FileIsNotXML fileIsNotXML){
                updateMessage("Error: " + fileIsNotXML);
                isGameLoaded = false;
            } catch (NoXMLFileException noXMLFile) {
                //noXMLFile.printStackTrace();
                updateMessage("Error: " + noXMLFile);
                isGameLoaded = false;
            } catch (OutOfRangeNumberOfPlayersException playersInitPositionsOutOfRangeException) {
                updateMessage("Error: " + playersInitPositionsOutOfRangeException);
                isGameLoaded = false;
            } catch (TooManyInitialPositionsException tooManyInitialPositionsException) {
                updateMessage("Error: " + tooManyInitialPositionsException);
                isGameLoaded = false;
            } catch (ThereAreAtLeastTwoPlayersWithSameID thereAreAtLeastTwoPlayersWithSameID) {
                updateMessage("Error: " + thereAreAtLeastTwoPlayersWithSameID);
                isGameLoaded = false;
            }
            catch (PlayerHasNoInitialPositionsException playerHasNoInitialPositionsException) {
                updateMessage("Error: " + playerHasNoInitialPositionsException);
                isGameLoaded = false;
            } catch (PlayersInitPositionsOverrideEachOtherException playersInitPositionsOverrideEachOtherException) {
                //playersInitPositionsOverrideEachOtherException.printStackTrace();
                updateMessage("Error: " + playersInitPositionsOverrideEachOtherException);
                isGameLoaded = false;
            } catch (BoardSizeDoesntMatchNumOfPlayersException boardSizeDoesntMatchNumOfPlayersException) {
                //boardSizeDoesntMatchNumOfPlayersException.printStackTrace();
                updateMessage("Error: " + boardSizeDoesntMatchNumOfPlayersException);
                isGameLoaded = false;
            } catch (PlayersInitPositionsOutOfRangeException playersInitPositionsOutOfRangeException) {
                //playersInitPositionsOutOfRangeException.printStackTrace();
                updateMessage("Error: " + playersInitPositionsOutOfRangeException);
                isGameLoaded = false;
            } catch (RowsNotInRangeException rowsNotInRangeException) {
                //rowsNotInRangeException.printStackTrace();
                updateMessage("Error: " + rowsNotInRangeException);
                isGameLoaded = false;
            } catch (ColumnsNotInRangeException columnsNotInRangeException) {
                //columnsNotInRangeException.printStackTrace();
                updateMessage("Error: " + columnsNotInRangeException);
                isGameLoaded = false;
            } catch (IslandsOnRegularModeException islandsOnRegularModeException) {
                //islandsOnRegularModeException.printStackTrace();
                updateMessage("Error: " + islandsOnRegularModeException);
                isGameLoaded = false;
            }
        }
        else {
            isGameLoaded  = false;
        }
        return isGameLoaded;
    }
}

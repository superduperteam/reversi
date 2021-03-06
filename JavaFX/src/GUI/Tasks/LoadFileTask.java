package GUI.Tasks;

import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class LoadFileTask extends Task<Boolean> {
    private boolean isChooseFile;
    private static final int SLEEP_TIME = 500;
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
        StringBuilder messageBuilder = new StringBuilder();

        fileChooser.setTitle("Select XML game file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Documents", "*.XML"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            isChooseFile = false;
            return Boolean.FALSE;
        } else {
            isChooseFile = true;
        }

        messageBuilder.append("Successfully fetched file\n");
        updateMessage(messageBuilder.toString());
        String absolutePath = selectedFile.getAbsolutePath();

        //try{Thread.sleep(SLEEP_TIME);} catch (InterruptedException e) { e.printStackTrace(); }


        messageBuilder.append("Loading XML file...\n");
        updateMessage(messageBuilder.toString());

        // try{Thread.sleep(SLEEP_TIME);} catch (InterruptedException e) { e.printStackTrace(); }

        didLoadSuccessfully = loadXML(absolutePath);
        if (didLoadSuccessfully) {

            messageBuilder.append("Successfully loaded XML file!\n");
            updateMessage(messageBuilder.toString());
        }

        // simulateProgress();
        updateProgress(10, 10);
//        return Boolean.TRUE;
        return didLoadSuccessfully;
    }

    private void simulateProgress(){
        for (int i = 0; i < 100; i++) {
            updateProgress(i + 1, 100);
            // sleep is used to simulate doing some work which takes some time....
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean loadXML(String filePath){
        GameManager currGameManager;
        GameSettingsReader gameSettingsReader = new GameSettingsReader();
        boolean isGameLoaded = true;

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
            }
                catch (InvalidNumberOfPlayersException invalidNumberOfPlayers) {
                    //noXMLFile.printStackTrace();
                    updateMessage("Error: " + invalidNumberOfPlayers);
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

    public boolean IsChooseFile() {
        return isChooseFile;
    }
}

package Exceptions;

import GameEngine.GameSettingsReader;

public class RowsNotInRangeException extends Exception  {

    @Override
    public String toString() {
        return "Rows not in range [" + GameSettingsReader.getMIN_ROWS() +","+GameSettingsReader.getMAX_ROWS()+"]";
    }
}

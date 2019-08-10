package Exceptions;

import GameEngine.GameSettingsReader;
public class ColumnsNotInRangeException extends Exception  {
    @Override
    public String toString() { return "Columns not in range [" + GameSettingsReader.getMIN_COLS() +","+GameSettingsReader.getMAX_COLS()+"]"; }
}

package GameEngine;


import com.sun.org.glassfish.external.statistics.Statistic;

import java.util.*;

public class GameManager
{
    private eGameMode gameMode;
    private HashMap<eDiscType, Player> discTypeToPlayer;
    private TurnHistory turnHistory;
    private List<Player> playersList;
    private ListIterator<Player> playersIterator;
    private Player activePlayer;
    private Board board;

    public GameManager(eGameMode gameMode, List<Player> playersList, Board board)
    {
        this.playersList = new ArrayList<>(playersList);
        playersIterator = playersList.listIterator(0);
        mapDiscTypesToPlayers();
        this.gameMode = gameMode;
        activePlayer = playersList.get(0);
        this.board = board;
    }

    public boolean IsGameOVer()
    {
        return true;
    }

    public Board GetBoard()
    {
        return board;
    }
    
    public void ChangeTurn()
    {
        if(playersIterator.hasNext())
        {
            playersIterator.next();
        }
        else // Iterator is pointing to the tail of the list.
        {
            playersIterator = playersList.listIterator(0);
        }

        // call updategamescore here
    }

    public Player GetActivePlayer()
    {
        return activePlayer;
    }

    private void mapDiscTypesToPlayers()
    {
        eDiscType discs[];
        int currentDiscIndex = 0;

        discTypeToPlayer = new HashMap<>();
        discs = eDiscType.values();

        for(Player player : playersList)
        {
            discTypeToPlayer.put(discs[currentDiscIndex], player);
          //  player.SetDiscType(discs[currentDiscIndex]);  Commented because player's disc type is given in Player c'tor
            currentDiscIndex++;
        }
    }

    public void UpdateGameScore()
    {
        int length = board.getGameBoardLength();
        Disc currDisc;

        for(Player player: playersList) {
            Player.Statistics  statistics = player.getStatistics();

            statistics.resetScore();
        }

        for(int i = 0; i < length; ++i) {
            for(int j = 0; j < length; ++j){
                currDisc = board.Get(i,j);
                Player player = discTypeToPlayer.get(currDisc.GetType());

                player.getStatistics().incScore();
            }
        }
    }

    private class TurnHistory
    {
        Stack<Turn> turnHistoryStack;

        private class Turn
        {
            List<Player> playersList;
            Player activePlayer;
            Board board;
        }
    }

    public enum eGameMode
    {
        Regular, Islands
    }
}

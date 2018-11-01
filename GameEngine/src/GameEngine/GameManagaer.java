package GameEngine;

import java.util.*;

public class GameManagaer
{
    private eGameMode gameMode;
    private HashMap<eDiscType, Player> discTypeToPlayer;
    private TurnHistory turnHistory;
    private List<Player> playersList;
    private ListIterator<Player> playersIterator;
    private Player activePlayer;

    public GameManagaer(eGameMode gameMode, List<Player> playersList)
    {
        this.playersList = new ArrayList<Player>(playersList);
        playersIterator = playersList.listIterator(0);
        mapDiscTypesToPlayers();
        this.gameMode = gameMode;
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

    public Player getActivePlayer()
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

package GameEngine;


import com.sun.org.glassfish.external.statistics.Statistic;

import java.util.*;

public class GameManager
{
    private eGameMode gameMode;
    private HashMap<eDiscType, Player> discTypeToPlayer;
    private TurnHistory turnHistory;
    private List<Player> playersList;
//    private ListIterator<Player> playersIterator; // not working :(
    private  int activePlayerIndex;
    private Player activePlayer;
    private Board board;

    public GameManager(eGameMode gameMode, List<Player> playersList, Board board)
    {
        turnHistory = new TurnHistory();
        this.playersList = new ArrayList<>(playersList);
//        playersIterator = playersList.listIterator(); // not working :(
        activePlayerIndex = 0;
        mapDiscTypesToPlayers();
        this.gameMode = gameMode;
        activePlayer = playersList.get(0);
        this.board = board;
    }

    public boolean isGameOver(){
        return !board.areThereAnyMovesForPlayers(playersList);
    }

    public Board GetBoard()
    {
        return board;
    }

    // Not working :(
//    public void ChangeTurn()
//    {
//        if(playersIterator.hasNext())
//        {
//            activePlayer = playersIterator.next();
//        }
//        else // Iterator is pointing to the tail of the list.
//        {
//            playersIterator = playersList.listIterator();
//            activePlayer = playersIterator.;
//        }
//
//        UpdateGameScore();
//    }

    public void ChangeTurn()
    {
        int nextTurnIndex;

        activePlayerIndex++;
        nextTurnIndex = activePlayerIndex%(playersList.size());
        activePlayer = playersList.get(nextTurnIndex);

        UpdateGameScore();
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
        int height = board.GetHeight(), width = board.GetWidth();
        Disc currDisc;
        Player playerToAddScoreTo;

        for(Player player: playersList) {
            Player.Statistics  statistics = player.getStatistics();

            statistics.resetScore();
        }

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j){
                currDisc = board.Get(i,j);

                if(currDisc != null)
                {
                    playerToAddScoreTo = discTypeToPlayer.get(currDisc.GetType());
                    playerToAddScoreTo.getStatistics().incScore();
                }
            }
        }
    }

    private static class TurnHistory
    {
        private Stack<Turn> turnHistoryStack;

        private static class Turn
        {
            private List<Player> playersList;
            private Player activePlayer;
            private Board board;
            private HashMap<eDiscType, Player> discTypeToPlayer;

            //the method clones the last turn using copy constructors.
            public Turn(Board board, Player activePlayer, List<Player> players) {
                playersList = new LinkedList<>();
                this.board = new Board(board);
                this.discTypeToPlayer = new HashMap<>();

                for(Player player: players) {
                    Player copiedPlayer = new Player(player);

                    if(activePlayer.equals(player)){
                        activePlayer = copiedPlayer;
                    }

                    playersList.add(copiedPlayer);
                    this.discTypeToPlayer.put(copiedPlayer.getDiscType(), copiedPlayer);
                }
            }
        }

        public void addHistoryEntry(Turn turn) {
            turnHistoryStack.push(turn);
        }

        //is there are no turns in the stack: returns null
        public Turn getLastTurn() {
            if(turnHistoryStack.isEmpty()) {
                return null;
            }

            return turnHistoryStack.pop();
        }
    }

    public void addTurnToHistory(){
        TurnHistory.Turn turnToAdd = new TurnHistory.Turn(board, activePlayer, playersList);

        turnHistory.addHistoryEntry(turnToAdd);
    }

    //if undo failed (there are no turns to undo): returns false
    public boolean undo(){
        boolean didUndoFailed = false;
        TurnHistory.Turn lastTurn = turnHistory.getLastTurn();

        if(lastTurn == null) {
            didUndoFailed = true;
        }
        else {
            gameMode = lastTurn.board.getGameMode();
            discTypeToPlayer = lastTurn.discTypeToPlayer;
            playersList = lastTurn.playersList;
            activePlayer = lastTurn.activePlayer;
            activePlayerIndex = playersList.indexOf(activePlayer);
            board = lastTurn.board;
        }

        return didUndoFailed;
    }

    public enum eGameMode
    {
        Regular, Islands
    }
}

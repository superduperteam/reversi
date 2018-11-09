package GameEngine;

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
    private TurnHistory.Turn currTurn;

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

        currTurn = getCurrentTurn(); // ##
    }

    public boolean isGameOver(){
        return !board.areThereAnyMovesForPlayers(playersList);
    }

    public Board getBoard()
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

    public void changeTurn()
    {
        addTurnToHistory(currTurn);

        activePlayerIndex = (activePlayerIndex + 1)%(playersList.size());
        activePlayer = playersList.get(activePlayerIndex);

        updateGameScore();

        currTurn = getCurrentTurn(); // ##
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

    public void updateGameScore()
    {
        int height = board.getHeight(), width = board.getWidth();
        Disc currDisc;
        Player playerToAddScoreTo;

        for(Player player: playersList) {
            Player.Statistics  statistics = player.getStatistics();

            statistics.resetScore();
        }

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j){
                currDisc = board.get(i,j);

                if(currDisc != null)
                {
                    playerToAddScoreTo = discTypeToPlayer.get(currDisc.getType());
                    playerToAddScoreTo.getStatistics().incScore();
                }
            }
        }
    }

    public static class TurnHistory
    {
        private Stack<Turn> turnHistoryStack;

        public TurnHistory(){
            turnHistoryStack = new Stack<>();
        }

        public static class Turn
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
                        this.activePlayer = copiedPlayer;
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

    public TurnHistory.Turn getCurrentTurn() {
        TurnHistory.Turn turn = new TurnHistory.Turn(board, activePlayer, playersList);

        return turn;
    }


    public List<Board> getHistoryOfBoardStates()
    {
        List<TurnHistory.Turn> turnsList = new ArrayList<>(turnHistory.turnHistoryStack);
        List<Board> boardsList = new ArrayList<>();

        for(TurnHistory.Turn turn : turnsList)
        {
            boardsList.add(turn.board);
        }

        boardsList.add(board);

        return boardsList;
    }

    public void addTurnToHistory(TurnHistory.Turn turnToAdd){

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

            currTurn = getCurrentTurn();
        }

        return didUndoFailed;
    }

    public enum eGameMode
    {
        Regular, Islands
    }
}

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
//        currTurn = getCurrentTurn(); // ##
    }


    public List<Player> getPlayersList()
    {
        return playersList;
    }

    public eGameMode getGameMode() {
        return gameMode;
    }

    public Board getInitialBoard()
    {
        int boardHeight = board.getHeight();
        int boardWidth = board.getWidth();
        HashMap<Player, List<Point>> initialDiscPositionsOfPlayer = board.getInitialDiscPositionOfPlayers();
        return new Board(boardHeight, boardWidth, initialDiscPositionsOfPlayer, gameMode);
    }

    // Call this only after game is over.
    public Player getWinner()
    {
        int maxPlayerPoints = playersList.get(0).getScore();
        Player winner = playersList.get(0);

        for(Player player : playersList)
        {
            if(maxPlayerPoints < player.getScore())
            {
                maxPlayerPoints = player.getScore();
                winner = player;
            }
        }

        return winner;
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

    public void resetGame()
    {
        List<TurnHistory.Turn> turnsList = new ArrayList<>(turnHistory.turnHistoryStack);
        goBackToTurn(turnsList.get(0));
        turnHistory.turnHistoryStack.clear();
    }

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

    private void addTurnToHistory(TurnHistory.Turn turnToAdd){

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
            goBackToTurn(lastTurn);
        }

        return didUndoFailed;
    }

    private void goBackToTurn(TurnHistory.Turn turnToChangeTo)
    {
        gameMode = turnToChangeTo.board.getGameMode();
        discTypeToPlayer = turnToChangeTo.discTypeToPlayer;
        playersList = turnToChangeTo.playersList;
        activePlayer = turnToChangeTo.activePlayer;
        activePlayerIndex = playersList.indexOf(activePlayer);
        board = turnToChangeTo.board;

        currTurn = getCurrentTurn();
    }

    public enum eGameMode
    {
        Regular, Islands
    }

    public enum eMoveStatus
    {
        OK,
        CELL_IS_ALREADY_TAKEN {            @Override
        public String toString() {
            return new String("Cell is already taken!");
        }},
        ILLEGAL_ISLAND {
            public String toString() {
            return new String("The new disc should be adjacent to other discs on board!");
        }},
        POINT_IS_NOT_IN_RANGE_OF_BOARD{
            public String toString() {
            return new String("The coordinates are not in the board's range!");
        }}
    }

    // call this only after all info about players is gathered.
    public void activateGame()
    {
        currTurn = getCurrentTurn(); // ##
    }
}

package GameEngine;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;
import java.util.*;

public class GameManager implements Serializable
{
    private SimpleBooleanProperty canUndoProperty = new SimpleBooleanProperty();
    private SimpleBooleanProperty isGameActive;

    private eGameMode gameMode;
    private HashMap<eDiscType, Player> discTypeToPlayer;
    private TurnHistory turnHistory;
    private List<Player> playersList;
    private int activePlayerIndex;
    private Player activePlayer;
    private Board board;
    private TurnHistory.Turn currTurn;
    private int totalNumOfPlayers;

    public GameManager(eGameMode gameMode, Board board, int totalNumOfPlayers)
    {
        turnHistory = new TurnHistory();
        this.playersList = new ArrayList<>();
        this.gameMode = gameMode;
        this.board = board;
        this.totalNumOfPlayers = totalNumOfPlayers;
    }

    public void calcFlipPotential(){
        Point currPoint;
        int flipPotential;

        for(int row = 0; row < board.getHeight(); ++row){
            for(int col = 0; col < board.getWidth(); ++col){
                currPoint = new Point(row, col);
                if(board.getDisc(row, col) != null){
                    flipPotential = 0;
                }
                else {
                    flipPotential = board.checkFlipPotential(currPoint, getActivePlayer().getDiscType());
                }

                board.get(row, col).setCountOfFlipsPotential(flipPotential);
            }
        }
    }

    public boolean isGameActive() {
        return isGameActive.get();
    }

    public void setIsGameActive(boolean _isGameActive){
        isGameActive.set(_isGameActive);
    }

    public List<Player> getPlayersList()
    {
        return playersList;
    }

    public eGameMode getGameMode() {
        return gameMode;
    }

    private void setActivePlayerToBeNextPlayer()
    {
        activePlayerIndex = (activePlayerIndex + 1)%(playersList.size());
        activePlayer = playersList.get(activePlayerIndex);
    }

    public Board getInitialBoard()
    {
        int boardHeight = board.getHeight();
        int boardWidth = board.getWidth();
        HashMap<Player, List<Point>> initialDiscPositionsOfPlayer = board.getInitialDiscPositionOfPlayers();
        return new Board(boardHeight, boardWidth, initialDiscPositionsOfPlayer, gameMode);
    }

    // Call this only after game is over.
    // should be test again when more than 2 players are allowed in a game.
    public List<Player> getHighestScoringPlayers()
    {
        List<Player> highestScoringPlayers = new ArrayList<>();
        int maxPlayerPoints = playersList.get(0).getScore();
        highestScoringPlayers.add(playersList.get(0));

        for(Player player : playersList)
        {
            if(player.getScore() > maxPlayerPoints)
            {
                maxPlayerPoints = player.getScore();
                highestScoringPlayers.clear(); // everybody that is in the list is not relevant anymore..
                highestScoringPlayers.add(player);
            }
            else if(player.getScore() == maxPlayerPoints)
            {
                if(!highestScoringPlayers.contains(player)) // maybe it's the first
                    highestScoringPlayers.add(player);
            }
        }

        return highestScoringPlayers;
    }

    public boolean isGameOver(){
        return !board.areThereAnyMovesForPlayers(playersList) || playersList.size() == 1;
    }

    public Board getBoard()
    {
        return board;
    }

    public void resetGame()
    {
        if(!turnHistory.turnHistoryStack.isEmpty())
        {
            List<TurnHistory.Turn> turnsList = new ArrayList<>(turnHistory.turnHistoryStack);
            goBackToTurn(turnsList.get(0));
            turnHistory.turnHistoryStack.clear();
        }

        playersList.forEach(player -> player.getStatistics().resetStatistics());
        isGameActive.set(false);
    }

    public void changeTurn()
    {
        addTurnToHistory(currTurn);
        setActivePlayerToBeNextPlayer();

        updateGameScore();
        calcFlipPotential();
        currTurn = getCurrentTurn();

        updateCanUndo();
    }

    public void retirePlayerFromGame(Player quitter) // in ex2 it is only possible to quit when it is your turn
    {
        int nextActivePlayer;

        if(activePlayer == quitter){ // in ex 3 - a player can retire at any time.
            currTurn.retiredPlayer = quitter;
            addTurnToHistory(currTurn);
            discTypeToPlayer.remove(quitter.getDiscType());
            playersList.remove(quitter);

            if(activePlayerIndex > playersList.size() - 1){ // new line
                activePlayerIndex = 0; // new line
            }
            //activePlayer = playersList.get(activePlayerIndex % playersList.size());
            activePlayer = playersList.get(activePlayerIndex); // changed
            currTurn = getCurrentTurn();
        }
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
                currDisc = board.getDisc(i,j);

                if(currDisc != null)
                {
                    if(discTypeToPlayer.containsKey(currDisc.getType()))
                    {
                        playerToAddScoreTo = discTypeToPlayer.get(currDisc.getType());
                        playerToAddScoreTo.getStatistics().incScore();
                    }
                }
            }
        }
    }

    public static class TurnHistory implements  Serializable {
        private Stack<Turn> turnHistoryStack;

        private TurnHistory(){
            turnHistoryStack = new Stack<>();
        }

        public static class Turn implements  Serializable {
            private List<Player> playersList;
            private Player activePlayer;
            private Board board;
            private HashMap<eDiscType, Player> discTypeToPlayer;
            private Player retiredPlayer = null;

            public List<Player> getPlayersList() {
                return playersList;
            }

            public Player getActivePlayer() {
                return activePlayer;
            }

            public Board getBoard() {
                return board;
            }

            //the method clones the last turn using copy constructors.
            private Turn(Board board, Player activePlayer, List<Player> players) {
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

        private void addHistoryEntry(Turn turn) {
            turnHistoryStack.push(turn);
        }

        //if there are no turns in the stack: returns null
        private Turn getLastTurn() {
            if(turnHistoryStack.isEmpty()) {
                return null;
            }

            return turnHistoryStack.pop();
        }

        private int getCountOfArchivedTurns(){
            if(turnHistoryStack == null){
                return 0;
            }
            else{
                return turnHistoryStack.size();
            }

        }
    }

    public SimpleBooleanProperty isGameActiveProperty() {
        return isGameActive;
    }

    public SimpleBooleanProperty canUndoProperty(){
        return canUndoProperty;
    }

    private void updateCanUndo() {
        if(turnHistory.getCountOfArchivedTurns() >= 1){
            canUndoProperty.set(true);
        }
        else{
            canUndoProperty.set(false);
        }

        if(isGameOver()){
            canUndoProperty.set(false);
        }
    }

    private TurnHistory.Turn getCurrentTurn() {
        TurnHistory.Turn turn = new TurnHistory.Turn(board, activePlayer, playersList);

        if(isGameOver()){
            turn.activePlayer = null;
        }

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

    public List<TurnHistory.Turn> getHistoryOfTurns()
    {
        List<TurnHistory.Turn> turnsList = new ArrayList<>(turnHistory.turnHistoryStack);

        currTurn = getCurrentTurn();

        turnsList.add(getCurrentTurn());
        return turnsList;
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
            updateGameScore(); // new line
        }

        updateCanUndo();
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
        currTurn.retiredPlayer = turnToChangeTo.retiredPlayer;
        calcFlipPotential();
    }

    public Player getReturnedRetiredPlayer()
    {
        return currTurn.retiredPlayer;
    }

    public enum eGameMode
    {
        Regular, Islands
    }

    public enum eMoveStatus {
        OK {
            @Override
            public String toString() {
                return "";
            }
        },
        CELL_IS_ALREADY_TAKEN {
            @Override
            public String toString() {
                return "Cell is already taken!";
            }
        },
        ILLEGAL_ISLAND {
            public String toString() {
                return "The new disc should be adjacent to other discs on board!";
            }
        },
        POINT_IS_NOT_IN_RANGE_OF_BOARD {
            public String toString() {
                return "The coordinates are not in the board's range!";
            }
        }
    }

    // call this only after all info about players is gathered.
    public void activateGame()
    {
        updateCanUndo(); // ##
        isGameActive = new SimpleBooleanProperty(false);
        isGameActive.set(true);
        activePlayerIndex = 0;
        activePlayer = playersList.get(0);
        mapDiscTypesToPlayers();
        currTurn = getCurrentTurn(); // ##
        updateGameScore();
        calcFlipPotential();
        updateGameScore();
    }

    public  void  addtoPlayersList(Player player)  {
        playersList.add(player);
    }

    public void removePlayerFromList(String playerName) {
        List<Player> playersListCopy = new ArrayList<>(playersList);

        for(Player player : playersListCopy) {
            if(player.getName().equals(playerName)){
                playersList.remove(player);
            }
        }
    }

    public int getNumOfPlayers() { return playersList.size(); }

    public int getTotalNumOfPlayers() { return totalNumOfPlayers; }

    public Player getPlayerByName(String name) {
        for(Player player: playersList) {
            if(player.getName().equals(name))
                return player;
        }

        return null;
    }
}

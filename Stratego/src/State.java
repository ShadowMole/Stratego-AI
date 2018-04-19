import java.util.ArrayList;

/**
 * Created by Steven Bruman on 4/12/2018.
 * Edited by Steven Bruman and William Jacobs
 * Version 4/18/2018
 */
public class State {

    private Unit[][] board;
    private int level;
    private double score;
    private int maxLevel;
    private Moves move;
    private ArrayList<ArrayList<Moves>> moves;
    private ArrayList<Moves> moveable;
    private ArrayList<Double> parentScores;
    private double[] prune;
    private ArrayList<Moves> allMoves;
    private int origial;
    private ArrayList<Moves> memoryAI;
    private ArrayList<Moves> memoryP;

    /**
     * This is the top level State which does not haev to deal with pruning.
     * @param b The 2D Unit array that represents how the AI views the board.
     * @param l The level of the State within the Minimax tree.
     * @param m The maximum level of the Minimax tree.
     * @param memA An ArrayList containing the last 2 Moves of the AI on the shadowBoard.
     * @param memP An ArrayList containing the last 2 Moves of the Player on the shadowBoard.
     */
    public State(Unit[][] b, int l, int m, ArrayList<Moves> memA, ArrayList<Moves> memP){
        board = new Unit[b.length][b[0].length];
        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < b[i].length; j++){
                board[i][j] = b[i][j];
            }
        }
        level = l;
        score = 0;
        maxLevel = m;
        prune = new double[maxLevel - 1];
        for(int i = 0; i < prune.length; i++){
            prune[i] = -1;
        }
        memoryAI = new ArrayList<>();
        for(int i = 0; i < memA.size(); i++){
            memoryAI.add(memA.get(i));
        }
        memoryP = new ArrayList<>();
        for(int i = 0; i < memP.size(); i++){
            memoryP.add(memP.get(i));
        }
    }

    /**
     * This is all other States that have to deal with pruning as well as pieces
     * that have moved.
     * @param b The 2D Unit array that represents how the AI views the board.
     * @param l The level of the State within the Minimax tree.
     * @param m The maximum level of the Minimax tree.
     * @param old Where the moving piece was.
     * @param knew Where the moving piece is.
     * @param p The int array that the State will use to prune its search
     * @param memA An ArrayList containing the last 2 Moves of the AI on the shadowBoard.
     * @param memP An ArrayList containing the last 2 Moves of the Player on the shadowBoard.
     */
    public State(Unit[][] b, int l, int m, Moves old, Moves knew, double[] p, ArrayList<Moves> memA, ArrayList<Moves> memP){
        board = new Unit[b.length][b[0].length];
        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < b[i].length; j++){
                board[i][j] = b[i][j];
            }
        }
        level = l;
        score = 0;
        maxLevel = m;
        prune = p;

        move = knew;

        int x = old.getX();
        int y = old.getY();
        if(knew.getPiece() == null) {
            board[knew.getY()][knew.getX()] = old.getPiece();
        } else {
            Moves current = Driver.ruleBook(old, knew, Players.AI, false);
            if(current == null) {
                board[y][x] = null;
                board[knew.getY()][knew.getX()] = null;
            }
            else {
                board[knew.getY()][knew.getX()] = current.getPiece();
            }
        }
        board[y][x] = null;
        memoryAI = new ArrayList<>();
        for(int i = 0; i < memA.size(); i++){
            memoryAI.add(memA.get(i));
        }
        memoryP = new ArrayList<>();
        for(int i = 0; i < memP.size(); i++){
            memoryP.add(memP.get(i));
        }
        if(level % 2 == 0){
            if(memoryAI.size() > 1) {
                memoryAI.remove(0);
            }
            memoryAI.add(old);
        }else{
            if(memoryP.size() > 1) {
                memoryP.remove(0);
            }
            memoryP.add(old);
        }
    }

    /**
     * Return the Move of the State.
     * @return Moves The move of the State.
     */
    public Moves getMove(){
        return move;
    }

    /**
     * Returns the moveable pieces of the State.
     * @return ArrayList<Moves> The moveable pieces of the State.
     */
    public ArrayList<Moves> getMoveable(){
        return moveable;
    }

    /**
     * Return the level of the State.
     * @return int The level of the State.
     */
    public int getLevel(){
        return level;
    }

    /**
     * Calculates the total score of the State.
     * It adds the scores of pieces owned by the
     * AI and subtracts the scores of pieces owned
     * by the player.
     */
    public void calcScore(){
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null) {
                    switch (board[i][j].getOwner()){
                        case PLAYER:
                            score -= board[i][j].getScore();
                            break;

                        case AI:
                            score += board[i][j].getScore();
                            break;
                    }
                }
            }
        }
    }

    /**
     * Returns the score of the State.
     * @return double The score of the State.
     */
    public double getScore(){
        return score;
    }

    /**
     * Returns all possible moves that could be made
     * from the State.
     * @return ArrayList<Moves> All possible moves.
     */
    public ArrayList<Moves> getAllMoves(){
        return allMoves;
    }

    /**
     * Return a 2D ArrayList that keeps all possible Moves
     * by the same piece together.
     * @return ArrayList<ArrayList<Moves>>
     */
    public ArrayList<ArrayList<Moves>> getMoves(){
        return moves;
    }

    /**
     * Returns an int representing the index in the
     * moveable ArrayList that corresponds to the piece
     * chosen to move.
     * @return int
     */
    public int getOrigial(){
        return origial;
    }

    /**
     * Determines the best possible moves via a Minimax algorithm
     * that utilizes alpha-beta pruning. First the moveable pieces are
     * determined, then the possible moves of those pieces are generated.
     * These Moves are used to create the next level of States, each with
     * a different board. The process is repeated until the maxLevel is reached.
     * Then the score is calculated and returned back up the chain. Then,
     * alaph-beta pruning begins to prune worse paths off.
     * @return double Represents either the score of the State,
     *         the score of the best move of the State, or the
     *         index of the best move of the State depending on
     *         the level of the State
     */
    public double getBestMove(){
        if(level == maxLevel){
            calcScore();
            return score;
        }else if(level == 0){
            Players player;
            ArrayList<Moves> currentMem;
            if (level % 2 == 0) {
                player = Players.AI;
                currentMem = memoryAI;
            } else {
                player = Players.PLAYER;
                currentMem = memoryP;
            }
            double best = -1;
            double index = -1;
            moveable = Driver.moveFilter(board, player, false, currentMem);
            moves = new ArrayList<>();
            for (int i = 0; i < moveable.size(); i++) {
                moves.add(moveable.get(i).generateMoves(currentMem));
            }
            allMoves = new ArrayList<>();
            for(int i = 0; i < moves.size(); i++){
                for(int j = 0; j < moves.get(i).size(); j++){
                    allMoves.add(moves.get(i).get(j));
                }
            }
            ArrayList<State> states = new ArrayList<>();
            for (int i = 0, k = 0; i < moves.size(); i++) {
                for (int j = 0; j < moves.get(i).size(); j++, k++) {
                    State st = new State(board, level + 1, maxLevel, moveable.get(i), moves.get(i).get(j), prune, memoryAI, memoryP);
                    states.add(st);
                    double s = st.getBestMove();
                    if (best == -1) {
                        best = s;
                        if(level < maxLevel - 1){
                            prune[level] = best;
                        }
                        index = k;
                        origial = i;
                    } else {
                        if (player == Players.PLAYER && s < best) {
                            best = s;
                            if(level < maxLevel - 1){
                                prune[level] = best;
                            }
                            index = k;
                            origial = i;
                        } else if (player == Players.AI && s > best) {
                            best = s;
                            if(level < maxLevel - 1){
                                prune[level] = best;
                            }
                            index = k;
                            origial = i;
                        }
                    }
                }
            }
            return index;
        }else {
            Players player;
            ArrayList<Moves> currentMem;
            if (level % 2 == 0) {
                player = Players.AI;
                currentMem = memoryAI;

            } else {
                player = Players.PLAYER;
                currentMem = memoryP;
            }
            double best = -1;
            moveable = Driver.moveFilter(board, player, false, currentMem);
            moves = new ArrayList<>();
            ArrayList<State> states = new ArrayList<>();
            boolean stop = false;
            for (int i = 0, k = 0; !stop && i < moveable.size(); i++) {
                moves.add(moveable.get(i).generateMoves(currentMem));

                for (int j = 0; !stop && j < moves.get(i).size(); j++, k++) {
                    if (j > 0 && level < maxLevel) {
                        if (player == Players.PLAYER) {
                            for (int m = 0; !stop && m < level; m += 2) {
                                if (prune[m] != -1 && best >= prune[m]) {
                                    stop = true;
                                }
                            }
                        } else {
                            for (int m = 1; !stop && m < level; m += 2) {
                                if (prune[m] != -1 && best <= prune[m]) {
                                    stop = true;
                                }
                            }
                        }
                    }
                    if (!stop) {
                        State st = new State(board, level + 1, maxLevel, moveable.get(i), moves.get(i).get(j), prune, memoryAI, memoryP);
                        states.add(st);
                        double s = st.getBestMove();
                        if (best == -1) {
                            best = s;
                            if (level < maxLevel - 1) {
                                prune[level] = best;
                            }
                        } else {
                            if (player == Players.PLAYER && s < best) {
                                best = s;
                                if (level < maxLevel - 1) {
                                    prune[level] = best;
                                }
                            } else if (player == Players.AI && s > best) {
                                best = s;
                                if (level < maxLevel - 1) {
                                    prune[level] = best;
                                }
                            }
                        }
                    }
                }
            }
            return best;
        }
    }
}

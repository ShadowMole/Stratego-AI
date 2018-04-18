import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mole on 4/12/2018.
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

    public Moves getMove(){
        return move;
    }

    public ArrayList<Moves> getMoveable(){
        return moveable;
    }

    public int getLevel(){
        return level;
    }

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

    public double getScore(){
        return score;
    }

    public ArrayList<Moves> getAllMoves(){
        return allMoves;
    }

    public ArrayList<ArrayList<Moves>> getMoves(){
        return moves;
    }

    public int getOrigial(){
        return origial;
    }

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

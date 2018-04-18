import java.util.ArrayList;
public class Moves {
    private int x;
    private int y;
    private Unit[][] board;

    public Moves(Unit[][] board, int y, int x) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Unit getPiece()
    {
        return board[y][x];
    }

    public ArrayList<Moves> generateMoves(ArrayList<Moves> past)
    {
        ArrayList<Moves> moves = new ArrayList();
        int count = 0; /* Variable coordinates to check board for acceptable moves; i is the offset.
                        * count used for array to store board coordinates in moves.
                        */
                if (board[y][x].getType() == PieceType.SCOUT) {
                    int i = 1;
                    boolean guard = false; // Used to prevent scout from going beyond one enemy piece.
                    /*
                     * Searching for potential moves in the Right direction.
                     */
                        while (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y) == past.get(1).getY() && (x+i) == past.get(1).getX()) && ((x+i) < 10) &&
                                (board[y][x+i] == null || (board[y][x+i].getOwner() != board[y][x].getOwner() && board[y][x+i].getOwner() != Players.LAKE)) &&
                                guard == false) {
                            if(board[y][x+i] == null) { // Eager Advancing, space is unoccupied AND isn't a Lake.
                                moves.add(new Moves(board, y, x+i));
                                count++;
                                i++;
                            }
                            else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves.add(new Moves(board, y, x+i));
                                    count++;
                                    i++;
                                }
                            }
                        }
                    i = 1; // reset
                    guard = false;
                    /*
                     * Searching for potential moves in the Left direction.
                     */
                        while (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y) == past.get(1).getY() && (x-i) == past.get(1).getX()) && ((x-i) > -1) &&
                                (board[y][x-i] == null || (board[y][x-i].getOwner() != board[y][x].getOwner() && board[y][x-i].getOwner() != Players.LAKE)) &&
                                guard == false) {
                            if (board[y][x-i] == null) { // Eager Advancing, space is unoccupied AND isn't a Lake.
                                moves.add(new Moves(board, y, x-i));
                                count++;
                                i++;
                            }
                            else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves.add(new Moves(board, y, x-i));
                                    count++;
                                    i++;
                                }
                            }
                        }
                    i = 1; //reset
                    guard = false;
                    /*
                     * Searching for potential moves in the Up direction.
                     */
                        while (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y+i) == past.get(1).getY() && x == past.get(1).getX()) && ((y+i) < 10) &&
                                (board[y+i][x] == null || (board[y+i][x].getOwner() != board[y][x].getOwner() && board[y+i][x].getOwner() != Players.LAKE)) &&
                                guard == false) {
                            if (board[y+i][x] == null) { // Eager Advancing, space is unoccupied AND isn't a Lake.
                                moves.add(new Moves(board, y+i, x));
                                count++;
                                i++;
                            }
                            else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves.add(new Moves(board, y+i, x));
                                    count++;
                                    i++;
                                }
                            }
                        }
                    i = 1; // reset
                    guard = false;
                    /*
                     * Searching for potential moves in the Down direction.
                     */
                        while (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y-i) == past.get(1).getY() && x == past.get(1).getX()) && ((y-i) > -1) &&
                                (board[y-i][x] == null || (board[y-i][x].getOwner() != board[y][x].getOwner() && board[y-i][x].getOwner() != Players.LAKE)) &&
                                guard == false) {
                            if (board[y-i][x] == null) { // Eager Advancing, space is unoccupied AND isn't a Lake.
                                moves.add(new Moves(board, y-i, x));
                                count++;
                                i++;
                            } else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves.add(new Moves(board, y-i, x));
                                    count++;
                                    i++;
                                }
                            }
                        }
                } else {
                    // Searching for a potential move in the Right direction.
                    if (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y) == past.get(1).getY() && (x+1) == past.get(1).getX()) && ((x+1) < 10) &&
                            (board[y][x+1] == null || (board[y][x+1].getOwner() != board[y][x].getOwner() && board[y][x+1].getOwner() != Players.LAKE))) {
                        moves.add(new Moves(board, y, x+1));
                        count++;
                    }
                    // Searching for a potential move in the Left direction.
                    if (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y) == past.get(1).getY() && (x-1) == past.get(1).getX()) && ((x-1) > -1) &&
                            (board[y][x-1] == null || (board[y][x-1].getOwner() != board[y][x].getOwner() && board[y][x-1].getOwner() != Players.LAKE))) {
                        moves.add(new Moves(board, y, x-1));
                        count++;
                    }
                    // Searching for a potential move in the Up direction.
                    if (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y+1) == past.get(1).getY() && x == past.get(1).getX()) && ((y+1) < 10) &&
                            (board[y+1][x] == null || (board[y+1][x].getOwner() != board[y][x].getOwner() && board[y+1][x].getOwner() != Players.LAKE))) {
                        moves.add(new Moves(board,y+1, x));
                        count++;
                    }
                    // Searching for a potential move in the Down direction.
                    if (!(past.size() > 1 && y == past.get(0).getY() && x == past.get(0).getX() && (y-1) == past.get(1).getY() && x == past.get(1).getX()) && ((y-1) > -1) &&
                            (board[y-1][x] == null || (board[y-1][x].getOwner() != board[y][x].getOwner() && board[y-1][x].getOwner() != Players.LAKE))) {
                        moves.add(new Moves(board,y-1, x));
                        count++;
                    }
                }
        return moves;
    }
}

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

    public Moves[] generateMoves()
    {
        Moves moves[] = new Moves[40];
        int count = 0; /* Variable coordinates to check board for acceptable moves; i is the offset.
                        * count used for array to store board coordinates in moves.
                        */
                if (board[y][x].getType() == PieceType.SCOUT) {
                    int i = 1;
                    boolean guard = false; // Used to prevent scout from going beyond one enemy piece.
                    /*
                     * Searching for potential moves in the right direction.
                     */
                        while (((x+i) < 10) &&
                                (board[y][x+i] == null || board[y][x+i].getOwner() != board[y][x].getOwner()) &&
                                guard == false) {
                            if(board[y][x+i] == null) { // Eager Advancing, space is unoccupied.
                                moves[count] = new Moves(board, y, x+i);
                                count++;
                                i++;
                            }
                            else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves[count] = new Moves(board, y, x+i);
                                    count++;
                                    i++;
                                }
                            }
                        }
                    i = 1; // reset
                    guard = false;
                    /*
                     * Searching for potential moves in the left direction.
                     */
                        while (((x-i) > -1) &&
                                (board[y][x-i] == null || board[y][x-i].getOwner() != board[y][x].getOwner()) &&
                                guard == false) {
                            if (board[y][x-i] == null) { // Eager Advancing, space is unoccupied.
                                moves[count] = new Moves(board, y, x-i);
                                count++;
                                i++;
                            }
                            else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves[count] = new Moves(board, y, x-i);
                                    count++;
                                    i++;
                                }
                            }
                        }
                    i = 1; //reset
                    guard = false;
                    /*
                     * Searching for potential moves in the up direction.
                     */
                        while (((y+i) < 10) &&
                                (board[y+i][x] == null || board[y+i][x].getOwner() != board[y][x].getOwner()) &&
                                guard == false) {
                            if (board[y+i][x] == null) { // Eager Advancing, space is unoccupied.
                                moves[count] = new Moves(board, y+i, x);
                                count++;
                                i++;
                            }
                            else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves[count] = new Moves(board, y+i, x);
                                    count++;
                                    i++;
                                }
                            }
                        }
                    i = 1; // reset
                    guard = false;
                    /*
                     * Searching for potential moves in the down direction.
                     */
                        while (((y-i) > -1) &&
                                (board[y-i][x] == null || board[y-i][x].getOwner() != board[y][x].getOwner()) &&
                                guard == false) {
                            if (board[y-i][x] == null) { // Eager Advancing, space is unoccupied.
                                moves[count] = new Moves(board, y-i, x);
                                count++;
                                i++;
                            } else { // If Scout encounters an enemy
                                guard = true;
                                if(i == 1) { // Used to determine if the scout can attack the enemy piece.
                                    moves[count] = new Moves(board, y-i, x);
                                    count++;
                                    i++;
                                }
                            }
                        }
                } else {
                    // Searching for a potential move in the right direction.
                    if (((x+1) < 10) && (board[y][x+1] == null || board[y][x+1].getOwner() != board[y][x].getOwner())) {
                        moves[count] = new Moves(board, y, x+1);
                        count++;
                    }
                    // Searching for a potential move in the left direction.
                    if (((x-1) > -1) && (board[y][x-1] == null || board[y][x-1].getOwner() != board[y][x].getOwner())) {
                        moves[count] = new Moves(board, y, x-1);
                        count++;
                    }
                    // Searching for a potential move in the up direction.
                    if (((y+1) < 10) && (board[y+1][x] == null || board[y+1][x].getOwner() != board[y][x].getOwner())) {
                        moves[count] = new Moves(board,y+1, x);
                        count++;
                    }
                    // Searching for a potential move in the down direction.
                    if (((y-1) > -1) && (board[y-1][x] == null || board[y-1][x].getOwner() != board[y][x].getOwner())) {
                        moves[count] = new Moves(board,y-1, x);
                        count++;
                    }
                }
        return moves;
    }
}

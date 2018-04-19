/**
 * This class represents a piece on the board. This piece could be known,
 * theoretical, or even owned by a Lake. (Not Joking, lakes actually own
 * pieces)
 * Created by Steven Bruman on 1/30/2018.
 * Edited by Steven Bruman and William Jacobs
 * Version 4/18/2018
 */
public class Unit {

    private int strength;
    private String name;
    private Players owner;
    private String character;
    private boolean placed;
    private double score;
    private PieceType type;
    private int[][] stats;
    private boolean hasMoved;

    /**
     * This is for pieces that are known and are owned by either the Player
     * or the AI.
     * @param s The strength of the Unit.
     * @param n The name of the Unit.
     * @param c The character that will be printed on the board.
     * @param o The owner of the Unit.
     * @param sc The score of the Unit.
     * @param t The type of the Unit.
     */
    public Unit(int s, String n, String c, Players o, double sc, PieceType t) {
        strength = s;
        name = n;
        owner = o;
        character = c;
        placed = false;
        score = sc;
        type = t;
    }

    /**
     * This is for pieces that are unknown to the AI.
     * @param sc The score of the Unit
     * @param o The owner of the Unit
     * @param s A 2D array containing various statistics used to predict what the Unit is.
     */
    public Unit(double sc, Players o, int[][] s){
        score = sc;
        owner = o;
        stats = s;
        hasMoved = false;
        predictStrength();
    }

    /**
     * This is for pieces owned by the Lake. This was done in order to assure
     * that other pieces do not enter lake spaces. We promise that we tried other
     * solutions first, but this was the first solution that worked entirely.
     * @param o The owner of the Unit
     */
    public Unit(Players o){
        owner = o;
    }

    /**
     * Returns the name of the Unit.
     * @return String The name of the Unit.
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the strength of the Unit.
     * @return int The strength of the Unit.
     */
    public int getStrength(){
        return strength;
    }

    /**
     * Returns the owner of the Unit.
     * @return Players The owner of the Unit.
     */
    public Players getOwner() {
        return owner;
    }

    /**
     * Returns the character of the Unit that will be printed on the board.
     * @return String The character of the Unit.
     */
    public String getCharacter(){
        return character;
    }

    /**
     * Returns whether or not the Unit was placed.
     * @return boolean Whether or not the Unit was placed.
     */
    public boolean getPlaced(){
        return placed;
    }

    /**
     * Sets placed.
     * @param p True, the Unit was placed, otherwise false.
     */
    public void setPlaced(boolean p){
        placed = p;
    }

    /**
     * Sets the score of the Unit.
     * @param s The score of the Unit.
     */
    public void setScore(double s){
        score = s;
    }

    /**
     * Modifies the score statistic for all the values of the given type.
     * It the recalculates the total score of the Unit.
     * @param s The score of the Type.
     * @param p The type to be changed.
     */
    public void setScore(int s, PieceType p) {
        if (stats != null) {
            switch(p){
                case MARSHALL:
                    stats[0][3] = s;
                    break;
                    
                case GENERAL:
                    stats[1][3] = s;
                    break;
                    
                case COLONEL:
                    stats[2][3] = s;
                    break;
                    
                case MAJOR:
                    stats[3][3] = s;
                    break;
                    
                case CAPTAIN:
                    stats[4][3] = s;
                    break;
                    
                case LIEUTENANT:
                    stats[5][3] = s;
                    break;
                    
                case SERJEANT:
                    stats[6][3] = s;
                    break;
                    
                case MINER:
                    stats[7][3] = s;
                    break;
                    
                case SCOUT:
                    stats[8][3] = s;
                    break;
                    
                case SPY:
                    stats[9][3] = s;
                    break;
                    
                case BOMB:
                    stats[10][3] = s;
                    break;
                    
                case FLAG:
                    stats[11][3] = s;
                    break;
            }
            reCalculateScore();
        }
    }

    /**
     * Modifies the amount statistic for all the values of the given type.
     * It the recalculates the total score of the Unit.
     * @param s The amount of the Type.
     * @param p The type to be changed.
     */
    public void setAmount(int s, PieceType p) {
        if (stats != null) {
            switch(p){
                case MARSHALL:
                    stats[0][4] = s;
                    break;

                case GENERAL:
                    stats[1][4] = s;
                    break;

                case COLONEL:
                    stats[2][4] = s;
                    break;

                case MAJOR:
                    stats[3][4] = s;
                    break;

                case CAPTAIN:
                    stats[4][4] = s;
                    break;

                case LIEUTENANT:
                    stats[5][4] = s;
                    break;

                case SERJEANT:
                    stats[6][4] = s;
                    break;

                case MINER:
                    stats[7][4] = s;
                    break;

                case SCOUT:
                    stats[8][4] = s;
                    break;

                case SPY:
                    stats[9][4] = s;
                    break;

                case BOMB:
                    stats[10][4] = s;
                    break;

                case FLAG:
                    stats[11][4] = s;
                    break;
            }
            reCalculateScore();
        }
    }

    /**
     * Returns the amount used to calculate the score of a certain type.
     * @param p The type in question.
     * @return int The amount of that type.
     */
    public int getAmount(PieceType p){
        if (stats != null) {
            switch (p) {
                case MARSHALL:
                    return stats[0][4];

                case GENERAL:
                    return stats[1][4];

                case COLONEL:
                    return stats[2][4];

                case MAJOR:
                    return stats[3][4];

                case CAPTAIN:
                    return stats[4][4];

                case LIEUTENANT:
                    return stats[5][4];

                case SERJEANT:
                    return stats[6][4];

                case MINER:
                    return stats[7][4];

                case SCOUT:
                    return stats[8][4];

                case SPY:
                    return stats[9][4];

                case BOMB:
                    return stats[10][4];

                case FLAG:
                    return stats[11][4];
            }
        }
        return 0;
    }

    /**
     * Recalculates the theoretical score of the Unit based on statistics.
     */
    public void reCalculateScore(){
        if(stats != null){
            score = 0;
            int num = 0;
            for(int k = 0; k < stats.length; k++){
                score += ((1.0 * stats[k][0] * stats[k][3] * stats[k][4]) / (stats[k][1] + stats[k][2]));
                num += stats[k][4];
            }
            score /= num;
            predictStrength();
        }
    }

    /**
     * Predicts the strength of the Unit based on where its
     * theoretical score falls.
     */
    public void predictStrength() {
        if(!hasMoved && score >= 200){
            strength = -1;
        }else if(score >= 95) { // MARSHALL Strength
            strength = 10;
        }
        else if(score >= 80) { // GENERAL Strength
            strength = 9;
        }
        else if(score >= 65) { // COLONEL Strength
            strength = 8;
        }
        else if(score >= 45) { // MAJOR Strength
            int rng = Randomizer.getRgen(12);
            if(rng == 0) {
                strength = 1;
            }else if(rng > 0 && rng < 7){
                strength = 3;
            }else{
                strength = 7;
            }
        }
        else if(score >= 30) {
            if(hasMoved) { // CAPTAIN Strength
                strength = 6;
            }
            else strength = 11; // BOMB Strength
        }
        else if(score >= 18) { // LIEUTENANT Strength
            strength = 5;
        }
        else if(score >= 12.5) { // SCOUT Strength
            strength = 2;
        }
        else strength = 4; // SERGEANT Strength

    }

    /**
     * Returns the score of the Unit.
     * @return double The score of the Unit.
     */
    public double getScore() {
        return score;
    }

    /**
     * Returns the score of a certain type.
     * @param p The given type.
     * @return int The score of that type.
     */
    public int getScore(PieceType p){
        if (stats != null) {
            switch (p) {
                case MARSHALL:
                    return stats[0][3];

                case GENERAL:
                    return stats[1][3];

                case COLONEL:
                    return stats[2][3];

                case MAJOR:
                    return stats[3][3];

                case CAPTAIN:
                    return stats[4][3];

                case LIEUTENANT:
                    return stats[5][3];

                case SERJEANT:
                    return stats[6][3];

                case MINER:
                    return stats[7][3];

                case SCOUT:
                    return stats[8][3];

                case SPY:
                    return stats[9][3];

                case BOMB:
                    return stats[10][3];

                case FLAG:
                    return stats[11][3];
            }
        }
        return 0;
    }

    /**
     * Returns the type of the Unit.
     * @return PieceType The type of the Unit.
     */
    public PieceType getType(){
        return type;
    }

    /**
     * Returns whether or not the Unit has moved.
     * @return boolean True, the Unit has moved, other false.
     */
    public boolean getHasMoved(){
        return hasMoved;
    }

    /**
     * Sets hasMoved to true, then lets the AI know that the
     * Unit is not a bomb or a flag.
     */
    public void moved(){
        hasMoved = true;
        setAmount(0,PieceType.FLAG);
        setAmount(0,PieceType.BOMB);
    }
}


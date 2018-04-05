/**
 * Created by Mole on 1/30/2018.
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

    public Unit(int s, String n, String c, Players o, double sc, PieceType t) {
        strength = s;
        name = n;
        owner = o;
        character = c;
        placed = false;
        score = sc;
        type = t;
    }

    public Unit(double sc, Players o, int[][] s){
        score = sc;
        owner = o;
        stats = s;
    }

    public String getName(){
        return name;
    }

    public int getStrength(){
        return strength;
    }

    public Players getOwner() {
        return owner;
    }

    public String getCharacter(){
        return character;
    }

    public boolean getPlaced(){
        return placed;
    }

    public void setPlaced(boolean p){
        placed = p;
    }

    public void setScore(double s){
        score = s;
    }
    
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
    
    public void reCalculateScore(){
        if(stats != null){
            double score = 0;
            int num = 0;
            for(int k = 0; k < stats.length; k++){
                score += ((1.0 * stats[k][0] * stats[k][3] * stats[k][4]) / (stats[k][1] + stats[k][2]));
                num += stats[k][4];
            }
            score /= num;
        }
    }

    public double getScore(){
        return score;
    }

    public PieceType getType(){
        return type;
    }
}


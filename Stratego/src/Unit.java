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

    public Players getOwner(){
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

    public double getScore(){
        return score;
    }

    public PieceType getType(){
        return type;
    }
}


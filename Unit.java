/**
 * Created by Mole on 1/30/2018.
 */
public class Unit {

    private int strength;
    private String name;
    private Players owner;

    public Unit(int s, String n, Players o){
        strength = s;
        name = n;
        owner = o;
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
}


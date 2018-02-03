import javax.swing.*;

/**
 * Created by Mole on 1/30/2018.
 */
public class Unit {

    private int strength;
    private String name;
    private Players owner;
    private ImageIcon image;
    private final ImageIcon back;

    public Unit(int s, String n, Players o){
        strength = s;
        name = n;
        owner = o;
        switch(owner){
            case AI:
                switch(name) {
                    case "Marshal":
                        image = new ImageIcon(getClass().getResource("/../rec/red_marshal.png"));
                        break;

                    case "General":
                        image = new ImageIcon(getClass().getResource("/../rec/red_general.png"));
                        break;

                    case "Colonel":
                        image = new ImageIcon(getClass().getResource("/../rec/red_colonel.png"));
                        break;

                    case "Major":
                        image = new ImageIcon(getClass().getResource("/../rec/red_major.png"));
                        break;

                    case "Captain":
                        image = new ImageIcon(getClass().getResource("/../rec/red_captain.png"));
                        break;

                    case "Lieutenant":
                        image = new ImageIcon(getClass().getResource("/../rec/red_lieutenant.png"));
                        break;

                    case "Serjeant":
                        image = new ImageIcon(getClass().getResource("/../rec/red_serjeant.png"));
                        break;

                    case "Miner":
                        image = new ImageIcon(getClass().getResource("/../rec/red_miner.png"));
                        break;

                    case "Scout":
                        image = new ImageIcon(getClass().getResource("/../rec/red_scout.png"));
                        break;

                    case "Flag":
                        image = new ImageIcon(getClass().getResource("/../rec/red_flag.png"));
                        break;

                    case "Bomb":
                        image = new ImageIcon(getClass().getResource("/../rec/red_bomb.png"));
                        break;

                    case "Spy":
                        image = new ImageIcon(getClass().getResource("/../rec/red_spy.png"));
                        break;

                    default:
                        image = null;
                        break;
                }
                back = new ImageIcon(getClass().getResource("/../rec/red_back.png"));
                break;

            case PLAYER:
                switch(name) {
                    case "Marshal":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_marshal.png"));
                        break;

                    case "General":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_general.png"));
                        break;

                    case "Colonel":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_colonel.png"));
                        break;

                    case "Major":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_major.png"));
                        break;

                    case "Captain":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_captain.png"));
                        break;

                    case "Lieutenant":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_lieutenant.png"));
                        break;

                    case "Serjeant":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_serjeant.png"));
                        break;

                    case "Miner":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_miner.png"));
                        break;

                    case "Scout":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_scout.png"));
                        break;

                    case "Flag":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_flag.png"));
                        break;

                    case "Bomb":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_bomb.png"));
                        break;

                    case "Spy":
                        image = new ImageIcon(getClass().getResource("/../rec/blue_spy.png"));
                        break;

                    default:
                        image = null;
                        break;
                }
                back = new ImageIcon(getClass().getResource("/../rec/blue_back.png"));
                break;

            default:
                image = null;
                back = null;
                break;
        }
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

    public ImageIcon getImage(){
        return image;
    }
}


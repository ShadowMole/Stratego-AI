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
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_marshal.png");
                        break;

                    case "General":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_general.png");
                        break;

                    case "Colonel":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_colonel.png");
                        break;

                    case "Major":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_major.png");
                        break;

                    case "Captain":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_captain.png");
                        break;

                    case "Lieutenant":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_lieutenant.png");
                        break;

                    case "Serjeant":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_serjeant.png");
                        break;

                    case "Miner":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_miner.png");
                        break;

                    case "Scout":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_scout.png");
                        break;

                    case "Flag":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_flag.png");
                        break;

                    case "Bomb":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_bomb.png");
                        break;

                    case "Spy":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//red_spy.png");
                        break;

                    default:
                        image = null;
                        break;
                }
                back = new ImageIcon(System.getProperty("user.dir") + "//..//red_back.png");
                break;

            case PLAYER:
                switch(name) {
                    case "Marshal":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_marshal.png");
                        break;

                    case "General":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_general.png");
                        break;

                    case "Colonel":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_colonel.png");
                        break;

                    case "Major":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_major.png");
                        break;

                    case "Captain":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_captain.png");
                        break;

                    case "Lieutenant":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_lieutenant.png");
                        break;

                    case "Serjeant":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_serjeant.png");
                        break;

                    case "Miner":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_miner.png");
                        break;

                    case "Scout":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_scout.png");
                        break;

                    case "Flag":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_flag.png");
                        break;

                    case "Bomb":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_bomb.png");
                        break;

                    case "Spy":
                        image = new ImageIcon(System.getProperty("user.dir") + "//..//blue_spy.png");
                        break;

                    default:
                        image = null;
                        break;
                }
                back = new ImageIcon(System.getProperty("user.dir") + "//..//blue_back.png");
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


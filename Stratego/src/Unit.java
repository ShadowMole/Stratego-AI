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
                        image = new ImageIcon(this.getClass().getResource("red_marshal.png"));
                        break;

                    case "General":
                        image = new ImageIcon(this.getClass().getResource("red_general.png"));
                        break;

                    case "Colonel":
                        image = new ImageIcon(this.getClass().getResource("red_colonel.png"));
                        break;

                    case "Major":
                        image = new ImageIcon(this.getClass().getResource("red_major.png"));
                        break;

                    case "Captain":
                        image = new ImageIcon(this.getClass().getResource("red_captain.png"));
                        break;

                    case "Lieutenant":
                        image = new ImageIcon(this.getClass().getResource("red_lieutenant.png"));
                        break;

                    case "Sergeant":
                        image = new ImageIcon(this.getClass().getResource("red_sergeant.png"));
                        break;

                    case "Miner":
                        image = new ImageIcon(this.getClass().getResource("red_miner.png"));
                        break;

                    case "Scout":
                        image = new ImageIcon(this.getClass().getResource("red_scout.png"));
                        break;

                    case "Flag":
                        image = new ImageIcon(this.getClass().getResource("red_flag.png"));
                        break;

                    case "Bomb":
                        image = new ImageIcon(this.getClass().getResource("red_bomb.png"));
                        break;

                    case "Spy":
                        image = new ImageIcon(this.getClass().getResource("red_spy.png"));
                        break;

                    default:
                        image = null;
                        break;
                }
                back = new ImageIcon(this.getClass().getResource("red_back.png"));
                break;

            case PLAYER:
                switch(name) {
                    case "Marshal":
                        image = new ImageIcon(this.getClass().getResource("blue_marshal.png"));
                        break;

                    case "General":
                        image = new ImageIcon(this.getClass().getResource("blue_general.png"));
                        break;

                    case "Colonel":
                        image = new ImageIcon(this.getClass().getResource("blue_colonel.png"));
                        break;

                    case "Major":
                        image = new ImageIcon(this.getClass().getResource("blue_major.png"));
                        break;

                    case "Captain":
                        image = new ImageIcon(this.getClass().getResource("blue_captain.png"));
                        break;

                    case "Lieutenant":
                        image = new ImageIcon(this.getClass().getResource("blue_lieutenant.png"));
                        break;

                    case "Sergeant":
                        image = new ImageIcon(this.getClass().getResource("blue_sergeant.png"));
                        break;

                    case "Miner":
                        image = new ImageIcon(this.getClass().getResource("blue_miner.png"));
                        break;

                    case "Scout":
                        image = new ImageIcon(this.getClass().getResource("blue_scout.png"));
                        break;

                    case "Flag":
                        image = new ImageIcon(this.getClass().getResource("blue_flag.png"));
                        break;

                    case "Bomb":
                        image = new ImageIcon(this.getClass().getResource("blue_bomb.png"));
                        break;

                    case "Spy":
                        image = new ImageIcon(this.getClass().getResource("blue_spy.png"));
                        break;

                    default:
                        image = null;
                        break;
                }
                back = new ImageIcon(this.getClass().getResource("blue_back.png"));
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


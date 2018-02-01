/**
 * Created by Mole on 1/24/2018.
 */
public class Driver {

    public static void main(String[] args) {

        Unit[][] armies = new Unit[2][40];
        for(int i = 0; i < armies.length; i++){
           Players current;
            if(i == 0){
                current = Players.PLAYER;
            }else{
                current = Players.AI;
            }
            armies[i][0] = new Unit(10, "Marshal", current);
            armies[i][1] = new Unit(9, "General", current);
            for(int j = 2; j < 4; j++){
                armies[i][j] = new Unit(8, "Colonel", current);
            }
            for(int j = 4; j < 7; j++){
                armies[i][j] = new Unit(7, "Major", current);
            }
            for(int j = 7; j < 11; j++){
                armies[i][j] = new Unit(6, "Captain", current);
            }
            for(int j = 11; j < 15; j++){
                armies[i][j] = new Unit(5, "Lieutenant", current);
            }
            for(int j = 15; j < 19; j++){
                armies[i][j] = new Unit(4, "Sergeant", current);
            }
            for(int j = 19; j < 24; j++){
                armies[i][j] = new Unit(3, "Miner", current);
            }
            for(int j = 24; j < 32; j++){
                armies[i][j] = new Unit(2, "Scout", current);
            }
            armies[i][32] = new Unit(0, "Spy", current);
            for(int j = 33; j < 39; j++){
                armies[i][j] = new Unit(11, "Bomb", current);
            }
            armies[i][39] = new Unit(-1, "Flag", current);
        }
        Unit[][] board = new Unit[10][10];
        GUI gui = new GUI(armies);
    }
}

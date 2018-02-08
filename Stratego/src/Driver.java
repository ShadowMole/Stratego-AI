import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Mole on 1/24/2018.
 */
public class Driver {

    public static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws IOException { //Main method
        boolean finished = false;
        while (! finished) {
            printGameMenu();
            int command = Integer.parseInt(stdin.readLine());
            System.out.println(command);
            finished = processMenuCommand(command);
        }
    }

    public static boolean processMenuCommand(int command){
        boolean wantToQuit = false;

        switch(command){
            case 1:
                playGame();
                break;

            case 2:
                showRules();
                break;

            case 3:
                wantToQuit = true;
                break;
        }
        return wantToQuit;
    }
    
    public static void showRules(){
        
    }

    public static void playGame(){
        Unit[][][] armies = new Unit[2][2][40];
        for(int i = 0; i < armies.length; i++){
            Players current;
            if(i == 0){
                current = Players.PLAYER;
            }else{
                current = Players.AI;
            }
            armies[i][0][0] = new Unit(10, "Marshal", "10", current);
            armies[i][0][1] = new Unit(9, "General", "9", current);
            for(int j = 2; j < 4; j++){
                armies[i][0][j] = new Unit(8, "Colonel", "8", current);
            }
            for(int j = 4; j < 7; j++){
                armies[i][0][j] = new Unit(7, "Major", "7", current);
            }
            for(int j = 7; j < 11; j++){
                armies[i][0][j] = new Unit(6, "Captain", "6", current);
            }
            for(int j = 11; j < 15; j++){
                armies[i][0][j] = new Unit(5, "Lieutenant", "5", current);
            }
            for(int j = 15; j < 19; j++){
                armies[i][0][j] = new Unit(4, "Sergeant", "4", current);
            }
            for(int j = 19; j < 24; j++){
                armies[i][0][j] = new Unit(3, "Miner", "3", current);
            }
            for(int j = 24; j < 32; j++){
                armies[i][0][j] = new Unit(2, "Scout", "2", current);
            }
            armies[i][0][32] = new Unit(0, "Spy", "S", current);
            for(int j = 33; j < 39; j++){
                armies[i][0][j] = new Unit(11, "Bomb", "B", current);
            }
            armies[i][0][39] = new Unit(-1, "Flag", "F", current);
        }
        Unit[][] board = new Unit[10][10];
        try {
            setupBoard(board, armies[0][0]);
        }catch(Exception e){}
    }

    public static void printGameMenu(){
        System.out.println("Hello and welcome to Bill's and Steven's game of Stratego.");
        System.out.println("Here you will be playing against an AI that we have created.");
        System.out.println("Please try and record wins and losses as well as anything strange \nor unique that the AI does.");
        System.out.println("Have fun and good luck!");
        System.out.println("1. Play game.\n2. Rules.\n3. Exit.");
    }

    public static void printBoard(Unit[][] board){
        for(int i = -1; i < board.length + 1; i++){
            for(int j = -1; j < 10 + 1; j++){
                if(i == -1 || i == board.length){
                    if(j != -1 && j != 10){
                        System.out.print((j + 1) + "\t");
                    }else{
                        System.out.print("\t");
                    }
                }else if(j == -1 || j == board[i].length){
                    System.out.print((i + 1) + "\t");
                }else if((i == 4 || i == 5) && (j == 2 || j == 3 || j ==6 || j == 7 )){
                    System.out.print("L" + "\t");
                }else if(board[i][j] != null){
                    System.out.print(board[i][j].getCharacter() + "\t");
                }else{
                    System.out.print("\t");
                }
            }
            System.out.println("");
            System.out.println("");
        }
    }

    public static void setupBoard(Unit[][] board, Unit[] army)throws IOException{
        int placed = 0;
        while(placed != 40){
            printBoard(board);
            System.out.println("You have have the following units left to place:\n");
            for(int i = 0; i < army.length; i++){
                if (!army[i].getPlaced()) {
                    System.out.print((i+1) + ". " + army[i].getName() +"\n");
                }
            }
            int unit = Integer.parseInt(stdin.readLine());
            if(placeUnit(unit - 1, board, army)){
                placed++;
            }
        }
    }

    public static boolean placeUnit(int unit,Unit[][] board, Unit[] army){
        printBoard(board);
        System.out.print("The following spaces are where you can still place units:\n");
        return true;
    }
}
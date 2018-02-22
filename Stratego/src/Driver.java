import java.io.*;

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

    /**
     * This method will be used to print the rules of the
     * game so that a player can understand how to play.
     */
    public static void showRules(){
        
    }

    public static void playGame(){
        Unit[][][] armies = new Unit[2][2][40];
        setupArmies(armies);

        Unit[][] board = new Unit[10][10];

        setupBoard(board, armies);

        printBoard(board);

        int turn = Randomizer.getRgen(2);
        switch (turn){
            case 0:
                System.out.println("The Player goes first.");
                break;

            case 1:
                System.out.println("The Computer goes first.");
                break;
        }

        Unit[][] shadowBoard = new Unit[10][10];
        Unit[] shadowArmy = new Unit[40];

        buildShadowBoard(board, shadowBoard, shadowArmy);
        for(int i = 0; i < shadowArmy.length; i++){
            System.out.println(shadowArmy[i].getScore());
        }

        boolean end = false;

        for(;!end; turn = (turn + 1) % 2){
            switch (turn){
                case 0:
                   playerMove(board, armies);
                    break;

                case 1:
                    aiMove(board, armies);
                    break;
            }
        }
    }

    public static void printGameMenu(){
        System.out.println("Hello and welcome to Bill's and Steven's game of Stratego.");
        System.out.println("Here you will be playing against an AI that we have created.");
        System.out.println("Please try and record wins and losses as well as anything strange \nor unique that the AI does.");
        System.out.println("Have fun and good luck!");
        System.out.println("1. Play game.\n2. Rules.\n3. Exit.");
    }

    public static void setupArmies(Unit[][][] armies){
        for(int i = 0; i < armies.length; i++){
            Players current;
            if(i == 0){
                current = Players.PLAYER;
            }else{
                current = Players.AI;
            }
            armies[i][0][0] = new Unit(10, "Marshal", "10", current, 100, PieceType.MARSHALL);
            armies[i][0][1] = new Unit(9, "General", "9", current, 90, PieceType.GENERAL);
            for(int j = 2; j < 4; j++){
                armies[i][0][j] = new Unit(8, "Colonel", "8", current, 75, PieceType.COLONEL);
            }
            for(int j = 4; j < 7; j++){
                armies[i][0][j] = new Unit(7, "Major", "7", current, 50, PieceType.MAJOR);
            }
            for(int j = 7; j < 11; j++){
                armies[i][0][j] = new Unit(6, "Captain", "6", current, 40, PieceType.CAPTAIN);
            }
            for(int j = 11; j < 15; j++){
                armies[i][0][j] = new Unit(5, "Lieutenant", "5", current, 20, PieceType.LIEUTENANT);
            }
            for(int j = 15; j < 19; j++){
                armies[i][0][j] = new Unit(4, "Sergeant", "4", current, 10, PieceType.SERJEANT);
            }
            for(int j = 19; j < 24; j++){
                armies[i][0][j] = new Unit(3, "Miner", "3", current, 50, PieceType.MINER);
            }
            for(int j = 24; j < 32; j++){
                armies[i][0][j] = new Unit(2, "Scout", "2", current, 15, PieceType.SCOUT);
            }
            armies[i][0][32] = new Unit(0, "Spy", "S", current, 50, PieceType.SPY);
            for(int j = 33; j < 39; j++){
                armies[i][0][j] = new Unit(11, "Bomb", "B", current, 40, PieceType.BOMB);
            }
            armies[i][0][39] = new Unit(-1, "Flag", "F", current, 1000, PieceType.FLAG);
        }
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

    public static void setupBoard(Unit[][] board, Unit[][][] armies){
        try {
            playerChoosePiece(board, armies[0][0]);
        }catch(Exception e){}
        aiSetup(board, armies[1][0]);
    }

    public static void playerChoosePiece(Unit[][] board, Unit[] army)throws IOException{
        int placed = 0;
        while(placed < 40){
            printBoard(board);
            System.out.println("You have have the following units left to place:\n");
            for(int i = 0; i < army.length; i++){
                if (!army[i].getPlaced()) {
                    System.out.print((i+1) + ". " + army[i].getName() +"\n");
                }
            }
            int unit = Integer.parseInt(stdin.readLine());
            if (unit > 40 || unit < 1) {
                System.out.println("This is an invalid input!!!");
                continue;
            }
            if(!army[unit - 1].getPlaced()) {
                if (playerChooseSpace(unit - 1, board, army)) {
                    placed++;
                }else{
                    System.out.println("This space is already taken!");
                    continue;
                }
            }else{
                System.out.println("That unit has already been placed!");
                continue;
            }
        }
    }

    public static boolean playerChooseSpace(int unit,Unit[][] board, Unit[] army)throws IOException{
        printBoard(board);
        System.out.print("The following spaces are where you can place the " + army[unit].getName() + ":\n");
        int k = 1;
        int x = -1;
        int y = -1;
        for(int i = 6; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++,k++){
                if(board[i][j] == null){
                    System.out.print(k + ". (" + (i + 1) + "," + (j + 1) + ")\n");
                }
            }
        }
        k = 1;
        int spot = Integer.parseInt(stdin.readLine());
        if (spot > 40 || spot < 1) {
            System.out.println("This is an invalid input!!!\nPlease input another number");
            spot = Integer.parseInt(stdin.readLine());
            while(spot > 40 || spot < 1) {
                System.out.println("This is an invalid input!!!\nPlease input another number");
                spot = Integer.parseInt(stdin.readLine());
            }
        }
        for(int i = 6; i < board.length && x != i; i++){
            for(int j = 0; j < board[i].length && y != j; j++,k++){
                if(k == spot){
                    x = i;
                    y = j;
                }
            }
        }
        if(board[x][y] == null) {
            board[x][y] = army[unit];
            army[unit].setPlaced(true);
            return true;
        }else{
            return false;
        }
    }

    public static void aiSetup(Unit[][] board, Unit[] army){
        int placed = 0;
        while(placed < 40){
            int unit = Randomizer.getRgen(40);
            if(!(army[unit].getPlaced())){
                int x = Randomizer.getRgen(4);
                int y = Randomizer.getRgen(10);
                if(board[x][y] == null){
                    board[x][y] = army[unit];
                    army[unit].setPlaced(true);
                    placed++;
                }
            }
        }
    }

    /**
     * Bill this is the method that let's the player make a move.
     * @param Unit[][] board The current state of the board.
     * @param Unit[][][] armies
     * @return boolean Whether the game has ended or not
     */
    public static void playerMove(Unit[][] board, Unit[][][] armies){

    }

    /**
     * Bill this is the method that let's the player make a move.
     * @param Unit[][] board The current state of the board.
     * @param Unit[][][] armies
     * @return boolean Whether the game has ended or not
     */
    public static void aiMove(Unit[][] board, Unit[][][] armies){

    }

    public static void buildShadowBoard(Unit[][] board, Unit[][] sb, Unit[] sa){
        BufferedReader[] readers = new BufferedReader[12];
        try{

            ClassLoader classLoader = Driver.class.getClassLoader();
            File file = new File(classLoader.getResource("MarshallStats.txt").getFile());
            readers[0] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("GeneralStats.txt").getFile());
            readers[1] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("ColonelStats.txt").getFile());
            readers[2] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("MajorStats.txt").getFile());
            readers[3] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("CaptainStats.txt").getFile());
            readers[4] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("LieutenantStats.txt").getFile());
            readers[5] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("SerjeantStats.txt").getFile());
            readers[6] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("MinerStats.txt").getFile());
            readers[7] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("ScoutStats.txt").getFile());
            readers[8] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("SpyStats.txt").getFile());
            readers[9] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("BombStats.txt").getFile());
            readers[10] = new BufferedReader(new FileReader(file));
            file = new File(classLoader.getResource("FlagStats.txt").getFile());
            readers[11] = new BufferedReader(new FileReader(file));
            try{
                int count = 0;
                for(int i = 0; i < board.length; i++){
                    for(int j = 0; j < board[i].length; j++){
                        if(board[i][j] != null){
                            if(board[i][j].getOwner() == Players.AI){
                                sb[i][j] = board[i][j];
                            }else if (board[i][j].getOwner() == Players.PLAYER){
                                int[][] info = new int[12][5];
                                for(int k = 0; k < readers.length; k++){
                                    String s = readers[k].readLine();
                                    String[] tokenize = s.split(",");
                                    for(int l = 0; l < tokenize.length; l++){
                                        info[k][l] = Integer.parseInt(tokenize[l]);
                                    }
                                    switch(k){
                                        case 0:
                                            info[k][3] = 100;
                                            info[k][4] = 1;
                                            break;

                                        case 1:
                                            info[k][3] = 90;
                                            info[k][4] = 1;
                                            break;

                                        case 2:
                                            info[k][3] = 75;
                                            info[k][4] = 2;
                                            break;

                                        case 3:
                                            info[k][3] = 50;
                                            info[k][4] = 3;
                                            break;

                                        case 4:
                                            info[k][3] = 40;
                                            info[k][4] = 4;
                                            break;

                                        case 5:
                                            info[k][3] = 20;
                                            info[k][4] = 4;
                                            break;

                                        case 6:
                                            info[k][3] = 10;
                                            info[k][4] = 4;
                                            break;

                                        case 7:
                                            info[k][3] = 50;
                                            info[k][4] = 5;
                                            break;

                                        case 8:
                                            info[k][3] = 15;
                                            info[k][4] = 8;
                                            break;

                                        case 9:
                                            info[k][3] = 50;
                                            info[k][4] = 1;
                                            break;

                                        case 10:
                                            info[k][3] = 40;
                                            info[k][4] = 6;
                                            break;

                                        case 11:
                                            info[k][3] = 1000;
                                            info[k][4] = 1;
                                            break;
                                    }
                                }
                                double score = 0;
                                int num = 0;
                                for(int k = 0; k < info.length; k++){
                                    score += ((1.0 * info[k][0] * info[k][3] * info[k][4]) / (info[k][1] + info[k][2]));
                                    num += info[k][4];
                                }
                                score /= num;
                                sb[i][j] = new Unit(score, Players.PLAYER, info);
                                sa[count] = sb[i][j];
                                count++;
                            }
                        }
                    }
                }
                for(int i = 0; i < readers.length; i++){
                    readers[i].close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("file not found");
        }
    }
}
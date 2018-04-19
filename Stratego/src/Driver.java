import java.io.*;
import java.util.ArrayList;

/**
 * This class contains the main method and runs the entire program.
 * Created by Steven Bruman on 1/24/2018.
 * Edited by Steven Bruman and William Jacobs
 * Version 4/18/2018
 */
public class Driver {

    public static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    /*
     * An input reader to receive user input.
     * It is an instance variable so that it
     * can be accessed from all methods of the
     * Driver class.
     */

    /**
     * This is the main method which makes use of processMenuCommand() method method.
     * @param args Unused.
     * @exception IOException On input error.
     * @see IOException
     */
    public static void main(String[] args) throws IOException {

        /*Main method*/
        boolean finished = false;   /*True if the user chooses to end the game, otherwise false*/
        while (! finished) {

            /*While the user chooses to not exit the game*/
            printGameMenu();                                    //Prints game menu so user can see options
            int command = Integer.parseInt(stdin.readLine());   //Gets user input
            System.out.println(command);
            finished = processMenuCommand(command);             //Processes user input
        }   /*End while loop*/
    }   /*End main method*/

    /**
     * This method processes the menu command chosen by the user.
     * 1 plays the game, 2 show the rules, 3 ends the game session.
     * @param command The command chosen by the user at the game menu.
     * @return boolean Whether or not the user chose to end the game session.
     */
    public static boolean processMenuCommand(int command){
        boolean wantToQuit = false;     //Turns true when command == 3

        switch(command){

            /*Switches on user input*/
            case 1:
                playGame();             /*Starts a new game*/
                break;  //Stops the switch statement

            case 2:
                showRules();            /*Displays the rules to the user*/
                break;  //Stops the switch statement

            case 3:
                wantToQuit = true;      /*Ends the programs*/
                break;  //Stops the switch statement
        }   /*End switch statement*/
        return wantToQuit;
    }   /*End processMenuCommand method*/

    /**
     * This method will be used to print the rules of the
     * game so that a player can understand how to play.
     */
    public static void showRules(){
        System.out.println("You have a board that is 10X10, " +
                "there are two 4 space square lakes evenly spread in the center of the board which neither player can cross into, \n" +
                "there are two players you and the computer, each player starts with 40 pieces: \n" +
                "1 Flag, 6 Bombs, 8 Scouts, 5 Miners, 4 Sergeants, 4 Lieutenants, 4 Captains, 3 Majors, 2 Colonels, 1 General, and 1 Marshall; \n" +
                "a flag is your most important piece, the goal of the game is to catch your opponents flag, \n" +
                "bombs will destroy any piece that attacks it except a miner which is only stronger than a scout, \n" +
                "bombs and flags are stationary while every other piece can move one space at a time except for a scout \n" +
                "which can move as far as it would like in any direction but only being able to attack another piece if it is directly in front of it, \n" +
                "your opponents pieces are a complete mystery to you until you attack, \n" +
                "then both you and your opponents pieces are reveled and a winner is selected based off of the piece hierarchy, \n" +
                "pieces listed above are in order of weakest to strongest starting with the scouts. \n" +
                "A piece cannot travel between two spaces for more than 2 consecutive turns meaning \n" +
                "if you move a piece forward and then back you cannot move it forward again you must move in a different direction or move a different piece all together.");
    }   /*End showRules method*/

    /**
     * This method starts the game. It allows the player to set up
     * the board, then sets up the AI. It randomly selects who has
     * the first turn. It then alternates between the players until
     * the game ends.
     */
    public static void playGame(){
        Unit[][][] armies = new Unit[2][2][40];
        /*
         * This 3D array is used to store the pieces of both the user
         * and the AI. The first array has indices 0 and 1. 0 holds a
         * 2D array of the user's pieces. 1 holds a 2D array of the AI's
         * pieces. These secondary arrays also have indices 0 and 1. 0
         * holds an array of that player's pieces that are currently in play.
         * 1 holds an array of that player's pieces that have been captured.
         */
        setupArmies(armies);    /*Creates the Units for each player*/

        Unit[][] board = new Unit[10][10];
        /*
         * This 2D array is meant to represent the game board. The first
         * array's indices refer to X-coordinates. The secondary arrays'
         * indices refer to Y-coordinates.
         */

        setupBoard(board, armies);            /*Allows the user to set up their side of the board and sets up the AI's side*/

        printBoard(board);                    /*Displays the current state of the game board to the user*/

        int turn = Randomizer.getRgen(2);   /*Returns a random int between 0 and (2 - 1)*/

        /*Switches on turn which is randomly 0 or 1*/
        switch (turn){
            case 0:     /*The user goes first*/
                System.out.println("The Player goes first.");
                break;  //Stops the switch statement

            case 1:     /*The AI goes first*/
                System.out.println("The Computer goes first.");
                break;  //Stops the switch statement
        }   /*End switch statement*/

        Unit[][] shadowBoard = new Unit[10][10];
        Unit[] shadowArmy = new Unit[40];

        buildShadowBoard(board, shadowBoard, shadowArmy);
        /*for(int i = 0; i < shadowArmy.length; i++){
            System.out.println(shadowArmy[i].getScore());
        }   /*End for loop*/

        boolean end = false;
        // Allows us to remember the last 3 moves to prevent switching between the same spaces each turn.
        ArrayList<Moves> memoryAI = new ArrayList<>(), memoryP = new ArrayList<>(), shadowAI = new ArrayList<>(), shadowP = new ArrayList<>();
        boolean playerEnd = false;
        boolean aiEnd = false;
        while(!playerEnd && !aiEnd){
            switch (turn){
                case 0:     //Player's turn
                    System.out.println("Your turn:");
                    try {
                        playerEnd = playerMove(board, armies, shadowBoard, shadowArmy, memoryP, shadowP);
                    }catch (IOException ioe){}
                    System.out.print("\n\n\n");     //Just some spacing
                    break;  //Stops the switch statement

                case 1:     //AI's turn
                    System.out.println("The computer's turn:");
                    aiEnd = aiMove(board, armies, shadowBoard, shadowArmy, memoryAI, shadowAI, shadowP);
                    System.out.print("\n\n\n");     //Just some spacing
                    break;  //Stops the switch statement
            }   /*End switch statement*/
            turn = (turn + 1) % 2;
        }   /*End for loop*/
        if(playerEnd){
            System.out.println("You have won the game!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }else{
            System.out.println("The computer has won. Now go contemplate your life in the corner.");
        }
    }   /*End playGame method*/

    /**
     * This method prints the game menu for the user.
     */
    public static void printGameMenu(){
        System.out.println("Hello and welcome to Bill's and Steven's game of Stratego.");
        System.out.println("Here you will be playing against an AI that we have created.");
        System.out.println("Please try and record wins and losses as well as anything strange \nor unique that the AI does.");
        System.out.println("Have fun and good luck!");
        System.out.println("1. Play game.\n2. Rules.\n3. Exit.");
    }   /*End printGameMenu method*/

    /**
     * This method creates the Units for both players and allows them
     * to set up the board.
     * @param armies The 3D array of Units that is divided (Player, AI), (Alive, Dead), actual pieces.
     */
    public static void setupArmies(Unit[][][] armies){
        for(int i = 0; i < armies.length; i++){     /*Loops through both players*/
            Players current;
            /*
             * PLAYER or AI
             * Used to assign ownership to the pieces being created.
             * This will be used during later when searching the board.
             */
            if(i == 0){
                current = Players.PLAYER;
            }else{
                current = Players.AI;
            }   /*End if-else statement*/
            armies[i][0][0] = new Unit(10, "Marshall", "10", current, 100, PieceType.MARSHALL);
            /*
             * Creates 1 Marshall Unit
             */
            armies[i][0][1] = new Unit(9, "General", "9", current, 90, PieceType.GENERAL);
            /*
             * Creates 1 General Unit
             */
            for(int j = 2; j < 4; j++){
                armies[i][0][j] = new Unit(8, "Colonel", "8", current, 75, PieceType.COLONEL);
                /*
                 * Creates 2 Colonel Units
                 */
            }   /*End inner for loop*/

            for(int j = 4; j < 7; j++){
                armies[i][0][j] = new Unit(7, "Major", "7", current, 50, PieceType.MAJOR);
                /*
                 * Creates 3 Major Units
                 */
            }   /*End inner for loop*/

            for(int j = 7; j < 11; j++){
                armies[i][0][j] = new Unit(6, "Captain", "6", current, 40, PieceType.CAPTAIN);
                /*
                 * Creates 4 Captain Units
                 */
            }   /*End inner for loop*/

            for(int j = 11; j < 15; j++){
                armies[i][0][j] = new Unit(5, "Lieutenant", "5", current, 20, PieceType.LIEUTENANT);
                    /*
                     * Creates 4 Lieutenant Units
                     */
            }   /*End inner for loop*/

            for(int j = 15; j < 19; j++){
                armies[i][0][j] = new Unit(4, "Serjeant", "4", current, 10, PieceType.SERJEANT);
                    /*
                     * Creates 4 Serjeant Units
                     */
            }   /*End inner for loop*/

            for(int j = 19; j < 24; j++){
                armies[i][0][j] = new Unit(3, "Miner", "3", current, 50, PieceType.MINER);
                /*
                 * Creates 5 Miner Units
                 */
            }   /*End inner for loop*/

            for(int j = 24; j < 32; j++){
                armies[i][0][j] = new Unit(2, "Scout", "2", current, 15, PieceType.SCOUT);
                    /*
                     * Creates 1 Scout Unit
                     */
            }   /*End inner for loop*/

            armies[i][0][32] = new Unit(0, "Spy", "S", current, 50, PieceType.SPY);
            /*
             * Creates 1 Marshall Unit
             */
            for(int j = 33; j < 39; j++){
                armies[i][0][j] = new Unit(11, "Bomb", "B", current, 40, PieceType.BOMB);
                /*
                 * Creates 6 Bomb Units
                 */
            }   /*End inner for loop*/

            armies[i][0][39] = new Unit(-1, "Flag", "F", current, 2000, PieceType.FLAG);
            /*
             * Creates 1 Flag Unit
             */
        }   /*End outer for loop*/
    }

    /**
     * This method prints the game board at the current state for
     * the player to view. It prints the user's pieces as the value
     * and the AI's pieces as A. It also prints a coordinate system
     * around the outside of the board to help the player as well as
     * L's to represent the lakes in the center of the board.
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     */
    public static void printBoard(Unit[][] board){
        for(int i = -1; i < board.length + 1; i++){
            /*
             * Loops through x-coordinates.
             * Starts at -1 and ends at (board.length + 1)
             * in order to print a coordinate system around
             * the edge of the board.
             */
            for(int j = -1; j < 10 + 1; j++){
                /*
                 * Loops through y-coordinates.
                 * Starts at -1 and ends at (board.length + 1)
                 * in order to print a coordinate system around
                 * the edge of the board.
                 */
                if(i == -1 || i == board.length){
                    /*
                     * If i at one of the coordinate
                     * spots rather than on the actual
                     * board.
                     */
                    if(j != -1 && j != 10){
                        /*
                         * If this is not one of the 4 corners,
                         * then print the y-coordinate.
                         */
                        System.out.print((j + 1) + "\t");
                    }else{
                        /*
                         * If this is a corner, print a tab.
                         */
                        System.out.print("\t");
                    }   /*End inner if-else statement*/

                }else if(j == -1 || j == board[i].length){
                     /*
                     * If j at one of the coordinate
                     * spots rather than on the actual
                     * board, print the x-coordinate.
                     */
                    System.out.print((i + 1) + "\t");
                }else if((i == 4 || i == 5) && (j == 2 || j == 3 || j ==6 || j == 7 )){
                    /*
                     * If this is one of the lake spaces, print L.
                     */
                    System.out.print("L" + "\t");
                }else if(board[i][j] != null){
                    /*
                     * If there is a Unit at this space.
                     */
                    switch (board[i][j].getOwner()) {
                        /*
                         * Check the owner of the Unit.
                         */
                        case PLAYER:
                            /*
                             * If owned by the user, print the piece.
                             */
                            System.out.print(board[i][j].getCharacter() + "\t");
                            break;  //Stops the switch statement

                        case AI:
                            /*
                             * If owned by the AI, print A to conceal it from
                             * the user's view.
                             */
                            System.out.print("A" + "\t");
                            //System.out.print(board[i][j].getCharacter() + "\t");
                            break;  //Stops the switch statement
                    }   /*End switch statement*/
                }else{
                    System.out.print("\t");
                }   /*End outer if-else statement*/
            }   /*End inner for loop*/
            System.out.println("");
            System.out.println("");
        }   /*End outer for loop*/
    }   /*End printBoard method*/

    /**
     * This method calls the initial set up methods for both the
     * user and AI.
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     * @param armies The 3D array of Units that is divided (Player, AI), (Alive, Dead), actual pieces.
     */
    public static void setupBoard(Unit[][] board, Unit[][][] armies){
        //aiSetup(board, armies[1][0]);   /*Sets up the AI's pieces*/

        printBoard(board);
        try {
            /*
             * This is for input exceptions in playerChoosePiece()
             * and playerChooseSpace().
             */
            playerChoosePiece(board, armies[0][0]);     /*Allows the user to set up their pieces*/
        }catch(Exception e){}           /*End try-catch block*/
        aiSetup(board, armies[1][0]);   /*Sets up the AI's pieces*/
        board[4][2]= new Unit(Players.LAKE);
        board[4][3]= new Unit(Players.LAKE);
        board[4][6]= new Unit(Players.LAKE);
        board[4][7]= new Unit(Players.LAKE);
        board[5][2]= new Unit(Players.LAKE);
        board[5][3]= new Unit(Players.LAKE);
        board[5][6]= new Unit(Players.LAKE);
        board[5][7]= new Unit(Players.LAKE);
    }   /*End setupBoard method*/

    /**
     * This method prints the available Units that are left to set up and allows the
     * user to select one.
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     * @param army The array of Units that represents the pieces of the user.
     * @exception IOException On input error.
     * @see IOException
     */
    public static void playerChoosePiece(Unit[][] board, Unit[] army)throws IOException{
        int placed = 0;     /*Number of pieces that the user has placed*/
        while(placed < 40){
            /*
             * There are 40 pieces per player, so as
             * long as the user has not placed 40
             * pieces, this loop will continue to run.
             */
            printBoard(board);  /*Allows the user to view the board*/
            System.out.println("You have have the following units left to place:\n");
            for(int i = 0; i < army.length; i++){
                /*
                 * Loops through the user's pieces
                 */
                if (!army[i].getPlaced()) {
                    /*
                     * If the piece has not been placed, then
                     * display it to the user along with it's
                     * (index + 1). This will form a menu-like
                     * application and allow the user to select
                     * what piece they would like to place.
                     */
                    System.out.print((i+1) + ". " + army[i].getName() +"\n");
                }   /*End if statement*/
            }   /*End inner for loop*/

            int unit = Integer.parseInt(stdin.readLine());  /*Gets user's selection choice*/
            if (unit > 40 || unit < 1) {
                /*
                 * If the number is invalid, say so and try again.
                 */
                System.out.println("This is an invalid input!!!");
                continue;
            }   /*End if statement*/

            if(!army[unit - 1].getPlaced()) {
                /*
                 * If the piece has not been placed.
                 */
                if (playerChooseSpace(unit - 1, board, army)) {
                    /*
                     * If the piece was placed in a valid location,
                     * increment placed.
                     */
                    placed++;
                }else{
                    /*
                     * If the piece was placed in an invalid
                     * location, say so and try again.
                     */
                    System.out.println("This space is already taken!");
                    continue;
                }   /*End inner if-else statement*/
            }else{
                /*
                 * If the piece has been placed, say so and try again.
                 */
                System.out.println("That unit has already been placed!");
                continue;
            }   /*End outer if-else statement*/
        }   /*End outer while loop*/
    }   /*End playerChoosePiece method*/

    /**
     * This method prints the available locations that are left on the board
     * in the user's initial area and allows the user to select one.
     * @param unit The index in army of the Unit that the player selected.
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     * @param army The array of Units that represents the pieces of the user.
     * @return boolean True if the Unit could be placed, otherwise false.
     * @exception IOException On input error.
     * @see IOException
     */
    public static boolean playerChooseSpace(int unit,Unit[][] board, Unit[] army)throws IOException{
        printBoard(board);  /*Allows the user to view the board*/
        System.out.print("The following spaces are where you can place the " + army[unit].getName() + ":\n");
        int k = 1;  /*Used to print selection input for the user*/
        int x = -1;
        /*
         * Used to remember x-coordinate of the user's selection.
         * Starts at -1 because is invalid input and will throw
         * an error if not changed. Used for error checking due to
         * this.
         */
        int y = -1;
        /*
         * Used to remember y-coordinate of the user's selection.
         * Starts at -1 because is invalid input and will throw
         * an error if not changed. Used for error checking due to
         * this.
         */
        for(int i = 6; i < board.length; i++){
            /*
             * Loops through x-coordinates.
             */
            for(int j = 0; j < board[i].length; j++,k++){
                /*
                 * Loops through y-coordinates.
                 */
                if(board[i][j] == null){
                    /*
                     * If the space does not have a Unit,
                     * print out (i,j) coordinates.
                     */
                    System.out.print(k + ". (" + (i + 1) + "," + (j + 1) + ")\n");
                }   /*End if statement*/
            }   /*End inner for loop*/
        }   /*End outer for loop*/
        k = 1;
        int spot = Integer.parseInt(stdin.readLine());  /*User input*/
        while(spot > 40 || spot < 1) {
            /*
             * Loops on invalid input and asks the user
             * to try again.
             */
            System.out.println("This is an invalid input!!!\nPlease input another number");
            spot = Integer.parseInt(stdin.readLine());  /*User input*/
        }   /*End while loop*/

        for(int i = 6; i < board.length && x != i; i++){
            /*
             * Loops through x-coordinates.
             */
            for(int j = 0; j < board[i].length && y != j; j++,k++){
                /*
                 * Loops through y-coordinates.
                 */
                if(k == spot){
                    /*
                     * If k is equal to the user's input, assign
                     * x to i and y to j.  This allows the program
                     * to correctly place units without the user
                     * having to enter (x,y) coordinates directly.
                     */
                    x = i;
                    y = j;
                }   /*End if statement*/
            }   /*End inner for loop*/
        }   /*End outer for loop*/

        if(board[x][y] == null) {
            /*
             * If the space chosen by the user is empty, then
             * assign the Unit that was selected earlier to it.
             * Tell the Unit that it was placed and return true.
             */
            board[x][y] = army[unit];
            army[unit].setPlaced(true);
            return true;
        }else{
            /*
             * If the space chosen by the user is not empty,
             * then return false.
             */
            return false;
        }   /*End if-else statement*/
    }   /*End playerChooseSpace method*/

    /**
     * This method randomly assigns all of the AI's pieces to valid starting locations.
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     * @param army The array of Units that represents the pieces of the AI.
     */
    public static void aiSetup(Unit[][] board, Unit[] army){
        double[][] values = new double[12][40];
        /*
         * Is used to hold the scores that the AI will use to set up
         */
        double[][] modified = new double[12][40];
        /*
         * Counts the number of times each value is the values 2D array
         * is modified. Will be used to normalize those values.
         */
        for(int i = 0; i < modified.length; i++){
            for(int j = 0; j < modified[i].length; j++){
                modified[i][j] = 1;
            } //End inner for loop
        }   //End outer for loop

        aiInitialValues(values);
        /*
         * Generates the initial values for the AI to use to place
         * pieces.
         */
        aiConfig(values, modified);
        /*
         * Modifies the values based on win rates of
         * different starting configurations
         */
        aiTeam(values, modified);
        /*
         * Modifies the values based on the win rates of
         * different team setups
         */
        for(int i = 0; i < values.length; i++){
            for (int j = 0; j < values[i].length; j++){
                values[i][j] /= modified[i][j];
            }   //End inner for loop
        }   //End outer for loop
        aiPlaceUnits(values, board, army);
        /*
         * Actually places the AI's units on the board
         */
    }   /*End aiSetup method*/

    /**
     * This method creates the AI's imperfect and statistical view of the game.
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     * @param sb (shadowBoard) The 2D array of Units that represents AI's view of the board.
     * @param sa (shadowArmy) The array of Units that represents the AI's view of the user's initial set up.
     */
    public static void buildShadowBoard(Unit[][] board, Unit[][] sb, Unit[] sa){
        BufferedReader[] readers = new BufferedReader[12];
        /*
         * This is an array of BufferedReaders.  These
         * will be used to read text files.
         */
        try{
            /*
             * Initializes each of the BufferedReader with a different text file.
             * These files contains the win,loss,draw statistics for each piece
             * on each starting space of the board.  There is 1 file per piece, and
             * there are 12 different pieces.  This means 12 BufferedReaders are
             * needed.
             */
            ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
            File file = new File(classLoader.getResource("MarshallStats.txt").getFile());
            readers[0] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Marshall Statistics
             */

            file = new File(classLoader.getResource("GeneralStats.txt").getFile());
            readers[1] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the General Statistics
             */

            file = new File(classLoader.getResource("ColonelStats.txt").getFile());
            readers[2] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Colonel Statistics
             */

            file = new File(classLoader.getResource("MajorStats.txt").getFile());
            readers[3] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Major Statistics
             */

            file = new File(classLoader.getResource("CaptainStats.txt").getFile());
            readers[4] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Captain Statistics
             */

            file = new File(classLoader.getResource("LieutenantStats.txt").getFile());
            readers[5] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Lieutenant Statistics
             */

            file = new File(classLoader.getResource("SerjeantStats.txt").getFile());
            readers[6] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Serjeant Statistics
             */

            file = new File(classLoader.getResource("MinerStats.txt").getFile());
            readers[7] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Miner Statistics
             */

            file = new File(classLoader.getResource("ScoutStats.txt").getFile());
            readers[8] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Scout Statistics
             */

            file = new File(classLoader.getResource("SpyStats.txt").getFile());
            readers[9] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Spy Statistics
             */

            file = new File(classLoader.getResource("BombStats.txt").getFile());
            readers[10] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Bomb Statistics
             */

            file = new File(classLoader.getResource("FlagStats.txt").getFile());
            readers[11] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Flag Statistics
             */

            try{
                /*
                 * Try-catch block in case input error exception such as
                 * type mismatches.
                 */
                int count = 0;  /*Index in the shadowArmy array*/
                for(int i = 0; i < board.length; i++){
                    /*
                     * Loops through x-coordinates of the board.
                     */
                    for(int j = 0; j < board[i].length; j++){
                        /*
                         * Loops through y-coordinates of the board.
                         */
                        if(board[i][j] != null){
                            /*
                             * If the space on the board contains a Unit.
                             */
                            if(board[i][j].getOwner() == Players.AI){
                                /*
                                 * If the Unit belongs to the AI, directly copy it
                                 * to the shadowBoard.
                                 */
                                sb[i][j] = board[i][j];
                            }else if (board[i][j].getOwner() == Players.PLAYER){
                                /*
                                 * If the Unit belongs to the user.
                                 */
                                int[][] info = new int[12][5];
                                /*
                                 * This 2D array will hold statistics used to calculate
                                 * theorectical Unit scores for AI.
                                 * The first level array refers the the different piece
                                 * types which is why it has a length of 12.
                                 * The secondary arrays will hold the infomation.
                                 * Index 0: wins, Index 1: losses, Index 2: draws,
                                 * Index 3: score, Index 4: amount.
                                 */
                                for(int k = 0; k < readers.length; k++){
                                    /*
                                     * Loops through the array of BufferedReaders and
                                     * allows each one to read a line from its file.
                                     */
                                    String s = readers[k].readLine();
                                    /*
                                     * Has to be read in a String due to how
                                     * BufferedReaders work.
                                     */
                                    String[] tokenize = s.split(",");
                                    /*
                                     * Splits the String into multiple Strings
                                     * based on ',' and stores them in an array.
                                     */
                                    for(int l = 0; l < tokenize.length; l++){
                                        /*
                                         * Loops through String[].
                                         */
                                        info[k][l] = Integer.parseInt(tokenize[l]);
                                        /*
                                         * Parses each String into an int and stores it
                                         * in the correct index int the info[][].
                                         */
                                    }   /*End fourth-level for loop*/

                                    switch(k){
                                        /*
                                         * Switches based on k because k represents the
                                         * current piece type.
                                         */
                                        case 0:     //Marshall
                                            info[k][3] = 100;
                                            info[k][4] = 1;
                                            break;  //Stops the switch statement

                                        case 1:     //General
                                            info[k][3] = 90;
                                            info[k][4] = 1;
                                            break;  //Stops the switch statement

                                        case 2:     //Colonel
                                            info[k][3] = 75;
                                            info[k][4] = 2;
                                            break;  //Stops the switch statement

                                        case 3:     //Major
                                            info[k][3] = 50;
                                            info[k][4] = 3;
                                            break;  //Stops the switch statement

                                        case 4:     //Captain
                                            info[k][3] = 40;
                                            info[k][4] = 4;
                                            break;

                                        case 5:     //Lieutenant
                                            info[k][3] = 20;
                                            info[k][4] = 4;
                                            break;  //Stops the switch statement

                                        case 6:     //Serjeant
                                            info[k][3] = 10;
                                            info[k][4] = 4;
                                            break;  //Stops the switch statement

                                        case 7:     //Miner
                                            info[k][3] = 50;
                                            info[k][4] = 5;
                                            break;  //Stops the switch statement

                                        case 8:     //Scout
                                            info[k][3] = 15;
                                            info[k][4] = 8;
                                            break;  //Stops the switch statement

                                        case 9:     //Spy
                                            info[k][3] = 50;
                                            info[k][4] = 1;
                                            break;  //Stops the switch statement

                                        case 10:    //Bomb
                                            info[k][3] = 40;
                                            info[k][4] = 6;
                                            break;  //Stops the switch statement

                                        case 11:    //Flag
                                            info[k][3] = 2000;
                                            info[k][4] = 1;
                                            break;  //Stops the switch statement
                                    }   /*End switch statement*/
                                }   /*End first third-level for loop*/

                                double score = 0;
                                /*
                                 * Used to store the theoretical score for this
                                 * starting position.
                                 */
                                int num = 0;
                                /*
                                 * Amount of total pieces that are used in the
                                 * calculation.
                                 */
                                for(int k = 0; k < info.length; k++){
                                    /*
                                     * Loops through outer info array.
                                     */
                                    score += ((1.0 * info[k][0] * info[k][3] * info[k][4]) / (info[k][1] + info[k][2]));
                                    /*
                                     * Part 1 of Prediction Heuristic
                                     * For each piece type do the following:
                                     * Product = wins * score * amount
                                     * Sum = losses + draws
                                     * Score = Product / Sum
                                     *
                                     * Then add the scores for each piece type together.
                                     */
                                    num += info[k][4];
                                    /*
                                     * Increase by the amount of pieces of this piece type.
                                     */
                                }   /*End second third-level for loop*/

                                score /= num;
                                /*
                                 * Part 2 of Prediction Heuristic
                                 * Divide the total score by the total amount of pieces.
                                 */
                                sb[i][j] = new Unit(score, Players.PLAYER, info);
                                /*
                                 * Use the second constructor of the Unit class
                                 * to make a theoretical Unit and place it on the
                                 * shadowboard.
                                 */
                                sa[count] = sb[i][j];
                                /*
                                 * Copy the theoretical Unit into the shadowArmy.
                                 * The shadowArmy will be used by the AI to remember
                                 * where of the theoretical Units on the shadowBoard
                                 * started. As the game progresses, the AI will improve
                                 * it's view of the board with new information.
                                 */
                                count++;        /*Increment index in shadowArmy array*/
                            }   /*End inner if-else statement*/
                        }   /*End outer if statement*/
                    }   /*End second-level for loop*/
                }   /*End first-level for loop*/

                for(int i = 0; i < readers.length; i++){
                    /*
                     * Loops through array of BufferedReaders and
                     * closes them.
                     */
                    readers[i].close();
                }   /*End for loop*/
            } catch (IOException ioe) {
                /*
                 * Catch input error exception.
                 */
                ioe.printStackTrace();
            }   /*End inner try-catch block*/
        } catch (FileNotFoundException fnfe) {
            /*
             * Catch file not found exception.
             */
            System.out.println("file not found");
        }   /*End outer try-catch block*/
        sb[4][2]= new Unit(Players.LAKE);
        sb[4][3]= new Unit(Players.LAKE);
        sb[4][6]= new Unit(Players.LAKE);
        sb[4][7]= new Unit(Players.LAKE);
        sb[5][2]= new Unit(Players.LAKE);
        sb[5][3]= new Unit(Players.LAKE);
        sb[5][6]= new Unit(Players.LAKE);
        sb[5][7]= new Unit(Players.LAKE);
    }   /*End buildShadowBoard method*/

    /**
     * This method generates the initial values that the AI will use to setup.  This is
     * similar to the buildShadowBoard method, but the values generated here will be
     * modified and slightly randomized in other methods.
     * @param values The 2D array that the AI will use to store the win rates of every piece in every starting spot
     */
    public static void aiInitialValues(double[][] values){
        BufferedReader[] readers = new BufferedReader[12];
        /*
         * This is an array of BufferedReaders.  These
         * will be used to read text files.
         */
        try{
            /*
             * Initializes each of the BufferedReader with a different text file.
             * These files contains the win,loss,draw statistics for each piece
             * on each starting space of the board.  There is 1 file per piece, and
             * there are 12 different pieces.  This means 12 BufferedReaders are
             * needed.
             */
            ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
            File file = new File(classLoader.getResource("MarshallStats.txt").getFile());
            readers[0] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Marshall Statistics
             */

            file = new File(classLoader.getResource("GeneralStats.txt").getFile());
            readers[1] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the General Statistics
             */

            file = new File(classLoader.getResource("ColonelStats.txt").getFile());
            readers[2] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Colonel Statistics
             */

            file = new File(classLoader.getResource("MajorStats.txt").getFile());
            readers[3] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Major Statistics
             */

            file = new File(classLoader.getResource("CaptainStats.txt").getFile());
            readers[4] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Captain Statistics
             */

            file = new File(classLoader.getResource("LieutenantStats.txt").getFile());
            readers[5] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Lieutenant Statistics
             */

            file = new File(classLoader.getResource("SerjeantStats.txt").getFile());
            readers[6] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Serjeant Statistics
             */

            file = new File(classLoader.getResource("MinerStats.txt").getFile());
            readers[7] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Miner Statistics
             */

            file = new File(classLoader.getResource("ScoutStats.txt").getFile());
            readers[8] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Scout Statistics
             */

            file = new File(classLoader.getResource("SpyStats.txt").getFile());
            readers[9] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Spy Statistics
             */

            file = new File(classLoader.getResource("BombStats.txt").getFile());
            readers[10] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Bomb Statistics
             */

            file = new File(classLoader.getResource("FlagStats.txt").getFile());
            readers[11] = new BufferedReader(new FileReader(file));
            /*
             * Creates a Reader for the Flag Statistics
             */

            try{
                /*
                 * Try-catch block in case input error exception such as
                 * type mismatches.
                 */
                int count = 0;  /*Index in the shadowArmy array*/
                for(int i = 0; i < readers.length; i++){
                    /*
                     * Loops through x-coordinates of the board.
                     */
                    for(int j = 0; j < values[i].length; j++){
                        /*
                         * Loops through y-coordinates of the board.
                         */

                                    /*
                                     * Loops through the array of BufferedReaders and
                                     * allows each one to read a line from its file.
                                     */
                                    String s = readers[i].readLine();
                                    /*
                                     * Has to be read in a String due to how
                                     * BufferedReaders work.
                                     */
                                    String[] tokenize = s.split(",");
                                    /*
                                     * Splits the String into multiple Strings
                                     * based on ',' and stores them in an array.
                                     */

                                        /*
                                         * Loops through String[].
                                         */
                                    values[i][j] += Double.parseDouble(tokenize[0]) / (Double.parseDouble(tokenize[1]) + Double.parseDouble(tokenize[2]));
                                        /*
                                         * Parses each String into an int and stores it
                                         * in the correct index int the info[][].
                                         */

                    }   /*End inner for loop*/
                }   /*End outer for loop*/

                for(int i = 0; i < readers.length; i++){
                    /*
                     * Loops through array of BufferedReaders and
                     * closes them.
                     */
                    readers[i].close();
                }   /*End for loop*/
            } catch (IOException ioe) {
                /*
                 * Catch input error exception.
                 */
                ioe.printStackTrace();
            }   /*End inner try-catch block*/
        } catch (FileNotFoundException fnfe) {
            /*
             * Catch file not found exception.
             */
            System.out.println("file not found");
        }   /*End outer try-catch block*/
    }   //End aiInitialValues method

    /**
     * This method will modify the values in the values array based on the
     * slightly randomized win rates of different starting configurations.
     * @param values The 2D array that the AI will use to store the win rates of every piece in every starting spot
     * @param modified The 2D array that counts the number of times each value in the values array was modified
     */
    public static void aiConfig(double[][] values, double[][] modified){
        try{
            ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
            File file = new File(classLoader.getResource("InitialSetups.txt").getFile());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            /*
             * Initializes the BufferedReader to read the text file.
             *
             */

            try{
                String s = reader.readLine();

                while(s != null){
                    String[] tokenize = s.split(",");

                    switch(tokenize[4]){
                        /*
                         * Y means the Marshall is in the front of the board for that setup
                         */
                        case "Y":
                            switch (tokenize[6]){
                                case "Left":
                                    /*
                                     * This means the Marshall is in the front left of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[0][39] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][39] += 1;
                                    values[0][38] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][38] += 1;
                                    values[0][37] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][37] += 1;
                                    values[0][29] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][29] += 1;
                                    values[0][28] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][28] += 1;
                                    values[0][27] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][27] += 1;
                                    break;  //Stops the switch statement

                                case "Right":
                                     /*
                                     * This means the Marshall is in the front right of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[0][32] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][32] += 1;
                                    values[0][31]+= (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][31] += 1;
                                    values[0][30] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][30] += 1;
                                    values[0][22] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][22] += 1;
                                    values[0][21] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][21] += 1;
                                    values[0][20] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][20] += 1;
                                    break;  //Stops the switch statement

                                default:
                                     /*
                                     * This means the Marshall is in the front center of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[0][36] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][36] += 1;
                                    values[0][35] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][35] += 1;
                                    values[0][34] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][34] += 1;
                                    values[0][33] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][33] += 1;
                                    values[0][26] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][26] += 1;
                                    values[0][25] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][25] += 1;
                                    values[0][24] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][24] += 1;
                                    values[0][23] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][23] += 1;
                                    break;  //Stops the switch statement
                            }   //End inner switch statement
                            break;  //Stops the switch statement

                        default:
                            /*
                             * This means the value was N which means the
                             * Marshall is in the back of the board for the setup
                             */
                            switch (tokenize[6]){
                                case "Left":
                                     /*
                                     * This means the Marshall is in the back left of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[0][19] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][19] += 1;
                                    values[0][18] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][19] += 1;
                                    values[0][17] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][19] += 1;
                                    values[0][9] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][9] += 1;
                                    values[0][8] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][9] += 1;
                                    values[0][7] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][9] += 1;
                                    break;  //Stops the switch statement

                                case "Right":
                                     /*
                                     * This means the Marshall is in the back right of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[0][12] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][12] += 1;
                                    values[0][11] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][11] += 1;
                                    values[0][10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][10] += 1;
                                    values[0][2] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][2] += 1;
                                    values[0][1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][1] += 1;
                                    values[0][0] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][0] += 1;
                                    break;  //Stops the switch statement

                                default:
                                     /*
                                     * This means the Marshall is in the back center of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[0][16] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][16] += 1;
                                    values[0][15] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][15] += 1;
                                    values[0][14] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][14] += 1;
                                    values[0][13] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][13] += 1;
                                    values[0][6] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][6] += 1;
                                    values[0][5] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][5] += 1;
                                    values[0][4] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][4] += 1;
                                    values[0][3] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[0][3] += 1;
                                    break;  //Stops the switch statement
                            }   //End inner switch statement
                            break;  //Stops the switch statement
                    }   //End first outer switch statement

                    switch(tokenize[5]){
                        case "Y":
                            /*
                             * Y means the General is in the front of the board
                             * for the setup
                             */
                            switch (tokenize[7]){
                                case "Left":
                                    /*
                                     * This means the General is in the front left of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[1][39] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][39] += 1;
                                    values[1][38] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][38] += 1;
                                    values[1][37] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][37] += 1;
                                    values[1][29] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][29] += 1;
                                    values[1][28] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][28] += 1;
                                    values[1][27] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][27] += 1;
                                    break;  //Stops the switch statement

                                case "Right":
                                    /*
                                     * This means the General is in the front right of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[1][32] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][32] += 1;
                                    values[1][31] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][31] += 1;
                                    values[1][30] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][30] += 1;
                                    values[1][22] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][22] += 1;
                                    values[1][21] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][21] += 1;
                                    values[1][20] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][20] += 1;
                                    break;  //Stops the switch statement

                                default:
                                    /*
                                     * This means the General is in the front center of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[1][36] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][36] += 1;
                                    values[1][35] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][35] += 1;
                                    values[1][34] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][34] += 1;
                                    values[1][33] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][33] += 1;
                                    values[1][26] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][26] += 1;
                                    values[1][25] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][25] += 1;
                                    values[1][24] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][24] += 1;
                                    values[1][23] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][23] += 1;
                                    break;  //Stops the switch statement
                            }   //End inner switch statement
                            break;  //Stops the switch statement

                        default:
                            /*
                             * This means the value was N and that the
                             * General is in the back of the board for the setup
                             */
                            switch (tokenize[7]){
                                case "Left":
                                    /*
                                     * This means the General is in the back left of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[1][19] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][19] += 1;
                                    values[1][18] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][18] += 1;
                                    values[1][17] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][17] += 1;
                                    values[1][9] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][9] += 1;
                                    values[1][8] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][8] += 1;
                                    values[1][7] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][7] += 1;
                                    break;  //Stops the switch statement

                                case "Right":
                                    /*
                                     * This means the General is in the back right of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[1][12] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][12] += 1;
                                    values[1][11] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][11] += 1;
                                    values[1][10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][10] += 1;
                                    values[1][2] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][2] += 1;
                                    values[1][1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][1] += 1;
                                    values[1][0] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][0] += 1;
                                    break;  //Stops the switch statement

                                default:
                                    /*
                                     * This means the General is in the back center of the board,
                                     * so the program modifies all of those values.
                                     */
                                    values[1][16] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][16] += 1;
                                    values[1][15] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][15] += 1;
                                    values[1][14] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][14] += 1;
                                    values[1][13] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][13] += 1;
                                    values[1][6] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][6] += 1;
                                    values[1][5] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][5] += 1;
                                    values[1][4] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][4] += 1;
                                    values[1][3] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[1][3] += 1;
                                    break;  //Stops the switch statement
                            }   //End inner switch statement
                            break;  //Stops the switch statement
                    }   //End second outer switch statement

                    int flagSpace = Integer.parseInt(tokenize[9]);
                    int k = 9;
                    boolean found = false;
                    for(int i = 0; !found && i < 4; i++) {
                        for (int j = 0; !found && j < 10; j++) {
                            /*
                             * Have to find the correct space on the board because
                             */
                            if (k == flagSpace) {
                                values[11][k] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                modified[11][k] += 1;
                                if(tokenize[10].equals("Y")){
                                    values[10][k - 1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[10][k - 1] += 1;
                                }   //End first inner if statement
                                if(tokenize[11].equals("Y")){
                                    values[10][k + 10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[10][k + 10] += 1;
                                }   //End second inner if statement
                                if(tokenize[12].equals("Y")){
                                    values[10][k + 1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[10][k + 1] += 1;
                                }   //End third inner if statement
                                if(tokenize[13].equals("Y")){
                                    values[10][k - 10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                    modified[10][k - 1] += 1;
                                }   //End fourth inner if statement
                            }   //End outer if statement
                        }   //End inner for loop
                        if (k % 10 == 0) {
                            k += 20;
                        }   //End if statement
                        k--;
                    }   //End outer for loop

                    s = reader.readLine();
                }   //End while loop
            reader.close();
            } catch (IOException ioe) {
                /*
                 * Catch input error exception.
                 */
                ioe.printStackTrace();
            }   /*End inner try-catch block*/
        } catch (FileNotFoundException fnfe) {
            /*
             * Catch file not found exception.
             */
            System.out.println("file not found");
        }   /*End outer try-catch block*/
    }   //End aiConfig method

    /**
     * This method will modify the values in the values array based on the
     * slightly randomized win rates of different starting configurations.
     * @param values The 2D array that the AI will use to store the win rates of every piece in every starting spot
     * @param modified The 2D array that counts the number of times each value in the values array was modified
     */
    public static void aiTeam(double[][] values, double[][] modified){
        BufferedReader[] readers = new BufferedReader[9];
        try{
            /*
             * Initializes each of the BufferedReader with a different text file.
             * These files contains the win,loss,draw statistics for each piece
             * on each starting space of the board.  There is 1 file per piece, and
             * there are 12 different pieces.  This means 12 BufferedReaders are
             * needed.
             */
            ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
            File file = new File(classLoader.getResource("BothLeftTeams.txt").getFile());
            readers[0] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("BothMiddleTeams.txt").getFile());
            readers[1] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("BothRightTeams.txt").getFile());
            readers[2] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("MarshallLeftTeams.txt").getFile());
            readers[3] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("MarshallMiddleTeams.txt").getFile());
            readers[4] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("MarshallRightTeams.txt").getFile());
            readers[5] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("GeneralLeftTeams.txt").getFile());
            readers[6] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("GeneralMiddleTeams.txt").getFile());
            readers[7] = new BufferedReader(new FileReader(file));

            file = new File(classLoader.getResource("GeneralRightTeams.txt").getFile());
            readers[8] = new BufferedReader(new FileReader(file));

            for(int i = 0; i < readers.length; i++){
                try{
                    String s = readers[i].readLine();

                    while(s != null){
                        String[] tokenize = s.split(",");

                        switch(i){
                            case 0:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][39] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                        values[j - 1][38] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][38] += 1;
                                        values[j - 1][37] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][37] += 1;
                                        values[j - 1][29] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][29] += 1;
                                        values[j - 1][28] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][28] += 1;
                                        values[j - 1][27] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][27] += 1;
                                    }else if(j == 1){
                                        values[j - 1][19] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][19] += 1;
                                        values[j - 1][18] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][18] += 1;
                                        values[j - 1][17] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][17] += 1;
                                        values[j - 1][9] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][9] += 1;
                                        values[j - 1][8] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][8] += 1;
                                        values[j - 1][7] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][7] += 1;
                                    }else if(j == 2){
                                        values[j - 1][19] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][19] += 1;
                                        values[j - 1][18] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][18] += 1;
                                        values[j - 1][17] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][17] += 1;
                                        values[j - 1][9] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][9] += 1;
                                        values[j - 1][8] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][8] += 1;
                                        values[j - 1][7] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][7] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 1:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][36] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][36] += 1;
                                        values[j - 1][35] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][35] += 1;
                                        values[j - 1][34] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][34] += 1;
                                        values[j - 1][33] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][33] += 1;
                                        values[j - 1][26] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][26] += 1;
                                        values[j - 1][25] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][25] += 1;
                                        values[j - 1][24] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][24] += 1;
                                        values[j - 1][23] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][23] += 1;
                                    }else if(j == 1){
                                        values[j - 1][16] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][16] += 1;
                                        values[j - 1][15] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][15] += 1;
                                        values[j - 1][14] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][14] += 1;
                                        values[j - 1][13] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][13] += 1;
                                        values[j - 1][6] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][6] += 1;
                                        values[j - 1][5] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][5] += 1;
                                        values[j - 1][4] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][4] += 1;
                                        values[j - 1][3] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][3] += 1;
                                    }else if(j == 2){
                                        values[j - 1][16] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][16] += 1;
                                        values[j - 1][15] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][15] += 1;
                                        values[j - 1][14] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][14] += 1;
                                        values[j - 1][13] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][13] += 1;
                                        values[j - 1][6] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][6] += 1;
                                        values[j - 1][5] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][5] += 1;
                                        values[j - 1][4] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][4] += 1;
                                        values[j - 1][3] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][3] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 2:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][32] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][32] += 1;
                                        values[j - 1][31] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][31] += 1;
                                        values[j - 1][30] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][30] += 1;
                                        values[j - 1][22] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][22] += 1;
                                        values[j - 1][21] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][21] += 1;
                                        values[j - 1][20] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][20] += 1;
                                    }else if(j == 1){
                                        values[j - 1][12] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][12] += 1;
                                        values[j - 1][11] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][11] += 1;
                                        values[j - 1][10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][10] += 1;
                                        values[j - 1][2] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][2] += 1;
                                        values[j - 1][1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][1] += 1;
                                        values[j - 1][0] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][0] += 1;
                                    }else if(j == 2){
                                        values[j - 1][12] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][12] += 1;
                                        values[j - 1][11] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][11] += 1;
                                        values[j - 1][10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][10] += 1;
                                        values[j - 1][2] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][2] += 1;
                                        values[j - 1][1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][1] += 1;
                                        values[j - 1][0] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][0] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 3:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][39] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                        values[j - 1][38] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                        values[j - 1][37] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                        values[j - 1][29] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                        values[j - 1][28] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                        values[j - 1][27] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                    }else if(j == 1){
                                        values[j - 1][19] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][19] += 1;
                                        values[j - 1][18] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][18] += 1;
                                        values[j - 1][17] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][17] += 1;
                                        values[j - 1][9] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][9] += 1;
                                        values[j - 1][8] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][8] += 1;
                                        values[j - 1][7] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][7] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 4:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][36] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][36] += 1;
                                        values[j - 1][35] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][35] += 1;
                                        values[j - 1][34] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][34] += 1;
                                        values[j - 1][33] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][33] += 1;
                                        values[j - 1][26] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][26] += 1;
                                        values[j - 1][25] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][25] += 1;
                                        values[j - 1][24] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][24] += 1;
                                        values[j - 1][23] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][23] += 1;
                                    }else if(j == 1){
                                        values[j - 1][16] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][16] += 1;
                                        values[j - 1][15] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][15] += 1;
                                        values[j - 1][14] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][14] += 1;
                                        values[j - 1][13] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][13] += 1;
                                        values[j - 1][6] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][6] += 1;
                                        values[j - 1][5] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][5] += 1;
                                        values[j - 1][4] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][4] += 1;
                                        values[j - 1][3] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][3] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 5:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][32] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][32] += 1;
                                        values[j - 1][31] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][31] += 1;
                                        values[j - 1][30] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][30] += 1;
                                        values[j - 1][22] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][22] += 1;
                                        values[j - 1][21] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][21] += 1;
                                        values[j - 1][20] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][20] += 1;
                                    }else if(j == 1){
                                        values[j - 1][12] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][12] += 1;
                                        values[j - 1][11] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][11] += 1;
                                        values[j - 1][10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][10] += 1;
                                        values[j - 1][2] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][2] += 1;
                                        values[j - 1][1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][1] += 1;
                                        values[j - 1][0] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][0] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 6:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][39] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][39] += 1;
                                        values[j - 1][38] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][38] += 1;
                                        values[j - 1][37] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][37] += 1;
                                        values[j - 1][29] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][29] += 1;
                                        values[j - 1][28] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][28] += 1;
                                        values[j - 1][27] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][27] += 1;
                                    }else if(j == 2){
                                        values[j - 1][19] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][19] += 1;
                                        values[j - 1][18] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][18] += 1;
                                        values[j - 1][17] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][17] += 1;
                                        values[j - 1][9] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][9] += 1;
                                        values[j - 1][8] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][8] += 1;
                                        values[j - 1][7] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][7] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 7:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][36] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][36] += 1;
                                        values[j - 1][35] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][35] += 1;
                                        values[j - 1][34] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][34] += 1;
                                        values[j - 1][33] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][33] += 1;
                                        values[j - 1][26] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][26] += 1;
                                        values[j - 1][25] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][25] += 1;
                                        values[j - 1][24] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][24] += 1;
                                        values[j - 1][23] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][23] += 1;
                                    }else if(j == 2){
                                        values[j - 1][16] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][16] += 1;
                                        values[j - 1][15] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][15] += 1;
                                        values[j - 1][14] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][14] += 1;
                                        values[j - 1][13] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][13] += 1;
                                        values[j - 1][6] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][6] += 1;
                                        values[j - 1][5] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][5] += 1;
                                        values[j - 1][4] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][4] += 1;
                                        values[j - 1][3] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][3] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement

                            case 8:
                                for(int j = 1; j < tokenize.length; j++){
                                    if(!(tokenize[j].equals("0"))){
                                        values[j - 1][32] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][32] += 1;
                                        values[j - 1][31] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][31] += 1;
                                        values[j - 1][30] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][30] += 1;
                                        values[j - 1][22] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][22] += 1;
                                        values[j - 1][21] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][21] += 1;
                                        values[j - 1][20] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][20] += 1;
                                    }else if(j == 2){
                                        values[j - 1][12] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][12] += 1;
                                        values[j - 1][11] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][11] += 1;
                                        values[j - 1][10] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][10] += 1;
                                        values[j - 1][2] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][2] += 1;
                                        values[j - 1][1] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][1] += 1;
                                        values[j - 1][0] += (Double.parseDouble(tokenize[0]) * ((Math.random() * 1.5) + 0.5));
                                        modified[j - 1][0] += 1;
                                    }   //End if-else statement
                                }   //End for loop
                                break;  //Stops the switch statement
                        }   //End switch statement

                        s = readers[i].readLine();
                    }   //End while loop
                    readers[i].close();
                } catch (IOException ioe) {
                /*
                 * Catch input error exception.
                 */
                    ioe.printStackTrace();
                }   /*End inner try-catch block*/
            }   //End for loop
        } catch (FileNotFoundException fnfe) {
            /*
             * Catch file not found exception.
             */
            System.out.println("file not found");
        }   //End outer try-catch block
    }   //End aiTeam method

    /**
     * This method places the AI's pieces on the board by selecting the
     * highest values that it finds.
     * @param values The 2D array that the AI will use to store the win rates of every piece in every starting spot
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     * @param army The array of Units that represents the AI's pieces.
     */
    public static void aiPlaceUnits(double[][] values, Unit[][] board, Unit[] army){
        int placed = 0;
        while(placed != 40) {
            double high = -1;
            int y = -1;
            int x = -1;
            int unit = -1;
            int place = -1;
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < values[i].length; j++) {
                    if (values[i][j] > high) {
                        high = values[i][j];
                        y = j / 10;
                        x = j % 10;
                        unit = i;
                        place = j;
                    }   //End if statement
                }   //End inner for loop
            }   //End outer for loop
            if(unit != -1){
                String name = "";
                switch (unit) {
                    case 0:
                        name = "Marshall";
                        break;  //Stops the switch statement

                    case 1:
                        name = "General";
                        break;  //Stops the switch statement

                    case 2:
                        name = "Colonel";
                        break;  //Stops the switch statement

                    case 3:
                        name = "Major";
                        break;  //Stops the switch statement

                    case 4:
                        name = "Captain";
                        break;  //Stops the switch statement

                    case 5:
                        name = "Lieutenant";
                        break;  //Stops the switch statement

                    case 6:
                        name = "Serjeant";
                        break;  //Stops the switch statement

                    case 7:
                        name = "Miner";
                        break;  //Stops the switch statement

                    case 8:
                        name = "Scout";
                        break;  //Stops the switch statement

                    case 9:
                        name = "Spy";
                        break;  //Stops the switch statement

                    case 10:
                        name = "Bomb";
                        break;  //Stops the switch statement

                    case 11:
                        name = "Flag";
                        break;  //Stops the switch statement
                }   //End switch statement
                boolean flag = false;
                for (int i = 0; !flag && i < army.length; i++) {
                    if (army[i].getName().equals(name) && !army[i].getPlaced()) {
                        board[y][x] = army[i];
                        army[i].setPlaced(true);
                        placed++;
                        for (int j = 0; j < values.length; j++) {
                            values[j][place] = -2;
                        }   //End inner for loop
                        flag = true;
                    }   //End if statement
                }   //End outer for loop
                if (!flag) {
                    for (int i = 0; i < values[unit].length; i++) {
                        values[unit][i] = -2;
                    }   //End for loop
                }   //End if statement
            }   //End the if statement
        }   //End the for loop
    }   //End aiPlaceUnits method

    /**
     * Lets the player make a move.
     * @param board 2 dimensional Unit object, The current state of the board.
     * @param armies 3 dimensional Unit object, [owner][y][x].
     * @param shadowBoard The 2D array of Units that represents AI's view of the board.
     * @param shadowArmy The array of Units that represents the AI's view of the user's initial set up.
     * @param memory ArrayList of Moves which stores pieces to retain pieceTypes rather than Moves from the
     *               generateMoves method.
     * @param shadow The ArrayList containing the last 2 moves of the player on the shadowboard
     * @return boolean Whether the game has ended or not
     * @exception IOException On input error.
     * @see IOException
     */
    public static boolean playerMove(Unit[][] board, Unit[][][] armies, Unit[][] shadowBoard, Unit[] shadowArmy, ArrayList<Moves> memory, ArrayList<Moves> shadow) throws IOException {
        ArrayList<Moves> options = moveFilter(board, Players.PLAYER, true, memory);
        int choice = Integer.parseInt(stdin.readLine()); // User input
        Moves piece = options.get(choice-1);
        Moves move;
        boolean end = false;
        int x = piece.getX();
        int y = piece.getY();
        boolean valid = true;
        ArrayList<Moves> moves = piece.generateMoves(memory);
        System.out.println("These are the moves you can make with your "+piece.getPiece().getName()+"("+(y+1)
                +", "+(x+1)+")"+" :");
        for(int i=0; i < moves.size(); i++) {
            Moves current = moves.get(i);
            System.out.println(i+1+". ("+(current.getY()+1)+", "+(current.getX()+1)+")");
        }   //End for loop
        choice = Integer.parseInt(stdin.readLine());
        move = moves.get(choice-1);
        if(move.getPiece() == null) {
            board[move.getY()][move.getX()] = piece.getPiece();
            shadowBoard[move.getY()][move.getX()] = shadowBoard[piece.getY()][piece.getX()];
        } else {

            //Updating the AI's knowledge
            if(shadowBoard[y][x].getName() == null){
                shadowBoard[y][x] = new Unit(board[y][x].getStrength(), board[y][x].getName(), board[y][x].getCharacter(), board[y][x].getOwner(), board[y][x].getScore(), board[y][x].getType());
                for(int i = 0; i < shadowArmy.length; i++){
                    if(shadowArmy[i].getName() == null){
                        shadowArmy[i].setAmount(shadowArmy[i].getAmount(board[y][x].getType()) - 1, board[y][x].getType());
                    }   //End inner if statement
                }   //End for loop
            }   //End outer if statement
            //Finished updating the AI's knowledge

            //Updating the AI's knowledge
            if(shadowBoard[piece.getY()][piece.getX()].getName() == null) {
                if (!shadowBoard[piece.getY()][piece.getX()].getHasMoved()) {
                    shadowBoard[piece.getY()][piece.getX()].moved();
                }   //End inner if statement
                if(!(piece.getX()+piece.getY() == (move.getX()+move.getY()-1) ||
                        piece.getX()+piece.getY() == (move.getX()+move.getY()+1))){
                    shadowBoard[piece.getY()][piece.getX()] = new Unit(2, "Scout", "S", Players.AI, 15, PieceType.SCOUT);
                    for(int i = 0; i < shadowArmy.length; i++) {
                        if (shadowArmy[i].getName() == null) {
                            shadowArmy[i].setAmount(shadowArmy[i].getAmount(PieceType.SCOUT) - 1, PieceType.SCOUT);
                        }   //End inner if statement
                    }   //End for loop
                }   //End inner if statement
            }   //End outer if statement
            //Finished updating the AI's knowledge
            Moves current = ruleBook(piece, move, Players.PLAYER, true);
            if(move.getPiece().getType() == PieceType.FLAG){
                end = true;
            }else{
                end = false;
            }
            if(current == null) {
                board[y][x] = null;
                board[move.getY()][move.getX()] = null;
                shadowBoard[y][x] = null;
                shadowBoard[move.getY()][move.getX()] = null;
            } else {
                board[move.getY()][move.getX()] = current.getPiece();
                shadowBoard[move.getY()][move.getX()] = shadowBoard[current.getY()][current.getX()];
            }   //End inner if-else statement
        }   //End outer if-else statement
        board[y][x] = null;
        shadowBoard[y][x] = null;

        printBoard(board);
        if(memory.size() > 1) {
            memory.remove(0);
        }
        memory.add(piece);
        if(shadow.size() > 1){
            shadow.remove(0);
        }
        shadow.add(new Moves(shadowBoard, piece.getY(), piece.getX()));
        return end;
    }   //End playerMove method

    /**
     * This method allows the AI to make a move. This happens by creating a State
     * to represent the current State of the board. This is then run a Minimax
     * algorithm utilizing alpha-beta pruning to select the best move for the AI
     * on its turn.
     * @param board The 2D Unit array that represents (X,Y) locations on the board.
     * @param armies The 3D array of Units that is divided (Player, AI), (Alive, Dead), actual pieces.
     * @param shadowBoard The 2D array of Units that represents AI's view of the board.
     * @param shadowArmy The array of Units that represents the AI's view of the user's initial set up.
     * @param memory The ArrayList containing the last 2 moves of the AI.
     * @param shadowAI The ArrayList containing the last 2 moves of the AI on the shadowboard.
     * @param shadowP The ArrayList containing the last 2 moves of the player on the shadowboard.
     * @return boolean Whether or not the game has ended.
     */
    public static boolean aiMove(Unit[][] board, Unit[][][] armies, Unit[][] shadowBoard, Unit[] shadowArmy, ArrayList<Moves> memory, ArrayList<Moves> shadowAI, ArrayList<Moves> shadowP) {
        State root = new State(shadowBoard, 0, 6, shadowAI, shadowP);
        int best = (int) root.getBestMove();
        int a = root.getOrigial();
        Moves piece = new Moves(board, root.getMoveable().get(a).getY(), root.getMoveable().get(a).getX());
        Moves move = new Moves(board, root.getAllMoves().get(best).getY(), root.getAllMoves().get(best).getX());
        boolean end = false;
        int x = piece.getX();
        int y = piece.getY();
        if(move.getPiece() == null) {
            board[move.getY()][move.getX()] = piece.getPiece();
            shadowBoard[move.getY()][move.getX()] = shadowBoard[piece.getY()][piece.getX()];
        } else {

            //Updating the AI's knowledge
            if(shadowBoard[move.getY()][move.getX()].getName() == null){
                shadowBoard[move.getY()][move.getX()] = new Unit(board[move.getY()][move.getX()].getStrength(),
                        board[move.getY()][move.getX()].getName(),
                        board[move.getY()][move.getX()].getCharacter(),
                        board[move.getY()][move.getX()].getOwner(),
                        board[move.getY()][move.getX()].getScore(),
                        board[move.getY()][move.getX()].getType());
                for(int i = 0; i < shadowArmy.length; i++){
                    if(shadowArmy[i].getName() == null){
                        shadowArmy[i].setAmount(shadowArmy[i].getAmount(board[move.getY()][move.getX()].getType()) - 1, board[move.getY()][move.getX()].getType());
                    }   //End inner if statement
                }   //End for loop
            }   //End outer fi statement
            //Finished updating the AI's knowledge

            Moves current = ruleBook(piece, move, Players.AI, true);
            if(move.getPiece().getType() == PieceType.FLAG){
                end = true;
            }else{
                end = false;
            }
            if(current == null) {
                board[y][x] = null;
                board[move.getY()][move.getX()] = null;
                shadowBoard[y][x] = null;
                shadowBoard[move.getY()][move.getX()] = null;
            } else {
                board[move.getY()][move.getX()] = current.getPiece();
                shadowBoard[move.getY()][move.getX()] = shadowBoard[current.getY()][current.getX()];
            }   //End inner if-else statement
        }   //End outer if-else statement
        board[y][x] = null;
        shadowBoard[y][x] = null;
        printBoard(board);
        if(shadowAI.size() > 1){
            shadowAI.remove(0);
        }
        shadowAI.add(new Moves(shadowBoard, piece.getY(), piece.getX()));
        return end;
    }   //End aiMove method

    public static ArrayList<Moves> moveFilter(Unit[][] board, Players player, boolean print, ArrayList<Moves> past){
        int index = 0;
        ArrayList<Moves> options = new ArrayList<>();
        if(print) {
            System.out.println("Pieces you can move : ");
        }   //End if statement
        for (int i = 0; i < board.length; i++) // Iterate through X coordinates of Board.
        {
            for(int j = 0; j < board[i].length; j++) // Iterate through Y coordinates of Board.
            {
                Unit current = board[i][j];
                if(current != null && current.getOwner() == player) // Is this the players piece?
                {   // Is this a piece that moves?
                        // Is this piece next to a lake?
                    if((current.getType() == null && current.getScore() != 11)|| (current.getType() != PieceType.FLAG && current.getType() != PieceType.BOMB)) {
                        // Can this piece move/fight? If so add piece to options and print to user.
                        // Checks for movement in the Down direction.
                        if (!(past.size() > 1 && i == past.get(0).getY() && j == past.get(0).getX() && (i+1) == past.get(1).getY() && j == past.get(1).getX()) && i < 9 && (board[i + 1][j] == null || (board[i + 1][j].getOwner() != player && board[i + 1][j].getOwner() != Players.LAKE))) {
                            if (player == Players.PLAYER && current.getName() != null) { // If it's players turn print message.
                                options.add(new Moves(board, i, j));// Count incremented below
                                if(print) {
                                    System.out.println(++index + ". " + current.getName() + "(" + (i + 1) + ", " + (j + 1) + ")");
                                }   //End inner if statement
                            } // Else just store move and increment count.
                            else {
                                options.add(new Moves(board, i, j));
                                index++;
                            }   //End inner if-else statement
                        }
                        // Checks for movement in the Up direction.
                        else if (!(past.size() > 1 && i == past.get(0).getY() && j == past.get(0).getX() && (i-1) == past.get(1).getY() && j == past.get(1).getX()) && i > 0 &&
                                (board[i - 1][j] == null || (board[i - 1][j].getOwner() != player  && board[i - 1][j].getOwner() != Players.LAKE))) {
                            if (player == Players.PLAYER) { // If it's players turn print message.
                                options.add(new Moves(board, i, j));// Count incremented below
                                if(print) {
                                    System.out.println(++index + ". " + current.getName() + "(" + (i + 1) + ", " + (j + 1) + ")");
                                }   //End inner if statement
                            } // Else just store move and increment count.
                            else {
                                options.add(new Moves(board, i, j));
                                index++;
                            }   //End inner if-else statement
                        }
                        // Checks for movement in the Right direction.
                        else if (!(past.size() > 1 && i == past.get(0).getY() && j == past.get(0).getX() && (i) == past.get(1).getY() && (j+1) == past.get(1).getX()) && j < 9 &&
                                (board[i][j + 1] == null || (board[i][j + 1].getOwner() != player && board[i][j + 1].getOwner() != Players.LAKE))) {
                            if (player == Players.PLAYER) { // If it's players turn print message.
                                options.add(new Moves(board, i, j));// Count incremented below
                                if(print) {
                                    System.out.println(++index + ". " + current.getName() + "(" + (i + 1) + ", " + (j + 1) + ")");
                                }   //End inner if statement
                            } // Else just store move and increment count.
                            else {
                                options.add(new Moves(board, i, j));
                                index++;
                            }   //End inner if-else statement
                        }
                        // Checks for movement in the Left direction.
                        else if (!(past.size() > 1 && i == past.get(0).getY() && j == past.get(0).getX() && (i) == past.get(1).getY() && (j-1) == past.get(1).getX()) && j > 0 && (board[i][j - 1] == null || (board[i][j - 1].getOwner() != player && board[i][j - 1].getOwner() != Players.LAKE))) {
                            if (player == Players.PLAYER) { // If it's players turn print message.
                                options.add(new Moves(board, i, j));// Count incremented below
                                if(print) {
                                    System.out.println(++index + ". " + current.getName() + "(" + (i + 1) + ", " + (j + 1) + ")");
                                }   //End inner if statement
                            } // Else just store move and increment count.
                            else {
                                options.add(new Moves(board, i, j));
                                index++;
                            }   //End inner if-else statement
                        }   //End outer if-else statement
                    }   //End inner if statement
                }   //End outer if statement
            }   //End inner for loop
        }   //End outer for loop
        return options;
    }   //End moveFilter method

    /**
     * ruleBook() serves to simplify the process of which pieces will occupy a space on the board at a given time.
     *
     * @param offense - a Moves object wishing to dominate defense for a space on the board.
     * @param defense - a Moves object which offense wishes to overtake on the board.
     * @param p - The player who owns the attacking piece.
     * @param print - Whether or not the results should be printed to the screen.
     * @return winner - the Moves object which dominates, null if both pieces strength's match.
     */
    public static Moves ruleBook(Moves offense, Moves defense, Players p, boolean print) {
        Moves winner = null;
        String s = "";
        if(print) {
            if (p == Players.PLAYER) {
                s = "Your ";
            } else {
                s = "The AI's ";
            }   //End inner if-else statement
            s += offense.getPiece().getName() + " attacks ";
            if (p == Players.PLAYER) {
                s += "the AI's ";
            } else {
                s += "your ";
            }   //End inner if-else statement
            s += defense.getPiece().getName();
        }
        if(defense.getPiece().getType() == null || offense.getPiece().getType() == null){
            if(offense.getPiece().getType() == null && offense.getPiece().getStrength() == 3 && defense.getPiece().getType() == PieceType.BOMB){
                winner = offense;
            }else if(offense.getPiece().getType() == null && offense.getPiece().getStrength() == 1 && defense.getPiece().getType() == PieceType.MARSHALL){
                winner = offense;
            }else if (defense.getPiece().getType() == null && defense.getPiece().getStrength() == 10 && offense.getPiece().getType() == PieceType.SPY){
                winner = offense;
            }else if (defense.getPiece().getStrength() == offense.getPiece().getStrength()) {
                winner = null;
            } else if (defense.getPiece().getStrength() < offense.getPiece().getStrength()) {
                winner = offense;
            } else {
                winner = defense;
            }   //End inner if-else statement
        }else {
            switch (defense.getPiece().getType()) {
                case BOMB:
                    if (offense.getPiece().getType() == PieceType.MINER) {
                        winner = offense;
                        if(print){
                            s += " and wins!";
                        }   //End if statement
                    } else {
                        winner = defense;
                        if(print){
                            s += " and loses.";
                        }   //End if statement
                    }   //End inner if-else statement
                    break;
                case MARSHALL:
                    if (offense.getPiece().getType() == PieceType.SPY) {
                        winner = offense;
                        if(print){
                            s += " and wins!";
                        }   //End if statement
                    } else {
                        winner = defense;
                        if(print){
                            s += " and loses.";
                        }   //End if statement
                    }   //End inner if-else statement
                    break;
                case SPY:
                    winner = offense;
                    if(print){
                        s += " and wins!";
                    }   //End if statement
                    break;
                case FLAG:
                    winner = offense;
                    if(print){
                        s += " and wins!";
                    }   //End if statement
                    break;
                default:
                    if (defense.getPiece().getStrength() == offense.getPiece().getStrength()) {
                        winner = null;
                        if(print){
                            s += " and both die.";
                        }   //End if statement
                    } else if (defense.getPiece().getStrength() < offense.getPiece().getStrength()) {
                        winner = offense;
                        if(print){
                            s += " and wins!";
                        }   //End if statement
                    } else {
                        winner = defense;
                        if(print){
                            s += " and loses.";
                        }   //End if statement
                    }   //End inner if-else statement
            }   //End switch statement
        }   //End outer if-else statement
        if(print){
            System.out.println(s);
        }   //End if statement
        return winner;
    }   //End ruleBook method
}   /*End Driver class*/


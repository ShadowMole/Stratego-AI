import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Mole on 1/24/2018.
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
        for(int i = 0; i < shadowArmy.length; i++){
            System.out.println(shadowArmy[i].getScore());
        }   /*End for loop*/

        boolean end = false;

        for(;!end; turn = (turn + 1) % 2){
            switch (turn){
                case 0:
                   playerMove(board, armies);
                    break;  //Stops the switch statement

                case 1:
                    aiMove(board, armies);
                    break;  //Stops the switch statement
            }   /*End switch statement*/
        }   /*End for loop*/
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
            armies[i][0][0] = new Unit(10, "Marshal", "10", current, 100, PieceType.MARSHALL);
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
                armies[i][0][j] = new Unit(4, "Sergeant", "4", current, 10, PieceType.SERJEANT);
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

            armies[i][0][39] = new Unit(-1, "Flag", "F", current, 1000, PieceType.FLAG);
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
                            //System.out.print("A" + "\t");
                            System.out.print(board[i][j].getCharacter() + "\t");
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
        aiSetup(board, armies[1][0]);
        printBoard(board);
       // try {
            /*
             * This is for input exceptions in playerChoosePiece()
             * and playerChooseSpace().
             */
           // playerChoosePiece(board, armies[0][0]);     /*Allows the user to set up their pieces*/
        //}catch(Exception e){}           /*End try-catch block*/
        //aiSetup(board, armies[1][0]);   /*Sets up the AI's pieces*/
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
        int placed = 0;     /*Number of pieces that the user has placed*/
        int opt;
        int rand = Randomizer.getRgen(100);
        if(rand >= 60){
            opt = 1;
        }else if(rand >= 20){
            opt = 2;
        }else if(rand >= 5){
            opt = 3;
        }else{
            opt = 4;
        }

        String[] config = aiConfiguration(opt);

        int flagSpace = Integer.parseInt(config[9]);
        int k = 9;
        boolean found = false;
        for(int i = 0; !found && i < board.length; i++){
            for(int j = 0; !found && j < board[i].length; j++){
                if(k == flagSpace){
                    for(int l = 0; l < army.length; l++){
                        if(army[l].getType() == PieceType.FLAG){
                            board[i][j] = army[l];
                            army[l].setPlaced(true);
                            placed++;
                        }
                    }
                    if(config[10].equals("Y")){
                        for(int l = 0; l < army.length; l++){
                            if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                board[i][j+1] = army[l];
                                army[l].setPlaced(true);
                                placed++;
                                break;
                            }
                        }
                    }
                    if(config[11].equals("Y")){
                        for(int l = 0; l < army.length; l++){
                            if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                board[i+1][j] = army[l];
                                army[l].setPlaced(true);
                                placed++;
                                break;
                            }
                        }
                    }
                    if(config[12].equals("Y")){
                        for(int l = 0; l < army.length; l++){
                            if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                board[i][j-1] = army[l];
                                army[l].setPlaced(true);
                                placed++;
                                break;
                            }
                        }
                    }
                    if(config[13].equals("Y")){
                        for(int l = 0; l < army.length; l++){
                            if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                board[i-1][j] = army[l];
                                army[l].setPlaced(true);
                                placed++;
                                break;
                            }
                        }
                    }
                }
                if(k % 10 == 0){
                    k += 20;
                }
                k--;
            }
        }
        String[][] teams = aiTeamSelect(config, opt);

        while(teams == null){
            placed = 0;
            for(int i = 0; i < army.length; i++){
                army[i].setPlaced(false);
            }
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < board[i].length; j++){
                    board[i][j] = null;
                }
            }
            rand = Randomizer.getRgen(100);
            if(rand >= 60){
                opt = 1;
            }else if(rand >= 20){
                opt = 2;
            }else if(rand >= 5){
                opt = 3;
            }else{
                opt = 4;
            }

            config = aiConfiguration(opt);

            flagSpace = Integer.parseInt(config[9]);
            k = 9;
            found = false;
            for(int i = 0; !found && i < board.length; i++){
                for(int j = 0; !found && j < board[i].length; j++){
                    if(k == flagSpace){
                        for(int l = 0; l < army.length; l++){
                            if(army[l].getType() == PieceType.FLAG){
                                board[i][j] = army[l];
                                army[l].setPlaced(true);
                                placed++;
                            }
                        }
                        if(config[10].equals("Y")){
                            for(int l = 0; l < army.length; l++){
                                if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                    board[i][j+1] = army[l];
                                    army[l].setPlaced(true);
                                    placed++;
                                    break;
                                }
                            }
                        }
                        if(config[11].equals("Y")){
                            for(int l = 0; l < army.length; l++){
                                if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                    board[i+1][j] = army[l];
                                    army[l].setPlaced(true);
                                    placed++;
                                    break;
                                }
                            }
                        }
                        if(config[12].equals("Y")){
                            for(int l = 0; l < army.length; l++){
                                if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                    board[i][j-1] = army[l];
                                    army[l].setPlaced(true);
                                    placed++;
                                    break;
                                }
                            }
                        }
                        if(config[13].equals("Y")){
                            for(int l = 0; l < army.length; l++){
                                if(army[l].getType() == PieceType.BOMB && !(army[l].getPlaced())){
                                    board[i-1][j] = army[l];
                                    army[l].setPlaced(true);
                                    placed++;
                                    break;
                                }
                            }
                        }
                    }
                    if(k % 10 == 0){
                        k += 20;
                    }
                    k--;
                }
            }
            teams = aiTeamSelect(config, opt);
        }
        printBoard(board);
        placed = aiPlacement(teams, config, board, army, placed);
    }   /*End aiSetup method*/

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
                                            info[k][3] = 1000;
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
    }   /*End buildShadowBoard method*/

    public static String[] aiConfiguration(int opt){
        try{
            ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
            File file = new File(classLoader.getResource("InitialSetups.txt").getFile());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try{
                ArrayList<String[]> configs = new ArrayList<>();
                boolean end = false;
                while(!end){
                    String s = reader.readLine();
                    if(s != null) {
                        String[] array = s.split(",");
                        configs.add(array);
                    }else{
                        end = true;
                    }
                }
                reader.close();

                double threshold;
                switch(opt){
                    case 1:
                        threshold = 1.5;
                        break;

                    case 2:
                        threshold = 1;
                        break;

                    case 3:
                        threshold = .75;
                        break;

                    default:
                        threshold = 0;
                        break;
                }

                Iterator<String[]> it = configs.iterator();
                while(it.hasNext()){
                    String[] i = it.next();
                    if(Double.parseDouble(i[0]) < threshold){
                        it.remove();
                    }
                }
                int rand = Randomizer.getRgen(configs.size());
                return configs.get(rand);
            }catch (IOException ioe){ioe.printStackTrace();
                return null;
            }
        }catch (FileNotFoundException fnfe){
            System.out.println("file not found");
            return null;
        }
    }

    public static String[][] aiTeamSelect(String[] config, int opt){
        double threshold;
        switch(opt){
            case 1:
                threshold = 1.5;
                break;

            case 2:
                threshold = 1;
                break;

            case 3:
                threshold = .75;
                break;

            default:
                threshold = 0;
                break;
        }
        String[][] teams = null;
        if(config[2].equals("Y")){
            teams = new String[1][];
            String name = "";
            switch(config[8]){
                case "Middle":
                    name = "BothMiddleTeams.txt";
                    break;

                case "Left":
                    name = "BothLeftTeams.txt";
                    break;

                case "Right":
                    name = "BothRightTeams.txt";
                    break;
            }
            try{
                ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
                File file = new File(classLoader.getResource(name).getFile());
                BufferedReader reader = new BufferedReader(new FileReader(file));
                try{
                    ArrayList<String[]> posTeams= new ArrayList<>();
                    boolean end = false;
                    while(!end){
                        String s = reader.readLine();
                        if(s != null) {
                            String[] array = s.split(",");
                            posTeams.add(array);
                        }else{
                            end = true;
                        }
                    }
                    reader.close();

                    String gFront;
                    String mFront;

                    if(config[6].equals("Y")){
                        mFront = "1";
                    }else{
                        mFront = "0";
                    }

                    if(config[7].equals("Y")){
                        gFront = "1";
                    }else{
                        gFront = "0";
                    }

                    Iterator<String[]> it = posTeams.iterator();
                    while(it.hasNext()){
                        String[] i = it.next();
                        if(Double.parseDouble(i[0]) < threshold || !(mFront.equals(i[1])) || !(gFront.equals(i[2]))){
                            it.remove();
                        }
                    }
                    if(posTeams.size() == 0){
                        return null;
                    }
                    int rand = Randomizer.getRgen(posTeams.size());
                    teams[0] = posTeams.get(rand);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                    return null;
                }
            }catch (FileNotFoundException fnfe){
                System.out.println("file not found");
                return null;
            }
        }else{
            teams = new String[2][];
            String name = "";
            switch(config[8]) {
                case "Middle":
                    name = "MarshallMiddleTeams.txt";
                    break;

                case "Left":
                    name = "MarshallLeftTeams.txt";
                    break;

                case "Right":
                    name = "MarshallRightTeams.txt";
                    break;
            }

            try{
                ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
                File file = new File(classLoader.getResource(name).getFile());
                BufferedReader reader = new BufferedReader(new FileReader(file));
                try{
                    ArrayList<String[]> posTeams= new ArrayList<>();
                    boolean end = false;
                    while(!end){
                        String s = reader.readLine();
                        if(s != null) {
                            String[] array = s.split(",");
                            posTeams.add(array);
                        }else{
                            end = true;
                        }
                    }
                    reader.close();

                    String mFront;

                    if(config[6].equals("Y")){
                        mFront = "1";
                    }else{
                        mFront = "0";
                    }

                    Iterator<String[]> it = posTeams.iterator();
                    while(it.hasNext()){
                        String[] i = it.next();
                        if(Double.parseDouble(i[0]) < threshold || !(mFront.equals(i[1]))){
                            it.remove();
                        }
                    }
                    int rand = Randomizer.getRgen(posTeams.size());
                    teams[0] = posTeams.get(rand);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                    return null;
                }
            }catch (FileNotFoundException fnfe){
                System.out.println("file not found");
                return null;
            }

            switch(config[9]) {
                case "Middle":
                    name = "GeneralMiddleTeams.txt";
                    break;

                case "Left":
                    name = "GeneralLeftTeams.txt";
                    break;

                case "Right":
                    name = "GeneralRightTeams.txt";
                    break;
            }

            try{
                ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
                File file = new File(classLoader.getResource(name).getFile());
                BufferedReader reader = new BufferedReader(new FileReader(file));
                try{
                    ArrayList<String[]> posTeams= new ArrayList<>();
                    boolean end = false;
                    while(!end){
                        String s = reader.readLine();
                        if(s != null) {
                            String[] array = s.split(",");
                            posTeams.add(array);
                        }else{
                            end = true;
                        }
                    }
                    reader.close();

                    String gFront;

                    if(config[7].equals("Y")){
                        gFront = "1";
                    }else{
                        gFront = "0";
                    }

                    Iterator<String[]> it = posTeams.iterator();
                    while(it.hasNext()){
                        String[] i = it.next();
                        if(Double.parseDouble(i[0]) < threshold || !(gFront.equals(i[2]))){
                            it.remove();
                        }
                    }
                    int rand = Randomizer.getRgen(posTeams.size());
                    teams[1] = posTeams.get(rand);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                    return null;
                }
            }catch (FileNotFoundException fnfe){
                System.out.println("file not found");
                return null;
            }
        }
        return teams;
    }

    public static int aiPlacement(String[][] teams, String[] config, Unit[][] board, Unit[] army, int placed){
        if(teams.length == 1){
            int[] positions;
            switch(config[8]){
                case "Middle":
                    positions = new int[13];
                    positions[0] = 8;
                    positions[1] = 3;
                    positions[2] = 6;
                    positions[3] = 2;
                    positions[4] = 3;
                    positions[5] = 24;
                    positions[6] = 25;
                    positions[7] = 26;
                    positions[8] = 27;
                    positions[9] = 34;
                    positions[10] = 35;
                    positions[11] = 36;
                    positions[12] = 37;
                    break;

                case "Left":
                    positions = new int[11];
                    positions[0] = 6;
                    positions[1] = 7;
                    positions[2] = 9;
                    positions[3] = 2;
                    positions[4] = 3;
                    positions[5] = 28;
                    positions[6] = 29;
                    positions[7] = 30;
                    positions[8] = 38;
                    positions[9] = 39;
                    positions[10] = 40;
                    break;

                default:
                    positions = new int[11];
                    positions[0] = 6;
                    positions[1] = 0;
                    positions[2] = 2;
                    positions[3] = 2;
                    positions[4] = 3;
                    positions[5] = 21;
                    positions[6] = 22;
                    positions[7] = 23;
                    positions[8] = 31;
                    positions[9] = 32;
                    positions[10] = 33;
                    break;
            }
            aiPlacePieces(teams[0], positions, board, army, placed);
            printBoard(board);
        }else{
            int[] positions1;
            switch(config[8]){
                case "Middle":
                    positions1 = new int[13];
                    positions1[0] = 8;
                    positions1[1] = 3;
                    positions1[2] = 6;
                    positions1[3] = 2;
                    positions1[4] = 3;
                    positions1[5] = 24;
                    positions1[6] = 25;
                    positions1[7] = 26;
                    positions1[8] = 27;
                    positions1[9] = 34;
                    positions1[10] = 35;
                    positions1[11] = 36;
                    positions1[12] = 37;
                    break;

                case "Left":
                    positions1 = new int[11];
                    positions1[0] = 6;
                    positions1[1] = 7;
                    positions1[2] = 9;
                    positions1[3] = 2;
                    positions1[4] = 3;
                    positions1[5] = 28;
                    positions1[6] = 29;
                    positions1[7] = 30;
                    positions1[8] = 38;
                    positions1[9] = 39;
                    positions1[10] = 40;
                    break;

               default:
                   positions1 = new int[11];
                   positions1[0] = 6;
                   positions1[1] = 0;
                   positions1[2] = 2;
                   positions1[3] = 2;
                   positions1[4] = 3;
                   positions1[5] = 21;
                   positions1[6] = 22;
                   positions1[7] = 23;
                   positions1[8] = 31;
                   positions1[9] = 32;
                   positions1[10] = 33;
                    break;
            }
            aiPlacePieces(teams[0], positions1, board, army, placed);
            printBoard(board);
            int[] positions2;
            switch(config[9]){
                case "Middle":
                    positions2 = new int[13];
                    positions2[0] = 8;
                    positions2[1] = 3;
                    positions2[2] = 6;
                    positions2[3] = 2;
                    positions2[4] = 3;
                    positions2[5] = 24;
                    positions2[6] = 25;
                    positions2[7] = 26;
                    positions2[8] = 27;
                    positions2[9] = 34;
                    positions2[10] = 35;
                    positions2[11] = 36;
                    positions2[12] = 37;
                    break;

                case "Left":
                    positions2 = new int[11];
                    positions2[0] = 6;
                    positions2[1] = 7;
                    positions2[2] = 9;
                    positions2[3] = 2;
                    positions2[4] = 3;
                    positions2[5] = 28;
                    positions2[6] = 29;
                    positions2[7] = 30;
                    positions2[8] = 38;
                    positions2[9] = 39;
                    positions2[10] = 40;
                    break;

                default:
                    positions2 = new int[11];
                    positions2[0] = 6;
                    positions2[1] = 0;
                    positions2[2] = 2;
                    positions2[3] = 2;
                    positions2[4] = 3;
                    positions2[5] = 21;
                    positions2[6] = 22;
                    positions2[7] = 23;
                    positions2[8] = 31;
                    positions2[9] = 32;
                    positions2[10] = 33;
                    break;
            }
            placed = aiPlacePieces(teams[1], positions2, board, army, placed);
            printBoard(board);
        }
        return placed;
    }

    public static int aiPlacePieces(String[] team, int[] positions, Unit[][] board, Unit[] army, int placed) {
        ArrayList<String> files = new ArrayList<>();
        if (!(team[1].equals("0"))) {
            files.add("MarshallStats.txt");
        }
        if (!(team[2].equals("0"))) {
            files.add("GeneralStats.txt");
        }
        if (!(team[3].equals("0"))) {
            files.add("ColonelStats.txt");
        }
        if (!(team[4].equals("0"))) {
            files.add("MajorStats.txt");
        }
        if (!(team[5].equals("0"))) {
            files.add("CaptainStats.txt");
        }
        if (!(team[6].equals("0"))) {
            files.add("LieutenantStats.txt");
        }
        if (!(team[7].equals("0"))) {
            files.add("SerjeantStats.txt");
        }
        if (!(team[8].equals("0"))) {
            files.add("MinerStats.txt");
        }
        if (!(team[9].equals("0"))) {
            files.add("ScoutStats.txt");
        }
        if (!(team[10].equals("0"))) {
            files.add("SpyStats.txt");
        }
        if (!(team[11].equals("0"))) {
            files.add("BombStats.txt");
        }

        BufferedReader[] readers = new BufferedReader[files.size()];
        double[][] scores = new double[positions[0]][files.size()];
        for (int i = 0; i < readers.length; i++) {
            try {
                ClassLoader classLoader = Driver.class.getClassLoader();
            /*
             * This is used only to access the text files because of the way
             * IntelliJ handles resources.
             */
                File file = new File(classLoader.getResource(files.get(i)).getFile());
                BufferedReader reader = new BufferedReader(new FileReader(file));
                try {
                    int line = 1;
                    int found = 0;
                    while (found < positions[0]) {
                        String s = reader.readLine();
                        boolean finished = false;
                        for(int j = 5; !finished && j < positions.length; j++){
                            if(line == positions[j]){
                                String[] split = s.split(",");
                                System.out.println(scores.length + "," + found + "," + scores[found].length + "," + i + "," + split.length);
                                scores[found][i] = Double.parseDouble(split[0]) / (Double.parseDouble(split[1]) + Double.parseDouble(split[2]));
                                found++;
                                finished = true;
                            }
                        }
                        line++;
                    }
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println("file not found");
            }
        }
        int[] pieceCount = new int[files.size()];
        ArrayList<String> pieceNames = new ArrayList<>();
        for(int i = 0, j = 1; i < pieceCount.length && j < team.length - 1; j++){
            if(!(team[j].equals("0"))){
                pieceCount[i] = Integer.parseInt(team[j]);
                i++;
            }
        }
        if (!(team[1].equals("0"))) {
            pieceNames.add("Marshall");
        }
        if (!(team[2].equals("0"))) {
            pieceNames.add("General");
        }
        if (!(team[3].equals("0"))) {
            pieceNames.add("Colonel");
        }
        if (!(team[4].equals("0"))) {
            pieceNames.add("Major");
        }
        if (!(team[5].equals("0"))) {
            pieceNames.add("Captain");
        }
        if (!(team[6].equals("0"))) {
            pieceNames.add("Lieutenant");
        }
        if (!(team[7].equals("0"))) {
            pieceNames.add("Serjeant");
        }
        if (!(team[8].equals("0"))) {
            pieceNames.add("Miner");
        }
        if (!(team[9].equals("0"))) {
            pieceNames.add("Scout");
        }
        if (!(team[10].equals("0"))) {
            pieceNames.add("Spy");
        }
        if (!(team[11].equals("0"))) {
            pieceNames.add("Bomb");
        }
        int current = 0;
        for(int i = positions[3]; i <= positions[4]; i++){
            for(int j = positions[1]; j<= positions[2]; j++, current++){
                if(board[i][j] == null){
                    double high = 0;
                    int num = -1;
                    for(int k = 0; k < scores[current].length; k++){
                        if(pieceCount[k] > 0 && scores[current][k] > high){
                            high = scores[current][k];
                            num = k;
                        }
                    }
                    if(num != -1) {
                        boolean found = false;
                        for (int k = 0; !found && k < army.length; k++) {
                            if (army[k].getName().equals(pieceNames.get(num)) && !(army[k].getPlaced())) {
                                found = true;
                                board[i][j] = army[k];
                                army[k].setPlaced(true);
                                pieceCount[num] = pieceCount[num] - 1;
                                placed++;
                            }
                        }
                        while (!found) {
                            if (num != -1) {
                                pieceCount[num] = 0;
                            }
                            high = 0;
                            num = -1;
                            for (int k = 0; k < scores[current].length; k++) {
                                if (pieceCount[k] > 0 && scores[current][k] > high) {
                                    high = scores[current][k];
                                    num = k;
                                }
                            }
                            if(num != -1) {
                                for (int k = 0; !found && k < army.length; k++) {
                                    if (army[k].getName().equals(pieceNames.get(num)) && !(army[k].getPlaced())) {
                                        found = true;
                                        board[i][j] = army[k];
                                        army[k].setPlaced(true);
                                        pieceCount[num] = pieceCount[num] - 1;
                                        placed++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return placed;
    }
}   /*End Driver class*/
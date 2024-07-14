import java.util.*;

class Solver {
    public static void main(String args[]){
        System.out.println("Let's play 2048");
//        TwentyFortyEight game = new TwentyFortyEight();
//        game.print();

        // Play the game without bot
        //Solver.playInteractive(game);

        // Get the win percentage
        int winCounter = 0;
        int lossCounter = 0;
        for (int i =0; i < 100; i++){
            TwentyFortyEight game = new TwentyFortyEight();
            while(!game.won()){
                String decision = Solver.aiNextMove(game);
                if (decision.equals("up")){
                    game.up();
                } else if (decision.equals("down")){
                    game.down();
                } else if (decision.equals("left")){
                    game.left();
                } else {
                    game.right();
                }

                // Add the next tile and print the game
                if (!game.noMoreZeros()){
                    game.addNewNumber();
                }

                // If game has been won
                if (game.won()){
                    winCounter ++;
                }

                // If it has been lost
                if (game.noMoreZeros() && game.noMoreMerges()){
                    lossCounter ++;
                    break;
                }

            }

        }
        System.out.println ("Wins:" + winCounter);
        System.out.println ("Losses:" + lossCounter);

        // Testing a single game outcome
//        while(!game.won()){
//            String decision = Solver.aiNextMove(game);
//            if (decision.equals("up")){
//                game.up();
//                System.out.println("up");
//            } else if (decision.equals("down")){
//                game.down();
//                System.out.println("down");
//            } else if (decision.equals("left")){
//                game.left();
//                System.out.println("left");
//            } else {
//                game.right();
//                System.out.println("right");
//            }
//
//            // Add the next tile and print the game
//            if (!game.noMoreZeros()){
//                game.addNewNumber();
//            }
//            game.print();
//
//            // If game has been won
//            if (game.won()){
//                System.out.println("YOU WON!!!");
//            }
//
//            // If it has been lost
//            if (game.noMoreZeros() && game.noMoreMerges()){
//                System.out.println("YOU LOSE :(");
//                break;
//            }
//
//        }

//        String decision = Solver.aiNextMove(game);
//        System.out.println(decision);




    }

    public static String aiNextMove(TwentyFortyEight game) {
        ArrayList<Integer> finalList = new ArrayList<>();
        finalList = Solver.recurse(game, 1);

        int finalMax = Collections.max(finalList);
        int finalIndex = finalList.indexOf(finalMax) + 1;

        ///////////
        // Debug //
        ///////////
//        for (int y : finalList){
//            System.out.println(y);
//        }


        ///////////////////////////////////////////
        // Step 7: Return the answer as a string //
        ///////////////////////////////////////////

        if (finalIndex == 1){
          return "up";
        } else if (finalIndex == 2){
            return "down";
        } else if (finalIndex == 3){
            return "left";
        } else {
            return "right";
        }


    } // END OF METHOD

    public static ArrayList<Integer> recurse(TwentyFortyEight board, int depth){
        // Create the array for weighted max average of each move
        ArrayList<Integer> weightedScores = new ArrayList<>();
        int indexTracker = -1;
        ////////////////////////////////////////////////////////////
        // STEP 1 : Start with one action [up, down, left, right] //
        ////////////////////////////////////////////////////////////
        for (int step = 1; step < 5; step ++){
            indexTracker += 1;
            // Create a copy of the board to analyze with expectiminimax
            TwentyFortyEight gameCopy = board.copyTable(board);

            if (step == 1){
                gameCopy.up();

            } else if (step == 2){
                gameCopy.down();

            } else if (step == 3){
                gameCopy.left();

            } else {
                gameCopy.right();

            }

            // Add the heuristic score of the first move if it is a valid move
            if (!gameCopy.validTurn(board, gameCopy)) {
                weightedScores.add(0);
                continue;
            }

            // Add the heuristic score of the first move
            weightedScores.add(gameCopy.heuristic(gameCopy));

            ///////////////////////////////////////////////////////////////////////////////
            // STEP 2: With the copy of the board, find the coordinates of all the zeros //
            ///////////////////////////////////////////////////////////////////////////////
            Queue<Integer> coordinates = gameCopy.locationOfZeros(gameCopy);

            /////////////////////////////////////////////////////////////
            // STEP 3: Add a 2 to the first or next set of coordinates //
            /////////////////////////////////////////////////////////////
            int numerator = 0;
            int denominator = 0;
            while (!coordinates.isEmpty()) {
                // Create a copy of the board to analyze with expectiminimax (initializes every while loop iteration)
                TwentyFortyEight gameCopy2 = gameCopy.copyTable(gameCopy);

                // Queue is made up of [y,x,y,x,y,x,y,x...]
                gameCopy2.positions[coordinates.remove()][coordinates.remove()] = 2;

                //////////////////////////////////////////////////////////////////////////////
                // STEP 4: Find the Max Heuristic score out of the four moves on this board //
                //////////////////////////////////////////////////////////////////////////////
                // recurse
                if (depth >=2){
                    numerator = Collections.max(Solver.recurse(gameCopy2, depth - 1));
                } else {
                    int maxHeuristic = 0;
                    for (int i = 1; i < 5; i++) {
                        TwentyFortyEight gameCopy3 = gameCopy2.copyTable(gameCopy2);
                        // Populate the copy with the information of the old board

                        if (i == 1) {
                            gameCopy3.up();
                        } else if (i == 2) {
                            gameCopy3.down();
                        } else if (i == 3) {
                            gameCopy3.left();
                        } else {
                            gameCopy3.right();
                        }
                        if (gameCopy3.heuristic(gameCopy3) > maxHeuristic) {
                            maxHeuristic = gameCopy3.heuristic(gameCopy3);
                        }
                    } // STEP 4 FOR LOOP END
                    numerator += maxHeuristic;
                }
                denominator ++;
            } // END OF WHILE LOOP

            //////////////////////////////////////////////////////////////////////////////////////////
            // STEP 5: Calculate the weighted average for that turn and add it to the overall score //
            //////////////////////////////////////////////////////////////////////////////////////////
            int weightedAverageForTurn = (Math.abs(numerator)/denominator);
            int currentValue = weightedScores.get(indexTracker);
            int newValue = currentValue + weightedAverageForTurn;
            weightedScores.set(indexTracker, newValue);

        } // END OF FOR LOOP FROM STEP 1

        /////////////////////////////////////////////////////////////////////////////////////
        // Step 6: Look through the heuristic scores of each initial turn and pick the max //
        /////////////////////////////////////////////////////////////////////////////////////
        // For some reason, the averages are coming out negative, so I will make them all positive
        ArrayList<Integer> positiveList = new ArrayList<>();
        for (int number : weightedScores){
            positiveList.add(Math.abs(number));
        }

        return positiveList;
    }


    public static void playInteractive(TwentyFortyEight game) {
        System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");

        int num = -1;
        Scanner keyboard = new Scanner(System.in);
        boolean endGameChecker = false;
        while (!endGameChecker) {
            num = keyboard.nextInt();
            if (num == 1) {
                game.up();
                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
            } else if (num == 2) {
                game.down();
                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
            } else if (num == 3) {
                game.left();
                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
            } else if (num == 4) {
                game.right();
                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
            }
            // Check to add a new number
            if (!game.noMoreZeros()){
                game.addNewNumber();
                game.print();
            }

            // Check whether we have won or lost
            endGameChecker = (game.noMoreZeros() && game.noMoreMerges());
            if (endGameChecker){
                break;
            }
            endGameChecker = game.won();
        } // END OF WHILE LOOP
    }
}

//////////////////////////////
//////////////////////////////
//////////////////////////////
// TWENTY FORTY EIGHT CLASS //
//////////////////////////////
//////////////////////////////
//////////////////////////////




class TwentyFortyEight {
    // setting up the original grid
    TwentyFortyEight() {
        positions = new int[4][];
        positions[0] = new int[4];
        positions[1] = new int[4];
        positions[2] = new int[4];
        positions[3] = new int[4];

        Random rand = new Random();
        int x1 = rand.nextInt(4);
        int y1 = rand.nextInt(4);
        int x2 = x1;
        int y2 = y1;

        while (x1 == x2 && y1 == y2) {
            x2 = rand.nextInt(4);
            y2 = rand.nextInt(4);
        }

        positions[y1][x1] = 2;
        positions[y2][x2] = 2;
    }

    public void print() {
        System.out.format("------------\n[%d, %d, %d, %d]\n[%d, %d, %d, %d]\n[%d, %d, %d, %d]\n[%d, %d, %d, %d]\n", positions[0][0], positions[0][1], positions[0][2], positions[0][3], positions[1][0], positions[1][1], positions[1][2], positions[1][3], positions[2][0], positions[2][1], positions[2][2], positions[2][3], positions[3][0], positions[3][1], positions[3][2], positions[3][3]);
    }

    public boolean won() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (positions[y][x] >= 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    // This adds the heuristic value to each board we analyze
    public int heuristic(TwentyFortyEight board){
        int hSum = 0;
        hSum += board.positions[0][0] != 0 ? (int) Math.pow(2, 1) * board.positions[0][0] : 0;
        hSum += board.positions[0][1] != 0 ? (int) Math.pow(2, 2) * board.positions[0][1] : 0;
        hSum += board.positions[0][2] != 0 ? (int) Math.pow(2, 3) * board.positions[0][2] : 0;
        hSum += board.positions[0][3] != 0 ? (int) Math.pow(2, 4) * board.positions[0][3] : 0;
        hSum += board.positions[1][3] != 0 ? (int) Math.pow(2, 5) * board.positions[1][3] : 0;
        hSum += board.positions[1][2] != 0 ? (int) Math.pow(2, 6) * board.positions[1][2]: 0;
        hSum += board.positions[1][1] != 0 ? (int) Math.pow(2, 7) * board.positions[1][1] : 0;
        hSum += board.positions[1][0] != 0 ? (int) Math.pow(2, 8) * board.positions[1][0]: 0;
        hSum += board.positions[2][0] != 0 ? (int) Math.pow(2, 9) * board.positions[2][0]: 0;
        hSum += board.positions[2][1] != 0 ? (int) Math.pow(2, 10) * board.positions[2][1]: 0;
        hSum += board.positions[2][2] != 0 ? (int) Math.pow(2, 11) * board.positions[2][2]: 0;
        hSum += board.positions[2][3] != 0 ? (int) Math.pow(2, 12) * board.positions[2][3]: 0;
        hSum += board.positions[3][3] != 0 ? (int) Math.pow(2, 13) * board.positions[3][3] : 0;
        hSum += board.positions[3][2] != 0 ? (int) Math.pow(2, 14) * board.positions[3][2] : 0;
        hSum += board.positions[3][1] != 0 ? (int) Math.pow(2, 15) * board.positions[3][1] : 0;
        hSum += board.positions[3][0] != 0 ? (int) Math.pow(2, 16) * board.positions[3][0]: 0;

        return hSum;
    }

    // This gets all the positions of Zeros on the board, to test in the Expectiminimax approach
    public Queue<Integer> locationOfZeros(TwentyFortyEight board){
        Queue<Integer> positionOfZeros = new LinkedList<>();
        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 4; x++){
                if (board.positions[y][x] == 0){
                    positionOfZeros.add(y);
                    positionOfZeros.add(x);
                }
            }
        }
        return positionOfZeros;

    }

    public TwentyFortyEight copyTable(TwentyFortyEight board){
        TwentyFortyEight returnCopy = new TwentyFortyEight();
        // Populate the copy with the information of the old board
        for (int y = 0; y < 4; y++) {
            System.arraycopy(board.positions[y], 0, returnCopy.positions[y], 0, 4);
        }

        return returnCopy;
    }

    public boolean validTurn(TwentyFortyEight board, TwentyFortyEight oldBoard){
        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 4; x++){
                if (board.positions[y][x] != oldBoard.positions[y][x]){
                    return true;
                }
            }
        }
        return false;
    }


    public int playRandom2(TwentyFortyEight board, int playNumber){
        int score = 0;
        if (playNumber == 1) {
            board.up();

        } else if (playNumber == 2) {
            board.down();

        } else if (playNumber == 3) {
            board.left();

        } else  {
            board.right();
        }
        return score;
    }

    // This is a method to play what the randomizer calls to make the code neater
    public void playRandom(TwentyFortyEight board, int playNumber){
        if (playNumber == 1) {
            board.up();
            System.out.println("up");

        } else if (playNumber == 2) {
            board.down();
            System.out.println("down");

        } else if (playNumber == 3) {
            board.left();
            System.out.println("left");

        } else  {
            board.right();
            System.out.println("right");
        }
    }


    // functions need to add another 2 every time.
    public boolean noMoreZeros() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (positions[y][x] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean noMoreMerges(){
        // This checks if there are no matches next to each other
        for (int y = 0; y < 4; y++){
            if (positions[y][0] == positions[y][1] || positions[y][1] == positions[y][2] || positions[y][2] == positions[y][3]){
                return false;
            }
        }
        for (int x = 0; x < 4; x++){
            if (positions[0][x] == positions[1][x] || positions[1][x] == positions[2][x] || positions[2][x] == positions[3][x]){
                return false;
            }
        }
        return true;
    }

    // Don't call this if you don't have a 0 on the board!
    public void addNewNumber() {
        Random rand = new Random();
        int x = rand.nextInt(4);
        int y = rand.nextInt(4);
        int i = 0; // Break-glass counter.

        // Make sure space is not taken already.
        while (positions[y][x] != 0 && i != 1000) {
            x = rand.nextInt(4);
            y = rand.nextInt(4);
            i++;
        }

        if (i == 10000) {
            System.out.println("Couldn't find a place on the board to put a new number. This should not happen. Bug in code.");
            System.exit(1);
        }

        // Put either a 2 or a 4 on the board.
        positions[y][x] = rand.nextInt(6) == 0 ? 4 : 2;
    }

    //
    public void right() {
        for (int y = 0; y < 4; y++) {
            // first, move everything right
            if (positions[y][3] == 0) {
                if (positions[y][2] != 0) {
                    positions[y][3] = positions[y][2];
                    positions[y][2] = 0;
                } else if (positions[y][1] != 0) {
                    positions[y][3] = positions[y][1];
                    positions[y][1] = 0;
                } else if (positions[y][0] != 0) {
                    positions[y][3] = positions[y][0];
                    positions[y][0] = 0;
                }

            }
            if (positions[y][2] == 0) {
                if (positions[y][1] != 0) {
                    positions[y][2] = positions[y][1];
                    positions[y][1] = 0;
                } else if (positions[y][0] != 0) {
                    positions[y][2] = positions[y][0];
                    positions[y][0] = 0;
                }

            }
            if (positions[y][1] == 0) {
                if (positions[y][0] != 0) {
                    positions[y][1] = positions[y][0];
                    positions[y][0] = 0;
                }

            }


            // then, merge right, starting with the rightmost
            if (positions[y][3] == positions[y][2]) {
                positions[y][3] = 2*positions[y][3];
                positions[y][2] = positions[y][1];
                positions[y][1] = positions[y][0];
                positions[y][0] = 0;

            }
            if (positions[y][2] == positions[y][1]) {
                positions[y][2] = 2*positions[y][2];
                positions[y][1] = positions[y][0];
                positions[y][0] = 0;
            }
            if (positions[y][1] == positions[y][0]) {
                positions[y][1] = 2*positions[y][1];
                positions[y][0] = 0;
            }
        }
    }

    // Returns true if game is still active, false if it ended.
    public void left() {
        for (int y = 0; y < 4; y++) {
            // first, move everything left
            if (positions[y][0] == 0) {
                if (positions[y][1] != 0) {
                    positions[y][0] = positions[y][1];
                    positions[y][1] = 0;
                } else if (positions[y][2] != 0) {
                    positions[y][0] = positions[y][2];
                    positions[y][2] = 0;
                } else if (positions[y][3] != 0) {
                    positions[y][0] = positions[y][3];
                    positions[y][3] = 0;
                }

            }
            if (positions[y][1] == 0) {
                if (positions[y][2] != 0) {
                    positions[y][1] = positions[y][2];
                    positions[y][2] = 0;
                } else if (positions[y][3] != 0) {
                    positions[y][1] = positions[y][3];
                    positions[y][3] = 0;
                }

            }
            if (positions[y][2] == 0) {
                if (positions[y][3] != 0) {
                    positions[y][2] = positions[y][3];
                    positions[y][3] = 0;
                }

            }


            // then, merge left, starting with the leftmost
            if (positions[y][0] == positions[y][1]) {
                positions[y][0] = 2*positions[y][0];
                positions[y][1] = positions[y][2];
                positions[y][2] = positions[y][3];
                positions[y][3] = 0;
            }
            if (positions[y][1] == positions[y][2]) {
                positions[y][1] = 2*positions[y][1];
                positions[y][2] = positions[y][3];
                positions[y][3] = 0;
            }
            if (positions[y][2] == positions[y][3]) {
                positions[y][2] = 2*positions[y][2];
                positions[y][3] = 0;
            }
        }
    }

    // Returns true if game is still active, false if it ended.
    public void up() {
        for (int x = 0; x < 4; x++) {
            // first, move everything up
            if (positions[0][x] == 0) {
                if (positions[1][x] != 0) {
                    positions[0][x] = positions[1][x];
                    positions[1][x] = 0;
                } else if (positions[2][x] != 0) {
                    positions[0][x] = positions[2][x];
                    positions[2][x] = 0;
                } else if (positions[3][x] != 0) {
                    positions[0][x] = positions[3][x];
                    positions[3][x] = 0;
                }

            }
            if (positions[1][x] == 0) {
                if (positions[2][x] != 0) {
                    positions[1][x] = positions[2][x];
                    positions[2][x] = 0;
                } else if (positions[3][x] != 0) {
                    positions[1][x] = positions[3][x];
                    positions[3][x] = 0;
                }

            }
            if (positions[2][x] == 0) {
                if (positions[3][x] != 0) {
                    positions[2][x] = positions[3][x];
                    positions[3][x] = 0;
                }

            }


            // then, merge up, starting with the upmost
            if (positions[0][x] == positions[1][x]) {
                positions[0][x] = 2*positions[0][x];
                positions[1][x] = positions[2][x];
                positions[2][x] = positions[3][x];
                positions[3][x] = 0;
            }
            if (positions[1][x] == positions[2][x]) {
                positions[1][x] = 2*positions[1][x];
                positions[2][x] = positions[3][x];
                positions[3][x] = 0;
            }
            if (positions[2][x] == positions[3][x]) {
                positions[2][x] = 2*positions[2][x];
                positions[3][x] = 0;
            }
        }
    }

    // Returns true if game is still active, false if it ended.
    public void down() {
        for (int x = 0; x < 4; x++) {
            // first, move everything down
            if (positions[3][x] == 0) {
                if (positions[2][x] != 0) {
                    positions[3][x] = positions[2][x];
                    positions[2][x] = 0;
                } else if (positions[1][x] != 0) {
                    positions[3][x] = positions[1][x];
                    positions[1][x] = 0;
                } else if (positions[0][x] != 0) {
                    positions[3][x] = positions[0][x];
                    positions[0][x] = 0;
                }

            }
            if (positions[2][x] == 0) {
                if (positions[1][x] != 0) {
                    positions[2][x] = positions[1][x];
                    positions[1][x] = 0;
                } else if (positions[0][x] != 0) {
                    positions[2][x] = positions[0][x];
                    positions[0][x] = 0;
                }

            }
            if (positions[1][x] == 0) {
                if (positions[0][x] != 0) {
                    positions[1][x] = positions[0][x];
                    positions[0][x] = 0;
                }


            }

            // then, merge down, starting with the downmost
            if (positions[3][x] == positions[2][x]) {
                positions[3][x] = 2*positions[3][x];
                positions[2][x] = positions[1][x];
                positions[1][x] = positions[0][x];
                positions[0][x] = 0;
            }
            if (positions[2][x] == positions[1][x]) {
                positions[2][x] = 2*positions[2][x];
                positions[1][x] = positions[0][x];
                positions[0][x] = 0;
            }
            if (positions[1][x] == positions[0][x]) {
                positions[1][x] = 2*positions[1][x];
                positions[0][x] = 0;
            }
        }
    }

    public int positions[][];
}
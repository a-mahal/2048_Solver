//import java.util.Random;
//import java.util.Scanner;
//import java.util.concurrent.ThreadLocalRandom;
//
//class Solver {
//    public static void main(String args[]){
//        System.out.println("Let's play 2048");
//        TwentyFortyEight game = new TwentyFortyEight();
//        game.print();
//
//        //Solver.playInteractive(game);
//
//        int counter = 0;
//        while(!game.won()){
//            Solver.aiNextMove(game);
//
//            if (game.won()){
//                System.out.println("WINNER!!!!!!!");
//                break;
//            }
//            if (game.gameOver() && game.gameOver2()){
//                System.out.println("You lost");
//                break;
//            }
//            counter ++;
//            System.out.println("counter:" + counter);
//        }
//
//
//
////        for (int i = 0; i<2000; i++) {
////            Solver.aiNextMove(game);
////            System.out.println("Next move");
////        }
//    }
//
//    public static void aiNextMove(TwentyFortyEight game) {
//        /////////////////////////////////////////////////////
//        // ONE: Section for each of the first four choices //
//        /////////////////////////////////////////////////////
//        int scoreCouter = -1;
//        int[] score = new int[4];
//        for (int firstChoice = 1; firstChoice < 5; firstChoice++){
//
//
//            // create a new copy of the game board to mess with
//            TwentyFortyEight game2 = new TwentyFortyEight();
//            // Populate the copy with the information of the old board
//            for (int y = 0; y < 4; y++){
//                System.arraycopy(game.positions[y], 0, game2.positions[y], 0, 4);
//            }
//
//            scoreCouter ++;
//            score[scoreCouter] = 0;
//            int validMove = 0;
//
//            if (firstChoice == 1) {
//                score[0] = game2.up() + 1;
//
//            } else if (firstChoice == 2) {
//                // Multiply by four to give the first move a heavier importance
//                score[1] = game2.down() + 1;
//
//            } else if (firstChoice == 3) {
//                score[2] = game2.left() + 1;
//
//            } else {
//                score[3] = game2.right() + 1;
//
//            }
//
//
//            // check if game2 is different from game
//            for (int y = 0; y < 4; y++){
//                for (int x = 0; x < 4; x++){
//                    if (game.positions[y][x] != game2.positions[y][x]) {
//                        validMove ++;
//                    }
//                }
//            }
//            // If the board is the same after the first move, skip this option
//            if (validMove == 0){
//                continue;
//            }
//
//            // Check if there are any 0's left, if-so add a new value of 2 or 4
//            if (!game2.gameOver()){
//                game2.addNewNumber();
//            }
//
//
//
//            ////////////////////////////////////////////////////////////////////////////////
//            // TWO: Section for the following choices starting from one of the above ones //
//            ////////////////////////////////////////////////////////////////////////////////
//
//            int searchesPerMove = 20;
//            int maxScore = 0;
//            int mergeScore;
//
//            for (int i = 1; i <200; i++){
//
//                // Reinitialize workingScore to 0
//                int workingScore = 0;
//
//                // create a new copy of the game board to mess with
//                TwentyFortyEight newBoard = new TwentyFortyEight();
//
//                // Populate the copy with the information of the old board
//                for (int y = 0; y < 4; y++){
//                    System.arraycopy(game2.positions[y], 0, newBoard.positions[y], 0, 4);
//                }
//
//                // While loop checker
//                boolean isOver2 = false;
//
//                // start the move count over
//                int moveNumber = 1;
//
//                /////////////////////////////////////////////////////////
//                // THREE: Sees into the "future" based on random moves //
//                /////////////////////////////////////////////////////////
//
//                while (!isOver2 && moveNumber < searchesPerMove){
//                    // Increments the move counter
//                    moveNumber ++;
//
//                    // generate a random 2048 choice
//                    int randomChoice = ThreadLocalRandom.current().nextInt(1, 5);
//
//                    // Plays the random choice and returns the score (score is solely base on merges)
//                    mergeScore = newBoard.playRandom2(newBoard, randomChoice);
//                    workingScore += mergeScore;
//
//                    // Credit the amount of zeros on the board
//                    workingScore += (5 * newBoard.emptySpaceCounter(newBoard));
//
//                    // Give credit for increasing order (left to right)
//                    for (int x = 0; x < 3; x++){
//                        workingScore += (newBoard.positions[0][x] * 2) == newBoard.positions[0][x+1] ? 5 : 0;
//                        workingScore += (newBoard.positions[1][x] * 2) == newBoard.positions[1][x+1] ? 5 : 0;
//                        workingScore += (newBoard.positions[2][x] * 2) == newBoard.positions[2][x+1] ? 5 : 0;
//                        workingScore += (newBoard.positions[3][x] * 2) == newBoard.positions[3][x+1] ? 5 : 0;
//                    }
//                    // Give credit for decreasing order (up to down)
//                    for (int y = 0; y < 3; y++){
//                        workingScore += (newBoard.positions[y][0] * 2) == newBoard.positions[y+1][0] ? 5 : 0;
//                        workingScore += (newBoard.positions[y][1] * 2) == newBoard.positions[y+1][1] ? 5 : 0;
//                        workingScore += (newBoard.positions[y][2] * 2) == newBoard.positions[y+1][2] ? 5 : 0;
//                        workingScore += (newBoard.positions[y][3] * 2) == newBoard.positions[y+1][3] ? 5 : 0;
//
//                    }
//
//
//
//
//                    // Check to see if 2 or 4 can be placed on the board after a move
//                    if (!newBoard.gameOver()){
//                        newBoard.addNewNumber();
//                    }
//
//                    // Check to see if the game is over, in this case it was a bad choice
//                    if (newBoard.gameOver() && newBoard.gameOver2()){
//                        //workingScore = 0;
//                        isOver2 = true;
//                    }
//
//                    // Check if the move won the game
//                    if (newBoard.won()){
//                        System.out.println("You will win the game!!");
//                        //newBoard.print();
//                        // make the working score a really high number
//                        maxScore = 100000;
//                        isOver2 = true;
//                    }
//                } // END OF WHILE LOOP
//
//
//
////                // Give credit for increasing order (left to right)
////                for (int x = 0; x < 3; x++){
////                    workingScore += (newBoard.positions[0][x] * 2) == newBoard.positions[0][x+1] ? 5 : 0;
////                    workingScore += (newBoard.positions[1][x] * 2) == newBoard.positions[1][x+1] ? 5 : 0;
////                    workingScore += (newBoard.positions[2][x] * 2) == newBoard.positions[2][x+1] ? 5 : 0;
////                    workingScore += (newBoard.positions[3][x] * 2) == newBoard.positions[3][x+1] ? 5 : 0;
////                }
////                // Give credit for decreasing order (up to down)
////                for (int y = 0; y < 3; y++){
////                    workingScore += (newBoard.positions[y][0] * 2) == newBoard.positions[y+1][0] ? 5 : 0;
////                    workingScore += (newBoard.positions[y][1] * 2) == newBoard.positions[y+1][1] ? 5 : 0;
////                    workingScore += (newBoard.positions[y][2] * 2) == newBoard.positions[y+1][2] ? 5 : 0;
////                    workingScore += (newBoard.positions[y][3] * 2) == newBoard.positions[y+1][3] ? 5 : 0;
////
////                }
//
//                if (workingScore > maxScore){
//                    maxScore = workingScore;
//                }
//
//
//            } // END OF SMALLER FOR LOOP
//
//
//
//            // For the regular score method
//            score[scoreCouter] += maxScore;
//
//
//        } // END OF FOR LOOP
//        //System.out.println("Test 1");
//
//
//        //////////////////////////////////////////////////////////////////////
//        // FINAL: Analysis of the best score to make the next move decision //
//        //////////////////////////////////////////////////////////////////////
//
//        // Create a new graph of game to check if the movement actually did anything
//        TwentyFortyEight boardCheck = new TwentyFortyEight();
//
//        // Populate the copy with the information of the old board
//        for (int y = 0; y < 4; y++){
//            System.arraycopy(game.positions[y], 0, boardCheck.positions[y], 0, 4);
//        }
//
//
//        int maxScore = 0;
//        int maxScoreIndex = 0;
//        int indexCounter = 0;
//        // change to score instead of highestValueScore to go back to the old way
//        for (int scr : score){
//            indexCounter ++;
//            if (scr > maxScore){
//                maxScore = scr;
//                maxScoreIndex = indexCounter;
//            }
//        }
//
//        // Make sure if right == left, then we chose right
//        if (score[2] == score[3] && score[1] < score[2]){
//            maxScoreIndex = 4;
//        }
//
//        // THIS PLAYS THE MOVE ON THE ACTUAL BOARD
//        game.playRandom(game, maxScoreIndex);
//
//        // Check if the boards are the same
//        boolean areBoardsTheSame = true;
//        for (int y = 0; y < 4; y++){
//            for (int x = 0; x < 4; x++){
//                if (game.positions[y][x] != boardCheck.positions[y][x]) {
//                    areBoardsTheSame = false;
//                    break;
//                }
//            }
//        }
//
//
//        // Check to see if we can add another 2 or 4, then add
//        if (!game.gameOver() && !areBoardsTheSame){
//            game.addNewNumber();
//        }
//
//
//        // Print the matrix
//        for (int x : score){
//            System.out.println(x);
//        }
//        game.print();
//
//        System.out.println("Outer workings");
//
//
//
//    } // END OF METHOD
//
//
//
//
//
//    public static void playInteractive(TwentyFortyEight game) {
//        System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
//
//        int num = -1;
//        Scanner keyboard = new Scanner(System.in);
//        boolean endGameChecker = false;
//        while (!endGameChecker) {
//            num = keyboard.nextInt();
//            if (num == 1) {
//                game.up();
//                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
//            } else if (num == 2) {
//                game.down();
//                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
//            } else if (num == 3) {
//                game.left();
//                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
//            } else if (num == 4) {
//                game.right();
//                System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");
//            }
//            // Check to add a new number
//            if (!game.gameOver()){
//                game.addNewNumber();
//                game.print();
//            }
//
//            // Check whether we have won or lost
//            endGameChecker = (game.gameOver() && game.gameOver2());
//            if (endGameChecker){
//                break;
//            }
//            endGameChecker = game.won();
//        } // END OF WHILE LOOP
//    }
//}
//
//
//
//
//
//
//
//
//
//class TwentyFortyEight {
//    // setting up the original grid
//    TwentyFortyEight() {
//        positions = new int[4][];
//        positions[0] = new int[4];
//        positions[1] = new int[4];
//        positions[2] = new int[4];
//        positions[3] = new int[4];
//
//        Random rand = new Random();
//        int x1 = rand.nextInt(4);
//        int y1 = rand.nextInt(4);
//        int x2 = x1;
//        int y2 = y1;
//
//        while (x1 == x2 && y1 == y2) {
//            x2 = rand.nextInt(4);
//            y2 = rand.nextInt(4);
//        }
//
//        positions[y1][x1] = 2;
//        positions[y2][x2] = 2;
//    }
//
//    public void print() {
//        System.out.format("------------\n[%d, %d, %d, %d]\n[%d, %d, %d, %d]\n[%d, %d, %d, %d]\n[%d, %d, %d, %d]\n", positions[0][0], positions[0][1], positions[0][2], positions[0][3], positions[1][0], positions[1][1], positions[1][2], positions[1][3], positions[2][0], positions[2][1], positions[2][2], positions[2][3], positions[3][0], positions[3][1], positions[3][2], positions[3][3]);
//    }
//
//    public boolean won() {
//        for (int x = 0; x < 4; x++) {
//            for (int y = 0; y < 4; y++) {
//                if (positions[y][x] >= 2048) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//    public int playRandom2(TwentyFortyEight board, int playNumber){
//        int score = 0;
//        if (playNumber == 1) {
//            score = board.up();
//
//        } else if (playNumber == 2) {
//            score = board.down();
//
//        } else if (playNumber == 3) {
//            score = board.left();
//
//        } else  {
//            score = board.right();
//        }
//        return score;
//    }
//
//    // This is a method to play what the randomizer calls to make the code neater
//    public void playRandom(TwentyFortyEight board, int playNumber){
//        if (playNumber == 1) {
//            board.up();
//            System.out.println("up");
//
//        } else if (playNumber == 2) {
//            board.down();
//            System.out.println("down");
//
//        } else if (playNumber == 3) {
//            board.left();
//            System.out.println("left");
//
//        } else  {
//            board.right();
//            System.out.println("right");
//        }
//    }
//
//    // TODO(jean)
//    public int emptySpaceCounter(TwentyFortyEight board) {
//        int totalZeros = 0;
//        for (int x = 0; x < 4; x++) {
//            for (int y = 0; y < 4; y++) {
//                if (positions[y][x] == 0) {
//                    totalZeros ++;
//                }
//            }
//        }
//        return totalZeros;
//    }
//
//
//
//    // TODO(jean): Use this at the end of move functions. Also the move
//    // functions need to add another 2 every time.
//    public boolean gameOver() {
//        for (int y = 0; y < 4; y++) {
//            for (int x = 0; x < 4; x++) {
//                if (positions[y][x] == 0) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    public boolean gameOver2(){
//        // This checks if there are no matches next to each other
//        for (int y = 0; y < 4; y++){
//            if (positions[y][0] == positions[y][1] || positions[y][1] == positions[y][2] || positions[y][2] == positions[y][3]){
//                return false;
//            }
//        }
//        for (int x = 0; x < 4; x++){
//            if (positions[0][x] == positions[1][x] || positions[1][x] == positions[2][x] || positions[2][x] == positions[3][x]){
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    // Don't call this if you don't have a 0 on the board!
//    public void addNewNumber() {
//        Random rand = new Random();
//        int x = rand.nextInt(4);
//        int y = rand.nextInt(4);
//        int i = 0; // Break-glass counter.
//
//        // Make sure space is not taken already.
//        while (positions[y][x] != 0 && i != 1000) {
//            x = rand.nextInt(4);
//            y = rand.nextInt(4);
//            i++;
//        }
//
//        if (i == 10000) {
//            System.out.println("Couldn't find a place on the board to put a new number. This should not happen. Bug in code.");
//            System.exit(1);
//        }
//
//        // Put either a 2 or a 4 on the board.
//        positions[y][x] = rand.nextInt(9) == 0 ? 4 : 2;
//    }
//
//    // Returns true if game is still active, false if it ended.
//    public int right() {
//        int score = 0;
//        for (int y = 0; y < 4; y++) {
//            // first, move everything right
//            if (positions[y][3] == 0) {
//                if (positions[y][2] != 0) {
//                    positions[y][3] = positions[y][2];
//                    positions[y][2] = 0;
//                } else if (positions[y][1] != 0) {
//                    positions[y][3] = positions[y][1];
//                    positions[y][1] = 0;
//                } else if (positions[y][0] != 0) {
//                    positions[y][3] = positions[y][0];
//                    positions[y][0] = 0;
//                }
//
//            }
//            if (positions[y][2] == 0) {
//                if (positions[y][1] != 0) {
//                    positions[y][2] = positions[y][1];
//                    positions[y][1] = 0;
//                } else if (positions[y][0] != 0) {
//                    positions[y][2] = positions[y][0];
//                    positions[y][0] = 0;
//                }
//
//            }
//            if (positions[y][1] == 0) {
//                if (positions[y][0] != 0) {
//                    positions[y][1] = positions[y][0];
//                    positions[y][0] = 0;
//                }
//
//            }
//
//
//            // then, merge right, starting with the rightmost
//            if (positions[y][3] == positions[y][2]) {
//                positions[y][3] = 2*positions[y][3];
//                positions[y][2] = positions[y][1];
//                positions[y][1] = positions[y][0];
//                positions[y][0] = 0;
//                score += positions[y][3] == 2 || positions[y][3] == 4 ? 65 : positions[y][3];
//
//            }
//            if (positions[y][2] == positions[y][1]) {
//                positions[y][2] = 2*positions[y][2];
//                positions[y][1] = positions[y][0];
//                positions[y][0] = 0;
//                score += positions[y][2] == 2 || positions[y][2] == 4 ? 65 : positions[y][2];
//            }
//            if (positions[y][1] == positions[y][0]) {
//                positions[y][1] = 2*positions[y][1];
//                positions[y][0] = 0;
//                score += positions[y][1] == 2 || positions[y][1] == 4 ? 65 : positions[y][1];
//            }
//        }
//        return score;
//    }
//
//    // Returns true if game is still active, false if it ended.
//    public int left() {
//        int score = 0;
//        for (int y = 0; y < 4; y++) {
//            // first, move everything left
//            if (positions[y][0] == 0) {
//                if (positions[y][1] != 0) {
//                    positions[y][0] = positions[y][1];
//                    positions[y][1] = 0;
//                } else if (positions[y][2] != 0) {
//                    positions[y][0] = positions[y][2];
//                    positions[y][2] = 0;
//                } else if (positions[y][3] != 0) {
//                    positions[y][0] = positions[y][3];
//                    positions[y][3] = 0;
//                }
//
//            }
//            if (positions[y][1] == 0) {
//                if (positions[y][2] != 0) {
//                    positions[y][1] = positions[y][2];
//                    positions[y][2] = 0;
//                } else if (positions[y][3] != 0) {
//                    positions[y][1] = positions[y][3];
//                    positions[y][3] = 0;
//                }
//
//            }
//            if (positions[y][2] == 0) {
//                if (positions[y][3] != 0) {
//                    positions[y][2] = positions[y][3];
//                    positions[y][3] = 0;
//                }
//
//            }
//
//
//            // then, merge left, starting with the leftmost
//            if (positions[y][0] == positions[y][1]) {
//                positions[y][0] = 2*positions[y][0];
//                positions[y][1] = positions[y][2];
//                positions[y][2] = positions[y][3];
//                positions[y][3] = 0;
//                score += positions[y][0] == 2 || positions[y][0] == 4 ? 65 : positions[y][0];
//            }
//            if (positions[y][1] == positions[y][2]) {
//                positions[y][1] = 2*positions[y][1];
//                positions[y][2] = positions[y][3];
//                positions[y][3] = 0;
//                score += positions[y][1] == 2 || positions[y][1] == 4 ? 65 : positions[y][1];
//            }
//            if (positions[y][2] == positions[y][3]) {
//                positions[y][2] = 2*positions[y][2];
//                positions[y][3] = 0;
//                score += positions[y][2] == 2 || positions[y][2] == 4 ? 65 : positions[y][2];
//            }
//        }
//        return score;
//    }
//
//    // Returns true if game is still active, false if it ended.
//    public int up() {
//        int score = 0;
//        for (int x = 0; x < 4; x++) {
//            // first, move everything up
//            if (positions[0][x] == 0) {
//                if (positions[1][x] != 0) {
//                    positions[0][x] = positions[1][x];
//                    positions[1][x] = 0;
//                } else if (positions[2][x] != 0) {
//                    positions[0][x] = positions[2][x];
//                    positions[2][x] = 0;
//                } else if (positions[3][x] != 0) {
//                    positions[0][x] = positions[3][x];
//                    positions[3][x] = 0;
//                }
//
//            }
//            if (positions[1][x] == 0) {
//                if (positions[2][x] != 0) {
//                    positions[1][x] = positions[2][x];
//                    positions[2][x] = 0;
//                } else if (positions[3][x] != 0) {
//                    positions[1][x] = positions[3][x];
//                    positions[3][x] = 0;
//                }
//
//            }
//            if (positions[2][x] == 0) {
//                if (positions[3][x] != 0) {
//                    positions[2][x] = positions[3][x];
//                    positions[3][x] = 0;
//                }
//
//            }
//
//
//            // then, merge up, starting with the upmost
//            if (positions[0][x] == positions[1][x]) {
//                positions[0][x] = 2*positions[0][x];
//                positions[1][x] = positions[2][x];
//                positions[2][x] = positions[3][x];
//                positions[3][x] = 0;
//                score += positions[0][x] == 2 || positions[0][x] == 4 ? 65 : positions[0][x];
//            }
//            if (positions[1][x] == positions[2][x]) {
//                positions[1][x] = 2*positions[1][x];
//                positions[2][x] = positions[3][x];
//                positions[3][x] = 0;
//                score += positions[1][x] == 2 || positions[1][x] == 4 ? 65 : positions[1][x];
//            }
//            if (positions[2][x] == positions[3][x]) {
//                positions[2][x] = 2*positions[2][x];
//                positions[3][x] = 0;
//                score += positions[2][x] == 2 || positions[2][x] == 4 ? 65 : positions[2][x];
//            }
//        }
//        return score;
//    }
//
//    // Returns true if game is still active, false if it ended.
//    public int down() {
//        int score = 0;
//        for (int x = 0; x < 4; x++) {
//            // first, move everything down
//            if (positions[3][x] == 0) {
//                if (positions[2][x] != 0) {
//                    positions[3][x] = positions[2][x];
//                    positions[2][x] = 0;
//                } else if (positions[1][x] != 0) {
//                    positions[3][x] = positions[1][x];
//                    positions[1][x] = 0;
//                } else if (positions[0][x] != 0) {
//                    positions[3][x] = positions[0][x];
//                    positions[0][x] = 0;
//                }
//
//            }
//            if (positions[2][x] == 0) {
//                if (positions[1][x] != 0) {
//                    positions[2][x] = positions[1][x];
//                    positions[1][x] = 0;
//                } else if (positions[0][x] != 0) {
//                    positions[2][x] = positions[0][x];
//                    positions[0][x] = 0;
//                }
//
//            }
//            if (positions[1][x] == 0) {
//                if (positions[0][x] != 0) {
//                    positions[1][x] = positions[0][x];
//                    positions[0][x] = 0;
//                }
//
//
//            }
//
//            // then, merge down, starting with the downmost
//            if (positions[3][x] == positions[2][x]) {
//                positions[3][x] = 2*positions[3][x];
//                positions[2][x] = positions[1][x];
//                positions[1][x] = positions[0][x];
//                positions[0][x] = 0;
//                score += positions[3][x] == 2 || positions[3][x] == 4 ? 65 : positions[3][x];
//            }
//            if (positions[2][x] == positions[1][x]) {
//                positions[2][x] = 2*positions[2][x];
//                positions[1][x] = positions[0][x];
//                positions[0][x] = 0;
//                score += positions[2][x] == 2 || positions[2][x] == 4 ? 65 : positions[2][x];
//            }
//            if (positions[1][x] == positions[0][x]) {
//                positions[1][x] = 2*positions[1][x];
//                positions[0][x] = 0;
//                score += positions[1][x] == 2 || positions[1][x] == 4 ? 65 : positions[1][x];
//            }
//        }
//        return score;
//    }
//
//    public int positions[][];
//}
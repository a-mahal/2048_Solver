import java.util.Random;
import java.util.Scanner;

class Solver {
    public static void main(String args[]){
        System.out.println("Let's play 2048");
        TwentyFortyEight game = new TwentyFortyEight();
        game.print();

        // Solver.playInteractive(game);
        Solver.playAlgorithm(game);
    }

    public static void playAlgorithm(TwentyFortyEight game) {
        boolean gameOver = game.right();
        game.print();
        System.out.format("Is the game over? %b\n", gameOver);

        // etc

        System.out.format("Did my algorithm win? %b\n", game.won());
    }

    public static void playInteractive(TwentyFortyEight game) {
        System.out.println("Up=1, Down=2, Left=3, Right=4. Enter to confirm.");

        int num = -1;
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            num = keyboard.nextInt();
            if (num == 1) {
                game.up();
                game.print();
            } else if (num == 2) {
                game.down();
                game.print();
            } else if (num == 3) {
                game.left();
                game.print();
            } else if (num == 4) {
                game.right();
                game.print();
            }
        }
    }
}

class TwentyFortyEight {
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

    // TODO(jean)
    public int score() {
        return -1;
    }

    // TODO(jean): Use this at the end of move functions. Also the move
    // functions need to add another 2 every time.
    private boolean gameOver() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (positions[y][x] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Don't call this if you don't have a 0 on the board!
    private void addNewNumber() {
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

        if (i == 1000) {
            System.out.println("Couldn't find a place on the board to put a new number. This should not happen. Bug in code.");
            System.exit(1);
        }

        // Put either a 2 or a 4 on the board.
        positions[y][x] = rand.nextInt(2) == 0 ? 2 : 4;
    }

    // Returns true if game is still active, false if it ended.
    public boolean right() {
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
        if (gameOver()) {
            return false;
        }
        addNewNumber();
        return gameOver();
    }

    // Returns true if game is still active, false if it ended.
    public boolean left() {
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
        if (gameOver()) {
            return false;
        }
        addNewNumber();
        return gameOver();
    }

    // Returns true if game is still active, false if it ended.
    public boolean up() {
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
        if (gameOver()) {
            return false;
        }
        addNewNumber();
        return gameOver();
    }

    // Returns true if game is still active, false if it ended.
    public boolean down() {
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
        if (gameOver()) {
            return false;
        }
        addNewNumber();
        return gameOver();
    }

    private int positions[][];
}
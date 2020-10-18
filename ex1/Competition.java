import java.util.Scanner;


/**
 * The Competition class represents a Nim competition between two players, consisting of a given number of rounds.
 * It also keeps track of the number of victories of each player.
 */
public class Competition {

    private static final int NUM_OF_PLAYERS = 2;
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;
    private static final int[] typesThatNeedVerboseMessages = {4};
    private Player[] playerArray = new Player[3];
    private int[] playersScore = new int[3];
    private boolean showMessage;


    /**
     * The class constructor, which receives two Player objects representing the two competing opponents and a
     * flag indicating whether game play messages should be printed to screen.
     *
     * @param player1        - The Player objects representing the first player.
     * @param player2        - The Player objects representing the second player.
     * @param displayMessage - a flag indicating whether game play messages should be printed to the console.
     */

    public Competition(Player player1, Player player2, boolean displayMessage) {
        playerArray[0] = null;
        playerArray[PLAYER_ONE] = player1;
        playerArray[PLAYER_TWO] = player2;
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            playersScore[i] = 0;
        }
        showMessage = displayMessage;
    }

    /**
     * If playerPosition = 1, the results of the first player is returned. If playerPosition = 2, the result of the
     * second player is returned. If playerPosition equals neither, -1 is returned.
     *
     * @param playerPosition - the player whose score we wish to know
     * @return the number of victories of a player.
     */

    public int getPlayerScore(int playerPosition) {
        switch (playerPosition) {
            case PLAYER_ONE:
                return this.playersScore[PLAYER_ONE];

            case PLAYER_TWO:
                return this.playersScore[PLAYER_TWO];

            default:
                return -1;
        }
    }

    /**
     * Run the game for the given number of rounds.
     *
     * @param numberOfRounds - number of rounds to play.
     */
    public void playMultipleRounds(int numberOfRounds) {
        System.out.println("Starting a Nim competition of " + numberOfRounds + " rounds between a " +
                this.playerArray[PLAYER_ONE].getTypeName() + " player and a " +
                this.playerArray[PLAYER_TWO].getTypeName() + " player.");
        for (int i = 0; i < numberOfRounds; i++) {
            /* initialize the board */

            Board newBoard = new Board();
            /* play a single game and add a point to the player who was victorious)*/

            int winner = this.playSingleGame(newBoard);
            this.playersScore[winner]++;
        }
        /*print the scores*/
        System.out.println("The results are " + this.getPlayerScore(1) + ":" +
                this.getPlayerScore(2));
    }


    /**
     * runs a single Nim Game. This function is called from the "playMultipleRounds" function.
     *
     * @param board - The fresh board on which the game will be played.
     * @return - the player who won the game.
     */
    private int playSingleGame(Board board) {
        //conditionedPrint prints ony if the flag for displaying messages is turned on.
        // displayes the welcome message, and sets the current player to player one.

        this.conditionedPrint("Welcome to the sticks game!");
        int currentPlayer = PLAYER_ONE;
        Move move = null;

        //while there are still sticks on the board, keep running the game

        while (board.getNumberOfUnmarkedSticks() > 0) {
            this.conditionedPrint("Player " + currentPlayer + ", it is now your turn!");

            //asks the player for a move

            move = this.playerArray[currentPlayer].produceMove(board);

            //checks if the move made the player is legal. if it isn't, the program will ask for another.

            while (board.markStickSequence(move) != 0) {
                this.conditionedPrint("Invalid move. Enter another:");
                move = this.playerArray[currentPlayer].produceMove(board);
            }

            //prints the current players move to the screen and changes the current player.
            this.conditionedPrint("Player " + currentPlayer + " made the move: " + move);
            currentPlayer = 3 - currentPlayer;
        }

        //print the player who won and return his number.

        this.conditionedPrint("Player " + currentPlayer + " won!");
        return currentPlayer;
    }

    /**
     * a wrapper function that only prints is the showMessage flag is on.
     *
     * @param string - the string to be printed.
     */
    private void conditionedPrint(String string) {
        if (this.showMessage)
            System.out.println(string);
    }

    /*
     * Returns the integer representing the type of player 1; returns -1 on bad
     * input.
     */
    private static int parsePlayer1Type(String[] args) {
        try {
            return Integer.parseInt(args[0]);
        } catch (Exception E) {
            return -1;
        }
    }

    /*
     * Returns the integer representing the type of player 2; returns -1 on bad
     * input.
     */
    private static int parsePlayer2Type(String[] args) {
        try {
            return Integer.parseInt(args[1]);
        } catch (Exception E) {
            return -1;
        }
    }

    /*
     * Returns the integer representing the type of player 2; returns -1 on bad
     * input.
     */
    private static int parseNumberOfGames(String[] args) {
        try {
            return Integer.parseInt(args[2]);
        } catch (Exception E) {
            return -1;
        }
    }

    /**
     * Checks if a number is in an array (kind of like "in" function in python).
     *
     * @param num   - the number
     * @param array - the array
     * @return - true if the number is in the array, and false otherwise
     */
    private static boolean numInArray(int num, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == num)
                return true;
        }
        return false;
    }


    /**
     * The method runs a Nim competition between two players according to the three user-specified arguments.
     * (1) The type of the first player, which is a positive integer between 1 and 4: 1 for a Random computer
     * player, 2 for a Heuristic computer player, 3 for a Smart computer player and 4 for a human player.
     * (2) The type of the second player, which is a positive integer between 1 and 4.
     * (3) The number of rounds to be played in the competition.
     *
     * @param args an array of string representations of the three input arguments, as detailed above.
     */
    public static void main(String[] args) {
        // Note - as stated in the forums, we can assume the input parameters to the main function are legit.

        // parse the input line
        int p1Type = parsePlayer1Type(args);
        int p2Type = parsePlayer2Type(args);
        int numGames = parseNumberOfGames(args);

        /* initialize a scanner to be used for i/o purposes */
        Scanner scanner = new Scanner(System.in);

        /* initialize the players */
        Player playerOne = new Player(p1Type, 1, scanner);
        Player playerTwo = new Player(p2Type, 2, scanner);

        /* check if we have a type of player that needs verbose messages prompted to the screen*/
        boolean needsMessages = numInArray(p1Type, typesThatNeedVerboseMessages)
                || numInArray(p2Type, typesThatNeedVerboseMessages);

        /* initialize the competition class, using the players we just created and setting the score to zero */
        Competition competition = new Competition(playerOne, playerTwo, needsMessages);

        /* play the whole competition consisting of numGames amount of games.*/
        competition.playMultipleRounds(numGames);

        /*free up the scanner resources*/
        scanner.close();
    }

}

import java.util.Random;
import java.util.Scanner;


/**
 * The Player class represents a player in the Nim game, producing Moves as a response to a Board state. Each player
 * is initialized with a type, either human or one of several computer strategies, which defines the move he
 * produces when given a board in some state. The heuristic strategy of the player is already implemented. You are
 * required to implement the rest of the player types according to the exercise description.
 *
 * @author OOP course staff
 */
public class Player {

    //Constants that represent the different players.
    /**
     * The constant integer representing the Random player type.
     */
    public static final int RANDOM = 1;
    /**
     * The constant integer representing the Heuristic player type.
     */
    public static final int HEURISTIC = 2;
    /**
     * The constant integer representing the Smart player type.
     */
    public static final int SMART = 3;
    /**
     * The constant integer representing the Human player type.
     */
    public static final int HUMAN = 4;

    private static final int BINARY_LENGTH = 4;    //Used by produceHeuristicMove() for binary representation of board rows.

    private final int playerType;
    private final int playerId;
    private Scanner scanner;

    /**
     * Initializes a new player of the given type and the given id, and an initialized scanner.
     *
     * @param type         The type of the player to create.
     * @param id           The id of the player (either 1 or 2).
     * @param inputScanner The Scanner object through which to get user input
     *                     for the Human player type.
     */
    public Player(int type, int id, Scanner inputScanner) {
        // Check for legal player type (we will see better ways to do this in the future).
        if (type != RANDOM && type != HEURISTIC
                && type != SMART && type != HUMAN) {
            System.out.println("Received an unknown player type as a parameter"
                    + " in Player constructor. Terminating.");
            System.exit(-1);
        }
        playerType = type;
        playerId = id;
        scanner = inputScanner;
    }

    /**
     * @return an integer matching the player type.
     */
    public int getPlayerType() {
        return playerType;
    }

    /**
     * @return the players id number.
     */
    public int getPlayerId() {
        return playerId;
    }


    /**
     * @return a String matching the player type.
     */
    public String getTypeName() {
        switch (playerType) {

            case RANDOM:
                return "Random";

            case SMART:
                return "Smart";

            case HEURISTIC:
                return "Heuristic";

            case HUMAN:
                return "Human";
        }
        //Because we checked for legal player types in the
        //constructor, this line shouldn't be reachable.
        return "UnknownPlayerType";
    }

    /**
     * This method encapsulates all the reasoning of the player about the game. The player is given the
     * board object, and is required to return his next move on the board. The choice of the move depends
     * on the type of the player: a human player chooses his move manually; the random player should
     * return some random move; the Smart player can represent any reasonable strategy; the Heuristic
     * player uses a strong heuristic to choose a move.
     *
     * @param board - a Board object representing the current state of the game.
     * @return a Move object representing the move that the current player will play according to his strategy.
     */
    public Move produceMove(Board board) {

        switch (playerType) {

            case RANDOM:
                return produceRandomMove(board);

            case SMART:
                return produceSmartMove(board);

            case HEURISTIC:
                return produceHeuristicMove(board);

            case HUMAN:
                return produceHumanMove(board);

            //Because we checked for legal player types in the
            //constructor, this line shouldn't be reachable.
            default:
                return null;
        }
    }

    /**
     * produces a random move.
     *
     * @param board - the current coard
     * @return - a Move type object representing the random move chosen by the computer.
     */
    private Move produceRandomMove(Board board) {
        Random rand = new Random();
        int leftTicker = 0;
        int rightTicker = 0;
        int numOfOpenSpacesInRow = 0;
        int rowNum, rowLength;
        do {

            //  randomizes a row number for the move

            rowNum = rand.nextInt(5) + 1;
            rowLength = board.getRowLength(rowNum);

            //  checks if there are any available sticks to mark in this row, otherwise, we will choose a new row.

            for (int i = 0; i < rowLength; i++) {

                // if we found a stick that is unmarked, we will increase the count of how many unmarked sticks there
                // are in this row/

                if (board.isStickUnmarked(rowNum, i + 1))
                    numOfOpenSpacesInRow++;
            }
        } while (numOfOpenSpacesInRow == 0);

        //   we'll mark all of the available spots in an array, and then choose from the available ones the left and
        //   right marker
        int[] availableSpots = new int[numOfOpenSpacesInRow];
        int j = 0;
        for (int i = 0; i < rowLength; i++) {
            if (board.isStickUnmarked(rowNum, i + 1)) {
                availableSpots[j] = i + 1;
                j++;
            }
        }

        //      randomizing the right and left sticks, and making sure that the right one is bigger than the left

        int first = availableSpots[rand.nextInt(availableSpots.length)];
        int second = availableSpots[rand.nextInt(availableSpots.length)];
        if (first > second) {
            leftTicker = second;
            rightTicker = first;
        } else {
            rightTicker = second;
            leftTicker = first;
        }

        // returns the move chosen randomly.
        return new Move(rowNum, leftTicker, rightTicker);
    }

    /**
     * produces an intelligent strategy move, as described in the readme file. The function first checks
     * if the board is in one of two positions such that the computer could win immediately. If not, then the function
     * strives to clear as much of the first row as possible. then, if none of these conditions are met,
     * then the function chooses a random move.
     *
     * @param board - the board state
     * @return - a smart move
     */
    private Move produceSmartMove(Board board) {

        /*
        first, we'll check if the board is in one of two "victory states", using the "winningMove" function. if the board
        can be won this turn, then smartMove will get the value of the move to play, otherwise, null.
         */
        Move smartMove = this.winningMove(board);
        if (smartMove != null) {
            return smartMove;
        }
        /*
        reaching here means the game isn't in one of the two victory states, and so the strategy is to clear the first
        row of all its sticks.
         */
        if (this.lastRowFull(board))
            return new Move(1, 1, 9);
        /*
        finally, if the board isn't in the victory states, or the first row can't be cleared in one move, we'll
        do a random move.
         */
        smartMove = produceRandomMove(board);
        return smartMove;
    }

    /**
     * returns true if the first row is full of unmarked sticks, and false otherwise.
     *
     * @param board - the state of board
     * @return true if the first row is full of unmarked sticks, and false otherwise.
     */
    private boolean lastRowFull(Board board) {
        for (int i = 1; i <= 9; i++) {
            if (!board.isStickUnmarked(1, i))
                return false;
        }
        return true;
    }

    /**
     * checks if the board is in one of the two victory states, meaning that the computer could win immediately.
     * If so, the board returns the move that will win the game, otherwise, it will return false. The victory states are
     * seperated by if the one stick if the last row in marked or unmarked.
     *
     * @param board - the state of the board
     * @return - the winning move, if there is one, null otherwise.
     */
    private Move winningMove(Board board) {

        //builds an array of size 3, that will hold the row number, the left stick index and the right stick index,
        // accordingly.

        int[] possibleMove = new int[3];

        //checks if the "top of the pyramid" is marked or unmarked, to know which victory state to check.

        if (board.isStickUnmarked(5, 1))
            possibleMove = this.clearRow(board);
        else
            possibleMove = this.clearRowButOne(board);

        //returns the move that corresponds with if there is a winnable move or not
        return makeMoveFromArray(possibleMove);
    }

    /**
     * the function receives an array of size 3, containing (in this order) the row number, the lef stick number, and
     * right stick number, and generates a Move type accordingly.
     *
     * @param moveArray - an array as described above
     * @return - a Move as described
     */
    private Move makeMoveFromArray(int[] moveArray) {
        if (moveArray[0] != 0)
            return new Move(moveArray[0], moveArray[1], moveArray[2]);
        return null;
    }

    /**
     * This function checks if the board is in the first "victory state", meaning, there the top stick is unmarked,
     * there is another row that can be cleared in a one move, and all other rows are empty.
     *
     * @param board - the state of the board
     * @return - an array containing the winning move, if one is available, otherwise an empty array.
     */
    private int[] clearRow(Board board) {

        // initializes an empty move
        int[] emptyMove = {0, 0, 0};

        /* checks if there is exactly one empty row, other than the top row*/
        if (!this.allRowEmptyButOne(board))
            return emptyMove;
        //we have exactly 1 row that isn't empty, besides the top row. let's find it.
        int rowToCheck = this.notEmptyRow(board);
        assert rowToCheck != 0;
        // if we can clear the row in 1 move, we'll do that move, otherwise, we'll return an empty move.
        return clearRowMove(board, rowToCheck, emptyMove);
    }

    /**
     * the function checks if the move being done isn't removing one stick, and if it isn't removes a single stick from
     * the move.
     *
     * @param board - the board state
     * @return - an array containing the move coordinates
     */
    private int[] clearRowButOne(Board board) {
        int[] possibleMove = clearRow(board);
        if (possibleMove[1] != possibleMove[2])
            possibleMove[2]--;
        return possibleMove;
    }

    /**
     * the function checks if there exists exactly one row that isn't emprty out of the 4 first rows in the board.
     *
     * @param board - the board state
     * @return - true if there is (exactly) one non empty row (not counting the last row) and false otherwise.
     */
    private boolean allRowEmptyButOne(Board board) {
        int numEmpty = 0;
        for (int i = 1; i <= 4; i++)
            numEmpty += isRowEmpty(board, i);
        if (numEmpty != 1)
            return false;
        return true;
    }

    /**
     * returns the number of the row that isn't empty
     *
     * @param board - the board state
     * @return - the row that isn't empty
     */
    private int notEmptyRow(Board board) {
        for (int i = 1; i <= 4; i++) {
            if (isRowEmpty(board, i) == 1)
                return i;
        }
        // we already checked that there is only one non empty row, this line shouldn't be reachable.
        return 0;
    }

    /**
     * Checks if a certain row can be cleared in a single move, and returns an array with the according coordinates.
     *
     * @param board      - the board state
     * @param rowToCheck - the row to be checked
     * @param emptyMove  - an array representing an empty move (can be changed later on)
     * @return - an array as described above.
     */
    private int[] clearRowMove(Board board, int rowToCheck, int[] emptyMove) {

        // our strategy is to check all of the unmarked sticks in thw row are in a single "cluster"

        int firstTicker = 0;
        int lastTicker = 0;

        // marks the first and last unmarked sticks in the row to be checked

        for (int i = 1; i <= board.getRowLength(rowToCheck); i++) {
            if (board.isStickUnmarked(rowToCheck, i)) {
                lastTicker = i;
                if (firstTicker == 0)
                    firstTicker = i;
            }
        }
        //making sure we found anything at all.

        assert firstTicker != 0;

        /*now, we will check the between the first and last unmarked sticks, there are no marked sticks - meaning,
        the row can be cleared with a single move*/

        boolean moveLegal = true;
        for (int i = firstTicker; i <= lastTicker; i++)
            if (!board.isStickUnmarked(rowToCheck, i))
                moveLegal = false;
        if (!moveLegal)
            /*
            if the row doesn't satisfy the requirements set above, we'll return an empty array, otherwise, we'll return
            the move that will clear the whole row.
             */
            return emptyMove;
        else
            return new int[]{rowToCheck, firstTicker, lastTicker};
    }

    /**
     * checks if a row in the board is empty or not
     *
     * @param board - the board
     * @param row   - the row number to be checked
     * @return 0 if the row is empty, 1 otherwise.
     */
    private int isRowEmpty(Board board, int row) {
        for (int i = 1; i <= board.getRowLength(row); i++) {
            if (board.isStickUnmarked(row, i))
                return 1;
        }
        return 0;
    }

    /*
     * Interact with the user to produce his move.
     */
    private Move produceHumanMove(Board board) {
        int playersChoice;
        do {
            System.out.println("Press 1 to display the board. Press 2 to make a move:");
            playersChoice = this.scanner.nextInt();
            if (playersChoice == 1)
                System.out.println(board);
            else if (playersChoice != 2)
                System.out.println("Unsupported command");
        } while (playersChoice != 2);
        System.out.println("Enter the row number:");
        int inputRow = scanner.nextInt();
        System.out.println("Enter the index of the leftmost stick:");
        int inputLeftStick = scanner.nextInt();
        System.out.println("Enter the index of the rightmost stick:");
        int inputRightStick = scanner.nextInt();
        return new Move(inputRow, inputLeftStick, inputRightStick);
    }

    /*
     * Uses a winning heuristic for the Nim game to produce a move.
     */
    private Move produceHeuristicMove(Board board) {

        int numRows = board.getNumberOfRows();
        int[][] bins = new int[numRows][BINARY_LENGTH];
        int[] binarySum = new int[BINARY_LENGTH];
        int bitIndex, higherThenOne = 0, totalOnes = 0, lastRow = 0, lastLeft = 0, lastSize = 0, lastOneRow = 0, lastOneLeft = 0;

        for (bitIndex = 0; bitIndex < BINARY_LENGTH; bitIndex++) {
            binarySum[bitIndex] = 0;
        }

        for (int k = 0; k < numRows; k++) {

            int curRowLength = board.getRowLength(k + 1);
            int i = 0;
            int numOnes = 0;

            for (bitIndex = 0; bitIndex < BINARY_LENGTH; bitIndex++) {
                bins[k][bitIndex] = 0;
            }

            do {
                if (i < curRowLength && board.isStickUnmarked(k + 1, i + 1)) {
                    numOnes++;
                } else {

                    if (numOnes > 0) {

                        String curNum = Integer.toBinaryString(numOnes);
                        while (curNum.length() < BINARY_LENGTH) {
                            curNum = "0" + curNum;
                        }
                        for (bitIndex = 0; bitIndex < BINARY_LENGTH; bitIndex++) {
                            bins[k][bitIndex] += curNum.charAt(bitIndex) - '0'; //Convert from char to int
                        }

                        if (numOnes > 1) {
                            higherThenOne++;
                            lastRow = k + 1;
                            lastLeft = i - numOnes + 1;
                            lastSize = numOnes;
                        } else {
                            totalOnes++;
                        }
                        lastOneRow = k + 1;
                        lastOneLeft = i;

                        numOnes = 0;
                    }
                }
                i++;
            } while (i <= curRowLength);

            for (bitIndex = 0; bitIndex < BINARY_LENGTH; bitIndex++) {
                binarySum[bitIndex] = (binarySum[bitIndex] + bins[k][bitIndex]) % 2;
            }
        }


        //We only have single sticks
        if (higherThenOne == 0) {
            return new Move(lastOneRow, lastOneLeft, lastOneLeft);
        }

        //We are at a finishing state
        if (higherThenOne <= 1) {

            if (totalOnes == 0) {
                return new Move(lastRow, lastLeft, lastLeft + (lastSize - 1) - 1);
            } else {
                return new Move(lastRow, lastLeft, lastLeft + (lastSize - 1) - (1 - totalOnes % 2));
            }

        }

        for (bitIndex = 0; bitIndex < BINARY_LENGTH - 1; bitIndex++) {

            if (binarySum[bitIndex] > 0) {

                int finalSum = 0, eraseRow = 0, eraseSize = 0, numRemove = 0;
                for (int k = 0; k < numRows; k++) {

                    if (bins[k][bitIndex] > 0) {
                        eraseRow = k + 1;
                        eraseSize = (int) Math.pow(2, BINARY_LENGTH - bitIndex - 1);

                        for (int b2 = bitIndex + 1; b2 < BINARY_LENGTH; b2++) {

                            if (binarySum[b2] > 0) {

                                if (bins[k][b2] == 0) {
                                    finalSum = finalSum + (int) Math.pow(2, BINARY_LENGTH - b2 - 1);
                                } else {
                                    finalSum = finalSum - (int) Math.pow(2, BINARY_LENGTH - b2 - 1);
                                }

                            }

                        }
                        break;
                    }
                }

                numRemove = eraseSize - finalSum;

                //Now we find that part and remove from it the required piece
                int numOnes = 0, i = 0;
                while (numOnes < eraseSize) {

                    if (board.isStickUnmarked(eraseRow, i + 1)) {
                        numOnes++;
                    } else {
                        numOnes = 0;
                    }
                    i++;

                }
                return new Move(eraseRow, i - numOnes + 1, i - numOnes + numRemove);
            }
        }

        //If we reached here, and the board is not symmetric, then we only need to erase a single stick
        if (binarySum[BINARY_LENGTH - 1] > 0) {
            return new Move(lastOneRow, lastOneLeft, lastOneLeft);
        }

        //If we reached here, it means that the board is already symmetric, and then we simply mark one stick from the last sequence we saw:
        return new Move(lastRow, lastLeft, lastLeft);
    }


}

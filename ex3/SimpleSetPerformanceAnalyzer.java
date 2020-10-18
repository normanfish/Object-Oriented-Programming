import java.util.*;

public class SimpleSetPerformanceAnalyzer {
    /**
     * A class that performs the tests specified in the assignment. Each test is a function,
     * and the results, the data sets themselves, and the scanner, are class variables.
     */
    private static final int NANO_TO_MILLI = 1000000;
    private static final long ONE_SECOND_IN_NANO = 1000000000;
    private static int NUM_OF_DATA_STRUCTURES = 5;
    private static int NUM_OF_TESTS = 6;
    private static int OPEN_HASH_SET = 0;
    private static int CLOSED_HASH_SET = 1;
    private static int JAVA_TREE_SET = 2;
    private static int JAVA_LINKED_LIST = 3;
    private static int JAVA_HASH_SET = 4;
    private static int FIRST_TEST = 0;
    private static final int SECOND_TEST = 1;
    private static final int THIRD_TEST = 2;
    private static final int FOURTH_TEST = 3;
    private static final int FIFTH_TEST = 4;
    private static final int SIXTH_TEST = 5;

    //the array containing the data sets
    private SimpleSet[] testArray = new SimpleSet[NUM_OF_DATA_STRUCTURES];

    // a two dimensional array that holds the result of each data set in each test
    private long[][] results;
    // the name (path) of the data files used for testing
    private String[] data1 ;
    private String[] data2;

    /**
     * Class constructor. Initializes the array of data sets by creating blank data sets and a fresh results array.
     */
    private SimpleSetPerformanceAnalyzer() {
        // intialize the array and the result array
        initializeArray();
        results = new long[NUM_OF_TESTS][NUM_OF_DATA_STRUCTURES];

        //parse the dat afiles using the Ex3Utils class provided to us.
        data1 = Ex3Utils.file2array("data1.txt");
        data2 = Ex3Utils.file2array("data2.txt");
    }

    /**
     * creates the 5 data sets of the assignment using the default constructor of each.
     */
    private void initializeArray() {
        testArray[OPEN_HASH_SET] = new OpenHashSet();
        testArray[CLOSED_HASH_SET] = new ClosedHashSet();
        testArray[JAVA_TREE_SET] = new CollectionFacadeSet(new TreeSet<>());
        testArray[JAVA_LINKED_LIST] = new CollectionFacadeSet(new LinkedList<>());
        testArray[JAVA_HASH_SET] = new CollectionFacadeSet(new HashSet<>());
    }

    /**
     * creates the 5 data sets of the assignment using the data constructor of each.
     */
    private void initializeArrayFromData(String[] dataArray) {
        testArray[OPEN_HASH_SET] = new OpenHashSet(dataArray);
        testArray[CLOSED_HASH_SET] = new ClosedHashSet(dataArray);
        testArray[JAVA_TREE_SET] = new CollectionFacadeSet(new TreeSet<>(Arrays.asList(dataArray)));
        testArray[JAVA_LINKED_LIST] = new CollectionFacadeSet(new LinkedList<>(Arrays.asList(dataArray)));
        testArray[JAVA_HASH_SET] = new CollectionFacadeSet(new HashSet<>(Arrays.asList(dataArray)));
    }

    /**
     * first and second test - checks the time for adding words from data1 and data2 after data sets were initialized
     * from blank.
     */
    private void test1and2() {
        //call the function that runs the the adding elements one by one
        results[FIRST_TEST] = checkTimeForAdding(data1);
        initializeArray();
        results[SECOND_TEST] = checkTimeForAdding(data2);
    }

    /**
     * checks the amount of time it takes to look for the word "hi" after initialized by the data constructor from data1.
     */
    private void test3() {
        initializeArrayFromData(data1);
        for (int i = 0; i < NUM_OF_DATA_STRUCTURES; i++)
            results[THIRD_TEST] = checkTimeForContains("hi");
    }

    /**
     * checks the amount of time it takes to look for -13170890158 after initialized by the data constructor from data1.
     */
    private void test4() {
        initializeArrayFromData(data1);
        for (int i = 0; i < NUM_OF_DATA_STRUCTURES; i++)
            results[FOURTH_TEST] = checkTimeForContains("-13170890158");
    }

    /**
     * checks the amount of time it takes to look for 23 after initialized by the data constructor from data2.
     */
    private void test5() {
        initializeArrayFromData(data2);
        for (int i = 0; i < NUM_OF_DATA_STRUCTURES; i++)
            results[FIFTH_TEST] = checkTimeForContains("23");
    }

    /**
     * checks the amount of time it takes to look for the word "hi" after initialized by the data constructor from data2.
     */
    private void test6() {
        initializeArrayFromData(data2);
        for (int i = 0; i < NUM_OF_DATA_STRUCTURES; i++)
            results[SIXTH_TEST] = checkTimeForContains("hi");
    }

    /**
     * checks the time each Set takes to add an array of strings.
     *
     * @param data - the array of strings
     * @return - the time it took to add the strings, in milliseconds.
     */
    private long[] checkTimeForAdding(String[] data) {
        long[] testTimes = new long[5];
        for (int i = 0; i < NUM_OF_DATA_STRUCTURES; i++) {
            SimpleSet type = testArray[i];
            long timeBefore = System.nanoTime();
            for (String word : data) {
                type.add(word);
            }
            testTimes[i] = (System.nanoTime() - timeBefore) / NANO_TO_MILLI;
        }
        return testTimes;
    }

    /**
     * The function checks the amount of time it takes for each data set to check if a string is inside of it.
     * Since checking for a single String can be hard to measure, the function runs the contains function on the string
     * for a seconds, and records each time it completed a full query. The returned value would be the amount of times
     * the data set completed the "contains" function, divided by the total time (a little more than 1 seconds).
     *
     * @param data - the string to check
     * @return - the time it took for each data Set to run "contains" on the string
     */
    private long[] checkTimeForContains(String data) {
        //this part is the warm up, in which we will search for an increasing number 10,000 times in each data structure
        long[] testTimes = new long[5];
        for (int i = 0; i < NUM_OF_DATA_STRUCTURES; i++) {
            SimpleSet type = testArray[i];
            int rounds = 0;
            String j = "0";
            //warm up - 10000 iterations
            while (rounds<10000) {
                type.contains(j);
                rounds++;
                j = Integer.toString(rounds);
            }

            // now that we're warmed up, we'll be checking time of the "contains" method in each data structure.
            // we will run "contains" on a certain value for one second, and divide by the amount of times
            // the search has been completed.
            long startTime = System.nanoTime();
            long end = startTime + ONE_SECOND_IN_NANO;
            int iterations = 0;
            while (System.nanoTime() < end) {
                type.contains(data);
                iterations++;
            }
            long totalTime = System.nanoTime();
            testTimes[i] = totalTime / iterations;
        }
        return testTimes;
    }

    /**
     * prints the results array
     */
    private void print() {
        for (int test = FIRST_TEST; test < NUM_OF_TESTS; test++) {
            System.out.println("Test Number " + (test + 1) + ": ");
            for (int type = 0; type < NUM_OF_DATA_STRUCTURES; type++) {
                System.out.println(results[test][type] + " ");
            }
            System.out.println("\n");
        }
    }

    /**
     * runs the tests
     */
    private void runTests() {
        test1and2();
        test3();
        test4();
        test5();
        test6();
    }

    public static void main(String[] args) {
        //create the Analyzer object and initialize the array
        SimpleSetPerformanceAnalyzer analyzer = new SimpleSetPerformanceAnalyzer();
        analyzer.runTests();
        analyzer.print();
    }
}

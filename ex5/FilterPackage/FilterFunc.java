package filesprocessing.FilterPackage;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is the abstract parent class for Filter Functions, that will be used by the program to filter files,
 * each function uses a different criteria to sort. In general, we have 3 types of Filter Functions:
 * 1. Functions that filter by the size of the files.
 * 2. Functions that filter by the permissions of the file.
 * 3. Functions that filter by the name of the file.
 * Each function checks the arguments passed to the function, to see if they make sense, and then filter accordingly.
 * For example: if the filter is between#10#5, the filter will be "all" (invalid parameters and warning in line message).
 * But, if the line is WRITABLE#YES, then the filter will be writable as expected.
 */
abstract public class FilterFunc {

    /**
     * The following are class constants that are going to be useful for each implementing filter function. Their job
     * is to specify the expected location of the relevant parameter for the filter in the "filterparameter" array,
     * which is located in the Command class.
     */
    //upper, lower, and size limit bound for type 1 filter functions.
    final int UPPER_BOUND = 1;
    final int LOWER_BOUND = 0;
    final int LIMIT = 0;
    // the location of the substring to be checked against the file name (type 3)
    final int SUBSTRING = 0;
    // the location of the parameter for type 2 functions (for instance, whether the file is readable)
    final int YESORNOPLACE = 0;
    final int BYTES_TO_KB = 1024;
    //a boolean flag class variable, whose job it is to signal if the parameters passed from the command line are indeed
    // legal parameters. will be used by the update function.
    boolean validParameters = true;

    int CORRECT_NUM_OF_PARAMS = 1;


    /**
     * The main function that does the "filtering", basically, receives a file path (in string form) and checks whether
     * the file complies with the filter. Here, there are two options:
     * The test parameters are invalid, in which case, the default filter being "all", it is an automatic pass.
     * The test parameters are valid, in which case, the function uses the "actualfilter" function, which is implemented
     * in each of the inheriting functions.
     * @param fileName - the string representing the path of the file to be tested against the filter.
     * @return - true if the file agrees with the filter, and false otherwise.
     */
    boolean accept(String fileName) {
        if (!validParameters)
            //the fitler parameters are invalid, therefor, we will just return true
            return true;
        else
            //run the actual filter.
            return actualFilter(fileName);
    }

    /**
     * checks whether the command line parameters for the filter is empty. this is a useful function that comes in handy
     * in most of the filters.
     * @param testParameters - the test parameters passed on from the command line
     * @return - true if there were no parameters, false if there were.
     */
    boolean isTestParamEmpty(ArrayList<String> testParameters) {
        return testParameters.size() == 0;
    }

    /**
     * This function is responsible for 2 things:
     * checks the validity of the filter parameters, and updates the function with the filter parameters to be checked
     * against. This function is abstract and is implemnted in each of the inhereting functions, naturally, because each
     * filter is different.
     * @param testParameters - the array containing the test parameters
     */
    abstract void update(ArrayList<String> testParameters);

    /**
     * this function receives a file (assuming the test parameters were legal) and checks whether it passes the filter.
     * Implemented in each function seperately.
     * @param fileName the name of the file
     * @return true if it passes the filter, and false otherwise
     */
    abstract boolean actualFilter(String fileName);

    /**
     * checks if a value is inside an array of legal values
     * @param value a string value to be checked
     * @param legalValues the legal values from which we wish to check value's inclusiveness
     * @return true if value is in legaValues, and false otherwise
     */
    static boolean checkIfValueIsFromSublist(String value, String[] legalValues) {
        return (Arrays.asList(legalValues).contains(value));
    }
    static boolean isWrongNumberOfParameters(int correctNumOfParameters,ArrayList<String> filterParam){
        return correctNumOfParameters!=filterParam.size();
    }

}


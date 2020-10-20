package filesprocessing.FilterPackage;


import filesprocessing.Command;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This Filter class is in charge of receiving a list of commands from the Parser, and filtering the files in the
 * directory according to the filters specified in each command.
 */
public class Filter {

    //a Set containing all of the files in each directory (as instructed, we are not supposed to touch other directories).
    private TreeSet<String> actualFiles;
    //The name of the directory
    private String directory;
    //All of the filter functions available to the program.
    private TreeMap<String, FilterFunc> filterFunctions;
    //The warning message that is to be printed in case the filter parameters from the command file are invalid.
    private static final String errorMessageType2 = "Warning in line ";

    // a string containing all of the names of the filter function available
    public static String[] allFilters = {"greater_than", "between", "smaller_than", "file", "contains", "prefix", "suffix", "writable", "executable", "hidden", "all"};

    /**
     * The class constructor. Initializes the Dictionary of filter functions using the FilterFunction factory, sets the
     * directory we will be working with, and initializes the Set that will hold the names of all the files
     * @param dir the name of the directory we wish to filter from
     */
    public Filter(String dir) {
        directory = dir;
        actualFiles = getActualFiles(); // is never null, so we don't have to worry about null pointers
        //initializes the factory and gets from it the filter functions.
        FilterFuncFactory filterFactory = new FilterFuncFactory(directory);
        filterFunctions = filterFactory.getAllFuncs();
    }

    /**
     * Goes over each "File" type in the directory (could be other directories as well, as defined by the abstract type
     * "File" in java). and checks whether it is a file type or not. returns a set of all of the files.
     * @return - a set containing the names of all the actual files in the directory.
     */
    private TreeSet<String> getActualFiles() {
        actualFiles = new TreeSet<>();
        File test1 = new File(directory);

        //System.out.println(test1.setReadable(true));
        //System.out.println(test1.canRead());

        //should return a String array containing the names of every "object" in the directory
        String[] listOfDirs = test1.list();
        if (listOfDirs!=null) {
            //checks whether the name represents a file type, and adds it to the set
            for (String fileName : listOfDirs
                    ) {
                if (new File(directory + File.separator + fileName).isFile())
                    actualFiles.add(directory + File.separator + fileName);
            }
        }
        return actualFiles;
    }

    /**
     * The function that actually does the filtering. Receives a command type that represents which type of filter
     * function and which parameters to use, gets the appropriate filter function "ready" by updating its test parameters,
     * and then spereates the files that pass the test from those that don't.
     * @param filterAndOrder The command specifying which filter to run and with which parameters
     * @return tan arraylist containing the files that did or didn't pass the test, according to what we are being asked.
     */
    public ArrayList<String> filter(Command filterAndOrder) {
        //gets the relevant filter function for the comparison, using the disctionary key being supplied from the "command"
        FilterFunc currentFilter = filterFunctions.get(filterAndOrder.getFilter());
        //validate the test parameters and update them in the function
        currentFilter.update(filterAndOrder.getFilterParameters());

        ArrayList<String> filesThatPassedFilter = new ArrayList<>();
        ArrayList<String> filesThatDidntPassFilter = new ArrayList<>();


        //print the warning if the filter parameters are invalid
        if (!currentFilter.validParameters || filterAndOrder.isErrorInFilterName())
            System.err.println(errorMessageType2 + filterAndOrder.getFilterParametersLine());


        //Go over each file name, and check if it passed the fitler. place it accordingly in the correctly arraylist and
        // return whatever.
        for (String filePath : actualFiles) {
            if (currentFilter.accept(filePath))
                filesThatPassedFilter.add(filePath);
            else {
                filesThatDidntPassFilter.add(filePath);
            }
        }
        if (filterAndOrder.isNotFilter()&&currentFilter.validParameters)
            return filesThatDidntPassFilter;
        else {
            return filesThatPassedFilter;
        }
    }

}

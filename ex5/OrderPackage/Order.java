package filesprocessing.OrderPackage;


import filesprocessing.Command;

import java.util.*;

/**
 * This class in in charge of ordering the files that came back from the filter in a certain order.
 */
 public class Order {

    private static final String errorMessageType2 = "Warning in line ";

    // a list of all of the orders available
    public static final String[] allOrders = {"abs", "type", "size"};
    //the files after filtering
    private ArrayList<String> filesToSort;
    private TreeMap<String,Comparator<String>> orderFunctions;

    /**
     * The class constructor, receives the files to be ordered and initializes the order functions that can be used
     * @param files the files that have been filtered
     * @param directory the name of the directory, (mainly used when we wish to sort by file name and not the absolute name)
     */
    public Order(ArrayList<String> files, String directory) {
        //the files to sort, received from the Filter object
        filesToSort = files;
        //creates the factory for the Order Functions and receives them
        OrderFuncFactory filterFactory = new OrderFuncFactory(directory);
        orderFunctions = filterFactory.getAllFuncs();
    }

    /**
     * the actual order functions which orders the files, according to the command .
     * @param command the command which contains information about how we should order the files
     * @return the name of the filtered files, sorted, and in a string array
     */
    public String[] order(Command command) {
        //first, we'll handle the case that command returns that the order should be by absolute value. In this case,
        //we could either have invalid parameters, which led us to reverting to the default order (which is absolute),
        // or the order really was just plain absolute. we'll check if the order name is valid or not, by checking the
        // appropriate variable in "command".
        if(command.getOrder().equals("abs")) {
            int NO_WARNING_IN_ORDER = 0;
            //getorderparametersline holds i formation about the line where we may have encountered an error, if it's 0,
            // then tat means there is no warning.
            if(command.getOrderParametersLine()!= NO_WARNING_IN_ORDER)
                //means there was a bad order name in the command file, so we have to print a warning.
                System.err.println(errorMessageType2+command.getOrderParametersLine());
            //since the files are by default sorted by their absolute path, we don't have to sort them any further, and
            //we can simply transfer them to an array and return.
            return toRegularArray(filesToSort,command);
        }
        //if we reached here, we are not in the default "absolute" order, and therfor we have to sort the file names,
        //according the correct order function.
        filesToSort.sort(orderFunctions.get(command.getOrder()));
        return toRegularArray(filesToSort,command);
    }

    /**
     * Converts an arraylist of strings to a regular array of string, and reverses the order if the "reverse" flag in
     * command is turned on.
     * @param filesList - an arraylist of file names
     * @param command - The current order command, which we will just use to see if the order should be reversed or not.
     * @return a regular array of strings, containing the file names.
     */
    private String[] toRegularArray(ArrayList<String> filesList,Command command){
        //transfer the file names from the arraylist to the array
        String[] filesArray = new String[filesList.size()];
        for (int i=0;i<filesArray.length;i++){
            filesArray[i]=filesList.get(i);
        }
        //check if the order should be reversed, and if so, reverse the order of the names.
        if(command.isReverseOrder())
            Collections.reverse(Arrays.asList(filesArray));
        //return the array
        return filesArray;
    }



}

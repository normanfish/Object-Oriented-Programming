package filesprocessing;

import filesprocessing.FilterPackage.Filter;
import filesprocessing.OrderPackage.Order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * receives 2 path names, one for the command file and one for the directory of files. prints all of the files according
 * to the description in ex5.
 */
public class DirectoryProcessor {
    //useful class constants
    private static final int NUM_OF_ARGUMENTS = 2;
    private static final String INVALID_USAGE_MSG = "ERROR: Wrong number of arguments. \n";
    private static final String ERROR_WHILE_READING_MSG = "ERROR: An error occurred while accessing the " +
            "Commands file. \n ";
    private static final int COMMAND_FILE_INDEX = 1;
    private static final int SOURCE_DIR_INDEX = 0;

    /**
     * the main function. receives as input the 2 file names, one for the command file name and the other the directory
     * name, and prints the files as required.
     *
     * @param args the command line arguments. ideally, the first argument will be the source directory name, and
     *             the second the command file name.
     */
    public static void main(String[] args) {
        //checks if we don't have exactly 2 arguments, and prints an error
        if (args.length != NUM_OF_ARGUMENTS) {
            System.err.println(INVALID_USAGE_MSG);
            return;
        }
        // attempts to parse the command file into section, such that each section will be one "command" object.
        Parser parser = new Parser(new File(args[COMMAND_FILE_INDEX]).getAbsolutePath());
        try {
            //checks for type 2 errors, such as bad section names and so on.
            parser.error2Handler();
            // the array list of commands (read - sections)
            ArrayList<Command> commands;
            //parses the command file
            commands = parser.parse();
            //creates a new filter object
            Filter filter = new Filter(args[SOURCE_DIR_INDEX]);

            //for each command (section), filer the files and order them, then print the results
            for (Command command : commands
                    ) {
                Order order = new Order(filter.filter(command), args[SOURCE_DIR_INDEX]);
                printAll(order.order(command), args[SOURCE_DIR_INDEX]);
            }
        }
        //catching the exceptions, in case not everything went smoothly :-(
        catch (IOException ioe) {
            System.err.println(ERROR_WHILE_READING_MSG);
        } catch (BadSubException commandFileError) {
            System.err.println(commandFileError.getMessage());
        }
    }

    /**
     * prints all of the strings in an array
     *
     * @param array the array of strings to be printed
     */
    private static void printAll(String[] array, String dir) {
        for (String object : array
                ) {
            System.out.println(object.replace(dir+File.separator, ""));
        }
    }

}

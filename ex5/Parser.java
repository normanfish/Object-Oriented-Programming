package filesprocessing;


import filesprocessing.FilterPackage.*;
import filesprocessing.OrderPackage.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is in charge of parsing the command file into sections.
 */
public class Parser {
    private static final String FILTER_SUBSECTION = "FILTER";
    private static final String ORDER_SUBSECTION = "ORDER";
    private static final String FILTER_ERROR2_MSG = "ERROR: Bad FILTER sub-section. \n";
    private static final String ORDER_ERROR2_MSG = "ERROR: Bad ORDER sub-section. \n";
    private static final String PARAM_DELIMITER = "#";
    private static final int NAME = 0;
    private static final int PARAM1 = 1;
    private static final int ORDER_WITH_SUFFIX = 2;
    private static final int FIRST_LINE = 1;
    private static final String DEFAULT_FILTER = "all";
    private static final String DEFAULT_ORDER = "abs";
    private static final String NOT_SUFFIX = "NOT";
    private String commandFile;

    /**
     * The constructor of the Parser class.
     *
     * @param fileName a String object representing the command file path.
     */
    public Parser(String fileName) {
        commandFile = fileName;
    }

    /**
     * This method searches for type 2 errors in the command file and throws a relevant error 2 exception
     * with a relevant message when found.
     *
     * @throws IOException     in case there was a problem accessing the command file.
     * @throws BadSubException in case of a type 2 error in the command file.
     */

    protected void error2Handler() throws IOException, BadSubException {
        FileReader fileReader = new FileReader(commandFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        if (line == null) {
            bufferedReader.close();
            return;
        }
        while (line != null) {
            if (!line.equals(FILTER_SUBSECTION)) { //checks the FILTER sub-section
                bufferedReader.close();
                throw new BadSubException(FILTER_ERROR2_MSG);
            }
            bufferedReader.readLine();// because we can assume there's always one line after FILTER
            line = bufferedReader.readLine();
            if (line == null || !line.equals(ORDER_SUBSECTION)) { //checks the ORDER sub-section
                bufferedReader.close();
                throw new BadSubException(ORDER_ERROR2_MSG);
            }
            line = bufferedReader.readLine();
            if (line != null && !line.equals(FILTER_SUBSECTION)) { //checks if there's an order type in the ORDER
                // subsection
                line = bufferedReader.readLine();
            }
        }
        bufferedReader.close();
    }


    /**
     * This method receives a String array which represents a line in the command file (a type of filter/order
     * with it's parameters), a start and finish index and a Command object. It creates an ArrayList<String> of
     * the parameters written in the line and sets them into the given command object.
     *
     * @param data the line in the command file (a type of filter/order
     *      * with it's parameters)
     * @param start start index
     * @param end finish index
     * @param command the command object
     */
    private void transferParameters(String[] data, int start, int end, Command command) {
        ArrayList<String> parameters = new ArrayList<>();
        int index = start;
        while (index < end) {
            parameters.add(data[index]);
            index++;
        }
        command.setFilterParameters(parameters);
    }

    /**
     * This method is in charge of parsing the command file into sections while considering possible type 1
     * errors in each FILTER/ORDER sub-section.
     *
     * @return an ArrayList of command objects which represent a single section in the command file.
     * @throws IOException in case there was a problem accessing the command file.
     */
    ArrayList<Command> parse() throws IOException {
        ArrayList<Command> commands = new ArrayList<>();
        FileReader fileReader = new FileReader(commandFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        int currentLine = FIRST_LINE;
        while (line != null) {
            Command command = new Command();
            if (line.equals(FILTER_SUBSECTION)) { //FILTER subsection
                line = bufferedReader.readLine();
                currentLine++;
                String[] data = line.split(PARAM_DELIMITER);
                command.setFilterParametersLine(currentLine);
                if (!Arrays.asList(Filter.allFilters).contains(data[NAME])) { //bad filter name in FILTER subsection
                    command.setFilter(DEFAULT_FILTER);
                    command.setErrorInFilterName(true);
                } else {
                    command.setFilter(data[NAME]);
                }
                if (data[data.length - 1].equals(NOT_SUFFIX)) {
                    command.setNotFilter(true);
                    this.transferParameters(data, PARAM1, data.length - 1, command);
                } else {
                    this.transferParameters(data, PARAM1, data.length, command);
                }
                bufferedReader.readLine(); //now in ORDER line
                currentLine++;
            }
            line = bufferedReader.readLine();//now possibly in the ORDER subsection
            currentLine++;
            if (line != null) {//not a case of an ORDER section w/o subsection and end of file
                String[] data = line.split(PARAM_DELIMITER);
                if (!data[NAME].equals(FILTER_SUBSECTION)) {//not a case of an ORDER section w/o subsection
                    if (!Arrays.asList(Order.allOrders).contains(data[NAME])) { //incorrect order name
                        command.setOrder(DEFAULT_ORDER);
                        command.setOrderParametersLine(currentLine);
                    } else { //correct order name
                        command.setOrder(data[NAME]);
                        if (data.length == ORDER_WITH_SUFFIX) { //order has REVERSE suffix
                            command.setReverseOrder(true);
                        }
                    }
                    line = bufferedReader.readLine();
                    currentLine++;
                }
            } else { //is case of an ORDER section w/o subsection and end of file
                command.setOrder(DEFAULT_ORDER);
            }
            commands.add(command);
        }
        bufferedReader.close();
        return commands;
    }
}

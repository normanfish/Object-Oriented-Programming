package filesprocessing;

import java.util.ArrayList;

/**
 * a class which holds information about the current section in the command file. An array of this class is passed from
 * the "parser" to the "filter" and then to the order
 */
public class Command {
    //2 boolean variables, that are false if the order/filter is "normal" (no not in the command line) and true if
    // the order should be reversed or the filter should return all of the files that don't match up to the filter.
    private boolean reverseOrder;
    private boolean notFilter;
    // the list of parameters
    private ArrayList<String> filterParameters;
    //the name of the filter
    private String filter;
    //the name of the order
    private String order;
    //whether there were invalid parameters in the filter subsection
    private boolean errorInFilterName;
    //the line number of the filter parameters
    private int filterParametersLine;
    // the line number of the order name in the order subsection. This variable only has meaning if there is an
    //error to be printed about the order name, otherwise, it will just be 0.
    private int orderParametersLine;

    /**
     * the default constructor, initializes the class variables to their default values
     */
    public Command() {
        filterParameters = new ArrayList<>();
        filter = "all";
        order = "abs";
    }

    /**
     * sets the error in filter name flag
     *
     * @param val the new value for whether there was an error in the filter name.
     */
    public void setErrorInFilterName(boolean val) {
        this.errorInFilterName = val;
    }

    /**
     * sets the line in which the filter name is in the current section
     *
     * @param line the new line
     */
    public void setFilterParametersLine(int line) {
        this.filterParametersLine = line;
    }

    /**
     * sets the new line from which the order name had an error. 0 is there was no error in the order name
     *
     * @param line the line in which there was an error
     */
    public void setOrderParametersLine(int line) {
        this.orderParametersLine = line;
    }

    /**
     * sets the flag that shows whether we should display the files in order or reverse order
     *
     * @param reverseOrder the new flag value
     */
    public void setReverseOrder(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    /**
     * sets the flag that shows whether we should display the files that passed the filter or not
     *
     * @param notFilter the new flag value
     */
    public void setNotFilter(boolean notFilter) {
        this.notFilter = notFilter;
    }

    /**
     * sets the filter parameter array
     *
     * @param filterParameters the new filter parameters
     */
    public void setFilterParameters(ArrayList<String> filterParameters) {
        this.filterParameters = filterParameters;
    }

    /**
     * sets the new filter name
     *
     * @param filter the new filter name
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * sets the new order name
     *
     * @param order the new order name
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * @return whether the files should be displayed in reverse order
     */
    public boolean isReverseOrder() {
        return reverseOrder;
    }

    /**
     * @return whether to display the files that passed the filter or not
     */
    public boolean isNotFilter() {
        return notFilter;
    }

    /**
     * @return the filter parameters passed in the filter subsection
     */
    public ArrayList<String> getFilterParameters() {
        return filterParameters;
    }

    /**
     * @return the filter name
     */
    public String getFilter() {
        return filter;
    }

    /**
     * @return the order name
     */
    public String getOrder() {
        return order;
    }

    /**
     * @return if there was an error in the filter name
     */
    public boolean isErrorInFilterName() {
        return errorInFilterName;
    }

    /**
     * @return the line number of the filter parameters in the command file
     */
    public int getFilterParametersLine() {
        return filterParametersLine;
    }

    /**
     * @return the line of the order where there occurred an error, and 0 if the name of the order was ok.
     */
    public int getOrderParametersLine() {
        return orderParametersLine;
    }

    public Command(String filterName, ArrayList<String> fParameters, boolean filterNotActive,
                   String orderName, boolean orderBackwards) {
        reverseOrder = orderBackwards;
        notFilter = filterNotActive;
        order = orderName;
        filter = filterName;

        filterParameters = new ArrayList<>();

        if (fParameters != null) {
            for (String parameter : fParameters)
                filterParameters.add(parameter);
        }
    }

}

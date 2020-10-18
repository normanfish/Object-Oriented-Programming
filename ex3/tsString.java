/**
 * This class is a wrapper class for type String, which holds additional information about whether an attempt to insert
 * a value in the cell was made, which indicates that we had to use quadratic probing to find a correct space.
 * The purpose of the class it to help us in looking for values.
 */
public class tsString {

    // the string itself
    private String data;

    //a boolean variable whose job it is to hold information about whether we ever inserted a value in the cell which
    //tsString is in (even if no value is in data at the time of checking).
    private boolean keepChecking;

    private boolean INITIAL_VALUE_FOR_FIRST_TIME_CHECKED = false;

    /**
     * The default constructor. Initialized an empty tsString (no value in data and one that hasn't been probed over).
     */
    public tsString() {
        data = null;
        keepChecking = INITIAL_VALUE_FOR_FIRST_TIME_CHECKED;
    }

    /**
     * A constructor which assigns data from an input string.
     *
     * @param initialString - the string to be assigned to data
     */
    public tsString(String initialString) {
        data = initialString;
        keepChecking = INITIAL_VALUE_FOR_FIRST_TIME_CHECKED;
    }

    /**
     * marks the tsString as having been "probed over", meaning, we tried to put something in it's place in the hash
     * table, but it was occupied. This implies that if we want to look for a String, and it is not located in its
     * hash value, then if "keepChecking" is on (meaning it was probed over at least once) we have to keep checking
     * in quadratic probing (the name of the variable is pretty telling)
     */
    protected void touch() {
        keepChecking = true;
    }

    /**
     * resets the data of the string to null;
     */
    protected void resetData() {
        data = null;
    }

    /**
     * sets the data to newData
     *
     * @param newData - the new data for the string
     */
    protected void setData(String newData) {
        data = newData;
    }

    /**
     * @return - the data (String) in the tsString
     */
    protected String getData() {
        return data;
    }

    /**
     *
     * @return - returns the "keep checking" boolean variable
     */
    protected boolean isKeepChecking() {
        return keepChecking;
    }

    /**
     *
     * @return - if the string is empty
     */
    protected boolean isEmpty() {
        return data == null;
    }

    /**
     * checks if the string in the tsString equals another string
     * @param str - the string to compare with the tsString's data
     * @return true if they are eual, and false otherwise
     */
    protected boolean equalsToString(String str) {
        if (data == null)
            return false;
        return data.equals(str);
    }

    /**
     * changes the value of the tsString to match that of another tsString
     * @param other - the other string which we wish to copy
     */
    protected void copy(tsString other) {
        data = other.getData();
        keepChecking = other.isKeepChecking();
    }
}

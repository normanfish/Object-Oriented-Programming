import java.util.TreeSet;

public class OpenHashSet extends SimpleHashSet {
    /**
     * This class implements the OpenHashSet. It contains an array of TreeSets of size "tablesize". Each time a
     * value is added or removed from the Set, it is simply added or removed from the according TreeSet.
     */
    private TreeSet<String>[] table;
    private int tableSize;


    /**
     * A default constructor. Constructs a new, empty table with default initial capacity (16),
     * upper load factor (0.75) and lower load factor (0.25).
     */
    public OpenHashSet() {
        super();
        initializeTreeArray(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     *
     * @param upperLoadFactor - The upper load factor of the hash table.
     * @param lowerLoadFactor - The lower load factor of the hash table.
     */
    public OpenHashSet(float upperLoadFactor, float lowerLoadFactor) {
        super(upperLoadFactor, lowerLoadFactor);
        initializeTreeArray(INITIAL_CAPACITY);
    }

    /**
     * Data constructor - builds the hash set by adding the elements one by one.
     * Duplicate values should be ignored. The new table has the default values of initial capacity (16),
     * upper load factor (0.75), and lower load factor (0.25).
     *
     * @param data - Values to add to the set.
     */
    public OpenHashSet(java.lang.String[] data) {
        super();
        initializeTreeArray(INITIAL_CAPACITY);
        for (String str : data) {
            add(str);
        }
    }

    public int capacity() {
        return tableSize;
    }

    public boolean delete(String toDelete) {
        //remove element
        int index = clamp(toDelete.hashCode());
        boolean wasRemoved = table[index].remove(toDelete);
        if (wasRemoved) {
            numOfElements--;
        }
        // check lower load factor and rehash
        if (wasRemoved && getLowerLoadFactor() > load())
            rehash(DECREASE);
        return wasRemoved;
    }


    public boolean add(String newValue) {
        //add element
        int index = clamp(newValue.hashCode());
        boolean wasAdded = table[index].add(newValue);
        if (wasAdded) {
            numOfElements++;
        }
        // check upper load factor and rehash
        if ((wasAdded) && (getUpperLoadFactor() < load()))
            rehash(INCREASE);
        return wasAdded;
    }


    public boolean contains(String searchVal) {
        int index = clamp(searchVal.hashCode());
        return table[index].contains(searchVal);
    }


    public int size() {
        return numOfElements;
    }

    /**
     * initializes the hash table to be of size "size" (received as input), and filled with empty TreeSets
     *
     * @param size - the size of the hash table to be set.
     */
	 @SuppressWarnings({"rawtypes","unchecked"})
    private void initializeTreeArray(int size) {
        tableSize = size;
        this.capacityMinusOne = size - 1;
        numOfElements = 0;
        table = new TreeSet[size];
        for (int i = 0; i < size; i++)
            table[i] = new TreeSet<>();
    }

    /**
     * a function that rehashes the contents of a hash table into another hash table of a different size.
     * The new hash table can be either half or double the size of the old hash table.
     * The function has 3 main parts:
     * 1) First, it creates a copy of the current Hash table
     * 2) It expands (or halves) the Hash table, while reseting the values to new
     * 3) adds all of the values in the copt table back int othe original (and expanded/contracted) hash table
     *
     * @param reduceOrIncrease - a parameter which indicates if the hash table should be expanded or contracted.
     */
	 @SuppressWarnings({"rawtypes","unchecked"})
    private void rehash(int reduceOrIncrease) {
        if (capacityMinusOne == 0 && reduceOrIncrease == DECREASE)
            return;
        //create a clone of what's currently in the table
        TreeSet<String>[] cloneTable = new TreeSet[tableSize];
        for (int i = 0; i < tableSize; i++) {
            cloneTable[i] = new TreeSet<>();
            cloneTable[i].addAll(table[i]);
        }
        // halve the table size and reset.
        if (reduceOrIncrease == INCREASE) {
            initializeTreeArray(tableSize * 2);
            this.updateCapacityMinusOne(tableSize);
        }
        //increase the table size by a factor of 2 and reset.
        if (reduceOrIncrease == DECREASE) {
            initializeTreeArray(tableSize / 2);
            this.updateCapacityMinusOne(tableSize);
        }
        // go over all of the values that were in the old hash table, and rehash them into the new hash table
        for (TreeSet<String> treeSet: table)
            for (String str : treeSet) {
                add(str);
            }
    }

    /**
     * calculates the load of the set (calculated by the number of elements divided by the capacity of the table)
     *
     * @return - the load on the hash set
     */
    private float load() {
        return ((float) (size()) / (float) tableSize);
    }
}

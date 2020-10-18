public class ClosedHashSet extends SimpleHashSet {
    /**
     * This class implements the closed hash set, which consists of an array of type "tsString", which is essentially
     * a wrapper class for String with a boolean value that indicates whether that spot was "taken" when we tried to
     * insert another value. This will become useful when we try to search for a value and we come across an empty cell,
     * to know if we should "keep looking" because a value that was supposed to be there didn't have room.
     */
    /*
     *The array of objects of type tsString
     */
    private tsString[] table;
    /*
    saves the index of the search for a value in the table. If the value if found, we know where it is, and if not,
    we know where the next empty spot in the table is.
     */
    private int nextAvailableSpot = 0;


    /**
     * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     *
     * @param upperLoadFactor - The upper load factor of the hash table.
     * @param lowerLoadFactor - The lower load factor of the hash table
     */
    public ClosedHashSet(float upperLoadFactor, float lowerLoadFactor) {
        super(upperLoadFactor, lowerLoadFactor);
        initializeTable(INITIAL_CAPACITY);
    }

    /**
     * A default constructor. Constructs a new, empty table with default initial capacity (16),
     * upper load factor (0.75) and lower load factor (0.25).
     */
    public ClosedHashSet() {
        super();
        initializeTable(INITIAL_CAPACITY);
    }

    /**
     * Data constructor - builds the hash set by adding the elements one by one.
     * Duplicate values should be ignored. The new table has the default values of initial capacity (16),
     * upper load factor (0.75), and lower load factor (0.25).
     *
     * @param data - Values to add to the set.
     */
    public ClosedHashSet(String[] data) {
        super();
        initializeTable(INITIAL_CAPACITY);
        for (String string : data) {
            add(string);
        }
    }

    public int capacity() {
        return table.length;
    }


    public boolean add(String newValue) {
        /*
         * first off, we'll search for duplicates. if the value we are trying to insert already exists, we simple won't
         * add it and return false*/
        if (contains(newValue))
            return false;
        /*
         * add element. since we already tried looking for the value, if we didn't find it, we found the next empty spot,
         * which we saved, so we'll just put the item there.
         */
        table[nextAvailableSpot].setData(newValue);
        numOfElements++;
        table[nextAvailableSpot].touch();
        nextAvailableSpot = 0;

        //rehashing - if we go over 75% load in the hash set, then we'll rehash the table

        if (((float) (size()) / (float) (table.length)) > getUpperLoadFactor()) {
            rehash(INCREASE);
        }
        return true;
    }


    public boolean delete(String toDelete) {
        /*
         * first off, we'll search for duplicates. if the value we are trying to delete doesn't exist, we simple won't
         * change the table, and return false*/
        if (!contains(toDelete))
            return false;
        /*
        delete the item:
        we've reached this point in the code, so the item exists in the table. (That's why we won't check anymore
        if we have to keep checking, we know it exists somewhere, and we even saved the index using the "contains" method)

        */
        table[nextAvailableSpot].resetData();
        numOfElements--;
        nextAvailableSpot = 0;
        /*
        rehash, if necessary.
         */
        if (((float) (size()) / (float) (table.length)) < getLowerLoadFactor())
            rehash(DECREASE);
        return true;
    }


    public boolean contains(String searchVal) {
        int index;
        int originalHashValue = searchVal.hashCode();
        for (int i = 0; i < capacityMinusOne; i++) {
            //calculate the index where the value could be, starting from it's original hash to every possibility,
            //checked through quadratic probing
            index = clamp(originalHashValue + (i + i * i) / 2);
            //if we found the value, then return true and save the index where we found it in "nextAvailableSpot"
            if (table[index].equalsToString(searchVal)) {
                nextAvailableSpot = index;
                return true;
            }
            /* if there isn't a chance that we placed searchVal further down the quadratic probing line
             (we haven't gotten there yet in adding values), then we can stop searching, the value isn't in the set.
             In this case, we will save the index, as it will be useful to know where the next available spot is
             for a certain hash valued String*/
            if (!table[index].isKeepChecking()) {
                nextAvailableSpot = index;
                break;
            }
        }
        return false;
    }


    public int size() {
        return numOfElements;
    }

    /**
     * initializes the hash table to be of size "size" (received as input), and filled with empty tsStrings
     *
     * @param size - the size of the hash table to be set.
     */
    private void initializeTable(int size) {
        table = new tsString[size];
        numOfElements = 0;
        for (int i = 0; i < size; i++) {
            table[i] = new tsString();
        }
        updateCapacityMinusOne(size);
    }

    /**
     * a function that rehashes the contents of a hash table into another hash table of a different size.
     * The new hash table can be either half or double the size of the old hash table.
     * The function has 3 main parts:
     * 1) First, it creates a copy of the current Hash table
     * 2) It expands (or halves) the Hash table, while reseting the values to new
     * 3) adds all of the values in the copy table back in the original (and expanded/contracted) hash table
     *
     * @param reduceOrIncrease - a parameter which indicates if the hash table should be expanded or contracted.
     */
    private void rehash(int reduceOrIncrease) {
        //the program asked to reduce an array of size 1, which is impossible.
        if (capacityMinusOne == 0 && reduceOrIncrease == DECREASE)
            return;
        //create a clone of what's currently in the table
        int tableSize = table.length;
        tsString[] cloneTable = new tsString[tableSize];
        for (int i = 0; i < tableSize; i++) {
            cloneTable[i] = new tsString();
            cloneTable[i].copy(table[i]);
        }
        //increase the table size by a factor of 2 and reset.
        if (reduceOrIncrease == INCREASE) {
            tableSize *= 2;
        }
        // halve the table size and reset.
        if (reduceOrIncrease == DECREASE) {
            tableSize /= 2;
        }
        initializeTable(tableSize);


        // go over all of the values that were in the old hash table, and rehash them into the new hash table
        for (tsString str: cloneTable) {
            if (str.getData() != null)
                add(str.getData());
        }
    }

}

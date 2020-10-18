/**
 implements the abstract SimpleHashSet type, which is a super class for Hash scheme sets
 */
public abstract class SimpleHashSet implements SimpleSet {
    protected static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOWER_LIMIT_LOAD = 0.25F;
    private static final float DEFAULT_UPPER_LIMIT_LOAD = 0.75F;

    //class variables, to be used also in the implementing subclasses
    protected int capacityMinusOne;
    private float lowerLoadLimit;
    private float upperLoadLimit;
    protected int numOfElements;
    // helper variables used to tell rehashing how to resize the hash table
    protected static final int INCREASE = 1;
    protected static final int DECREASE = 2;

    /**
     * the default constructor
     */
    protected SimpleHashSet() {
        this.lowerLoadLimit = DEFAULT_LOWER_LIMIT_LOAD;
        this.upperLoadLimit = DEFAULT_UPPER_LIMIT_LOAD;
    }

    /**
     * the constructor that receives a lower and upper load factor from the user
     * @param upperLoadFactor the upper load factor for the set.
     * @param lowerLoadFactor the lower load factor for the set.
     */
    protected SimpleHashSet(float upperLoadFactor, float lowerLoadFactor) {
        this.upperLoadLimit = upperLoadFactor;
        this.lowerLoadLimit = lowerLoadFactor;
    }

    /**
     *
     * @return the capacity of the current hash table
     */
    public abstract int capacity();

    /**
     * receives an index as calculated by the hash function for an object, and returns the index fitted to the hash
     * table's size
     * @param index the index as calculated by the hash function (the hash value of the string)
     * @return - the index fitted to the size of the table
     */
    protected int clamp(int index) {
        return index & this.capacityMinusOne;
    }

    /**
     *
     * @return the lower load factor of the table
     */
    protected float getLowerLoadFactor() {
        return this.lowerLoadLimit;
    }

    /**
     *
     * @return the lower load factor of the table
     */
    protected float getUpperLoadFactor() {
        return this.upperLoadLimit;
    }

    /**
     * updates the capacity minus one class variable with a new size that is received as input
     * @param newCapacity  the new size of the hash table
     */
    protected void updateCapacityMinusOne(int newCapacity) {
        this.capacityMinusOne = newCapacity - 1;
    }
}
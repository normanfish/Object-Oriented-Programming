import java.util.HashSet;

public class CollectionFacadeSet implements SimpleSet {
    /**
     * Wraps an underlying Collection and serves to both simplify its API and
     * give it a common type with the implemented SimpleHashSets.
     */

    private java.util.Collection<java.lang.String> collection;

    /**
     * Creates a new facade wrapping the specified collection.
     *
     * @param collection - The Collection to wrap.
     */
    public CollectionFacadeSet(java.util.Collection<java.lang.String> collection) {
        this.collection = collection;

        HashSet<String> temp = new HashSet<>(collection);
        this.collection.clear();
        this.collection.addAll(temp);

    }


    public int size() {
        return collection.size();
    }


    public boolean delete(String toDelete) {
        if (collection.contains(toDelete)) {
            collection.remove(toDelete);
            return true;
        }
        return false;
    }


    public boolean add(String newValue) {
        if (!collection.contains(newValue)) {
            collection.add(newValue);
            return true;
        }
        return false;
    }


    public boolean contains(String searchVal) {
        return collection.contains(searchVal);
    }
}

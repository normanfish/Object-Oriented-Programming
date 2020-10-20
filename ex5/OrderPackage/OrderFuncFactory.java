package filesprocessing.OrderPackage;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * This class is a factory class for constructing the various ordering functions. The functions are put in a Treemap,
 * by their names serving as keys.
 */
class OrderFuncFactory {

    //The map (or dictionary) containing the ordering functions
    TreeMap<String, Comparator<String>> allFuncs;


    /**
     * The constructor for the factory class. Initializes the Ordering methods in the dictionary.
     * @param directory the name of the directory which the files were filtered from. this is used in the "type"
     *                  ordering function.
     */
    public OrderFuncFactory(String directory) {
        allFuncs= new TreeMap<>();
        allFuncs.put("size", new sizeComparator());
        allFuncs.put("type", new typeComparator(directory));
    }

    /**
     *
     * @return all of the ordering functions, in a TreeMap
     */
    TreeMap<String, Comparator<String>> getAllFuncs() {
        return allFuncs;
    }
}

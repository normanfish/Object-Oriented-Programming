package filesprocessing.FilterPackage;

import filesprocessing.*;

import java.util.TreeMap;

/**
 * This is the factory class for creating Filter Functions. Filter Functions are stored in a TreeMap, with their keys
 * being their names.
 */
class FilterFuncFactory {

    //The treemap of filter functions, with their names
    private TreeMap<String, FilterFunc> allFuncs;

    /**
     * the class constructor for the factory. simply inserts  the different filtering functions into the map.
     * @param directory - the directory we wish to filter from, needed by some filter functions.
     */
    public FilterFuncFactory(String directory) {
        allFuncs= new TreeMap<>();
        allFuncs.put("greater_than", new GreaterThanFilter());
        allFuncs.put("between", new BetweenFilter());
        allFuncs.put("smaller_than", new SmallerThanFilter());
        allFuncs.put("file", new FileNameEqualToFilter(directory));
        allFuncs.put("contains",new FileNameContainsFilter(directory));
        allFuncs.put("suffix", new FileNameSuffixFilter(directory));
        allFuncs.put("prefix", new FileNamePrefixFilter(directory));
        allFuncs.put("writable", new WritableFilter());
        allFuncs.put("executable", new ExecutableFilter());
        allFuncs.put("hidden", new HiddenFilter());
        allFuncs.put("all", new NoFilter());
    }

    /**
     *
     * @return - the Map containing the Filter Functions.
     */
    TreeMap<String, FilterFunc> getAllFuncs() {
        return allFuncs;
    }
}

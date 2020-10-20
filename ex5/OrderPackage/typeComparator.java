package filesprocessing.OrderPackage;

import java.util.Comparator;

/**
 * Ordering class by the type of the files.
 */
class typeComparator implements Comparator<String> {

    //the directory from which the files were filtered. Used to remove the path from the absolute name of the file name.
    private String dir;

    /**
     * simple constructor
     * @param directory the directory of the filtered files.
     */
    typeComparator(String directory) {
        dir = directory;
    }

    /**
     * compares two string by the values the comes after the last "."
     * @param o1 the fisrt file name
     * @param o2 the second file name
     * @return the order of the two file types, in lexicographic order.
     */
    @Override
    public int compare(String o1, String o2) {
        //remove the pathname frm the absolute file path, so we stay with only the file name
        String type1 = o1.replace(dir, "");
        String type2 = o2.replace(dir, "");
        //add point to the file names, in case they don't have one
        type1 = addPoints(type1);
        type2 = addPoints(type2);
        return type1.substring(type1.lastIndexOf(".") + 1).compareTo(type2.substring(type2.lastIndexOf(".") + 1));
    }

    /**
     * this function adds a "." to the end of files who don't have a type, so that we can compare them with other files
     * @param fileName the name of the file
     * @return the name of the file with a "." at the end
     */
    private String addPoints(String fileName) {
        if (!fileName.contains("."))
            return fileName + ".";
        return fileName;
    }

}

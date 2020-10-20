package filesprocessing.OrderPackage;

import java.io.File;
import java.util.Comparator;

/**
 * Ordering class by size of the files.
 */
 class sizeComparator implements Comparator<String> {

    /**
     * receives two string, and compares the size of the files whose names are the strings. returns an int based on the
     * comparison result.
     * @param o1 - the first filename
     * @param o2 - the second filename
     * @return - 1 if o1>o2, 0 if o1=o2, -1 if o1<o2
     */
     @Override
        public int compare(String o1, String o2) {
        File file1 = new File(o1);
        File file2 = new File(o2);
        return Long.compare(file1.length(), file2.length());
    }

}

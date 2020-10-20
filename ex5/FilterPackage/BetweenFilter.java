package filesprocessing.FilterPackage;

import java.io.File;
import java.util.ArrayList;


class BetweenFilter extends FilterFunc {

    private double lowerBound;
    private double upperBound;


    boolean actualFilter(String fileName) {
        File file = new File(fileName);
        //checks if the file size is between the lower bound and the upper bound
        return ((file.length()) >= (lowerBound) && (file.length() <= upperBound));

    }

    void update(ArrayList<String> testParameters) {
        // checks if the parameters are indeed non negative numbers, and updates the parameters to test
        // according to these numbers
        validParameters=true;
        upperBound = Double.parseDouble(testParameters.get(UPPER_BOUND)) * BYTES_TO_KB;
        lowerBound = Double.parseDouble(testParameters.get(LOWER_BOUND)) * BYTES_TO_KB;
        int CORRECT_NUM_OF_PARAMS = 2;
        if (upperBound < lowerBound || upperBound < 0 || lowerBound < 0 || isWrongNumberOfParameters(CORRECT_NUM_OF_PARAMS,testParameters))
            validParameters = false;
    }
}

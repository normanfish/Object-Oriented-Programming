package filesprocessing.FilterPackage;


import java.io.File;
import java.util.ArrayList;


class GreaterThanFilter extends FilterFunc {

    private double size;


    boolean actualFilter(String fileName) {
        File file = new File(fileName);
        return ((file.length()) > (size));
    }


    void update(ArrayList<String> testParameters) {
        validParameters=true;
        size = Double.parseDouble(testParameters.get(LIMIT)) * BYTES_TO_KB;
        if(size<0||isWrongNumberOfParameters(CORRECT_NUM_OF_PARAMS,testParameters)){
            validParameters=false;
        }
    }
}

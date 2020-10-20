package filesprocessing.FilterPackage;


import java.io.File;
import java.util.ArrayList;

class FileNameContainsFilter extends FilterFunc {

    private String toContain;
    private String dir;

    public FileNameContainsFilter(String directory) {
        dir = directory;
    }


    boolean actualFilter(String fileName) {
        return fileName.replace(dir+File.separator,"").contains(toContain);
    }



    void update(ArrayList<String> testParameters) {
        validParameters=true;
        //checks if the filter parameter passed from the command file does exist, and updates the filter criteria
        if (isTestParamEmpty(testParameters)||isWrongNumberOfParameters(CORRECT_NUM_OF_PARAMS,testParameters)) {
            validParameters = false;
            return;
        }
        toContain = testParameters.get(SUBSTRING);
    }
}

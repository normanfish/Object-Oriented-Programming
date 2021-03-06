package filesprocessing.FilterPackage;

import java.io.File;
import java.util.ArrayList;


class WritableFilter extends FilterFunc {


    private String[] validValues = {"YES", "NO"};
    // a boolean integer that signals whether we want the file to be writable or not
    private boolean writableOrNot = true;

    boolean actualFilter(String fileName) {
        File newFile = new File(fileName);
        //checks if the file is writable
        return newFile.canWrite()==writableOrNot;
    }


    void update(ArrayList<String> testParameters) {
        validParameters=true;
        //checks if the writable criteria is a yes or no.
        if (isTestParamEmpty(testParameters)||isWrongNumberOfParameters(CORRECT_NUM_OF_PARAMS,testParameters)) {
            validParameters = false;
            return;
        }
        //checks the validity of the parameter (in this case, if it's a yes or a no)
        validParameters=FilterFunc.checkIfValueIsFromSublist(testParameters.get(YESORNOPLACE),validValues);
        // sets the test parameter to check if the file is writable or not based on if there is a no in the command line
        writableOrNot = !testParameters.get(YESORNOPLACE).equals("NO");
    }

}

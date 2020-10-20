package filesprocessing.FilterPackage;

import java.io.File;
import java.util.ArrayList;


class ExecutableFilter extends FilterFunc {

    private String[] validValues = {"YES", "NO"};
    // a boolean integer that signals whether we want the file to be executable or not
    private boolean executableOrNot = true;


    boolean actualFilter(String fileName) {
        File newFile = new File(fileName);
        //checks if the file is executable
        return (newFile.canExecute() == executableOrNot);
    }


    void update(ArrayList<String> testParameters) {
        validParameters = true;
        //checks if the executable criteria is a yes or no.
        if (isTestParamEmpty(testParameters) || isWrongNumberOfParameters(CORRECT_NUM_OF_PARAMS, testParameters)) {
            validParameters = false;
            return;
        }
        //checks the validity of the parameter (in this case, if it's a yes or a no)
        validParameters = FilterFunc.checkIfValueIsFromSublist(testParameters.get(YESORNOPLACE), validValues);
        // sets the test parameter to check if the file is hidden or not based on if there is a no in the command line
        executableOrNot = !testParameters.get(YESORNOPLACE).equals("NO");
    }
}
